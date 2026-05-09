package com.wtc.chat_backend.repository;

import com.wtc.chat_backend.model.Segment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SegmentRepository extends MongoRepository<Segment, String> {

    Optional<Segment> findByName(String name);

    boolean existsByName(String name);
}