package com.stockstreaming.demo.events;

import java.util.UUID;

public record FileStartProcessingEvent(UUID fileId) {
}
