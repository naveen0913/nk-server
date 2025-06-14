package com.sample.sample.DTO;

import lombok.Data;

@Data
public class UserUpdateRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
}

