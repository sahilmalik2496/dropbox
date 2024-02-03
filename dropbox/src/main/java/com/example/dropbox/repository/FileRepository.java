package com.example.dropbox.repository;

import com.example.dropbox.modals.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {
    FileEntity findByFileId(String fileId);
}
