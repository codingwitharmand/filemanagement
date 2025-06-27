package com.cwa.filemanagement.controller;

import com.cwa.filemanagement.dto.FileUploadResponse;
import com.cwa.filemanagement.entity.FileEntity;
import com.cwa.filemanagement.service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/api/v1/files")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<FileUploadResponse> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "description", required = false) String description
    ) {
        var fileEntity = fileService.storeFile(file, description);
        var fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/files/download/")
                .path(fileEntity.getFileName())
                .toUriString();

        return ResponseEntity.ok(new FileUploadResponse(
                fileEntity.getFileName(),
                fileDownloadUri,
                fileEntity.getFileType(),
                file.getSize()
        ));
    }

    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName,
                                                 HttpServletRequest request) {
        var resource = fileService.loadFileAsResource(fileName);

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.error("Could not determine file type.", ex);
        }

        if (Objects.isNull(contentType)) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/all")
    public ResponseEntity<List<FileEntity>> getAllFiles() {
        return ResponseEntity.ok(fileService.getAllFiles());
    }

    @DeleteMapping("/{id}")
    public void deleteFileById(@PathVariable Long id) {
        fileService.deleteFileById(id);
    }

    @GetMapping("/search/{fileName}")
    public ResponseEntity<List<FileEntity>> searchFiles(@PathVariable String fileName) {
        return ResponseEntity.ok(fileService.searchFiles(fileName));
    }
}
