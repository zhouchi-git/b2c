package com.mr.api;

import com.mr.pojo.SpecParam;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("specParam")
public interface SpecParamApi {


    /**
     * 添加
     * @param specParam
     * @return
     */
    @PostMapping("addOrUpdateParam")
    Void addParam(@RequestParam  SpecParam specParam);

    /**
     * 修改
     * @param specParam
     * @return
     */
    @PutMapping("addOrUpdateParam")
    Void updateParam(@RequestParam SpecParam specParam);

    /**
     * 删除
     * @param pid
     * @return
     */
    @DeleteMapping("delParam/{pid}")
    Void delParam(@PathVariable("pid") Integer pid);

    /**
     * 根据分类id查询规格参数
     * @param cid
     * @return
     */
    @GetMapping("getSpecParamByGroupId")
    List<SpecParam> getParamsByCateGroupId(
            @RequestParam(value = "cid",required = false) Long cid,
            @RequestParam(value = "gid",required = false) Long gid,
            @RequestParam(value = "searching",required = false) Boolean searching,
            @RequestParam(value = "generic",required = false) Boolean generic

    );
}
