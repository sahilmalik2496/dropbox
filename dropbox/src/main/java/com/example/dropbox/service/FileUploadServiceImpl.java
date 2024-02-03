package com.example.dropbox.service;

import com.example.dropbox.dao.UploadDao;
import com.example.dropbox.modals.FileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Autowired
    private UploadDao uploadDao;
    @Override
    public FileEntity upload(byte[] fileData, String name, String type, long size) {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileId(UUID.randomUUID().toString());
        fileEntity.setFilename(name);
        fileEntity.setType(type);
        fileEntity.setSize(size);
        fileEntity.setCreatedAt(LocalDateTime.now());

        String filePath = saveFileToFileSystem(fileData, name);
        fileEntity.setFilePath(filePath);

        return uploadDao.saveFile(fileEntity);
    }

    @Override
    public FileEntity getFileByFileId(String fileId) {
        return uploadDao.findByFileId(fileId);
    }

    @Override
    public boolean update(String fileId, MultipartFile file, String fileName, String metadata) throws IOException {
        return uploadDao.updateFile(fileId, file, fileName, metadata);
    }

    @Override
    public boolean deleteFile(String fileId) {
        return uploadDao.deleteFile(fileId);
    }

    @Override
    public List<FileEntity> listAllFiles() {
        return uploadDao.listAllFiles();
    }

    private String saveFileToFileSystem(byte[] fileData, String fileName) {
        try {
            String directoryPath = "/Users/sahilmalik/Documents/dropboxService/";
            Files.createDirectories(Paths.get(directoryPath));
            String filePath = directoryPath + fileName;
            FileOutputStream fos = new FileOutputStream(filePath);
            fos.write(fileData);
            fos.close();
            return filePath;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

