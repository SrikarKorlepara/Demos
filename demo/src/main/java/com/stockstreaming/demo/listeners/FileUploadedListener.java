package com.stockstreaming.demo.listeners;

import com.stockstreaming.demo.events.FileProcessingEvent;
import com.stockstreaming.demo.events.FileUploadedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class FileUploadedListener {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Order(3)  // runs after all the new ones
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onFileUploaded(FileUploadedEvent event) {
        System.out.println("[Order 4] FileUploadedListener → Starting event chain");

        // trigger next event
        publisher.publishEvent(new FileProcessingEvent(event.fileId()));
    }
}

