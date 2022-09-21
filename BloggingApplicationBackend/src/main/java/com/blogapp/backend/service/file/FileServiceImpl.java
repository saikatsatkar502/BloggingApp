package com.blogapp.backend.service.file;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
@Service
public class FileServiceImpl implements FileServiceInterface{


    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException,NullPointerException {

        if(file.isEmpty()){
            throw new NullPointerException("file is empty");
        }
        //File Name
        String name = file.getOriginalFilename();

        //random name
        String randomId = UUID.randomUUID().toString();
        if(name == null){
           throw new NullPointerException("file name is null");
        }
        String fileName = randomId.concat(name.substring(name.lastIndexOf(".")));

        //Full path
        String filePath = path+ File.separator+fileName;

        //create folder if not created
        File newFile = new File(path);

        if(!newFile.exists()){
            newFile.mkdir();
        }

        //file copy
        Files.copy(file.getInputStream(), Paths.get(filePath));

        return fileName;
    }


    @Override
    public InputStream getResource(String path, String fileName) throws FileNotFoundException {

        String fullPath = path+File.separator+fileName;

        //db logic to return input stream.
        return new FileInputStream(fullPath);
    }
}
