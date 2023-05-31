package com.controllers;

import com.models.SongMetaData;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.services.SongService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class SongController {
    private SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping("/songs")
    public String testMethod(){
        return "Get to song service";
    }

    @PostMapping("/songs")
    public ResponseEntity<Object> saveSong(@RequestBody SongMetaData song){
        log.info("Received for storage metaData: "+song.toString());
        SongMetaData savedSong=songService.saveSong(song);
        Map<String,Integer> body=new HashMap<>();
        body.put("id",savedSong.getSongId());
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @GetMapping("/songs/{id}")
    public ResponseEntity<Object> findSong(@PathVariable Integer id){
        var songOptional=songService.findSong(id);
        if(songOptional.isEmpty())
            return new ResponseEntity<>("The song metadata with the specified id does not exist", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(songOptional.get(), HttpStatus.OK);
    }
    @DeleteMapping("/songs")
    public ResponseEntity<Object> deleteSong(@RequestParam List<Integer> ids){
        songService.deleteSongs(ids);
        return new ResponseEntity(ids,HttpStatus.OK);
    }
}
