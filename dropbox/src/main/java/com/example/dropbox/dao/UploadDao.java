package com.example.dropbox.dao;

import com.example.dropbox.modals.FileEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UploadDao {
    FileEntity saveFile(FileEntity fileEntity);

    FileEntity findByFileId(String fileId);

    boolean updateFile(String fileId, MultipartFile file, String fileName, String metadata) throws IOException;

    boolean deleteFile(String fileId);

    List<FileEntity> listAllFiles();
}
