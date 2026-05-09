package com.wtc.chat_backend.model.dto;

import jakarta.validation.constraints.NotBlank;

public record SegmentRequest(

        @NotBlank(message = "Nome é obrigatório")
        String name,

        String description
) {}