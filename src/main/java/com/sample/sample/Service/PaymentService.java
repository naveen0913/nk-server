package com.sample.sample.Service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.sample.sample.DTO.NotificationDTO;
import com.sample.sample.DTO.OrderDTO;
import com.sample.sample.DTO.PaymentRequestDTO;
import com.sample.sample.Model.*;
import com.sample.sample.Repository.*;
import com.sample.sample.Responses.AccountDetailsResponse;
import com.sample.sample.Responses.OrdersResponse;
import com.sample.sample.Responses.PaymentResponse;
import com.sample.sample.Responses.UserAddressResponse;
import jakarta.transaction.Transactional;
import org.apache.commons.codec.binary.Hex;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

//import static com.sample.sample.Model.TrackingStatus.ORDER_PLACED;


@Service
public class PaymentService {

    @Value("${razorpay.api.key}")
    private String key;

    @Value("${razorpay.api.secret}")
    private String secret;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private AccountDetailsRepository accountDetailsRepository;

    @Autowired
    private UserAddressRepository userAddressRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserOrderedItemRepository userOrderedItemRepository;

    @Autowired
    private OrdersTrackingRepository ordersTrackingRepository;

    public Payment createOrderPayment(Long accountId, Long addressId, PaymentRequestDTO request)throws RazorpayException{

        AccountDetails account = accountDetailsRepository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Account not found with ID: " + accountId));

        UserAddress address = userAddressRepository.findById(addressId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found with ID: " + addressId));

        List<CartItem> cartItems = cartItemRepository.findAllById(request.getCartItemIds());
        String generatedPaymentId = "pay_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);

        RazorpayClient razorpay = new RazorpayClient(key, secret);
        Payment payment = new Payment();
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", request.getAmount()*100);
        orderRequest.put("currency", request.getCurrency());
        orderRequest.put("receipt", request.getReceipt());

        Order razorpayOrder = razorpay.orders.create(orderRequest);

        payment.setRazorpayOrderId(razorpayOrder.get("id"));
        payment.setAmount(request.getAmount());
        payment.setCurrency(request.getCurrency());
        payment.setReceipt("rcpt_" + UUID.randomUUID().toString().substring(0, 8));
        payment.setStatus(PaymentStatus.PENDING);
        payment.setSignature("");
        payment.setShippingPrice(request.getShippingPrice());
        payment.setGstAmount(request.getGstAmount());
        payment.setPaymentId(generatedPaymentId);

        payment.setAccountDetails(account);
        payment.setUserAddress(address);

        return paymentRepository.save(payment);
    }

    public boolean verifyPayment(PaymentRequestDTO dto) {
        try {
            String payload = dto.getRazorpayOrderId() + "|" + dto.getPaymentId();
            String generatedSignature = hmacSHA256(payload, secret);

            if (generatedSignature.equals(dto.getSignature())) {
                Payment payment = paymentRepository.findByRazorpayOrderId(dto.getRazorpayOrderId())
                        .orElseThrow(() -> new RuntimeException("Payment not found for order ID: " + dto.getRazorpayOrderId()));

                RazorpayClient razorpayClient = new RazorpayClient(key, secret);
                com.razorpay.Payment razorpayPayment = razorpayClient.payments.fetch(dto.getPaymentId());

                // Extract payment method (mode) like card, upi, netbanking, etc.
//                String method = razorpayPayment.get("method");
//                String bank = razorpayPayment.has("bank") ? razorpayPayment.get("bank") : null;
//                String wallet = razorpayPayment.has("wallet") ? razorpayPayment.get("wallet") : null;
//                String payLater = razorpayPayment.has("paylater") ? razorpayPayment.get("paylater") : null;

                // Store data
                payment.setPaymentId(dto.getPaymentId());
                payment.setSignature(dto.getSignature());
                payment.setStatus(PaymentStatus.SUCCESS);
                payment.setPaymentPaidDate(new Date());
//                payment.setPaymentMode(method);

                paymentRepository.save(payment);
                createOrderAfterPayment(payment);

                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private String hmacSHA256(String data, String secret) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256_HMAC.init(secretKey);
        byte[] hash = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Hex.encodeHexString(hash);
    }

    public List<Payment> getAllPayments(){
        return paymentRepository.findAll();
    }

    public List<PaymentResponse> getPaymentsByAccount(Long accountId) {
        List<Payment> payments = paymentRepository.findByAccountDetails_Id(accountId);

        return payments.stream().map(payment -> {
            PaymentResponse dto = new PaymentResponse();
            dto.setId(payment.getId());
            dto.setRazorpayOrderId(payment.getRazorpayOrderId());
            dto.setPaymentId(payment.getPaymentId());
            dto.setSignature(payment.getSignature());
            dto.setAmount(payment.getAmount());
            dto.setGstAmount(payment.getGstAmount());
            dto.setShippingPrice(payment.getShippingPrice());
            dto.setCurrency(payment.getCurrency());
            dto.setReceipt(payment.getReceipt());
            dto.setStatus(payment.getStatus().name());
            dto.setPaymentMode(payment.getPaymentMode());
            dto.setPaymentDate(payment.getPaymentPaidDate());
            AccountDetails acc = payment.getAccountDetails();
            UserAddress usedAddress = payment.getUserAddress();

            AccountDetailsResponse accDto = new AccountDetailsResponse();
            accDto.setId(acc.getId());
            accDto.setFirstName(acc.getFirstName());
            accDto.setLastName(acc.getLastName());
            accDto.setPhone(acc.getPhone());
            accDto.setAccountEmail(acc.getAccountEmail());
            accDto.setAlternatePhone(acc.getAlternatePhone());

            if (usedAddress != null) {
                UserAddressResponse addressDTO = new UserAddressResponse();
                addressDTO.setAddressId(usedAddress.getAddressId());
                addressDTO.setFirstName(usedAddress.getFirstName());
                addressDTO.setLastName(usedAddress.getLastName());
                addressDTO.setPhone(usedAddress.getPhone());
                addressDTO.setAlterPhone(usedAddress.getAlterPhone());
                addressDTO.setAddressType(usedAddress.getAddressType());
                addressDTO.setAddressLine1(usedAddress.getAddressLine1());
                addressDTO.setAddressLine2(usedAddress.getAddressLine2());
                addressDTO.setCity(usedAddress.getCity());
                addressDTO.setState(usedAddress.getState());
                addressDTO.setCountry(usedAddress.getCountry());
                addressDTO.setPincode(usedAddress.getPincode());

                accDto.setAddresses(Collections.singletonList(addressDTO));
            } else {
                accDto.setAddresses(Collections.emptyList());
            }

            dto.setAccountDetails(accDto);

            // Map associated order
            Orders order = payment.getOrder();
            if (order != null) {
                OrdersResponse orderDto = new OrdersResponse();
                orderDto.setId(order.getId());
                orderDto.setOrderId(order.getOrderId());
                orderDto.setCreatedAt(order.getCreatedAt());
                orderDto.setOrderStatus(order.getOrderStatus());
                orderDto.setOrderTotal(order.getOrderTotal());
                orderDto.setOrderDiscount(order.getOrderDiscount());
                orderDto.setOrderGstPercent(orderDto.getOrderGstPercent());
                orderDto.setOrderShippingCharges(orderDto.getOrderShippingCharges());
                orderDto.setOrderItems(order.getOrderItems());
                orderDto.setOrderTracking(order.getOrderTracking());
                dto.setOrdersResponse(orderDto);
            }

            return dto;
        }).collect(Collectors.toList());
    }


    @Transactional
    private void createOrderAfterPayment(Payment payment) {
        String generateOrderId = "ORDER" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 13).toUpperCase();
        Orders order = new Orders();
        order.setOrderId(generateOrderId);
        order.setCreatedAt(new Date());
        order.setOrderStatus("PLACED");
        order.setOrderTotal(payment.getAmount());
        order.setOrderDiscount("0");
        order.setOrderGstPercent(String.valueOf(payment.getGstAmount()));
        order.setOrderShippingCharges(String.valueOf(payment.getShippingPrice()));
        order.setUserAddress(payment.getUserAddress());
//        order.setOrderTracking(payment.getOrder().getOrderTracking());

        Orders savedOrder = orderRepository.save(order);
//        payment.setOrder(savedOrder);
//        paymentRepository.save(payment);

        List<CartItem> cartItems = cartItemRepository.getAllItemsByUser(payment.getAccountDetails().getUser().getId());

        List<UserOrderedItems> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            UserOrderedItems orderItem = new UserOrderedItems();
            orderItem.setItemName(cartItem.getCartItemName());
            orderItem.setQuantity(cartItem.getCartQuantity());
            orderItem.setGiftWrap(cartItem.isCartGiftWrap());
            orderItem.setTotalPrice(cartItem.getTotalPrice());
            orderItem.setCustomName(cartItem.getCustomName());
            orderItem.setOptionCount(cartItem.getOptionCount());
            orderItem.setOptionPrice(cartItem.getOptionPrice());
            orderItem.setOptionDiscount(cartItem.getOptiondiscount());
            orderItem.setOptionDiscountPrice(cartItem.getOptiondiscountPrice());
            orderItem.setCustomImages(new ArrayList<>(cartItem.getCustomImages()));
            orderItem.setLabelDesigns(new HashMap<>(cartItem.getLabelDesigns()));
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setPayment(payment);
            orderItem.setOrder(savedOrder);

            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);

        payment.setOrder(savedOrder);
        paymentRepository.save(payment);

        userOrderedItemRepository.saveAll(orderItems);

        // Send order email
        String email = payment.getAccountDetails().getAccountEmail();
        String orderNumber = order.getOrderId();
        mailService.sendOrderStatusMail(email, orderNumber,payment.getAccountDetails().getFirstName(), payment.getAccountDetails().getLastName(), TrackingStatus.ORDER_PLACED);

//       creating tracking after order placement

        OrdersTracking tracking = new OrdersTracking();
        tracking.setTrackingId("track_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12));
        tracking.setTrackingStatus(TrackingStatus.ORDER_PLACED);
        tracking.setShipped(false);
        tracking.setPacked(false);
        tracking.setOutOfDelivery(false);
        tracking.setDelivered(false);
        tracking.setTrackingCreated(new Date());

        // Estimated delivery = today + 10 days
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 10);
        tracking.setEstimatedDelivery(calendar.getTime());

        tracking.setOrder(savedOrder);
        savedOrder.setOrderTracking(tracking); // Bidirectional mapping

        ordersTrackingRepository.save(tracking);
        savedOrder.setOrderTracking(tracking);
        orderRepository.save(savedOrder);

        // Notify admin
//        NotificationDTO dto = new NotificationDTO();
//        dto.setTitle("New Order");
//        dto.setMessage("Order " + order.getOrderId() + " has been placed.");
//        dto.setRecipientType("ADMIN");
//        dto.setOrderId(order.getOrderId());
//        notificationService.notifyAdmin(dto);


    }

}
