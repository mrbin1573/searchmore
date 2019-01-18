package com.smss.searchmore.controller;

import com.smss.searchmore.pojo.Tag;
import com.smss.searchmore.service.TagService;
import com.smss.searchmore.service.TagTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author: luokangyuan
 * @Date: 2019/1/18 21 46
 * @Description: 标签类型类
 */
@Api(tags="标签类型类",value="标签类型类")
@RestController
@RequestMapping(value = "api/v1/tagType", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class TagTypeController {
    @Autowired
    private TagTypeService tagTypeService;

    /**
     * @Description 获取所有的标签信息
     * @author luokangyuan
     * @since 2019-1-18 14:45
     * @return 所有的标签信息
     */
    @ApiOperation(value="获取标签类型列表", notes="获取标签类型列表")
    @RequestMapping(value={""}, method=RequestMethod.GET)
    public List<Tag> getAllTagTypes(){
        return null;
    }

    /**
     * @Description 获取一条标签信息
     * @author luokangyuan
     * @since 2019-1-18 15:00
     * @param code 标签code
     * @return 一条标签信息
     */
    @ApiOperation(value="获取标签类型详细信息", notes="根据url的code来获取标签类型详细信息")
    @ApiImplicitParam(name = "code", value = "标签类型主键", required = true, dataType = "String", paramType = "path")
    @RequestMapping(value="/{code}", method=RequestMethod.GET)
    public Tag getTag(@PathVariable String code){
        return null;
    }

    /**
     * @Description 更新一条标签信息
     * @author luokangyuan
     * @since 2019-1-18 15:00
     * @param code
     * @return
     */
    @ApiOperation(value="更新标签类型详细信息", notes="根据url的code来指定更新对象，并根据传过来的标签类型信息来更新标签详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "标签类型主键", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "tagType", value = "标签类型详细实体tagType", required = true, dataType = "TagType")
    })
    @RequestMapping(value="/{code}", method=RequestMethod.PUT)
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
    @ApiOperation(value="删除标签", notes="根据url的code来指定删除对象")
    @ApiImplicitParam(name = "code", value = "标签主键", required = true, dataType = "String", paramType = "path")
    @RequestMapping(value="/{code}", method= RequestMethod.DELETE)
    public ResponseEntity<Map<String, Object>> deleteTag(String code){
        return null;
    }

    /**
     * @Description 保存一条标签信息
     * @author luokangyuan
     * @param tag 需要保存的信息
     * @return 保存结果
     */
    @ApiOperation(value="创建标签", notes="根据Tag对象创建标签")
    @ApiImplicitParam(name = "tag", value = "标签实体tag", required = true, dataType = "Tag")
    @RequestMapping(value="", method=RequestMethod.POST)
    @PostMapping("/tag")
    public ResponseEntity<Map<String, Object>> insertTag(@RequestBody Tag tag){
        return null;
    }
}
