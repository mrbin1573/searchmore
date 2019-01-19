package com.smss.searchmore.service.impl;

import com.smss.searchmore.mapper.TagMapper;
import com.smss.searchmore.pojo.Tag;
import com.smss.searchmore.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName TagServiceImpl
 * @Description 标签
 * @Author luokangyuan
 * @Date 2019-1-18 14:53
 * @Version 1.0.0
 */
@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagMapper mapper;

    @Override
    public List<Tag> getTags() {
        return mapper.getTags();
    }

    @Override
    public Tag getTag(String code) {
        return mapper.getTag(code);
    }
}
