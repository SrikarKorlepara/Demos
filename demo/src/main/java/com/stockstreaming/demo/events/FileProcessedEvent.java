package com.stockstreaming.demo.events;


import java.util.UUID;

import lombok.*;


@Builder
public record FileProcessedEvent(UUID fileId, int lineCount) {
}
