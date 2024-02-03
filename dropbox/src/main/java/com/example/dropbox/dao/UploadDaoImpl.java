package com.example.dropbox.dao;

import com.example.dropbox.modals.FileEntity;
import com.example.dropbox.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class UploadDaoImpl implements UploadDao {
    @Autowired
    private FileRepository fileRepository;

    public FileEntity saveFile(FileEntity fileEntity) {
        return fileRepository.save(fileEntity);
    }

    @Override
    public FileEntity findByFileId(String fileId) {
        return fileRepository.findByFileId(fileId);
    }

    @Override
    public boolean updateFile(String fileId, MultipartFile file, String fileName, String metadata) throws IOException {
        FileEntity fileEntity = findByFileId(fileId);
        if (fileEntity == null) {
            throw new IllegalArgumentException("File not found for id " + fileId);
        }

        if (file != null) {
            String filePath = fileEntity.getFilePath() + fileEntity.getFilename();
            Files.write(Paths.get(filePath), file.getBytes());
            fileEntity.setFilePath(filePath);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteFile(String fileId) {
        FileEntity fileEntity = findByFileId(fileId);
        if (fileEntity == null) {
            throw new IllegalArgumentException("File not found: " + fileId);
        }
        try {
            Files.deleteIfExists(Paths.get(fileEntity.getFilePath()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete file");
        }
        deleteFileEntity(fileEntity);
        return false;
    }

    @Override
    public List<FileEntity> listAllFiles() {
        return fileRepository.findAll();
    }

    private void deleteFileEntity(FileEntity fileEntity) {
        fileEntity.setFileId(fileEntity.getFileId() + UUID.randomUUID()); // soft delete of file meta
    }
}
