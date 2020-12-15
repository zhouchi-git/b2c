package com.mr.service;

import com.mr.mapper.SpecParamMapper;
import com.mr.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class SpecParamService {
    @Autowired
    private SpecParamMapper specParamMapper;

    /**
     * 通过规格参数id 查询单组属性
     * @param gid
     * @return
     */
    public List<SpecParam> getSpecParamByGroupId(Long gid) {
        Example example = new Example(SpecParam.class);
        example.createCriteria().andEqualTo("groupId",gid);
        return specParamMapper.selectByExample(example);

    }

    /**
     * 添加与修改
     * @param specParam
     */
    public void addOrUpdateParam(SpecParam specParam) {
        if(specParam.getId() !=null){
            specParamMapper.updateByPrimaryKeySelective(specParam);
        }else{
            specParamMapper.insertSelective(specParam);
        }
    }

    /**
     * 删除
     * @param pid
     */
    public void delParam(Integer pid) {
        specParamMapper.deleteByPrimaryKey(pid);
    }

    /**
     * 根据分类id查询规格参数
     * @param cid
     * @return
     */
    public List<SpecParam> getParamsByCateGroupId(Long cid, Long gid, Boolean searching, Boolean generic) {
        SpecParam specParam = new SpecParam();
        specParam.setCid(cid);
        specParam.setGroupId(gid);
        specParam.setSearching(searching);
        specParam.setGeneric(generic);
        return specParamMapper.select(specParam);
    }
}
