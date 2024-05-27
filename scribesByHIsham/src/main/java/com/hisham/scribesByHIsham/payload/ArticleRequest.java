package com.hisham.scribesByHIsham.payload;

import com.hisham.scribesByHIsham.model.CategoryName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.List;

@Getter
public class ArticleRequest {
    @NotBlank
    @Size(min = 20, max = 100)
    private String heading;

    @NotBlank
    @Size(min = 50)
    private String content;

    private List<CategoryName> categories;
}
