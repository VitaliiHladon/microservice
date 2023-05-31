package com.repository;

import com.models.SongMetaData;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface SongRepository extends CrudRepository<SongMetaData,Integer> {

    @Modifying
    @Query("DELETE FROM SongMetaData e WHERE e.songId IN :ids")
    void deleteByIdList(@Param("ids") List<Integer> ids);

    Optional<List<SongMetaData>> findByResourceId(Integer resourcesId);
}
