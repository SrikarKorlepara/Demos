package com.stockstreaming.demo.listeners;

import com.stockstreaming.demo.events.FileUploadedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
public class SecurityCheckListener {

    @Order(1)
    @EventListener
    public void securityCheck(FileUploadedEvent event) {
        System.out.println("[Order 1] SecurityCheckListener → Checking file safety");
    }
}

