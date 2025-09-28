package com.sample.sample.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.sample.sample.DTO.PaymentRequestDTO;
import com.sample.sample.Model.*;
import com.sample.sample.Repository.*;
import com.sample.sample.Responses.*;
import jakarta.persistence.EntityNotFoundException;
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
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


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
    private UserOrderedItemRepository userOrderedItemRepository;

    @Autowired
    private OrdersTrackingRepository ordersTrackingRepository;

    @Autowired
    private ProductsRepository productsRepository;


//    public AuthResponse createOrderPayment(Long accountId, Long addressId, PaymentRequestDTO request) throws RazorpayException {
//        AccountDetails account = accountDetailsRepository.findById(accountId)
//                .orElseThrow(() -> new ResponseStatusException(
//                        HttpStatus.NOT_FOUND, "Account not found with ID: " + accountId));
//
//        UserAddress address = userAddressRepository.findById(addressId)
//                .orElseThrow(() -> new ResponseStatusException(
//                        HttpStatus.NOT_FOUND, "User not found with ID: " + addressId));
//
//        List<CartItem> cartItems = cartItemRepository.findAllById(request.getCartItemIds());
//
//        String generatedPaymentId = "pay_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
//
//        RazorpayClient razorpay = new RazorpayClient(key, secret);
//        JSONObject orderRequest = new JSONObject();
//        orderRequest.put("amount", request.getAmount() * 100);
//        orderRequest.put("currency", request.getCurrency());
//        orderRequest.put("receipt", request.getReceipt());
//
//        Order razorpayOrder = razorpay.orders.create(orderRequest);
//
//        Payment payment = new Payment();
//        payment.setRazorpayOrderId(razorpayOrder.get("id"));
//        payment.setAmount(request.getAmount());
//        payment.setCurrency(request.getCurrency());
//        payment.setReceipt("rcpt_" + UUID.randomUUID().toString().substring(0, 8));
//        payment.setStatus(PaymentStatus.PENDING);
//        payment.setSignature("");
//        payment.setShippingPrice(request.getShippingPrice());
//        payment.setGstAmount(request.getGstAmount());
//        payment.setPaymentId(generatedPaymentId);
//
//        payment.setAccountDetails(account);
//        payment.setUserAddress(address);
//
//        Payment savedPayment = paymentRepository.save(payment);
//
//        return new AuthResponse(HttpStatus.CREATED.value(), "Payment order created successfully", savedPayment);
//    }
//
//
//    public boolean verifyPayment(PaymentRequestDTO dto) {
//        try {
//            Payment payment = paymentRepository.findByRazorpayOrderId(dto.getRazorpayOrderId())
//                    .orElseThrow(() -> new RuntimeException(
//                            "Payment not found for order ID: " + dto.getRazorpayOrderId()));
//
//            String payload = dto.getRazorpayOrderId() + "|" + dto.getPaymentId();
//            String generatedSignature = hmacSHA256(payload, secret);
//            if (!generatedSignature.equals(dto.getSignature())) {
//                payment.setStatus(PaymentStatus.FAILED);
//                paymentRepository.save(payment);
//                return false;
//            }
//
//            RazorpayClient razorpayClient = new RazorpayClient(key, secret);
//            com.razorpay.Payment razorpayPayment = razorpayClient.payments.fetch(dto.getPaymentId());
//            String razorpayStatus = razorpayPayment.get("status");
//
//            if (!"captured".equalsIgnoreCase(razorpayStatus)) {
//                payment.setStatus(PaymentStatus.FAILED);
//                paymentRepository.save(payment);
//                return false;
//            }
//
//            payment.setPaymentId(dto.getPaymentId());
//            payment.setSignature(dto.getSignature());
//            payment.setStatus(PaymentStatus.SUCCESS);
//            payment.setPaymentPaidDate(new Date());
//
//            createOrderAfterPayment(payment);
//
//            byte[] receiptPdf = generateTransactionPDF(payment);
//
//            mailService.sendTransactionSuccessfulMail(
//                    payment.getAccountDetails().getAccountEmail(),
//                    payment.getAccountDetails().getUser().getUsername(),
//                    receiptPdf
//            );
//            paymentRepository.save(payment);
//            return true;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }


    private String hmacSHA256(String data, String secret) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256_HMAC.init(secretKey);
        byte[] hash = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Hex.encodeHexString(hash);
    }

//    public AuthResponse getPaymentsByAccount(Long accountId) {
//        List<Payment> payments = paymentRepository.findByAccountDetails_Id(accountId);
//
//        List<PaymentResponse> responseList = payments.stream().map(payment -> {
//            PaymentResponse dto = new PaymentResponse();
//            dto.setId(payment.getId());
//            dto.setRazorpayOrderId(payment.getRazorpayOrderId());
//            dto.setPaymentId(payment.getPaymentId());
//            dto.setSignature(payment.getSignature());
//            dto.setAmount(payment.getAmount());
//            dto.setGstAmount(payment.getGstAmount());
//            dto.setShippingPrice(payment.getShippingPrice());
//            dto.setCurrency(payment.getCurrency());
//            dto.setReceipt(payment.getReceipt());
//            dto.setStatus(payment.getStatus().name());
//            dto.setPaymentMode(payment.getPaymentMode());
//            dto.setPaymentDate(payment.getPaymentPaidDate());
//
//            AccountDetails acc = payment.getAccountDetails();
//            UserAddress usedAddress = payment.getUserAddress();
//
//            AccountDetailsResponse accDto = new AccountDetailsResponse();
//            accDto.setId(acc.getId());
//            accDto.setFirstName(acc.getFirstName());
//            accDto.setLastName(acc.getLastName());
//            accDto.setPhone(acc.getPhone());
//            accDto.setAccountEmail(acc.getAccountEmail());
//            accDto.setAlternatePhone(acc.getAlternatePhone());
//
//            if (usedAddress != null) {
//                UserAddressResponse addressDTO = new UserAddressResponse();
//                addressDTO.setAddressId(usedAddress.getAddressId());
//                addressDTO.setFirstName(usedAddress.getFirstName());
//                addressDTO.setLastName(usedAddress.getLastName());
//                addressDTO.setPhone(usedAddress.getPhone());
//                addressDTO.setAlterPhone(usedAddress.getAlterPhone());
//                addressDTO.setAddressType(usedAddress.getAddressType());
//                addressDTO.setAddressLine1(usedAddress.getAddressLine1());
//                addressDTO.setAddressLine2(usedAddress.getAddressLine2());
//                addressDTO.setCity(usedAddress.getCity());
//                addressDTO.setState(usedAddress.getState());
//                addressDTO.setCountry(usedAddress.getCountry());
//                addressDTO.setPincode(usedAddress.getPincode());
//
//                accDto.setAddresses(Collections.singletonList(addressDTO));
//            } else {
//                accDto.setAddresses(Collections.emptyList());
//            }
//
//            dto.setAccountDetails(accDto);
//
//            Orders order = payment.getOrder();
//            if (order != null) {
//                OrdersResponse orderDto = new OrdersResponse();
//                orderDto.setId(order.getId());
//                orderDto.setOrderId(order.getOrderId());
//                orderDto.setCreatedAt(order.getCreatedAt());
//                orderDto.setOrderStatus(order.getStatus().toString());
//                orderDto.setOrderTotal(orderDto.getOrderTotal());
//                orderDto.setOrderGstPercent(order.getOrderGstPercent());
//                orderDto.setOrderShippingCharges(order.getOrderShippingCharges());
////                orderDto.setOrderItems(order.getOrderItems());
//                List<UserOrdereditemsResponse> userOrdereditemsResponses  = order.getOrderItems().stream().map(item->{
//                    UserOrdereditemsResponse itemDTO = new UserOrdereditemsResponse();
//                    itemDTO.setId(item.getId());
//                    itemDTO.setQuantity(item.getOrderquantity());
//                    itemDTO.setTotalPrice(item.getPrice());
//
//                    return itemDTO;
//                }).collect(Collectors.toList());
//                orderDto.setOrderItems(userOrdereditemsResponses);
//                orderDto.setOrderTracking(order.getOrderTracking());
//                dto.setOrdersResponse(orderDto);
//            }
//
//            return dto;
//        }).collect(Collectors.toList());
//
//        return new AuthResponse(HttpStatus.OK.value(), "Payments fetched successfully for accountId: " + accountId, responseList);
//    }


//    @Transactional
//    private void createOrderAfterPayment(Payment payment) {
//        String generateOrderId = "ORDER" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 13).toUpperCase();
//        Orders order = new Orders();
//        order.setOrderId(generateOrderId);
//        order.setCreatedAt(new Date());
////        order.("PLACED");
//        order.setOrderGstPercent(String.valueOf(payment.getGstAmount()));
//        order.setOrderShippingCharges(String.valueOf(payment.getShippingPrice()));
//        order.setUserAddress(payment.getUserAddress());
////        order.setOrderTracking(payment.getOrder().getOrderTracking());
//
//        Orders savedOrder = orderRepository.save(order);
////        payment.setOrder(savedOrder);
////        paymentRepository.save(payment);
//
////
////        List<UserOrderedItems> orderItems = new ArrayList<>();
////        for (CartItem cartItem : cartItems) {
////            UserOrderedItems orderItem = new UserOrderedItems();
////            orderItem.setProduct(cartItem.getProduct());
////            orderItem.setPayment(payment);
////            orderItem.setOrder(savedOrder);
////
////            Products products = productsRepository.findById(cartItem.getProduct().getProductId()).orElseThrow(() -> new EntityNotFoundException("Product not found"));
////            products.setProductOrdered(true);
////            productsRepository.save(products);
////            orderItems.add(orderItem);
////        }
////        order.setOrderItems(orderItems);
//
//        payment.setOrder(savedOrder);
//        paymentRepository.save(payment);
//
////        userOrderedItemRepository.saveAll(orderItems);
//
//
//        String email = payment.getAccountDetails().getAccountEmail();
//        String orderNumber = order.getOrderId();
//        mailService.sendOrderStatusMail(email, orderNumber, payment.getAccountDetails().getFirstName(), payment.getAccountDetails().getLastName(), TrackingStatus.CREATED);
//
//
//        OrdersTracking tracking = new OrdersTracking();
//        tracking.setTrackingId("track_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12));
//        tracking.setTrackingStatus(TrackingStatus.CREATED);
//        tracking.setDelivered(false);
//        tracking.setTrackingCreated(new Date());
//
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DAY_OF_YEAR, 10);
//
//        tracking.setOrder(savedOrder);
//        savedOrder.setOrderTracking(tracking);
//
//        ordersTrackingRepository.save(tracking);
//        savedOrder.setOrderTracking(tracking);
//        orderRepository.save(savedOrder);
//
//    }

//    public AuthResponse getAllPayments() {
//        List<Payment> payments = paymentRepository.findAll();
//
//        List<PaymentResponse> responseList = payments.stream().map(payment -> {
//            PaymentResponse dto = new PaymentResponse();
//            dto.setId(payment.getId());
//            dto.setRazorpayOrderId(payment.getRazorpayOrderId());
//            dto.setPaymentId(payment.getPaymentId());
//            dto.setSignature(payment.getSignature());
//            dto.setAmount(payment.getAmount());
//            dto.setGstAmount(payment.getGstAmount());
//            dto.setShippingPrice(payment.getShippingPrice());
//            dto.setCurrency(payment.getCurrency());
//            dto.setReceipt(payment.getReceipt());
//            dto.setStatus(payment.getStatus().name());
//            dto.setPaymentMode(payment.getPaymentMode());
//            dto.setPaymentDate(payment.getPaymentPaidDate());
//
//            AccountDetails acc = payment.getAccountDetails();
//            UserAddress usedAddress = payment.getUserAddress();
//
//            AccountDetailsResponse accDto = new AccountDetailsResponse();
//            accDto.setId(acc.getId());
//            accDto.setFirstName(acc.getFirstName());
//            accDto.setLastName(acc.getLastName());
//            accDto.setPhone(acc.getPhone());
//            accDto.setAccountEmail(acc.getAccountEmail());
//            accDto.setAlternatePhone(acc.getAlternatePhone());
//
//            if (usedAddress != null) {
//                UserAddressResponse addressDTO = new UserAddressResponse();
//                addressDTO.setAddressId(usedAddress.getAddressId());
//                addressDTO.setFirstName(usedAddress.getFirstName());
//                addressDTO.setLastName(usedAddress.getLastName());
//                addressDTO.setPhone(usedAddress.getPhone());
//                addressDTO.setAlterPhone(usedAddress.getAlterPhone());
//                addressDTO.setAddressType(usedAddress.getAddressType());
//                addressDTO.setAddressLine1(usedAddress.getAddressLine1());
//                addressDTO.setAddressLine2(usedAddress.getAddressLine2());
//                addressDTO.setCity(usedAddress.getCity());
//                addressDTO.setState(usedAddress.getState());
//                addressDTO.setCountry(usedAddress.getCountry());
//                addressDTO.setPincode(usedAddress.getPincode());
//
//                accDto.setAddresses(Collections.singletonList(addressDTO));
//            } else {
//                accDto.setAddresses(Collections.emptyList());
//            }
//
//            dto.setAccountDetails(accDto);
//
//            Orders order = payment.getOrder();
//            if (order != null) {
//                OrdersResponse orderDto = new OrdersResponse();
//                orderDto.setId(order.getId());
//                orderDto.setOrderId(order.getOrderId());
//                orderDto.setCreatedAt(order.getCreatedAt());
//                orderDto.setOrderStatus(order.getStatus().toString());
//                orderDto.setOrderGstPercent(order.getOrderGstPercent());
//                orderDto.setOrderShippingCharges(order.getOrderShippingCharges());
//                orderDto.setOrderTracking(order.getOrderTracking());
//
//                List<UserOrdereditemsResponse> userOrdereditemsResponses  = order.getOrderItems().stream().map(item->{
//                    UserOrdereditemsResponse itemDTO = new UserOrdereditemsResponse();
//                    itemDTO.setId(item.getId());
//                    itemDTO.setQuantity(item.getOrderquantity());
//                    itemDTO.setTotalPrice(item.getPrice());
//
//
//                    return itemDTO;
//                }).collect(Collectors.toList());
//                orderDto.setOrderItems(userOrdereditemsResponses);
//                dto.setOrdersResponse(orderDto);
//            }
//
//            return dto;
//        }).collect(Collectors.toList());
//
//        return new AuthResponse(HttpStatus.OK.value(), "All payments fetched successfully", responseList);
//    }



    private byte[] generateTransactionPDF(Payment payment) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter.getInstance(document, baos);
            document.open();

            // Fonts
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font headerBoldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 10);

            // --- Logo + Title ---
            Paragraph logoTitle = new Paragraph("WeLoveYou - TAX INVOICE", titleFont);
            logoTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(logoTitle);
            document.add(Chunk.NEWLINE);

            // Transform Order ID for display and invoice number
            String originalOrderId = payment.getOrder().getOrderId();
            String transformedOrderId = originalOrderId;
            String invoiceNumberPart = originalOrderId;

            if (originalOrderId != null && originalOrderId.length() > 1) {
                int halfLength = originalOrderId.length() / 2;
                String firstHalf = originalOrderId.substring(0, halfLength).toUpperCase();
                String lastDigits = originalOrderId.substring(originalOrderId.length() - 4);
                transformedOrderId = firstHalf + originalOrderId.substring(halfLength).toLowerCase();
                invoiceNumberPart = firstHalf + lastDigits;
            }

            // --- Format Invoice Date + Time ---
            String formattedDateTime = "";
            if (payment.getPaymentPaidDate() != null) {
                ZonedDateTime zonedDateTime = payment.getPaymentPaidDate().toInstant()
                        .atZone(ZoneId.systemDefault());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM, h:mm a");
                formattedDateTime = zonedDateTime.format(formatter); // Example: 14 August, 6:00 PM
            }

            // --- Amazon-style Invoice Info Box ---
            PdfPTable invoiceInfoTable = new PdfPTable(2);
            invoiceInfoTable.setWidthPercentage(100);
            invoiceInfoTable.setWidths(new float[]{1, 1});

            invoiceInfoTable.addCell(getAmazonStyleCell("Order ID: " + transformedOrderId, headerBoldFont));
            invoiceInfoTable.addCell(getAmazonStyleCell("Invoice Date: " + formattedDateTime, headerBoldFont));

            invoiceInfoTable.addCell(getAmazonStyleCell("Invoice Number: INV-" + invoiceNumberPart, headerBoldFont));
            invoiceInfoTable.addCell(getAmazonStyleCell("Payment ID: " + payment.getPaymentId(), headerBoldFont));

            invoiceInfoTable.addCell(getAmazonStyleCell("Payment Status: " + payment.getStatus(), headerBoldFont));
            invoiceInfoTable.addCell(getAmazonStyleCell("", headerBoldFont)); // Empty cell

            document.add(invoiceInfoTable);
            document.add(Chunk.NEWLINE);

            // --- Addresses ---
            UserAddress userAddress = payment.getUserAddress();
            String shippingAddress = (userAddress != null)
                    ? userAddress.getFirstName() + "\n" + userAddress.getAddressLine1() + userAddress.getAddressLine2() + "\n" +
                    userAddress.getCity() + ", " + userAddress.getState() + " - " +
                    userAddress.getPincode() + "\n" + userAddress.getCountry()
                    : "N/A";

            PdfPTable addressTable = new PdfPTable(2);
            addressTable.setWidthPercentage(100);
            addressTable.setWidths(new float[]{1, 1});

            PdfPCell shippingCell = new PdfPCell();
            shippingCell.addElement(new Paragraph("Shipping Address:", headerBoldFont));
            shippingCell.addElement(new Paragraph(shippingAddress, normalFont));

            PdfPCell billingCell = new PdfPCell();
            billingCell.addElement(new Paragraph("Billing Address:", headerBoldFont));
            billingCell.addElement(new Paragraph(shippingAddress, normalFont));

            addressTable.addCell(shippingCell);
            addressTable.addCell(billingCell);

            document.add(addressTable);
            document.add(Chunk.NEWLINE);

            // --- Items Table ---
            PdfPTable itemTable = new PdfPTable(4);
            itemTable.setWidthPercentage(100);
            itemTable.setWidths(new float[]{4, 1, 2, 2});

            itemTable.addCell(getHeaderCell("Item"));
            itemTable.addCell(getHeaderCell("Qty"));
            itemTable.addCell(getHeaderCell("Price"));
            itemTable.addCell(getHeaderCell("Total"));

            double subtotal = 0;

            if (payment.getOrder() != null && payment.getOrder().getOrderItems() != null) {
                for (UserOrderedItems item : payment.getOrder().getOrderItems()) {
                    double totalPrice = item.getOrderquantity() * item.getPrice();
                    subtotal += totalPrice;

                    itemTable.addCell(getNormalCell(String.valueOf(item.getOrderquantity())));
                    itemTable.addCell(getNormalCell("₹" + item.getProduct().getDiscountPrice()));
                    itemTable.addCell(getNormalCell("₹" + totalPrice));
                }
            }

            document.add(itemTable);

            // --- Summary ---
            double gst = payment.getGstAmount() != null ? payment.getGstAmount() : 0;
//            double shipping = payment.getShippingPrice() != null ? payment.getShippingPrice() : 0;
//            double total = subtotal + gst + shipping;

            PdfPTable summaryTable = new PdfPTable(2);
            summaryTable.setWidthPercentage(40);
            summaryTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

            summaryTable.addCell(getNoBorderCell("Subtotal:", headerBoldFont));
            summaryTable.addCell(getNoBorderRightAlign("₹" + subtotal, headerBoldFont));

            summaryTable.addCell(getNoBorderCell("GST:", headerBoldFont));
            summaryTable.addCell(getNoBorderRightAlign("₹" + gst, headerBoldFont));

            summaryTable.addCell(getNoBorderCell("Shipping:", headerBoldFont));
//            summaryTable.addCell(getNoBorderRightAlign("₹" + shipping, headerBoldFont));

            summaryTable.addCell(getNoBorderCell("Total:", headerBoldFont));
//            summaryTable.addCell(getNoBorderRightAlign("₹" + total, headerBoldFont));

            document.add(summaryTable);

            // Footer note
            Paragraph footer = new Paragraph("This is a computer-generated invoice and does not require a signature.", normalFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(Chunk.NEWLINE);
            document.add(footer);

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // --- Helper methods ---
    private PdfPCell getAmazonStyleCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Paragraph(text, font));
        cell.setBackgroundColor(new BaseColor(242, 242, 242)); // light gray
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(8f);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        return cell;
    }

    private PdfPCell getHeaderCell(String text) {
        Font boldFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
        PdfPCell cell = new PdfPCell(new Paragraph(text, boldFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        return cell;
    }

    private PdfPCell getNormalCell(String text) {
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 10);
        PdfPCell cell = new PdfPCell(new Paragraph(text, normalFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    private PdfPCell getNoBorderCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Paragraph(text, font));
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    private PdfPCell getNoBorderRightAlign(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Paragraph(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }





}


