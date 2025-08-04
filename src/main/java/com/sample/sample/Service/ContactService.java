package com.sample.sample.Service;


import com.sample.sample.DTO.ContactDTO;
import com.sample.sample.Model.Contact;
import com.sample.sample.Repository.ContactRepo;
import com.sample.sample.Responses.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class ContactService {

    @Autowired
    ContactRepo contactRepo;

    public AuthResponse postContactMessage(ContactDTO contactDTO) {
        Contact newContact = new Contact();
        newContact.setContactName(contactDTO.getContactName());
        newContact.setContactEmail(contactDTO.getEmail());
        newContact.setContactMessage(contactDTO.getContactMessage());
        contactRepo.save(newContact);
        return new AuthResponse(HttpStatus.CREATED.value(), "Message Sent", newContact);
    }

    public AuthResponse getAllMessages() {
        List<Contact> allContacts = contactRepo.findAll();
        return new AuthResponse(HttpStatus.OK.value(), "Message Sent", allContacts);
    }
}