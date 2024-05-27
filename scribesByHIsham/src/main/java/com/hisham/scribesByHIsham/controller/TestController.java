package com.hisham.scribesByHIsham.controller;

import com.hisham.scribesByHIsham.model.User;
import com.hisham.scribesByHIsham.security.CurrentUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @GetMapping("")
    public String getTest(@CurrentUser UserDetails userDetails){
        logger.info(userDetails.getPassword());
        return userDetails.getPassword();
    }
}
