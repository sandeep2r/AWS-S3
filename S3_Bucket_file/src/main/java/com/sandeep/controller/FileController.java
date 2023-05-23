package com.sandeep.controller;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sandeep.service.S3Service;

@RestController
@RequestMapping("/files")
public class FileController {

	@Autowired
    private final S3Service s3Service;

    public FileController(S3Service s3Service) {
        this.s3Service = s3Service;
    }
// file search url => http://localhost:8080/files?userName=sandy&term=logistics
    @GetMapping
    public List<String> searchFiles(@RequestParam("userName") String userName,
                                    @RequestParam("term") String term) {
        return s3Service.searchFiles(userName, term);
    }

    @GetMapping("/{userName}/{fileName}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable("userName") String userName,
                                                            @PathVariable("fileName") String fileName) throws IOException {
        byte[] fileBytes = s3Service.downloadFile(userName, fileName);
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(fileBytes));

        return ResponseEntity.ok()
                .contentLength(fileBytes.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }


    /*
     * 
     * for file upload using postman
     * method => Post
     * url    => http://localhost:8080/files/sandy
     * 			body => form-data  => key :  file (select File from dropdown) => Value => select file => browse file => select file => send request
     * 
     * 
     */
    @PostMapping("/{userName}")
    public ResponseEntity<String> uploadFile(@PathVariable("userName") String userName,
                                             @RequestParam("file") MultipartFile file) {
        try {
            byte[] fileData = file.getBytes();
            String fileName = file.getOriginalFilename();

            s3Service.uploadFile(userName, fileData, fileName);

            return ResponseEntity.status(HttpStatus.OK).body("File uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file: " + e.getMessage());
        }
    }
}

