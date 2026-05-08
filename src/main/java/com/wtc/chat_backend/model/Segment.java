package com.wtc.chat_backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document(collection = "segments")
public class Segment {

    @Id
    private String id;

    private String name;
    private String description;
    private LocalDateTime createdAt = LocalDateTime.now();
}