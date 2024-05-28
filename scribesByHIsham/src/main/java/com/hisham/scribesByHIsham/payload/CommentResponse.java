package com.hisham.scribesByHIsham.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private String content;
    private UserSummary createdBy;
    private Instant createdAt;
}
