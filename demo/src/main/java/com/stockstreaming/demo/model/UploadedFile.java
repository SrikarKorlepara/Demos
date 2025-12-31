package com.stockstreaming.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Table(name = "uploaded_files")
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UploadedFile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String fileName;
    private long fileSize;

    private LocalDateTime uploadedAt;

    @Enumerated(EnumType.STRING)
    private UploadStatus status;

    private Integer lineCount;



    // getters & setters
}
