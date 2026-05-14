package com.wtc.chat_backend.service;

import com.wtc.chat_backend.model.dto.CustomerRequest;
import com.wtc.chat_backend.model.dto.CustomerResponse;
import com.wtc.chat_backend.model.dto.TimelineResponse;
import com.wtc.chat_backend.model.Customer;
import com.wtc.chat_backend.model.enums.CustomerStatus;
import com.wtc.chat_backend.repository.ConversationRepository;
import com.wtc.chat_backend.repository.CustomerRepository;
import com.wtc.chat_backend.repository.MessageRepository;
import com.wtc.chat_backend.repository.SegmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final SegmentRepository  segmentRepository;
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;

    // ─── LISTAR ──────────────────────────────────────────────────────────────

    public List<CustomerResponse> findAll() {
        return customerRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<CustomerResponse> findBySegment(String segmentId) {
        return customerRepository.findBySegmentId(segmentId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<CustomerResponse> findByTag(String tag) {
        return customerRepository.findByTagsContaining(tag)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Garante um {@link Customer} para o app na visão cliente (mesmo e-mail do login).
     * Se o operador já cadastrou esse e-mail no CRM, reutiliza o registro existente.
     *
     * @return id Mongo do cliente
     */
    public String ensurePortalProfileForClient(String name, String email) {
        return customerRepository.findByEmail(email)
                .map(Customer::getId)
                .orElseGet(() -> {
                    Customer c = new Customer();
                    c.setName(name);
                    c.setEmail(email);
                    c.setPhone(null);
                    c.setSegmentId(null);
                    c.setTags(Collections.emptyList());
                    c.setScore(0.0);
                    c.setCustomerStatus(CustomerStatus.ACTIVE);
                    c.setNotes(null);
                    return customerRepository.save(c).getId();
                });
    }

    // ─── BUSCAR POR ID ───────────────────────────────────────────────────────

    public CustomerResponse findById(String id) {
        Customer customer = getOrThrow(id);
        return toResponse(customer);
    }

    // ─── CRIAR ───────────────────────────────────────────────────────────────

    public CustomerResponse create(CustomerRequest req) {
        if (customerRepository.existsByEmail(req.email())) {
            throw new IllegalArgumentException("E-mail já cadastrado: " + req.email());
        }

        Customer customer = new Customer();
        applyRequest(customer, req);
        return toResponse(customerRepository.save(customer));
    }

    // ─── ATUALIZAR ───────────────────────────────────────────────────────────

    public CustomerResponse update(String id, CustomerRequest req) {
        Customer customer = getOrThrow(id);
        applyRequest(customer, req);
        return toResponse(customerRepository.save(customer));
    }

    // ─── DELETAR ─────────────────────────────────────────────────────────────

    public void delete(String id) {
        Customer customer = getOrThrow(id);
        customerRepository.delete(customer);
    }

    // ─── TIMELINE (Perfil 360°) ──────────────────────────────────────────────

    public TimelineResponse getTimeline(String id) {
        Customer customer = getOrThrow(id);
        String segmentName = resolveSegmentName(customer.getSegmentId());

        // Busca conversas do customer e pega as últimas 5 mensagens
        List<TimelineResponse.MessageSummary> lastMessages = conversationRepository
                .findByCustomerIdOrderByUpdatedAtDesc(customer.getId())
                .stream()
                .limit(3)
                .flatMap(conv -> messageRepository
                        .findTop5ByConversationIdOrderByCreatedAtDesc(conv.getId())
                        .stream())
                .map(m -> new TimelineResponse.MessageSummary(
                        m.getId(), m.getContent(), m.getCreatedAt()))
                .toList();

        return new TimelineResponse(
                customer.getId(), customer.getName(), customer.getEmail(),
                customer.getPhone(), segmentName, customer.getTags(),
                customer.getScore(), customer.getCustomerStatus(), customer.getCreatedAt(),
                lastMessages,
                Collections.emptyList(),  // campaigns — preenche no próximo módulo
                Collections.emptyList()   // tasks
        );
    }

    // ─── HELPERS PRIVADOS ────────────────────────────────────────────────────

    private Customer getOrThrow(String id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + id));
    }

    private void applyRequest(Customer customer, CustomerRequest req) {
        customer.setName(req.name());
        customer.setEmail(req.email());
        customer.setPhone(req.phone());
        customer.setSegmentId(req.segmentId());
        customer.setTags(req.tags());
        customer.setScore(req.score());
        customer.setCustomerStatus(req.customerStatus());
        customer.setNotes(req.notes());
    }

    private CustomerResponse toResponse(Customer customer) {
        String segmentName = resolveSegmentName(customer.getSegmentId());
        return CustomerResponse.from(customer, segmentName);
    }

    private String resolveSegmentName(String segmentId) {
        if (segmentId == null) return null;
        return segmentRepository.findById(segmentId)
                .map(s -> s.getName())
                .orElse(null);
    }


}