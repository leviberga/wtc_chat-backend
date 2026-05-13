package com.wtc.chat_backend.audit;

import com.wtc.chat_backend.model.AuditLog;
import com.wtc.chat_backend.model.enums.AuditAction;
import com.wtc.chat_backend.model.enums.AuditEntity;
import com.wtc.chat_backend.repository.AuditLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {

    private final AuditLogRepository auditLogRepository;

    @AfterReturning("execution(* com.wtc.chat_backend.controller.*.*(..))")
    public void audit(JoinPoint joinPoint) {
        try {
            String userId        = getCurrentUserId();
            String ip            = getCurrentIp();
            String methodName    = joinPoint.getSignature().getName();
            String controllerName = joinPoint.getTarget().getClass().getSimpleName();

            AuditAction action = resolveAction(methodName, controllerName);
            AuditEntity entity = resolveEntity(controllerName);

            AuditLog auditLog = new AuditLog();
            auditLog.setUserId(userId);
            auditLog.setAction(action);
            auditLog.setEntity(entity);
            auditLog.setIp(ip);

            auditLogRepository.save(auditLog);
        } catch (Exception e) {
            log.warn("Falha ao registrar audit log: {}", e.getMessage());
        }
    }

    private String getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            return auth.getName();
        }
        return "anonymous";
    }

    private String getCurrentIp() {
        try {
            ServletRequestAttributes attrs =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                HttpServletRequest request = attrs.getRequest();
                String forwarded = request.getHeader("X-Forwarded-For");
                return forwarded != null ? forwarded.split(",")[0] : request.getRemoteAddr();
            }
        } catch (Exception ignored) {}
        return "unknown";
    }

    private AuditAction resolveAction(String methodName, String controllerName) {
        return switch (methodName) {
            case "login"             -> AuditAction.LOGIN;
            case "logout"            -> AuditAction.LOGOUT;
            case "register"          -> AuditAction.REGISTER;
            case "send"              -> AuditAction.SEND_MESSAGE;
            case "findById",
                 "findAll",
                 "getInbox",
                 "getTimeline",
                 "getByConversation" -> AuditAction.READ_MESSAGE;
            case "updateStatus"      -> AuditAction.READ_MESSAGE;
            case "sendNow"           -> AuditAction.SEND_CAMPAIGN;
            case "schedule"          -> AuditAction.SCHEDULE_CAMPAIGN;
            case "create" -> switch (controllerName) {
                case "CampaignController" -> AuditAction.CREATE_CAMPAIGN;
                case "SegmentController"  -> AuditAction.CREATE_SEGMENT;
                default                   -> AuditAction.CREATE_CUSTOMER;
            };
            case "update" -> switch (controllerName) {
                case "SegmentController" -> AuditAction.UPDATE_SEGMENT;
                default                  -> AuditAction.UPDATE_CUSTOMER;
            };
            case "delete" -> switch (controllerName) {
                case "SegmentController" -> AuditAction.UPDATE_SEGMENT;
                default                  -> AuditAction.DELETE_CUSTOMER;
            };
            default -> AuditAction.LOGIN;
        };
    }

    private AuditEntity resolveEntity(String controllerName) {
        if (controllerName.contains("Customer"))     return AuditEntity.CUSTOMER;
        if (controllerName.contains("Message"))      return AuditEntity.MESSAGE;
        if (controllerName.contains("Campaign"))     return AuditEntity.CAMPAIGN;
        if (controllerName.contains("Segment"))      return AuditEntity.SEGMENT;
        if (controllerName.contains("Conversation")) return AuditEntity.CONVERSATION;
        return AuditEntity.USER;
    }
}