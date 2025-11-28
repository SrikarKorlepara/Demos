package com.stockstreaming.demo.repository;

import com.stockstreaming.demo.model.UploadedFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UploadedFileRepository extends JpaRepository<UploadedFile, UUID> {}

