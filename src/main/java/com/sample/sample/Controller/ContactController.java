package com.sample.sample.Controller;

import com.sample.sample.DTO.ContactDTO;
import com.sample.sample.Responses.AuthResponse;
import com.sample.sample.Service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/contact/")
@CrossOrigin("*")
public class ContactController {

    @Autowired
    private ContactService contactservice;

    // ✅ POST - Send a message
    @PostMapping("/message")
    public ResponseEntity<AuthResponse> createPostMessage(@RequestBody ContactDTO contactDTO) {
        AuthResponse response = contactservice.postContactMessage(contactDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // ✅ GET - Get all messages
    @GetMapping("/all")
    public ResponseEntity<AuthResponse> getAllMessages() {
        AuthResponse response = contactservice.getAllMessages();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}