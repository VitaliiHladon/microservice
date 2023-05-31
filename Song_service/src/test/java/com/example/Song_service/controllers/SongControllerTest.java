package com.example.Song_service.controllers;

import com.models.SongMetaData;
import com.services.SongService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Optional;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class SongControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private SongService service;

    @Test
    public void returnSongMetaDataWhenGivenId() throws Exception{
        SongMetaData song=new SongMetaData();
        song.setName("test");

        when(service.findSong(1)).thenReturn(Optional.of(song));

        mvc.perform(get("/songs/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void return404WhenGivenId() throws Exception{

        when(service.findSong(1)).thenReturn(Optional.empty());

        mvc.perform(get("/songs/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));

    }
}
