package com.services;

import com.models.SongMetaData;
import org.springframework.stereotype.Service;
import com.repository.SongRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SongService {
    SongRepository repository;

    public SongService(SongRepository repository) {
        this.repository = repository;
    }

    public SongMetaData saveSong(SongMetaData song){
        return repository.save(song);
    }

    public Optional<SongMetaData> findSong(Integer id){
        return repository.findById(id);
    }

    public void deleteSongs(List<Integer> ids){
        repository.deleteByIdList(ids);
    }
}
