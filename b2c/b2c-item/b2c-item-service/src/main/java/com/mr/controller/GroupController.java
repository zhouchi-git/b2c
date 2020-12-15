package com.mr.controller;

import com.mr.pojo.SpecGroup;
import com.mr.service.SpecGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("specGroup")
public class GroupController {
    @Autowired
    private SpecGroupService specGroupService;

    /**
     * 根据分类id查询分类规格
     * @param cid
     * @return
     */
    @GetMapping("getGroupByCateGroupId/{cid}")
    public ResponseEntity<List<SpecGroup>> getGroupByCateGroupId(@PathVariable("cid") Long cid){
        List<SpecGroup> specGroupList = specGroupService.getGroupByCateGroupId(cid);
        return ResponseEntity.ok(specGroupList);

    }

    /**
     * 添加
     * @param specGroup
     * @return
     */
    @PostMapping("addOrUpdateGroup")
    public ResponseEntity<Void> addGroup(SpecGroup specGroup){
        specGroupService.addOrUpdateGroup(specGroup);
        return ResponseEntity.ok(null);

    }

    /**
     * 修改
     * @param specGroup
     * @return
     */
    @PutMapping("addOrUpdateGroup")
    public ResponseEntity<Void> updateGroup(SpecGroup specGroup){
        specGroupService.addOrUpdateGroup(specGroup);
        return ResponseEntity.ok(null);

    }


    /**
     * 删除
     * @param gid
     * @return
     */
    @DeleteMapping("delGroup/{gid}")
    public ResponseEntity<Void> delGroup(@PathVariable("gid") Integer gid){
        specGroupService.delGroup(gid);
        return ResponseEntity.ok(null);

    }

}
