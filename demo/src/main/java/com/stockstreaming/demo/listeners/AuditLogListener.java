package com.stockstreaming.demo.listeners;

import com.stockstreaming.demo.events.FileUploadedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
public class AuditLogListener {

    @Order(2)
    @EventListener
    public void auditLog(FileUploadedEvent event) {
        System.out.println("[Order 2] AuditLogListener → Logging file upload event");
    }

}
