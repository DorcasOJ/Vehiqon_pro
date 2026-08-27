package com.vehiqon.features.carmgmt.service;

import com.vehiqon.features.carmgmt.dto.CarDocumentDto;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.util.List;

public interface StorageService {

        CarDocumentDto.StorageUploadResponse upload(
                String path,
                MultipartFile file
        );

        CarDocumentDto.StorageUploadResponse replace(
                String oldPath,
                String newPath,
                MultipartFile file
        );

        void delete(String path);

        void delete(List<String> paths);

        boolean exists(String path);

        String createSignedUrl(
                String path,
                Duration duration
        );

        List<String> createSignedUrls(
                List<String> paths,
                Duration duration
        );

}
