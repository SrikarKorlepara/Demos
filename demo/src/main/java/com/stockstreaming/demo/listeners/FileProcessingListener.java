package com.stockstreaming.demo.listeners;

import com.stockstreaming.demo.events.FileProcessedEvent;
import com.stockstreaming.demo.events.FileProcessingEvent;
import com.stockstreaming.demo.events.FileUploadedEvent;
import com.stockstreaming.demo.model.UploadStatus;
import com.stockstreaming.demo.model.UploadedFile;
import com.stockstreaming.demo.repository.UploadedFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class FileProcessingListener {

    @Autowired
    private UploadedFileRepository repo;

    @Autowired
    private ApplicationEventPublisher publisher;

    @EventListener
    public void handle(FileProcessingEvent event) {

        UploadedFile file = repo.findById(event.fileId()).orElseThrow();

        file.setStatus(UploadStatus.PROCESSING);
        repo.save(file);

        // fake line count logic (just for demo)
        int lines = 10;

        // publish another event
        System.out.println("Processing file: " + file.getFileName());

        // trigger next event
        publisher.publishEvent(new FileProcessedEvent(file.getId(), lines));
    }
}

