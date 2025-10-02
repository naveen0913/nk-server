package com.sample.sample.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.sample.sample.DTO.OrderDTO;
import com.sample.sample.DTO.PaymentRequestDTO;
import com.sample.sample.Model.*;
import com.sample.sample.Repository.*;
import com.sample.sample.Responses.*;
import com.sample.sample.configuration.DistanceUtils;
import com.sample.sample.configuration.RazorpayVerifier;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private AccountDetailsRepository accountDetailsRepository;

    @Autowired
    private UserAddressRepository userAddressRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserOrderedItemRepository userOrderedItemRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private PaymentRepository paymentRepository;

    @Value("${razorpay.api.key}")
    private String key;

    @Value("${razorpay.api.secret}")
    private String secret;

    @Autowired
    private DeliveryRepo deliveryRepo;

    @Transactional
    public Orders placeOrderFromCart(OrderDTO req) throws RazorpayException {
        // validate input
        if (req.getSessionId() == null && req.getUserId() == null) {
            throw new RuntimeException("Either sessionId or userId required");
        }

        UserAddress userAddress = userAddressRepository.findById(req.getShippingAddress()).orElseThrow(() -> new EntityNotFoundException("No address found with"));

        // find cart (guest -> sessionId; user -> userId)
        Cart cart;
        if (req.getUserId() != null) {
            User user = userRepo.findById(req.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            cart = cartRepo.findByUserAndStatus(user, Cart.Status.ACTIVE)
                    .orElseThrow(() -> new RuntimeException("Active cart not found for user"));
        } else {
            cart = cartRepo.findBySessionIdAndStatus(req.getSessionId(), Cart.Status.ACTIVE)
                    .orElseThrow(() -> new RuntimeException("Active guest cart not found"));
        }

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // create order
        Orders order = new Orders();
        order.setUser(cart.getUser());
        order.setSessionId(cart.getSessionId());
        order.setStatus(OrderStatus.CREATED);
        order.setUserAddress(userAddress);
        order.setOrderNote(req.getOrderNote());
        order.setCreatedAt(new Date());
        order.setOrderId("ORDER" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 13).toUpperCase());

        // convert cart items -> order items
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem ci : cart.getItems()) {
            UserOrderedItems oi = new UserOrderedItems();
            oi.setOrder(order);
            oi.setProduct(ci.getProduct());
            oi.setOrderquantity(ci.getCartQuantity());
            oi.setPrice(ci.getProduct().getDiscountPrice());
            if (order.getOrderItems() == null) {
                order.setOrderItems(new ArrayList<>());
            }
            order.getOrderItems().add(oi);

            Integer itemTotal = (int) Math.round(ci.getProduct().getDiscountPrice() * ci.getCartQuantity());
            total = total.add(BigDecimal.valueOf(itemTotal));
        }
        order.setOrderTotal(total);

        // persist order (cascade saves address and items)
        Orders saved = orderRepository.save(order);

        // mark cart completed
        cart.setStatus(Cart.Status.COMPLETED);
        cartRepo.save(cart);

        // create payment record depending on method
        Payment payment = new Payment();
        payment.setOrder(saved);
        payment.setAmount(total);
        if ("COD".equalsIgnoreCase(req.getPaymentMethod())) {
            payment.setMethod(PaymetMethod.COD);
            payment.setStatus(PaymentStatus.PENDING);
            saved.setPayment(payment);
            saved.setStatus(OrderStatus.PAYMENT_PENDING);
            paymentRepository.save(payment);

            OrdersTracking t = new OrdersTracking();
            t.setTrackingId("track_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12));
            t.setTrackingCreated(new Date());
            t.setDeliveryTime(LocalDateTime.now());
            t.setOrder(saved);
            t.setTrackingStatus(TrackingStatus.PAYMENT_PENDING);
            t.setNote("Awaiting collection on delivery");
            saved.setOrderTracking(t);
            orderRepository.save(saved);
            return saved;
        } else {
            payment.setMethod(PaymetMethod.ONLINE);
            payment.setStatus(PaymentStatus.PENDING);
            paymentRepository.save(payment);

            saved.setStatus(OrderStatus.PAYMENT_PENDING);
            saved.setPayment(payment);
            orderRepository.save(saved);
            return saved;

        }
    }

    public RazorpayResponse createRazorpayOrder(Long orderId) throws RazorpayException {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Ensure payment exists and is ONLINE and pending
        Payment payment = order.getPayment();

        if (payment == null) {
            throw new RuntimeException("Payment record not found for this order");
        }
        if (payment.getMethod() != PaymetMethod.ONLINE) {
            throw new RuntimeException("Payment method is not online");
        }

        // amount in paise
        Integer amountPaise = order.getOrderTotal().multiply(BigDecimal.valueOf(100)).intValue();

        RazorpayClient razorpay = new RazorpayClient(key, secret);
        JSONObject options = new JSONObject();
        options.put("amount", amountPaise);
        options.put("currency", "INR");
        options.put("receipt", "order_rcpt_" + order.getOrderId());
        options.put("payment_capture", 1);

        com.razorpay.Order rzpOrder = razorpay.orders.create(options);

        // save razorpayOrderId in payment
        payment.setRazorpayOrderId(rzpOrder.get("id"));
        paymentRepository.save(payment);

        RazorpayResponse resp = new RazorpayResponse();
        resp.setRazorpayOrderId(rzpOrder.get("id"));
        resp.setRazorpayKeyId(key);
        resp.setAmount(amountPaise);
        resp.setCurrency("INR");
        resp.setOrderId(order.getId());
        return resp;

    }

    @Transactional
    public void verifyAndCapturePayment(Long orderId, PaymentRequestDTO requestDTO) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Payment payment = order.getPayment();
        if (payment == null) {
            throw new RuntimeException("Payment not found for this order");
        }

        // verify signature
        String payload = requestDTO.getRazorpayOrderId() + "|" + requestDTO.getRazorpayPaymentId();
        boolean ok = RazorpayVerifier.verifySignature(payload, secret, requestDTO.getRazorpaySignature());
        if (!ok) {
            throw new RuntimeException("Razorpay signature verification failed");
        }

        // update payment record
        payment.setRazorpayPaymentId(requestDTO.getRazorpayPaymentId());
        payment.setRazorpayOrderId(requestDTO.getRazorpayOrderId());
        payment.setSignature(requestDTO.getRazorpaySignature());
        payment.setStatus(PaymentStatus.SUCCESS);
        paymentRepository.save(payment);

        // update order status
        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);

        // add tracking entry
        OrdersTracking t = new OrdersTracking();
        t.setOrder(order);
        t.setTrackingStatus(TrackingStatus.PAID);
        t.setTrackingCreated(new Date());
        t.setDelivered(false);
        t.setNote("Razorpay payment id: " + requestDTO.getRazorpayPaymentId());
        order.setOrderTracking(t);
        orderRepository.save(order);
    }

    // webhook handler can call this to update payment status based on events
    @Transactional
    public void handleRazorpayWebhook(JSONObject payload) {
        // parse event, e.g. payment.captured or payment.failed
        String event = payload.optString("event");
        if ("payment.captured".equals(event) || "payment.authorized".equals(event)) {
            JSONObject paymentEntity = payload.optJSONObject("payload").optJSONObject("payment").optJSONObject("entity");
            String paymentId = paymentEntity.optString("id");
            String orderId = paymentEntity.optJSONObject("order_id") != null ? paymentEntity.optJSONObject("order_id").optString("") : paymentEntity.optString("order_id");
            // find Payment by razorpayOrderId or paymentId
            Optional<Payment> pOpt = paymentRepository.findByRazorpayOrderId(orderId);
            if (pOpt.isPresent()) {
                Payment p = pOpt.get();
                p.setRazorpayOrderId(paymentId);
                p.setStatus(PaymentStatus.SUCCESS);
                paymentRepository.save(p);

                Orders o = p.getOrder();
                o.setStatus(OrderStatus.PAID);
                orderRepository.save(o);
            }
        } else if (event.startsWith("payment.failed")) {
            // handle failed event similarly
        }
    }


    public AuthResponse getAllOrders() {
        List<Orders> orders = orderRepository.findAll();

        List<OrdersResponse> responseList = orders.stream().map(order -> {

            AccountDetails acc = order.getUser().getAccountDetails();
            AccountDetailsResponse accountDTO = new AccountDetailsResponse();
            accountDTO.setId(acc.getId());
            accountDTO.setFirstName(acc.getFirstName());
            accountDTO.setLastName(acc.getLastName());
            accountDTO.setPhone(acc.getPhone());
            accountDTO.setAccountEmail(acc.getAccountEmail());
            accountDTO.setAlternatePhone(acc.getAlternatePhone());


            Payment payment = order.getPayment();
            PaymentResponse paymentDTO = new PaymentResponse();
            paymentDTO.setId(payment.getId());
            paymentDTO.setRazorpayOrderId(payment.getRazorpayOrderId());
            paymentDTO.setPaymentId(payment.getPaymentId());
            paymentDTO.setSignature(payment.getSignature());
            paymentDTO.setCurrency(payment.getCurrency());
            paymentDTO.setReceipt(payment.getReceipt());
            paymentDTO.setStatus(payment.getStatus().toString());


            UserAddress ua = order.getUserAddress();
            UserAddressResponse userAddressDTO = null;
            if (ua != null) {
                userAddressDTO = new UserAddressResponse();
                userAddressDTO.setAddressId(ua.getAddressId());
                userAddressDTO.setFirstName(ua.getFirstName());
                userAddressDTO.setLastName(ua.getLastName());
                userAddressDTO.setPhone(ua.getPhone());
                userAddressDTO.setAddressType(ua.getAddressType());
                userAddressDTO.setAddressLine1(ua.getAddressLine1());
                userAddressDTO.setAddressLine2(ua.getAddressLine2());
                userAddressDTO.setCity(ua.getCity());
                userAddressDTO.setState(ua.getState());
                userAddressDTO.setCountry(ua.getCountry());
                userAddressDTO.setPincode(ua.getPincode());
                userAddressDTO.setLatitude(ua.getLatitude());
                userAddressDTO.setLongitude(ua.getLongitude());
            }


            OrdersResponse dto = new OrdersResponse();
            dto.setId(order.getId());
            dto.setOrderId(order.getOrderId());
            dto.setCreatedAt(order.getCreatedAt());
            dto.setOrderStatus(order.getStatus().toString());
            dto.setOrderTotal(order.getOrderTotal());
            dto.setOrderShippingCharges(order.getOrderShippingCharges());
//            dto.setOrderItems(order.getOrderItems());
            dto.setOrderTracking(order.getOrderTracking());
            dto.setPayment(paymentDTO);
            dto.setAccountDetails(accountDTO);
            dto.setUserAddress(userAddressDTO);

            List<UserOrdereditemsResponse> userOrdereditemsResponses = order.getOrderItems().stream().map(item -> {
                UserOrdereditemsResponse itemDTO = new UserOrdereditemsResponse();
                itemDTO.setId(item.getId());
                itemDTO.setQuantity(item.getOrderquantity());
                itemDTO.setTotalPrice(item.getPrice());

                if (item.getProduct() != null) {
                    String baseUrl = "http://localhost:8083";
                    List<ImageResponse.ProductImagesResponse> imageResponses = item.getProduct().getProductImages()
                            .stream()
                            .map(image -> new ImageResponse.ProductImagesResponse(
                                    image.getImageId(),
                                    baseUrl + image.getImageUrl(),
                                    item.getProduct().getProductId()
                            ))
                            .collect(Collectors.toList());

                    ImageResponse productResponse = new ImageResponse(
                            item.getProduct().getProductId(),
                            item.getProduct().getProductName(),
                            item.getProduct().getProductDescription(),
                            item.getProduct().isProductOrdered(),
                            item.getProduct().getProductStatus(),
                            item.getProduct().getCustomProductId(),
                            item.getProduct().getCreatedTime(),
                            item.getProduct().getUpdatedTime(),
                            item.getProduct().getpCategory(),
                            item.getProduct().getpSubCategory(),
                            item.getProduct().isInStock(),
                            item.getProduct().getTotalQuantity(),
                            item.getProduct().getAvailableQuantity(),
                            item.getProduct().getpTag(),
                            item.getProduct().getPrice(),
                            item.getProduct().getDiscountPrice(),
                            item.getProduct().getWeight(),
                            item.getProduct().getWeightUnit(),
                            item.getProduct().getAttributeName(),
                            item.getProduct().getAttributeValue(),
                            imageResponses,
                            item.getProduct().isWishlisted()
                    );
                    itemDTO.setProduct(productResponse);
                }

                return itemDTO;
            }).collect(Collectors.toList());

            dto.setOrderItems(userOrdereditemsResponses);
            return dto;
        }).collect(Collectors.toList());

        return new AuthResponse(HttpStatus.OK.value(), "Orders fetched successfully", responseList);
    }


    public AuthResponse getOrderByOrderId(String orderId) {
        Orders order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found with ID: " + orderId));

        // --- Account Details ---
        AccountDetails acc = order.getUser().getAccountDetails();
        AccountDetailsResponse accountDTO = new AccountDetailsResponse();
        accountDTO.setId(acc.getId());
        accountDTO.setFirstName(acc.getFirstName());
        accountDTO.setLastName(acc.getLastName());
        accountDTO.setPhone(acc.getPhone());
        accountDTO.setAccountEmail(acc.getAccountEmail());
        accountDTO.setAlternatePhone(acc.getAlternatePhone());

        // --- Payment ---
        Payment payment = order.getPayment();
        PaymentResponse paymentDTO = new PaymentResponse();
        paymentDTO.setId(payment.getId());
        paymentDTO.setRazorpayOrderId(payment.getRazorpayOrderId());
        paymentDTO.setPaymentId(payment.getPaymentId());
        paymentDTO.setSignature(payment.getSignature());
        paymentDTO.setCurrency(payment.getCurrency());
        paymentDTO.setReceipt(payment.getReceipt());
        paymentDTO.setStatus(payment.getStatus().toString());

        // --- User Address ---
        UserAddress ua = order.getUserAddress();
        UserAddressResponse userAddressDTO = null;
        if (ua != null) {
            userAddressDTO = new UserAddressResponse();
            userAddressDTO.setAddressId(ua.getAddressId());
            userAddressDTO.setFirstName(ua.getFirstName());
            userAddressDTO.setLastName(ua.getLastName());
            userAddressDTO.setPhone(ua.getPhone());
            userAddressDTO.setAddressType(ua.getAddressType());
            userAddressDTO.setAddressLine1(ua.getAddressLine1());
            userAddressDTO.setAddressLine2(ua.getAddressLine2());
            userAddressDTO.setCity(ua.getCity());
            userAddressDTO.setState(ua.getState());
            userAddressDTO.setCountry(ua.getCountry());
            userAddressDTO.setPincode(ua.getPincode());
            userAddressDTO.setLatitude(ua.getLatitude());
            userAddressDTO.setLongitude(ua.getLongitude());
        }

        // --- Order Items ---
        List<UserOrdereditemsResponse> userOrdereditemsResponses = order.getOrderItems().stream().map(item -> {
            UserOrdereditemsResponse itemDTO = new UserOrdereditemsResponse();
            itemDTO.setId(item.getId());
            itemDTO.setQuantity(item.getOrderquantity());
            itemDTO.setTotalPrice(item.getPrice());

            if (item.getProduct() != null) {

                List<ImageResponse.ProductImagesResponse> imageResponses = item.getProduct().getProductImages()
                        .stream()
                        .map(image -> new ImageResponse.ProductImagesResponse(
                                image.getImageId(),
                                image.getImageUrl(),
                                item.getProduct().getProductId()
                        ))
                        .collect(Collectors.toList());

                ImageResponse productResponse = new ImageResponse(
                        item.getProduct().getProductId(),
                        item.getProduct().getProductName(),
                        item.getProduct().getProductDescription(),
                        item.getProduct().isProductOrdered(),
                        item.getProduct().getProductStatus(),
                        item.getProduct().getCustomProductId(),
                        item.getProduct().getCreatedTime(),
                        item.getProduct().getUpdatedTime(),
                        item.getProduct().getpCategory(),
                        item.getProduct().getpSubCategory(),
                        item.getProduct().isInStock(),
                        item.getProduct().getTotalQuantity(),
                        item.getProduct().getAvailableQuantity(),
                        item.getProduct().getpTag(),
                        item.getProduct().getPrice(),
                        item.getProduct().getDiscountPrice(),
                        item.getProduct().getWeight(),
                        item.getProduct().getWeightUnit(),
                        item.getProduct().getAttributeName(),
                        item.getProduct().getAttributeValue(),
                        imageResponses,
                        item.getProduct().isWishlisted()
                );
                itemDTO.setProduct(productResponse);
            }
            return itemDTO;
        }).collect(Collectors.toList());

        OrdersResponse dto = new OrdersResponse();
        dto.setId(order.getId());
        dto.setOrderId(order.getOrderId());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setOrderStatus(order.getStatus().toString());
        dto.setOrderTotal(order.getOrderTotal());
        dto.setOrderShippingCharges(order.getOrderShippingCharges());
        dto.setOrderTracking(order.getOrderTracking());
        dto.setPayment(paymentDTO);
        dto.setAccountDetails(accountDTO);
        dto.setUserAddress(userAddressDTO);
        dto.setOrderItems(userOrdereditemsResponses);

        deliveryRepo.findByOrder_OrderId(orderId).ifPresent(delivery -> {
            DeliveryResponse dr = new DeliveryResponse();
            dr.setTrackingId(delivery.getTrackingId());
            dr.setStatus(delivery.getDeliveryStatus());
            dr.setLatitude(delivery.getCurrentLatitude());
            dr.setLongitude(delivery.getCurrentLongitude());
            dr.setAgentName(delivery.getAgent().getAgentName());
            dr.setAgentPhone(delivery.getAgent().getPhone());
            dr.setTrackingWebSocketUrl("ws://localhost:8083/ws/topic/delivery/" + delivery.getTrackingId());


            if (order.getUserAddress() != null &&
                    order.getUserAddress().getLatitude() != null &&
                    order.getUserAddress().getLongitude() != null
                    ) {

                double distance = DistanceUtils.calculateDistance(
                        delivery.getCurrentLatitude(),
                        delivery.getCurrentLongitude(),
                        order.getUserAddress().getLatitude(),
                        order.getUserAddress().getLongitude()
                );
                dr.setEstimatedMinutes(DistanceUtils.calculateETA(distance, 30.0)); // 30 km/h avg speed
            }

            dto.setDeliveryResponse(dr);
        });

        return new AuthResponse(HttpStatus.OK.value(), "Order fetched successfully", dto);
    }

    public AuthResponse getUsersOrdersList(String userId){
        List<Orders> userOrders = orderRepository.findAllByUserId(userId);
        if (userOrders.isEmpty()) {
            return  new AuthResponse(HttpStatus.NOT_FOUND.value(), "No orders with user",null);
        }
        List<OrdersResponse> responseList = userOrders.stream().map(order -> {

            AccountDetails acc = order.getUser().getAccountDetails();
            AccountDetailsResponse accountDTO = new AccountDetailsResponse();
            accountDTO.setId(acc.getId());
            accountDTO.setFirstName(acc.getFirstName());
            accountDTO.setLastName(acc.getLastName());
            accountDTO.setPhone(acc.getPhone());
            accountDTO.setAccountEmail(acc.getAccountEmail());
            accountDTO.setAlternatePhone(acc.getAlternatePhone());


            Payment payment = order.getPayment();
            PaymentResponse paymentDTO = new PaymentResponse();
            paymentDTO.setId(payment.getId());
            paymentDTO.setRazorpayOrderId(payment.getRazorpayOrderId());
            paymentDTO.setPaymentId(payment.getPaymentId());
            paymentDTO.setSignature(payment.getSignature());
            paymentDTO.setCurrency(payment.getCurrency());
            paymentDTO.setReceipt(payment.getReceipt());
            paymentDTO.setStatus(payment.getStatus().toString());


            UserAddress ua = order.getUserAddress();
            UserAddressResponse userAddressDTO = null;
            if (ua != null) {
                userAddressDTO = new UserAddressResponse();
                userAddressDTO.setAddressId(ua.getAddressId());
                userAddressDTO.setFirstName(ua.getFirstName());
                userAddressDTO.setLastName(ua.getLastName());
                userAddressDTO.setPhone(ua.getPhone());
                userAddressDTO.setAddressType(ua.getAddressType());
                userAddressDTO.setAddressLine1(ua.getAddressLine1());
                userAddressDTO.setAddressLine2(ua.getAddressLine2());
                userAddressDTO.setCity(ua.getCity());
                userAddressDTO.setState(ua.getState());
                userAddressDTO.setCountry(ua.getCountry());
                userAddressDTO.setPincode(ua.getPincode());
                userAddressDTO.setLatitude(ua.getLatitude());
                userAddressDTO.setLongitude(ua.getLongitude());
            }

            Delivery delivery = order.getDelivery();
            DeliveryResponse response = new DeliveryResponse();

            if (delivery!=null) {
                response.setAgentName(order.getDelivery().getAgent().getAgentName());
                response.setAgentPhone(order.getDelivery().getAgent().getPhone());
                response.setLatitude(order.getDelivery().getCurrentLatitude());
                response.setLongitude(order.getDelivery().getCurrentLongitude());
                response.setStatus(order.getDelivery().getDeliveryStatus());
                response.setTrackingId(order.getDelivery().getTrackingId());
                response.setAssignedAt(order.getDelivery().getAssignedAt());
                response.setDeliveredAt(order.getDelivery().getDeliveredAt());
                response.setTrackingWebSocketUrl("ws://localhost:8083/ws/topic/delivery/" + delivery.getTrackingId());
            }

            OrdersResponse dto = new OrdersResponse();
            dto.setId(order.getId());
            dto.setOrderId(order.getOrderId());
            dto.setCreatedAt(order.getCreatedAt());
            dto.setOrderStatus(order.getStatus().toString());
            dto.setOrderTotal(order.getOrderTotal());
            dto.setOrderShippingCharges(order.getOrderShippingCharges());
//            dto.setOrderItems(order.getOrderItems());
            dto.setOrderTracking(order.getOrderTracking());
            dto.setPayment(paymentDTO);
            dto.setAccountDetails(accountDTO);
            dto.setUserAddress(userAddressDTO);
            dto.setDeliveryResponse(response);

            List<UserOrdereditemsResponse> userOrdereditemsResponses = order.getOrderItems().stream().map(item -> {
                UserOrdereditemsResponse itemDTO = new UserOrdereditemsResponse();
                itemDTO.setId(item.getId());
                itemDTO.setQuantity(item.getOrderquantity());
                itemDTO.setTotalPrice(item.getPrice());

                if (item.getProduct() != null) {
//                    String baseUrl = "http://localhost:8083";
                    List<ImageResponse.ProductImagesResponse> imageResponses = item.getProduct().getProductImages()
                            .stream()
                            .map(image -> new ImageResponse.ProductImagesResponse(
                                    image.getImageId(),
                                    image.getImageUrl(),
                                    item.getProduct().getProductId()
                            ))
                            .collect(Collectors.toList());

                    ImageResponse productResponse = new ImageResponse(
                            item.getProduct().getProductId(),
                            item.getProduct().getProductName(),
                            item.getProduct().getProductDescription(),
                            item.getProduct().isProductOrdered(),
                            item.getProduct().getProductStatus(),
                            item.getProduct().getCustomProductId(),
                            item.getProduct().getCreatedTime(),
                            item.getProduct().getUpdatedTime(),
                            item.getProduct().getpCategory(),
                            item.getProduct().getpSubCategory(),
                            item.getProduct().isInStock(),
                            item.getProduct().getTotalQuantity(),
                            item.getProduct().getAvailableQuantity(),
                            item.getProduct().getpTag(),
                            item.getProduct().getPrice(),
                            item.getProduct().getDiscountPrice(),
                            item.getProduct().getWeight(),
                            item.getProduct().getWeightUnit(),
                            item.getProduct().getAttributeName(),
                            item.getProduct().getAttributeValue(),
                            imageResponses,
                            item.getProduct().isWishlisted()
                    );
                    itemDTO.setProduct(productResponse);
                }

                return itemDTO;
            }).collect(Collectors.toList());

            dto.setOrderItems(userOrdereditemsResponses);
            return dto;
        }).collect(Collectors.toList());
return new AuthResponse(HttpStatus.OK.value(), "ok",responseList);

    }


}
