package com.blogapp.backend.service.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface FileServiceInterface {

    String uploadImage(String path, MultipartFile file) throws IOException;
    InputStream getResource (String path, String fileName) throws FileNotFoundException;
}
