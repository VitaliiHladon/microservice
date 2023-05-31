package com.example.Resource_service.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity
@Getter
@Setter
public class SongInfo {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String fileName;

    @Column
    private String filePath;

    @Column
    private String version;

    public SongInfo(String fileName, String filePath, String version) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.version = version;
    }
}
