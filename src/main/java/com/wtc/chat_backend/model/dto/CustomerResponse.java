package com.wtc.chat_backend.model.dto;

import com.wtc.chat_backend.model.Customer;
import com.wtc.chat_backend.model.enums.CustomerStatus;

import java.time.LocalDateTime;
import java.util.List;

public record CustomerResponse(
        String id,
        String name,
        String email,
        String phone,
        String segmentId,
        String segmentName,     // nome resolvido do segmento
        List<String> tags,
        Double score,
        CustomerStatus customerStatus,
        String notes,
        LocalDateTime createdAt
) {
    public static CustomerResponse from(Customer c, String segmentName) {
        return new CustomerResponse(
                c.getId(),
                c.getName(),
                c.getEmail(),
                c.getPhone(),
                c.getSegmentId(),
                segmentName,
                c.getTags(),
                c.getScore(),
                c.getCustomerStatus(),
                c.getNotes(),
                c.getCreatedAt()
        );
    }
}