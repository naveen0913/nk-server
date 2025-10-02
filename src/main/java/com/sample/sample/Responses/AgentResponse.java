package com.sample.sample.Responses;

import java.util.ArrayList;
import java.util.List;

public class AgentResponse {
    private Long agentId;
    private String name;
    private String email;
    private String phone;
    private boolean status;
    private List<?> delievries = new ArrayList<>();

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<?> getDelievries() {
        return delievries;
    }

    public void setDelievries(List<?> delievries) {
        this.delievries = delievries;
    }

    public AgentResponse() {
    }

    public AgentResponse(Long agentId, String name, String email, String phone, boolean status, List<?> delievries) {
        this.agentId = agentId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.delievries = delievries;
    }
}
