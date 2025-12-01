package com.stockstreaming.demo.events;

import com.stockstreaming.demo.model.UploadStatus;
import lombok.Builder;

import java.util.UUID;

@Builder
public record FileProcessingEvent(UUID fileId, UploadStatus stage) {
}
