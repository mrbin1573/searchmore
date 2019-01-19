package com.smss.searchmore.mapper;

import com.smss.searchmore.pojo.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TagMapper {

    public List<Tag> getTags();

    public Tag getTag(String code);
}
