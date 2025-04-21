package com.cottongallery.backend.item.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class S3StorageService {

    private final S3Client s3Client;

    @Value("${aws.bucket-name}")
    private String bucketName;

    public void deleteFile(String filePath) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(filePath)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
    }

    public String fileUpload(MultipartFile uploadFile) {
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "/";
        String fullPath = datePath + UUID.randomUUID().toString() + "_" + uploadFile.getOriginalFilename();

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fullPath)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(uploadFile.getBytes()));

            log.debug("파일 업로드 성공");

        } catch (IOException e) {
            throw new RuntimeException("Error reading file data", e);
        }

        return fullPath;
    }

    public ResponseInputStream<GetObjectResponse> downloadFile(String filePath) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filePath)
                    .build();

            return s3Client.getObject(getObjectRequest);

        } catch (S3Exception e) {
            throw new RuntimeException("다운로드 중 에러가 발생했습니다.", e);
        }
    }
}