package com.hisham.scribesByHIsham.service;

import com.cloudinary.Cloudinary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryUploadServiceImpl implements CloudinaryUploadService{
    @Autowired
    private Cloudinary cloudinary;

    private static final Logger logger = LoggerFactory.getLogger(CloudinaryUploadServiceImpl.class);

    @Override
    public Map upload(MultipartFile file) {
        try {
            Map imageData = cloudinary.uploader().upload(file.getBytes(), Map.of());
            String imageUrl = imageData.get("secure_url").toString();
            return Map.of("imageUrl", imageUrl);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload the image");
        }
    }
}
