package com.sample.sample.Model;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.*;

@Entity
@Table(name = "users")
public class User {

    @UuidGenerator
    @Id
    private String id;

    private String username;

    private String email;

    private String password;

    private Date created;

    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

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

}