package ru.ivvasch.niolibrary.service;

import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.ivvasch.niolibrary.configuration.MinioConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class MinioService {

    private final MinioClient minioClient;
    private final String bucket;

    public MinioService(MinioClient minioClient, MinioConfiguration minioConfiguration) {
        this.minioClient = minioClient;
        bucket = minioConfiguration.getMinioServer().bucket();
    }

    public GetObjectResponse getBookFromMinio(String name, String type) {
        boolean bucketExist;
        GetObjectResponse objectFromLibrary = null;
        try {
            bucketExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (bucketExist) {
                objectFromLibrary = minioClient.getObject(GetObjectArgs.builder().bucket(bucket).object(type + "/" + name).build());
            }
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            throw new RuntimeException(e);
        }
        return objectFromLibrary;
    }

    public void saveBookToMinio(MultipartFile file, String type) {
        InputStream inputStream;
        try {
            String originalFilename = file.getOriginalFilename();
            inputStream = file.getInputStream();
            minioClient.putObject(PutObjectArgs.builder().bucket(bucket)
                    .object(type + "/" + originalFilename)
                    .stream(inputStream, 0, -1)
                    .contentType("application/octet-stream").build());
            inputStream.close();
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isExistBook(String name) {
        Iterable<Result<Item>> results;
        boolean isExist = false;
        try {
            results = minioClient.listObjects(
                    ListObjectsArgs.builder().bucket(bucket).recursive(true).build());
            for (Result<Item> result : results) {
                String string = result.get().objectName().toLowerCase();
                int i = string.toLowerCase().lastIndexOf("/") + 1;
                isExist = string.toLowerCase().substring(i, string.length()).equals(name);
                if (isExist) {
                    break;
                }
            }
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            throw new RuntimeException(e);
        }
        return isExist;
    }
}
