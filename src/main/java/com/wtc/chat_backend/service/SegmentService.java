package com.wtc.chat_backend.service;

import com.wtc.chat_backend.model.dto.SegmentRequest;
import com.wtc.chat_backend.model.dto.SegmentResponse;
import com.wtc.chat_backend.model.Segment;
import com.wtc.chat_backend.repository.CustomerRepository;
import com.wtc.chat_backend.repository.SegmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SegmentService {

    private final SegmentRepository  segmentRepository;
    private final CustomerRepository customerRepository;

    // ─── LISTAR ──────────────────────────────────────────────────────────────

    public List<SegmentResponse> findAll() {
        return segmentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ─── BUSCAR POR ID ───────────────────────────────────────────────────────

    public SegmentResponse findById(String id) {
        return toResponse(getOrThrow(id));
    }

    // ─── CRIAR ───────────────────────────────────────────────────────────────

    public SegmentResponse create(SegmentRequest req) {
        if (segmentRepository.existsByName(req.name())) {
            throw new IllegalArgumentException("Segmento já existe: " + req.name());
        }

        Segment segment = new Segment();
        segment.setName(req.name());
        segment.setDescription(req.description());

        return toResponse(segmentRepository.save(segment));
    }

    // ─── ATUALIZAR ───────────────────────────────────────────────────────────

    public SegmentResponse update(String id, SegmentRequest req) {
        Segment segment = getOrThrow(id);
        segment.setName(req.name());
        segment.setDescription(req.description());
        return toResponse(segmentRepository.save(segment));
    }

    // ─── DELETAR ─────────────────────────────────────────────────────────────

    public void delete(String id) {
        Segment segment = getOrThrow(id);
        segmentRepository.delete(segment);
    }

    // ─── HELPERS PRIVADOS ────────────────────────────────────────────────────

    private Segment getOrThrow(String id) {
        return segmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Segmento não encontrado: " + id));
    }

    private SegmentResponse toResponse(Segment segment) {
        long count = customerRepository.countBySegmentId(segment.getId());
        return SegmentResponse.from(segment, count);
    }
}