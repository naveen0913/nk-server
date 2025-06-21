package com.sample.sample.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "users")
public class User {

    @UuidGenerator
    @Id
    private String id;

    private String username;

    private String email;

    /**
     * This is the encoded (hashed) user password.
     */
    private String password;

    private Date created;

    private String role;

    /**
     * One-to-One mapping with account details.
     */
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnore
    private AccountDetails accountDetails;

    /**
     * Flag indicating whether the password was ever updated.
     */
    private boolean passwordUpdated;

    /**
     * Time of last password update.
     */
    private LocalDateTime passwordUpdatedTime;

    /**
     * OTP and time for password reset.
     */
    private String resetOtp;
    private LocalDateTime otpGeneratedTime;

    // ======== Getters and Setters ========

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public AccountDetails getAccountDetails() {
        return accountDetails;
    }

    public void setAccountDetails(AccountDetails accountDetails) {
        this.accountDetails = accountDetails;
    }

    public boolean isPasswordUpdated() {
        return passwordUpdated;
    }

    public void setPasswordUpdated(boolean passwordUpdated) {
        this.passwordUpdated = passwordUpdated;
    }

    public LocalDateTime getPasswordUpdatedTime() {
        return passwordUpdatedTime;
    }

    public void setPasswordUpdatedTime(LocalDateTime passwordUpdatedTime) {
        this.passwordUpdatedTime = passwordUpdatedTime;
    }

    public String getResetOtp() {
        return resetOtp;
    }

    public void setResetOtp(String resetOtp) {
        this.resetOtp = resetOtp;
    }

    public LocalDateTime getOtpGeneratedTime() {
        return otpGeneratedTime;
    }

    public void setOtpGeneratedTime(LocalDateTime otpGeneratedTime) {
        this.otpGeneratedTime = otpGeneratedTime;
    }
}
