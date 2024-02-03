package com.example.dropbox.controller;

import com.example.dropbox.modals.FileEntity;
import com.example.dropbox.service.FileUploadService;
import jakarta.annotation.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileUploadController {

    public FileUploadService fileUploadService;

    @PostMapping("/upload")
    public Long uploadFilToDropbox(@RequestParam("file") MultipartFile file,
                                   @RequestParam("filename") String fileName,
                                   @RequestParam(value = "metadata", required = false) String metadata) throws IOException {
        FileEntity fileEntity = fileUploadService.upload(file.getBytes(), fileName, file.getContentType(), file.getSize());
        return fileEntity.getId();
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<Resource> getFile(@PathVariable String fileId) {
        FileEntity fileEntity = fileUploadService.getFileByFileId(fileId);
        if (fileEntity == null) {
            return ResponseEntity.notFound().build();
        }

        File file = new File(fileEntity.getFilePath());
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = (Resource) new FileSystemResource(file);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("fileAttachment", fileEntity.getFilename());

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    @PutMapping("/{fileId}")
    public boolean update(@PathVariable String fileId,
                          @RequestParam(value = "file", required = false) MultipartFile file,
                          @RequestParam(value = "filename", required = false) String fileName,
                          @RequestParam(value = "metadata", required = false) String metadata) {
        try {
            return fileUploadService.update(fileId, file, fileName, metadata);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @DeleteMapping("/{fileId}")
    public Boolean deleteFile(@PathVariable String fileId) {
        try {
            return fileUploadService.deleteFile(fileId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @GetMapping
    public ResponseEntity<List<FileEntity>> listFiles() {
        try {
            List<FileEntity> fileMetadataList = fileUploadService.listAllFiles();
            return ResponseEntity.ok(fileMetadataList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
