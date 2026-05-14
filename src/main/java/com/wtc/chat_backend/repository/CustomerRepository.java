package com.wtc.chat_backend.repository;

import com.wtc.chat_backend.model.Customer;
import com.wtc.chat_backend.model.enums.CustomerStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends MongoRepository<Customer, String> {

    List<Customer> findBySegmentId(String segmentId);

    List<Customer> findByCustomerStatus(CustomerStatus status);

    List<Customer> findByTagsContaining(String tag);

    long countBySegmentId(String segmentId);

    boolean existsByEmail(String email);

    Optional<Customer> findByEmail(String email);
}