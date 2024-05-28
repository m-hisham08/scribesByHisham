package com.hisham.scribesByHIsham.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {
    @Size(max = 300)
    @NotBlank
    private String content;
}
