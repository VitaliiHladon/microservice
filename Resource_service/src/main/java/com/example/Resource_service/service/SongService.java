package com.example.Resource_service.service;


import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.example.Resource_service.models.SongInfo;
import com.example.Resource_service.models.SongMetaData;
import com.example.Resource_service.repository.FileMetaRepository;
import com.example.Resource_service.repository.S3repository;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
@Slf4j
public class SongService {
    @Autowired
    private S3repository s3repository;
    @Autowired
    RabbitMQSender sender;
    @Autowired
    private FileMetaRepository fileMetaRepository;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    public SongInfo upload(MultipartFile file) throws IOException {

        if (file.isEmpty())
            throw new IllegalStateException("Cannot upload empty file");

        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));

        String path = String.format("%s/%s", bucketName, UUID.randomUUID());
        String fileName = String.format("%s", file.getOriginalFilename());

        PutObjectResult putObjectResult = s3repository.uploadFile(
                path, fileName, Optional.of(metadata), file.getInputStream());
        var metaInfo=fileMetaRepository.save(new SongInfo(fileName, path, putObjectResult.getMetadata().getVersionId()));
        sender.send(String.valueOf(metaInfo.getId()));
        return metaInfo;

    }

    public S3Object download(int id,String range)  {
        var optionalInfo=fileMetaRepository.findById(id);
        if (optionalInfo.isEmpty())
            return null;
        SongInfo fileMeta = optionalInfo.get();
        if(range==null)
            return s3repository.downloadFile(fileMeta.getFilePath(), fileMeta.getFileName());

        String[] ranges = range.split("=")[1].split("-");
        long start = Long.parseLong(ranges[0]);
        long end =  Long.parseLong(ranges[1]);

        return s3repository.downloadFile(fileMeta.getFilePath(), fileMeta.getFileName(),start,end);
    }

    public void delete(List<Integer> ids) {
        var fileMeta = fileMetaRepository.findAllById(ids);
        for (SongInfo info:fileMeta){
            s3repository.deleteObject(info.getFilePath(), info.getFileName());
        }
        fileMetaRepository.deleteByIdList(ids);
    }

    public Object getSongMetaData(Integer id){
        var obj=download(id,null);
        var metaData=obj.getObjectContent().getDelegateStream();
        var songMetaData=new SongMetaData();
        songMetaData.setResourceId(id);
        return retrieveMetaData(metaData,songMetaData);
    }
    public boolean isFileMP3(MultipartFile file) throws IOException{
        Detector detector = new DefaultDetector();
        TikaInputStream tikaInputStream = TikaInputStream.get(file.getInputStream());
        MediaType mediaType = detector.detect(tikaInputStream, new Metadata());
        tikaInputStream.close();
        if (mediaType.equals(MediaType.audio("mpeg")) || mediaType.equals(MediaType.audio("mp3"))) {
           return true;
        } else {
           return false;
        }
    }

    public SongMetaData retrieveMetaData(InputStream stream,SongMetaData songMetaData){
        try {
            Mp3Parser parser = new Mp3Parser();
            Metadata metadata = new Metadata();
            BodyContentHandler handler = new BodyContentHandler();
            parser.parse(stream, handler, metadata, new ParseContext());

            songMetaData.setAlbum(metadata.get("xmpDM:album"));
            songMetaData.setArtist(metadata.get("xmpDM:artist"));
            songMetaData.setLength(metadata.get("xmpDM:duration"));
            songMetaData.setName(metadata.get("dc:title"));

            return  songMetaData;
        } catch (Exception e) {
            log.error("Error processing input", e);
        }
        return null;
    }

}
