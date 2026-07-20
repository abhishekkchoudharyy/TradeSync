package com.abhishekkchoudharyy.tradesync.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


@Service
public class LocalDocumentStorageService implements DocumentStorageService {

    private final Path storageDirectory = Paths.get("uploads");

    public LocalDocumentStorageService() {
        try {
            // Automatically creates the "uploads" folder in your project root when the app starts
            Files.createDirectories(storageDirectory);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize local storage directory", e);
        }
    }

    @Override
    public String storeDocument(MultipartFile file, String documentType) {
        try {
            // Generate a secure, unique filename
            String uniqueFileName = documentType + "_" + UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path targetLocation = this.storageDirectory.resolve(uniqueFileName);

            // Save the raw file stream to disk
            Files.copy(file.getInputStream(), targetLocation);

            return targetLocation.toString();
        } catch (IOException ex) {
            throw new RuntimeException("Failed to store file locally: " + file.getOriginalFilename(), ex);
        }
    }
}
