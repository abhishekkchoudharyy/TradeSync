package com.abhishekkchoudharyy.tradesync.service;

import org.springframework.web.multipart.MultipartFile;

public interface DocumentStorageService {

    String storeDocument(MultipartFile file,String documentType);
}
