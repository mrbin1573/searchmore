package com.smss.searchmore.service;

import com.smss.searchmore.pojo.Tag;

import java.util.List;

/**
  * @author luokangyuan
  * @since 2019-1-18 14:51
  */
public interface TagService {
  public List<Tag> getTags();

  public Tag getTag(String code);

}
