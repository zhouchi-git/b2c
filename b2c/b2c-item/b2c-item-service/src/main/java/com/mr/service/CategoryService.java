package com.mr.service;

import com.mr.mapper.CategoryMapper;
import com.mr.pojo.Category;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


@Service
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;



    /**
     * 查询
     * @param pid
     * @return
     */
    public List<Category> getCategoryListByPid(Long pid){
        Category category = new Category();
        category.setParentId(pid);
        return  categoryMapper.select(category);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    public int delCategoryByPid(Long id){
        //id 是前台传过来的分类节点id

//        通过pid 查询出pid下的所有的子分类
//        如果不存在子分类改变isParent为0 前台判断

        //通过id 查询出这个节点的pid
        Category category = categoryMapper.selectByPrimaryKey(id);
        //首先执行删除
        int i = categoryMapper.deleteByPrimaryKey(id);
        //开启条件查询
        Example example = new Example(Category.class);
        //创建条件查询
        Example.Criteria criteria = example.createCriteria();
        //放入条件查询语句
        criteria.andEqualTo("parentId",category.getParentId());
        List<Category> categories = categoryMapper.selectByExample(example);
        if(categories.isEmpty()){
            if(categories.size() ==0){
                Category category1 = new Category();
                category1.setId(category.getParentId());
                category1.setIsParent(false);
                categoryMapper.updateByPrimaryKeySelective(category1);
            }
        }

       return i;
    }

    /**
     * 添加
     * @param category
     * @return
     */
    public int addCategory(Category category) {
        int i = categoryMapper.insertSelective(category);
        Category category1 = new Category();
        category1.setId(category.getParentId());
        category1.setIsParent(true);
        categoryMapper.updateByPrimaryKeySelective(category1);
        return  i;

    }

    /**
     * 修改
     * @param category
     * @return
     */
    public int updateCategory(Category category) {
        return  categoryMapper.updateByPrimaryKeySelective(category);
    }

    public Long getMaxId() {

        return categoryMapper.getMaxId();
    }

    /**
     * 回显获取分类
     * @param bid
     * @return
     */
    public List<Category> getCategoryByBrandId(Long bid) {

        return  categoryMapper.getCategoryByBrandId(bid);

    }

    /**
     * ids 查询分类名称
     * @param ids
     * @return
     */
    public List<String> getCategoryNameByIds(List<Long> ids) {

        return categoryMapper.selectCatGoryNameByIds(StringUtils.join(ids, ","));
    }

    /**
     * 通过id 查询单个分类名称
     * @param id
     * @return
     */
    public Category getCategoryById(Long id) {
        return categoryMapper.selectByPrimaryKey(id);
    }

    /**
     * 通过list Id 查询出分类集合
     * @param ids
     * @return
     */
    public List<Category> getCategoryListByIds(List<Long> ids) {
        return  categoryMapper. getCategoryListByIds(StringUtils.join(ids, ","));
    }
}
