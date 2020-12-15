package com.mr.api;

import com.mr.pojo.SpecGroup;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("specGroup")
public interface GroupApi {

    /**
     * 根据分类id查询分类规格
     * @param cid
     * @return
     */
    @GetMapping("getGroupByCateGroupId/{cid}")
    List<SpecGroup> getGroupByCateGroupId(@PathVariable("cid") Long cid);

    /**
     * 添加
     * @param specGroup
     * @return
     */
    @PostMapping("addOrUpdateGroup")
    Void addGroup(@RequestParam SpecGroup specGroup);

    /**
     * 修改
     * @param specGroup
     * @return
     */
    @PutMapping("addOrUpdateGroup")
    Void updateGroup(@RequestParam SpecGroup specGroup);



    @DeleteMapping("delGroup/{gid}")
    Void delGroup(@PathVariable("gid") Integer gid);

}
