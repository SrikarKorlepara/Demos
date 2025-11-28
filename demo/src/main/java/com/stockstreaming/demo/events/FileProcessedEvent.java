package com.stockstreaming.demo.events;


import java.util.UUID;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@Setter
@Builder
public class FileProcessedEvent {
    private final UUID fileId;
    private final int lineCount;
}
