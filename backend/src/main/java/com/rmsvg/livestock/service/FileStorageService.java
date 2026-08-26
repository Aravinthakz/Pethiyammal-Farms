package com.rmsvg.livestock.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path root;
    private final String publicBaseUrl;

    public FileStorageService(
            @Value("${app.upload-dir}") String uploadDir,
            @Value("${app.public-base-url}") String publicBaseUrl
    ) throws IOException {
        this.root = Path.of(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(this.root);
        this.publicBaseUrl = publicBaseUrl.replaceAll("/$", "");
    }

    public String store(MultipartFile file) {
        try {
            String ext = "";
            String original = file.getOriginalFilename();
            if (original != null && original.contains(".")) {
                ext = original.substring(original.lastIndexOf('.'));
            }
            String name = UUID.randomUUID() + ext;
            Files.copy(file.getInputStream(), root.resolve(name));
            return publicBaseUrl + "/uploads/" + name;
        } catch (IOException e) {
            throw new RuntimeException("Could not store file", e);
        }
    }
}
