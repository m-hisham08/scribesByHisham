package com.hisham.scribesByHIsham.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface CloudinaryUploadService {
    Map upload(MultipartFile file);
}
