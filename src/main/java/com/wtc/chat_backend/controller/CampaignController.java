package com.wtc.chat_backend.controller;

import com.wtc.chat_backend.model.dto.CampaignRequest;
import com.wtc.chat_backend.model.dto.CampaignResponse;
import com.wtc.chat_backend.service.CampaignService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/campaigns")
@RequiredArgsConstructor
public class CampaignController {

    private final CampaignService campaignService;

    // GET /campaigns
    @GetMapping
    public ResponseEntity<List<CampaignResponse>> findAll() {
        return ResponseEntity.ok(campaignService.findAll());
    }

    // GET /campaigns/{id}
    @GetMapping("/{id}")
    public ResponseEntity<CampaignResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(campaignService.findById(id));
    }

    // POST /campaigns — cria e envia imediatamente (ou agenda se scheduledAt informado)
    @PostMapping
    public ResponseEntity<CampaignResponse> create(@Valid @RequestBody CampaignRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(campaignService.create(req));
    }

    // POST /campaigns/{id}/schedule — agenda uma campanha existente
    @PostMapping("/{id}/schedule")
    public ResponseEntity<CampaignResponse> schedule(
            @PathVariable String id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime scheduledAt) {
        return ResponseEntity.ok(campaignService.schedule(id, scheduledAt));
    }

    // POST /campaigns/{id}/send — dispara uma campanha agendada agora
    @PostMapping("/{id}/send")
    public ResponseEntity<CampaignResponse> sendNow(@PathVariable String id) {
        return ResponseEntity.ok(campaignService.sendNow(id));
    }
}