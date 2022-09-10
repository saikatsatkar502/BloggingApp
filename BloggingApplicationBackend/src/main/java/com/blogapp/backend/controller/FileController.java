package com.blogapp.backend.controller;

import com.blogapp.backend.payloads.FileResponse;
import com.blogapp.backend.service.file.FileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/file")
public class FileController {


    @Autowired
    private FileServiceImpl fileService;

    @Value("${project.image}")
    private String path;

    @PostMapping("/upload")
    public ResponseEntity<FileResponse> uploadFile(
            @RequestParam(value = "image") MultipartFile image
    ){
        String file = null;
        try {
            file = this.fileService.uploadImage(path,image);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new FileResponse(null,"fail to upload image . "), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new FileResponse(file,"image uploaded successfully."), HttpStatus.OK);
    }

    @GetMapping(value = "/serve/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public void serveImage(
            @PathVariable String imageName,
            HttpServletResponse response
    )  {
        try {
            InputStream resource = this.fileService.getResource(path, imageName);
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            StreamUtils.copy(resource, response.getOutputStream());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
