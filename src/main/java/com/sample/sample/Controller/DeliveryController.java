package com.sample.sample.Controller;

import com.sample.sample.DTO.DeliveryRequest;
import com.sample.sample.DTO.LocationDTO;
import com.sample.sample.DTO.LoginDTO;
import com.sample.sample.Responses.AuthResponse;
import com.sample.sample.Service.DeliveryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/delivery")
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;

    @PostMapping("/admin/assign")
    public ResponseEntity<?> assignAgent(@RequestBody DeliveryRequest request) {
        return ResponseEntity.ok(deliveryService.assignDeliveryAgent(request));
    }

    @GetMapping("/status/{orderId}")
    public ResponseEntity<?> getStatus(@PathVariable String orderId) {
        return ResponseEntity.ok(deliveryService.getDeliveryStatus(orderId));
    }

    @PostMapping("/agent/new")
    public ResponseEntity<?> addDeliveryAgent(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "email") String email,
            @RequestParam(value = "phone") String phone,
            @RequestParam(value = "status") boolean status,
            @RequestParam(value = "password") String password
    ){
        AuthResponse authResponse = deliveryService.addNewDeliveryAgent(name,email,phone,status,password);
        return ResponseEntity.status(authResponse.getCode()).body(authResponse);
    }

    @GetMapping("/agent/all")
    public ResponseEntity<?> getDeliveryAgents(){
        AuthResponse response = deliveryService.getAllDeliveryAgents();
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/agent/login")
    public ResponseEntity<AuthResponse> userLogin(@RequestBody LoginDTO loginDTO) {
        AuthResponse response = deliveryService.agentLogin(loginDTO);
        if (response.getCode() != HttpStatus.OK.value()) {
            return ResponseEntity.status(response.getCode()).body(response);
        }
        @SuppressWarnings("unchecked")
        Map<String, Object> responseData = (Map<String, Object>) response.getData();
        String token = (String) responseData.get("token");

        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .secure(false) // true in production with HTTPS
                .path("/")
                .maxAge(24 * 60 * 60)
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response);

    }

    @GetMapping("/agent/authorized")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        AuthResponse response = deliveryService.getUserFromRequest(request);

        if (response.getCode() != HttpStatus.OK.value()) {
            return ResponseEntity.status(response.getCode()).body(response.getMessage());
        }
        return ResponseEntity.ok(response);
    }


}
