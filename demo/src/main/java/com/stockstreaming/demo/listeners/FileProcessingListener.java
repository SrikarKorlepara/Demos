package com.stockstreaming.demo.listeners;

import com.stockstreaming.demo.events.FileProcessedEvent;
import com.stockstreaming.demo.events.FileProcessingEvent;
import com.stockstreaming.demo.events.FileStartProcessingEvent;
import com.stockstreaming.demo.model.UploadStatus;
import com.stockstreaming.demo.model.UploadedFile;
import com.stockstreaming.demo.repository.UploadedFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class FileProcessingListener {

    @Autowired
    private UploadedFileRepository repo;

    @Autowired
    private ApplicationEventPublisher publisher;

    @EventListener
    @Transactional
    public void handleStartProcessing(FileStartProcessingEvent event) {

        UploadedFile file = repo.findById(event.fileId()).orElseThrow();

        file.setStatus(UploadStatus.PROCESSING);
        repo.save(file);

        // publish another event
        System.out.println("Processing file: " + file.getFileName());

        // trigger next event
        publisher.publishEvent(new FileProcessingEvent(file.getId(), UploadStatus.PROCESSING));
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleContinueProcessing(FileProcessingEvent event) {

        UploadedFile file = repo.findById(event.fileId()).orElseThrow();

        // fake line count logic (just for demo)
        int lines = 20;
        // need to call for work here

        // publish another event
        System.out.println("Continuing processing file: " + file.getFileName());
        file.setStatus(UploadStatus.COMPLETED);
        file.setLineCount(lines);

        repo.save(file);

        System.out.println("File summary updated: " + file.getFileName());
        // trigger next event
        publisher.publishEvent(new FileProcessedEvent(file.getId(), lines,UploadStatus.COMPLETED));
    }
}

