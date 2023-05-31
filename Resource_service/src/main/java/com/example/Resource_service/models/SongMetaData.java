package com.example.Resource_service.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Year;

@Data
@NoArgsConstructor
public class SongMetaData {
    private Integer songId;
    private String name;
    private String artist;
    private String album;
    private String length;
    private Integer resourceId;
    private Year year;

}
