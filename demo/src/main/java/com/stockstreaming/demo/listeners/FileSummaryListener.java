package com.stockstreaming.demo.listeners;

import com.stockstreaming.demo.events.FileProcessedEvent;
import com.stockstreaming.demo.model.UploadStatus;
import com.stockstreaming.demo.model.UploadedFile;
import com.stockstreaming.demo.repository.UploadedFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class FileSummaryListener {

    @Autowired
    private UploadedFileRepository repo;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(FileProcessedEvent event) {

        UploadedFile file = repo.findById(event.fileId()).orElseThrow();
        file.setStatus(UploadStatus.COMPLETED);
        file.setLineCount(event.lineCount());

        repo.save(file);

        System.out.println("File summary updated: " + file.getFileName());
    }
}

