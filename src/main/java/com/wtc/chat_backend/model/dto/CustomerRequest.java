package com.wtc.chat_backend.model.dto;

import com.wtc.chat_backend.model.enums.CustomerStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CustomerRequest(

        @NotBlank(message = "Nome é obrigatório")
        String name,

        @Email(message = "E-mail inválido")
        @NotBlank(message = "E-mail é obrigatório")
        String email,

        String phone,
        String segmentId,
        List<String> tags,
        Double score,
        CustomerStatus customerStatus
) {}