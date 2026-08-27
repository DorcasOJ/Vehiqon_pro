package com.vehiqon.features.carmgmt.service.impl;

import com.vehiqon.common.exception.BadRequestException;
import com.vehiqon.config.SupabaseProperties;
import com.vehiqon.features.carmgmt.dto.CarDocumentDto;
import com.vehiqon.features.carmgmt.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class StorageServiceImpl implements StorageService {
    private final RestClient restClient;
    private final SupabaseProperties properties;

    public StorageServiceImpl(SupabaseProperties properties) {
        this.properties = properties;
        this.restClient = RestClient.builder()
                .baseUrl(properties.getUrl() + "/storage/v1/")
                .defaultHeader("Authorization", "Bearer" + properties.getServiceKey())
                .defaultHeader("apiKey", properties.getServiceKey())
                .build();
    }

    @Override
    public CarDocumentDto.StorageUploadResponse upload(String path, MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            String contentType = file.getContentType() != null ?
                    file.getContentType() : MediaType.APPLICATION_OCTET_STREAM_VALUE;
            restClient.post()
                    .uri("/object/" + properties.getBucket() + "/" + path)
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(new ByteArrayResource(bytes))
                    .retrieve()
                    .toBodilessEntity();
//            String.format("%s/storage/v1/object/public/%s/%s", supabaseUrl, bucketName, path);)
           return new CarDocumentDto.StorageUploadResponse(path, file.getOriginalFilename(),file.getSize(), contentType);
        } catch (IOException e) {
            log.error("Failed to read file bytes for path: {}", path, e);
            throw new BadRequestException("Failed to read upload file contents" +e.getMessage());
        } catch (Exception e) {
            log.error("Supabase file upload failed for path: {}", path, e);
            throw new BadRequestException("Failed to upload file to storage" +e.getMessage());
        }
    }

    @Override
    public CarDocumentDto.StorageUploadResponse replace(String oldPath, String newPath, MultipartFile file) {
        if (oldPath != null && oldPath.isBlank()) {
            delete(oldPath);
        }
        return upload(newPath, file);
    }

    @Override
    public void delete(String path) {
        if (path == null || path.isBlank()) return;
        try {
            restClient.delete()
                    .uri("/object/" + properties.getBucket()+ "/" + path)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            log.error("Failed to delete file from Supabase storage: {}", path, e);
        }
    }

    @Override
    public void delete(List<String> paths) {
        if (paths == null || paths.isEmpty()) return;
        try {
            restClient.post()
                    .uri("/object/" + properties.getBucket())
                    .body(Map.of("prefixes", paths))
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            log.error("Failed to batch delete file from Supabase storage: {}", paths, e);
        }
    }

    @Override
    public boolean exists(String path) {
       try {
           var response = restClient.get()
                   .uri("/object/info/public/" + properties.getBucket()+ "/" + path)
                   .retrieve()
                   .toBodilessEntity();
           return response.getStatusCode().is2xxSuccessful();
       } catch (Exception e) {
           log.error("{} do not exist in storage", path);
           return false;
       }
    }

    @Override
    public String createSignedUrl(String path, Duration duration) {
        try {
            Map<?,?> response = restClient.post()
                    .uri("/object/sign/" + properties.getBucket()+ "/" + path)
                    .retrieve()
                    .body(Map.class);
            if (response != null && response.containsKey("signedURL")) {
                return properties.getUrl() + "/storage/v1" + response.get("signedURL");
            }
            throw new BadRequestException("Failed to retrieve signed URL from Supabase response");
        } catch (Exception e) {
            log.error("Error creating signed URL for path: {}", path, e);
            throw new BadRequestException("Failed to generate signed URL. "+ e.getMessage());
        }
    }

    @Override
    public List<String> createSignedUrls(List<String> paths, Duration duration) {
        return paths.stream()
                .map(path -> createSignedUrl(path,  duration))
                .toList();
    }
}
