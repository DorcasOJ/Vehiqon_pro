package com.vehiqon.features.insights.auditLog.consumer;

import com.vehiqon.common.exception.BadRequestException;
import com.vehiqon.features.insights.enums.PublishAction;
import com.vehiqon.features.insights.auditLog.dto.AuditLogDto;
import com.vehiqon.features.insights.auditLog.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuditLogConsumer {
    private final AuditLogService auditLogService;

    @Async("asyncTaskExecutor")
//    @EventListener
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT,
            fallbackExecution = true)
    public void consume(AuditLogDto.AuditEvent event) {
        if(event.userId() == null) {
            throw new BadRequestException("User Id is required");
        }
        if (event.publishAction() != PublishAction.AUDIT_LOG) {
            return;
        }
        try {
            auditLogService.log(event);
        } catch (Exception e) {
            log.error("Failed to process analytics event {}", event, e);
//            throw new BadRequestException(e.getMessage());
        }


    }

    @Async("asyncTaskExecutor")
    @EventListener
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void handleRollback(AuditLogDto.AuditEvent event) {
        if(event.userId() == null) {
            throw new BadRequestException("User Id is required");
        }
        if (event.publishAction() != PublishAction.AUDIT_LOG) {
            return;
        }
        try {
            auditLogService.logFailure(event);
        } catch (Exception e) {
            log.error("Failed to process analytics event {}", event, e);
//            throw new BadRequestException(e.getMessage());
        }
    }
}
