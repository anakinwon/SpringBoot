package com.pisien.springbatch.dao;

import com.pisien.springbatch.entity.NewsEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface INewsDAO {
    public List<NewsEntity> listNews();
}
