package com.wtc.chat_backend.model.dto;

import com.wtc.chat_backend.model.Segment;

import java.time.LocalDateTime;

public record SegmentResponse(
        String id,
        String name,
        String description,
        long customerCount,     // total de clientes neste segmento
        LocalDateTime createdAt
) {
    public static SegmentResponse from(Segment s, long customerCount) {
        return new SegmentResponse(
                s.getId(),
                s.getName(),
                s.getDescription(),
                customerCount,
                s.getCreatedAt()
        );
    }
}