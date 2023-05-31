package com.example.Resource_service.repository;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class S3repository  {
    @Autowired
    private AmazonS3 amazonS3;
    @Autowired
    private FileMetaRepository fileMetaRepository;

    public PutObjectResult uploadFile(
            String path,
            String fileName,
            Optional<Map<String, String>> optionalMetaData,
            InputStream inputStream) {
        ObjectMetadata objectMetadata = new ObjectMetadata();

        optionalMetaData.ifPresent(map -> {
            if (!map.isEmpty()) {
                map.forEach(objectMetadata::addUserMetadata);
            }
        });
        log.debug("Path: " + path + ", FileName:" + fileName);
        return amazonS3.putObject(path, fileName, inputStream, objectMetadata);
    }

    public S3Object downloadFile(String path, String fileName) {
        return amazonS3.getObject(path, fileName);
    }
    public S3Object downloadFile(String path, String fileName,long start, long end) {
        GetObjectRequest getObjectRequest = new GetObjectRequest(path, fileName)
                .withRange(start, end);
        S3Object objectPortion = amazonS3.getObject(getObjectRequest);
        return objectPortion;
    }

    public ObjectMetadata getMetaData(String path, String fileName){
        return amazonS3.getObjectMetadata(path, fileName);
    }

    public void deleteObject(String path, String fileName){
        amazonS3.deleteObject(path,fileName);

    }
}
