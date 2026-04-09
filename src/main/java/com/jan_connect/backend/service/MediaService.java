package com.jan_connect.backend.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class MediaService {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.s3.cdn-based-url}")
    private String cdnBaseUrl;

    // Uploads an image to S3 and returns the public CDN URL.
    // The file is renamed to a UUID to prevent naming collisions
    // and to avoid exposing original filenames.
    public String uploadImage(MultipartFile file) throws Exception {

        String originalName = file.getOriginalFilename();
        String extension = originalName != null && originalName.contains(".")
                ? originalName.substring(originalName.lastIndexOf("."))
                : ".jpg";

        String key = "posts/" + UUID.randomUUID() + extension;

        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(putRequest,
                RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        return cdnBaseUrl + "/" + key;
    }
}
