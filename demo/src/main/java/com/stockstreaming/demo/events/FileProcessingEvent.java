package com.stockstreaming.demo.events;

import lombok.Builder;

import java.util.UUID;

@Builder
public record FileProcessingEvent(UUID fileId) {
}
