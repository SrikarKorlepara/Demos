package com.stockstreaming.demo.events;


import lombok.*;

import java.util.UUID;

@Getter
@ToString
@AllArgsConstructor
@Setter
@Builder
public class FileUploadedEvent {
    private final UUID fileId;

    public UUID getFileId() { return fileId; }
}

