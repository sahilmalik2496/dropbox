package com.example.dropbox.service;

import com.example.dropbox.modals.FileEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileUploadService {
    public FileEntity upload(byte[] fileData, String name, String type, long size);

    FileEntity getFileByFileId(String fileId);

    boolean update(String fileId, MultipartFile file, String fileName, String metadata) throws IOException;

    boolean deleteFile(String fileId);

    List<FileEntity> listAllFiles();
}
