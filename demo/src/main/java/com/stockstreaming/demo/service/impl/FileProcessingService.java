package com.stockstreaming.demo.service.impl;

import com.stockstreaming.demo.events.FileProcessedEvent;
import com.stockstreaming.demo.events.FileProcessingEvent;
import com.stockstreaming.demo.events.FileStartProcessingEvent;
import com.stockstreaming.demo.model.UploadStatus;
import com.stockstreaming.demo.model.UploadedFile;
import com.stockstreaming.demo.repository.UploadedFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileProcessingService {

    private final UploadedFileRepository repo;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public void processFileStart(FileStartProcessingEvent event) {
        UploadedFile file = repo.findById(event.fileId()).orElseThrow();
        System.out.println("Received FileStartProcessingEvent for fileId and status: " + event.fileId() + " - " + file.getStatus());

        file.setStatus(UploadStatus.PROCESSING);
        repo.save(file);

        System.out.println("Processing file with id and status : " + file.getId() + " - " + file.getStatus());

        publisher.publishEvent(new FileProcessingEvent(file.getId(), UploadStatus.PROCESSING));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processFileContinue(UUID fileId)  {
        System.out.println("Received FileProcessingEvent for fileId: " + fileId);
        UploadedFile file = repo.findById(fileId).orElseThrow();

        int lines = 20;
        try {
            Thread.sleep(10000);
            // Simulate processing time
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Continuing processing file: " + file.getFileName());
        file.setStatus(UploadStatus.COMPLETED);
        file.setLineCount(lines);

        repo.save(file);

        System.out.println("File summary updated: " + file.getFileName());
        publisher.publishEvent(new FileProcessedEvent(file.getId(), lines, UploadStatus.COMPLETED));
    }
}
