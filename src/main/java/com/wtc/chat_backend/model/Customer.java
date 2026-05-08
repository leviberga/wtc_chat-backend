package com.wtc.chat_backend.model;

import com.wtc.chat_backend.model.enums.CustomerStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "customers")
public class Customer {

    @Id
    private String id;

    private String name;
    private String email;
    private String phone;
    private String segmentId;
    private List<String> tags;
    private Double score;
    private CustomerStatus customerStatus;
    private LocalDateTime createdAt = LocalDateTime.now();


}
