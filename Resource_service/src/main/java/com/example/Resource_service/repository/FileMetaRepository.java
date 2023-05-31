package com.example.Resource_service.repository;

import com.example.Resource_service.models.SongInfo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface FileMetaRepository extends CrudRepository<SongInfo,Integer> {
    @Modifying
    @Query("DELETE FROM SongInfo e WHERE e.id IN :ids")
    void deleteByIdList(@Param("ids") List<Integer> ids);
}
