package com.mr.api;


import com.mr.pojo.Category;
import com.mr.success.MySuccess;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("category")
public interface CategoryApi {




    @GetMapping("list")
    List<Category> getCategoryListByPid(@RequestParam  long pid);

    @DeleteMapping("delCategoryByPid")
    MySuccess  delCategoryByPid(@RequestParam  long pid);

    @PostMapping("addCategory")
    MySuccess  addCategory(@RequestBody Category category);

    @PutMapping("updateCategory")
    MySuccess updateCategory(@RequestBody Category category);

    @GetMapping("getMaxId")
    Long getMaxId();

    @GetMapping("getCategoryByBrandId/{pid}")
    List<Category> getCategoryByBrandId(@PathVariable("pid") Long bid);

    @GetMapping("categoryName")
    List<String> getCategoryNameByIds(@RequestParam("ids") List<Long> ids);

    @GetMapping("categoryById")
    Category getCategoryById(@RequestParam("id") Long id);

    @GetMapping("categoryList")
    List<Category> getCategoryListByIds(@RequestParam("ids") List<Long> ids);

}
