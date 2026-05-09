package com.wtc.chat_backend.controller;

import com.wtc.chat_backend.model.dto.CustomerRequest;
import com.wtc.chat_backend.model.dto.CustomerResponse;
import com.wtc.chat_backend.model.dto.TimelineResponse;
import com.wtc.chat_backend.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    // GET /customers
    // GET /customers?segmentId=xxx
    // GET /customers?tag=xxx
    @GetMapping
    public ResponseEntity<List<CustomerResponse>> findAll(
            @RequestParam(required = false) String segmentId,
            @RequestParam(required = false) String tag) {

        if (segmentId != null) return ResponseEntity.ok(customerService.findBySegment(segmentId));
        if (tag       != null) return ResponseEntity.ok(customerService.findByTag(tag));

        return ResponseEntity.ok(customerService.findAll());
    }

    // GET /customers/{id}
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(customerService.findById(id));
    }

    // GET /customers/{id}/timeline
    @GetMapping("/{id}/timeline")
    public ResponseEntity<TimelineResponse> getTimeline(@PathVariable String id) {
        return ResponseEntity.ok(customerService.getTimeline(id));
    }

    // POST /customers
    @PostMapping
    public ResponseEntity<CustomerResponse> create(@Valid @RequestBody CustomerRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.create(req));
    }

    // PUT /customers/{id}
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> update(
            @PathVariable String id,
            @Valid @RequestBody CustomerRequest req) {
        return ResponseEntity.ok(customerService.update(id, req));
    }

    // DELETE /customers/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}