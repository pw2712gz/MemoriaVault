package com.ayubyusuf.memoriavault;

import com.ayubyusuf.memoriavault.controller.DownloadController;
import com.ayubyusuf.memoriavault.service.PhotoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class DownloadControllerTests {

    private MockMvc mockMvc;

    @Mock
    private PhotoService photoService;

    @BeforeEach
    void setUp() {
        openMocks(this);
        DownloadController downloadController = new DownloadController(photoService);
        mockMvc = MockMvcBuilders.standaloneSetup(downloadController).build();
    }

    @Test
    void downloadPhotoAsPngTest() throws Exception {
        Resource pngImage = new ClassPathResource("test-photo.png");
        when(photoService.loadFileAsResourceById(anyLong())).thenReturn(pngImage);

        mockMvc.perform(get("/photos/download/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG))
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"" + pngImage.getFilename() + "\""));
    }

    @Test
    void downloadPhotoAsJpegTest() throws Exception {
        Resource jpegImage = new ClassPathResource("test-photo.jpg");
        when(photoService.loadFileAsResourceById(anyLong())).thenReturn(jpegImage);

        mockMvc.perform(get("/photos/download/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"" + jpegImage.getFilename() + "\""));
    }


}
