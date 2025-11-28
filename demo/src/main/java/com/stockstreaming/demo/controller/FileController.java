package com.stockstreaming.demo.controller;

import com.stockstreaming.demo.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService service;

    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) {
        UUID id = service.uploadFile(file);
        return "File uploaded with ID: " + id;
    }
}

