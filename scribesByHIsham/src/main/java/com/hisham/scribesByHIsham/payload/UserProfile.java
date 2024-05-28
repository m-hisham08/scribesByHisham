package com.hisham.scribesByHIsham.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class UserProfile {
    private Long id;
    private String username;
    private String firstname;
    private String lastname;
    private Instant createdAt;
    private int likeCount;
    private int commentCount;
}
