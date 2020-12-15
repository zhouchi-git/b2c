package com.mr.utils;

import com.mr.pojo.Brand;
import com.mr.pojo.Category;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class SearchResult<T> extends PageResult {
    private List<Category> categoryList;
    private List<Brand>    brandList;
    private List<Map<String,Object>> specList;

    public SearchResult(Long total, Long totalPage, List items, List<Category> categoryList, List<Brand> brandList,List<Map<String,Object>> specList) {
        super(total, totalPage, items);
        this.categoryList = categoryList;
        this.brandList = brandList;
        this.specList=specList;
    }
}
