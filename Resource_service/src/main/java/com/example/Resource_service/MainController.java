package com.example.Resource_service;

import com.amazonaws.services.s3.model.S3Object;
import com.example.Resource_service.models.SongMetaData;
import com.example.Resource_service.service.SongService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MainController {
    private SongService service;

    public MainController(SongService service) {
        this.service = service;
    }

    @PostMapping(value = "/resources",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> upload(@RequestParam("file")MultipartFile file) throws Exception {
        if (!service.isFileMP3(file))
            return new ResponseEntity<>("Validation failed or request body is invalid MP3",HttpStatus.BAD_REQUEST);
        var savedSong = service.upload(file);
        Map<String, Integer> body = new HashMap<>();
        body.put("id", savedSong.getId());
        return new ResponseEntity<>(body,HttpStatus.OK);
    }


    @GetMapping("resources/{id}")
    public ResponseEntity<Object> download(@PathVariable Integer id,@RequestHeader(value = "Range",required = false) String range) throws Exception {
        S3Object s3Object = service.download(id,range);
        if (s3Object==null)
            return new ResponseEntity<>("The resource with the specified id does not exist", HttpStatus.NOT_FOUND);
        String contentType = s3Object.getObjectMetadata().getContentType();
        var bytes = s3Object.getObjectContent().readAllBytes();
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.valueOf(contentType));
        header.setContentLength(bytes.length);
        var status=HttpStatus.OK;
        if (range!=null)
            status=HttpStatus.PARTIAL_CONTENT;
        return new ResponseEntity<>(bytes, status);
    }

    @DeleteMapping("/resources")
    public ResponseEntity<Object> deleteSong(@RequestParam List<Integer> id){
        service.delete(id);
        return new ResponseEntity(id,HttpStatus.OK);
    }

    @GetMapping("/resources/metaData/{id}")
    public ResponseEntity<Object> getMetaData(@PathVariable Integer id,@RequestHeader(value = "Range",required = false) String range) throws Exception {
        var songMetaData=service.getSongMetaData(id);
        return new ResponseEntity<>(songMetaData, HttpStatus.OK);
    }

}
