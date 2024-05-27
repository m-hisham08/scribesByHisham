package com.hisham.scribesByHIsham.payload;

import com.hisham.scribesByHIsham.model.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ArticleResponse {
    private Long id;
    private String heading;
    private String content;
    private List<CategoryResponse> categories;
    private UserSummary createdBy;
    private int likesCount;
}
