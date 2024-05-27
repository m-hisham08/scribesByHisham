package com.hisham.scribesByHIsham.payload;

import com.hisham.scribesByHIsham.model.CategoryName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CategoryResponse {
    private CategoryName name;
}
