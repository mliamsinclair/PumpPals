package com.pumppals.pumppalsapi.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.pumppals.pumppalsapi.exceptions.FileDownloadException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {
    @Value("${aws.bucket.name}")
    private String bucketName;

    private final AmazonS3 s3Client;

    public String uploadFile(MultipartFile multipartFile) throws IOException {
        // convert multipart file to a file
        File file = new File(multipartFile.getOriginalFilename());
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(multipartFile.getBytes());
        }

        // generate file name
        String fileName = generateFileName(multipartFile);

        // upload file
        PutObjectRequest request = new PutObjectRequest(bucketName, fileName, file);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("plain/" + FilenameUtils.getExtension(multipartFile.getOriginalFilename()));
        metadata.addUserMetadata("Title", "File Upload - " + fileName);
        metadata.setContentLength(file.length());
        request.setMetadata(metadata);
        s3Client.putObject(request);

        // delete file
        file.delete();

        return fileName;
    }

    public String uploadFile(MultipartFile multipartFile, String fileName) throws IOException {
        // convert multipart file to a file
        File file = new File(multipartFile.getOriginalFilename());
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(multipartFile.getBytes());
        }

        // upload file
        PutObjectRequest request = new PutObjectRequest(bucketName, fileName, file);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("plain/" + FilenameUtils.getExtension(multipartFile.getOriginalFilename()));
        metadata.addUserMetadata("Title", "File Upload - " + fileName);
        metadata.setContentLength(file.length());
        request.setMetadata(metadata);
        s3Client.putObject(request);

        // delete file
        file.delete();

        return fileName;
    }

    public Object downloadFile(String fileName) throws FileDownloadException, IOException {
        System.out.println("Downloading file: " + fileName);
        if (bucketIsEmpty()) {
            throw new FileDownloadException("Requested bucket does not exist or is empty");
        }
        S3Object object = s3Client.getObject(bucketName, fileName);
        Path pathObject = Paths.get(fileName);
        byte[] fileContent;
        try (S3ObjectInputStream s3is = object.getObjectContent();
                FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
            byte[] read_buf = new byte[1024];
            int read_len;
            while ((read_len = s3is.read(read_buf)) > 0) {
                fileOutputStream.write(read_buf, 0, read_len);
            }
            fileContent = Files.readAllBytes(pathObject);
        }
        Resource resource = new ByteArrayResource(fileContent);
        try {
            // Delete the file from local storage
            Files.deleteIfExists(pathObject);
        } catch (IOException e) {
            throw new IOException("Could not delete the file!", e);
        }
        return resource;
    }

    public boolean delete(String fileName) {
        File file = Paths.get(fileName).toFile();
        if (file.exists()) {
            file.delete();
            return true;
        }
        return false;
    }

    private boolean bucketIsEmpty() {
        ListObjectsV2Result result = s3Client.listObjectsV2(this.bucketName);
        if (result == null) {
            return false;
        }
        List<S3ObjectSummary> objects = result.getObjectSummaries();
        return objects.isEmpty();
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }

    public void deleteLocalFile(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
    }
}
