package com.ayubyusuf.memoriavault.controller;

import com.ayubyusuf.memoriavault.dto.PhotoDTO;
import com.ayubyusuf.memoriavault.exception.PhotoNotFoundException;
import com.ayubyusuf.memoriavault.model.Photo;
import com.ayubyusuf.memoriavault.service.PhotoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/photos")
public class PhotoController {

	private static final Logger logger = LoggerFactory.getLogger(PhotoController.class);
	private final PhotoService photoService;

	@Autowired
	public PhotoController(PhotoService photoService) {
		this.photoService = photoService;
	}

	@PostMapping("/upload")
	public ResponseEntity<PhotoDTO> uploadPhoto(@RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) {
			logger.error("Upload failed: Received an empty file");
			return ResponseEntity.badRequest().body(null);
		}

		try {
			Photo photo = photoService.savePhoto(file);
			PhotoDTO dto = convertToDto(photo);
			return ResponseEntity.ok(dto);
		} catch (Exception e) {
			logger.error("Upload failed: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping
	public ResponseEntity<List<PhotoDTO>> getAllPhotos() {
		List<PhotoDTO> photosDTO = photoService.getAllPhotos().stream()
				.map(this::convertToDto)
				.collect(Collectors.toList());
		return ResponseEntity.ok(photosDTO);
	}

	private PhotoDTO convertToDto(Photo photo) {
		PhotoDTO dto = new PhotoDTO();
		dto.setId(photo.getId());
		dto.setFileName(photo.getFileName());
		dto.setFileType(photo.getFileType());
		dto.setUploadDate(photo.getUploadDate());
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/photos/download/")
				.path(photo.getId().toString())
				.toUriString();
		dto.setFileDownloadUri(fileDownloadUri);
		return dto;
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletePhoto(@PathVariable Long id) {
		try {
			photoService.deletePhoto(id);
			String message = "Photo deleted successfully";
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.TEXT_PLAIN);
			return new ResponseEntity<>(message, headers, HttpStatus.OK);
		} catch (PhotoNotFoundException e) {
			logger.error("Error deleting photo: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error deleting photo: " + e.getMessage());
		} catch (Exception e) {
			logger.error("Error deleting photo: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting photo: " + e.getMessage());
		}
	}
	@GetMapping("/{id}")
	public ResponseEntity<PhotoDTO> getPhoto(@PathVariable Long id) {
		Photo photo = photoService.getPhotoById(id); // assuming this method exists in your service
		PhotoDTO photoDTO = convertToDto(photo);
		return ResponseEntity.ok(photoDTO);
	}


}
