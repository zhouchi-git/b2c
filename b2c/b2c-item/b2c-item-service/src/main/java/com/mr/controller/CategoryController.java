package com.mr.controller;

import com.mr.exception.MyException;
import com.mr.enums.ExceptionEnums;
import com.mr.pojo.Category;
import com.mr.service.CategoryService;
import com.mr.success.MySuccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;



    @GetMapping("list")
    public ResponseEntity<List<Category>>  getCategoryListByPid(@RequestParam  long pid){

        List<Category> categoryList = categoryService.getCategoryListByPid(pid);
        if(categoryList == null && categoryList.size() ==0){
            throw  new MyException(ExceptionEnums.CATEGORY_CANNOT_IS_NULL);
        }
        return ResponseEntity.ok(categoryList);
    }

    @DeleteMapping("delCategoryByPid")
    public ResponseEntity<MySuccess>  delCategoryByPid(@RequestParam  long pid){

        int i = categoryService.delCategoryByPid(pid);
        if(i != 1){
            throw  new MyException(ExceptionEnums.CATEGORY_CANNOT_IS_NULL);
        }
        return ResponseEntity.ok(new MySuccess(200,"删除成功！"));
    }

    @PostMapping("addCategory")
    public ResponseEntity<MySuccess>  addCategory(@RequestBody Category category){

        int i = categoryService.addCategory(category);
        if(i != 1){
            throw  new MyException(ExceptionEnums.CATEGORY_ADD);
        }
        return  new ResponseEntity(HttpStatus.CREATED);
    }

    @PutMapping("updateCategory")
    public ResponseEntity<MySuccess>  updateCategory(@RequestBody Category category){

        int i = categoryService.updateCategory(category);
        if(i != 1){
            throw  new MyException(ExceptionEnums.CATEGORY_ADD);
        }
        return ResponseEntity.ok(new MySuccess(200,"修改成功！"));
    }

    @GetMapping("getMaxId")
    public ResponseEntity<Long>  getMaxId(){

        return ResponseEntity.ok( categoryService.getMaxId());
    }

    @GetMapping("getCategoryByBrandId/{pid}")
    public ResponseEntity<List<Category>>  getCategoryByBrandId(@PathVariable("pid") Long bid){

        return ResponseEntity.ok( categoryService.getCategoryByBrandId(bid));
    }

    @GetMapping("categoryName")
    public ResponseEntity<List<String>> getCategoryNameByIds(@RequestParam("ids") List<Long> ids){

        return  ResponseEntity.ok(categoryService.getCategoryNameByIds(ids));
    }

    @GetMapping("categoryById")
    public ResponseEntity<Category> getCategoryById(@RequestParam("id") Long id){
        return  ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @GetMapping("categoryList")
    public ResponseEntity<List<Category>> getCategoryListByIds(@RequestParam("ids") List<Long> ids){

        return  ResponseEntity.ok(categoryService.getCategoryListByIds(ids));
    }

}
