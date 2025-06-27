package com.cwa.filemanagement.dto;

public record FileUploadResponse(
        String fileName,
        String fileDownloadUri,
        String fileType,
        long size
) {
}
