package com.ayubyusuf.memoriavault;

import com.ayubyusuf.memoriavault.controller.PhotoController;
import com.ayubyusuf.memoriavault.exception.PhotoNotFoundException;
import com.ayubyusuf.memoriavault.model.Photo;
import com.ayubyusuf.memoriavault.service.PhotoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PhotoController.class)
public class PhotoControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PhotoService photoService;

	@Test
	void getAllPhotosTest() throws Exception {
		Photo photo = new Photo("test.jpg", "image/jpeg", new Date());
		photo.setId(1L); // Set the ID of the photo
		List<Photo> allPhotos = Collections.singletonList(photo);
		when(photoService.getAllPhotos()).thenReturn(allPhotos);

		mockMvc.perform(get("/photos")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].fileName").value("test.jpg"));
	}

	@Test
	void uploadPhotoTest() throws Exception {
		MockMultipartFile file = new MockMultipartFile(
				"file",
				"test.jpg",
				MediaType.IMAGE_JPEG_VALUE,
				"test image content".getBytes()
		);
		Photo photo = new Photo("test.jpg", "image/jpeg", new Date());
		photo.setId(1L);

		when(photoService.savePhoto(any(MultipartFile.class))).thenReturn(photo);

		mockMvc.perform(multipart("/photos/upload").file(file))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.fileName").value("test.jpg"));
	}

	@Test
	void deletePhotoTest() throws Exception {
		Long photoId = 1L;
		doNothing().when(photoService).deletePhoto(photoId);

		mockMvc.perform(delete("/photos/{id}", photoId))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Photo deleted successfully")));

		verify(photoService, times(1)).deletePhoto(photoId);
	}

	@Test
	void deleteNonExistentPhotoTest() throws Exception {
		Long nonExistentPhotoId = 99L;
		doThrow(new PhotoNotFoundException("Photo not found with id: " + nonExistentPhotoId))
				.when(photoService).deletePhoto(nonExistentPhotoId);

		mockMvc.perform(delete("/photos/{id}", nonExistentPhotoId))
				.andExpect(status().isNotFound())
				.andExpect(content().string(containsString("Photo not found with id: " + nonExistentPhotoId)));

		verify(photoService, times(1)).deletePhoto(nonExistentPhotoId);
	}

	@Test
	void getPhotoByIdTest() throws Exception {
		Long validPhotoId = 1L;
		Photo photo = new Photo("test.jpg", "image/jpeg", new Date());
		photo.setId(validPhotoId);
		when(photoService.getPhotoById(validPhotoId)).thenReturn(photo);

		mockMvc.perform(get("/photos/{id}", validPhotoId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.fileName").value("test.jpg"));
	}
}
