package com.cwa.filemanagement.repository;

import com.cwa.filemanagement.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    List<FileEntity> findByFileNameIgnoreCase(String fileName);
    List<FileEntity> findByFileType(String fileType);
    Optional<FileEntity> findByFileName(String fileName);
}
