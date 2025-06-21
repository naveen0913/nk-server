package com.sample.sample.Responses;

import java.util.List;

public class AccountDetailsResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String accountEmail;
    private String alternatePhone;
    private List<UserAddressResponse> addresses;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    public String getAlternatePhone() {
        return alternatePhone;
    }

    public void setAlternatePhone(String alternatePhone) {
        this.alternatePhone = alternatePhone;
    }

    public List<UserAddressResponse> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<UserAddressResponse> addresses) {
        this.addresses = addresses;
    }
}
