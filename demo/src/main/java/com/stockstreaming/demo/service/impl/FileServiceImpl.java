package com.stockstreaming.demo.service.impl;

import com.stockstreaming.demo.events.FileUploadedEvent;
import com.stockstreaming.demo.model.UploadStatus;
import com.stockstreaming.demo.model.UploadedFile;
import com.stockstreaming.demo.repository.UploadedFileRepository;
import com.stockstreaming.demo.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private UploadedFileRepository repo;

    @Autowired
    private ApplicationEventPublisher publisher;

    public UUID uploadFile(MultipartFile file) {

        UploadedFile f = UploadedFile.builder()
                .fileName(file.getOriginalFilename())
                .fileSize(file.getSize())
                .uploadedAt(LocalDateTime.now())
                .status(UploadStatus.UPLOADED)
                .build();

        f = repo.save(f);

        // Publish event
        publisher.publishEvent(new FileUploadedEvent(f.getId()));

        return f.getId();
    }
}

