package com.smss.searchmore.controller;

import com.alibaba.fastjson.JSONObject;
import com.smss.searchmore.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 *@ClassName TagController
 *@Description 标签
 *@Author luokangyuan
 *@Date 2019-1-18 14:35
 *@Version 1.0.0
 */
@Controller
@RequestMapping(value = "api/v1/tag", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * @Description 获取所有的标签信息
     * @author luokangyuan
     * @since 2019-1-18 14:45
     * @param asyou 用户
     * @return 所有的标签信息
     */
    @GetMapping("/tags")
    public ResponseEntity<Map<String, Object>> getAllTags(String asyou){
        return null;
    }

    /**
     * @Description 获取一条标签信息
     * @author luokangyuan
     * @since 2019-1-18 15:00
     * @param code 标签code
     * @return 一条标签信息
     */
    @GetMapping("/tag/code")
    public ResponseEntity<Map<String, Object>> getTag(String code){
        return null;
    }

    /**
     * @Description 更新一条标签信息
     * @author luokangyuan
     * @since 2019-1-18 15:00
     * @param code
     * @return
     */
    @PutMapping("/tag/code")
    public ResponseEntity<Map<String, Object>> updateTag(String code){
        return null;
    }

    /**
     * @Description 删除一条标签信息
     * @author luokangyuan
     * @since 2019-1-18 15:00
     * @param code
     * @return
     */
    @DeleteMapping("/tag/code")
    public ResponseEntity<Map<String, Object>> deleteTag(String code){
        return null;
    }

    /**
     * @Description 保存一条标签信息
     * @author luokangyuan
     * @param tag 需要保存的信息
     * @return 保存结果
     */
    @PostMapping("/tag")
    public ResponseEntity<Map<String, Object>> insertTag(JSONObject tag){
        return null;
    }


}
