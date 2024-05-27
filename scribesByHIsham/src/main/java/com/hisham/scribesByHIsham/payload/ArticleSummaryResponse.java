package com.hisham.scribesByHIsham.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ArticleSummaryResponse {
    private Long id;
    private String heading;
    private List<CategoryResponse> categories;
    private UserSummary createdBy;
}
