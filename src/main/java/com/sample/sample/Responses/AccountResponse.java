package com.sample.sample.Responses;

import com.sample.sample.DTO.AccountDetailsDTO;
import com.sample.sample.Model.AccountDetails;
import com.sample.sample.Model.User;

import java.util.List;
import java.util.stream.Collectors;

public class AccountResponse {

    public static AccountDetailsDTO toDto(AccountDetails entity) {
        AccountDetailsDTO dto = new AccountDetailsDTO();
        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setPhone(entity.getPhone());
        dto.setAlternatePhone(entity.getAlternatePhone());
        dto.setUser(entity.getUser());
        return dto;
    }

    public static List<AccountDetailsDTO> toDtoList(List<AccountDetails> entities) {
        return entities.stream().map(AccountResponse::toDto).collect(Collectors.toList());
    }

}
