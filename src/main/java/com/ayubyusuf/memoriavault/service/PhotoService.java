package com.ayubyusuf.memoriavault.service;

import com.ayubyusuf.memoriavault.exception.PhotoNotFoundException;
import com.ayubyusuf.memoriavault.exception.StorageException;
import com.ayubyusuf.memoriavault.model.Photo;
import com.ayubyusuf.memoriavault.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final Path fileStorageLocation;

    @Autowired
    public PhotoService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
        this.fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new StorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public Photo savePhoto(MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            if (fileName.contains("..")) {
                throw new StorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            Photo photo = new Photo(fileName, file.getContentType(), new Date());
            return photoRepository.save(photo);
        } catch (IOException ex) {
            throw new StorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public List<Photo> getAllPhotos() {
        return photoRepository.findAll();
    }

    public Resource loadFileAsResourceById(Long id) {
        Photo photo = photoRepository.findById(id).orElseThrow(() ->
                new PhotoNotFoundException("Photo not found with id: " + id));
        try {
            Path filePath = this.fileStorageLocation.resolve(photo.getFileName()).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageException("Could not read file: " + photo.getFileName());
            }
        } catch (MalformedURLException ex) {
            throw new StorageException("Could not read file: " + photo.getFileName(), ex);
        }
    }

    public Photo getPhotoById(Long id) {
        return photoRepository.findById(id)
                .orElseThrow(() -> new PhotoNotFoundException("Photo not found with id: " + id));
    }

    public void deletePhoto(Long id) {
        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new PhotoNotFoundException("Photo not found with id: " + id));

        // Delete the file from the filesystem
        deleteFileFromSystem(photo.getFileName());

        // Delete the photo record from the database
        photoRepository.deleteById(id);
    }

    private void deleteFileFromSystem(String fileName) {
        try {
            Path file = this.fileStorageLocation.resolve(fileName).normalize();
            Files.deleteIfExists(file);
        } catch (IOException ex) {
            throw new StorageException("Could not delete file " + fileName, ex);
        }
    }

}
