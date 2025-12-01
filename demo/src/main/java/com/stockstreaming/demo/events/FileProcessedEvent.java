package com.stockstreaming.demo.events;


import java.util.UUID;

import com.stockstreaming.demo.model.UploadStatus;
import lombok.*;


@Builder
public record FileProcessedEvent(UUID fileId, int lineCount, UploadStatus status) {
}
