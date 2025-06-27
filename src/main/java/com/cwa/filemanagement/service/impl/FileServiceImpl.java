package com.cwa.filemanagement.service.impl;

import com.cwa.filemanagement.config.FileStorageProperties;
import com.cwa.filemanagement.entity.FileEntity;
import com.cwa.filemanagement.exception.FileNotFoundException;
import com.cwa.filemanagement.exception.FileStorageException;
import com.cwa.filemanagement.repository.FileRepository;
import com.cwa.filemanagement.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final Path fileStorageLocation;

    public FileServiceImpl(FileRepository fileRepository, FileStorageProperties fileStorageProperties) {
        this.fileRepository = fileRepository;
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create upload directory", ex);
        }
    }

    @Override
    public FileEntity storeFile(MultipartFile file, String description) {
        if (file.isEmpty()) {
            throw new FileStorageException("Failed to store empty file " + file.getOriginalFilename());
        }

        var originalFileName = file.getOriginalFilename();
        var fileName = UUID.randomUUID() + originalFileName;

        try {
            if (fileName.contains("..")) {
                throw new FileStorageException("Cannot store file with relative path outside current directory " + fileName);
            }

            var targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            var fileEntity = new FileEntity();
            fileEntity.setFileName(fileName);
            fileEntity.setOriginalFileName(originalFileName);
            fileEntity.setFileType(file.getContentType());
            fileEntity.setFileSize(String.valueOf(file.getSize()));
            fileEntity.setFilePath(targetLocation.toString());
            fileEntity.setDescription(description);

            return fileRepository.save(fileEntity);
        } catch (IOException ex) {
            throw new FileStorageException("Failed to store file " + fileName, ex);
        }
    }

    @Override
    public Resource loadFileAsResource(String fileName) {
        try {
            var filePath = this.fileStorageLocation.resolve(fileName).normalize();
            var resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileStorageException("File not found " + fileName, ex);
        }
    }

    @Override
    public List<FileEntity> getAllFiles() {
        return this.fileRepository.findAll();
    }

    @Override
    public FileEntity getFileById(Long id) {
        return this.fileRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteFileById(Long id) {
        this.fileRepository.findById(id).ifPresent(fileRepository::delete);
    }

    @Override
    public List<FileEntity> searchFiles(String fileName) {
        return this.fileRepository.findByFileNameIgnoreCase(fileName);
    }
}
