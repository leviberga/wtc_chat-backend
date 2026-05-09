package com.wtc.chat_backend.controller;

import com.wtc.chat_backend.model.dto.SegmentRequest;
import com.wtc.chat_backend.model.dto.SegmentResponse;
import com.wtc.chat_backend.service.SegmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/segments")
@RequiredArgsConstructor
public class SegmentController {

    private final SegmentService segmentService;

    // GET /segments
    @GetMapping
    public ResponseEntity<List<SegmentResponse>> findAll() {
        return ResponseEntity.ok(segmentService.findAll());
    }

    // GET /segments/{id}
    @GetMapping("/{id}")
    public ResponseEntity<SegmentResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(segmentService.findById(id));
    }

    // POST /segments
    @PostMapping
    public ResponseEntity<SegmentResponse> create(@Valid @RequestBody SegmentRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(segmentService.create(req));
    }

    // PUT /segments/{id}
    @PutMapping("/{id}")
    public ResponseEntity<SegmentResponse> update(
            @PathVariable String id,
            @Valid @RequestBody SegmentRequest req) {
        return ResponseEntity.ok(segmentService.update(id, req));
    }

    // DELETE /segments/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        segmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}