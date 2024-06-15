package com.ayubyusuf.memoriavault.controller;

import com.ayubyusuf.memoriavault.service.PhotoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/photos/download")
public class DownloadController {

	private final PhotoService photoService;

	@Autowired
	public DownloadController(PhotoService photoService) {
		this.photoService = photoService;
	}

	@GetMapping("/{id}")
	public ResponseEntity<Resource> downloadPhoto(@PathVariable Long id, HttpServletRequest request) {
		// Load photo metadata by id
		Resource resource = photoService.loadFileAsResourceById(id);

		// Try to determine file's content type
        String contentType;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			// Fallback to the default content type if type could not be determined
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}
}
