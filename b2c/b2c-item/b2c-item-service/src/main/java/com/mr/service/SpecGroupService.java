package com.mr.service;

import com.mr.mapper.SpecGroupMapper;
import com.mr.pojo.SpecGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class SpecGroupService {

    @Autowired
    private SpecGroupMapper specGroupMapper;

    /**
     * 根据分类id查询规格
     * @param cid
     * @return
     */
    public List<SpecGroup> getGroupByCateGroupId(Long cid) {
        Example example = new Example(SpecGroup.class);
        example.createCriteria().andEqualTo("cid",cid);
        return specGroupMapper.selectByExample(example);
    }

    /**
     * 添加与修改
     * @param specGroup
     */
    public void addOrUpdateGroup(SpecGroup specGroup) {
        if(specGroup.getId() == null){
            specGroupMapper.insertSelective(specGroup);
        }else{
            specGroupMapper.updateByPrimaryKeySelective(specGroup);
        }

    }

    /**
     * 删除
     * @param gid
     */
    public void delGroup(Integer gid) {
        specGroupMapper.deleteByPrimaryKey(gid);
    }
}
