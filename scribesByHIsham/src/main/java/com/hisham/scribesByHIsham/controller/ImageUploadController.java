package com.hisham.scribesByHIsham.controller;

import com.hisham.scribesByHIsham.service.CloudinaryUploadServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/upload")
public class ImageUploadController {
    @Autowired
    private CloudinaryUploadServiceImpl cloudinaryUploadService;

    @PostMapping("")
    public ResponseEntity<?> uploadImage(
            @RequestParam(value = "image") MultipartFile file
            ){
        Map imageData = cloudinaryUploadService.upload(file);
        return ResponseEntity.ok(imageData);
    }
}
