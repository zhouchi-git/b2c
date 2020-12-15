package com.mr.controller;

import com.mr.pojo.SpecParam;
import com.mr.service.SpecParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("specParam")
public class SpecParamController {
    @Autowired
    private SpecParamService specParamService;



    /**
     * 添加
     * @param specParam
     * @return
     */
    @PostMapping("addOrUpdateParam")
    public ResponseEntity<Void> addParam(SpecParam specParam){
         specParamService.addOrUpdateParam(specParam);
        return ResponseEntity.ok(null);
    }

    /**
     * 修改
     * @param specParam
     * @return
     */
    @PutMapping("addOrUpdateParam")
    public ResponseEntity<Void> updateParam(SpecParam specParam){
        specParamService.addOrUpdateParam(specParam);
        return ResponseEntity.ok(null);
    }

    /**
     * 删除
     * @param pid
     * @return
     */
    @DeleteMapping("delParam/{pid}")
    public ResponseEntity<Void> delParam(@PathVariable("pid") Integer pid){
        specParamService.delParam(pid);
        return ResponseEntity.ok(null);
    }

    /**
     * 根据分类id查询规格参数
     * @param cid
     * @return
     */
    @GetMapping("getSpecParamByGroupId")
    public ResponseEntity<List<SpecParam>> getParamsByCateGroupId(
            @RequestParam(value = "cid",required = false) Long cid,
            @RequestParam(value = "gid",required = false) Long gid,
            @RequestParam(value = "searching",required = false) Boolean searching,
            @RequestParam(value = "generic",required = false) Boolean generic

    ){
        List<SpecParam> specParamList = specParamService.getParamsByCateGroupId(cid,gid,searching,generic);
        return ResponseEntity.ok(specParamList);
    }



}
