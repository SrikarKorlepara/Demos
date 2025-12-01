package com.stockstreaming.demo.listeners;

import com.stockstreaming.demo.events.FileProcessedEvent;
import com.stockstreaming.demo.events.FileProcessingEvent;
import com.stockstreaming.demo.events.FileStartProcessingEvent;
import com.stockstreaming.demo.model.UploadStatus;
import com.stockstreaming.demo.model.UploadedFile;
import com.stockstreaming.demo.repository.UploadedFileRepository;
import com.stockstreaming.demo.service.impl.FileProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileProcessingListener {
    private final FileProcessingService service;

    @Async
    @EventListener
    public void handleStartProcessing(FileStartProcessingEvent event) {
        service.processFileStart(event);
    }

    @Async
    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT,
            condition = "#event.stage == T(com.stockstreaming.demo.model.UploadStatus).PROCESSING"
    )
    public void handleContinueProcessing(FileProcessingEvent event) {
        service.processFileContinue(event.fileId());
    }
}

