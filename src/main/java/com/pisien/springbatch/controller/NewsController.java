package com.pisien.springbatch.controller;

import com.pisien.springbatch.dao.INewsDAO;
import com.pisien.springbatch.entity.NewsEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class NewsController {
    private Logger logger = LoggerFactory.getLogger(NewsController.class);

    @Autowired private INewsDAO newsDAO;

    @RequestMapping("/news")
    @ResponseBody
    public List<NewsEntity> news(Model model) throws Exception {
        List<NewsEntity> newsList = newsDAO.listNews();
        logger.info("newsList = "+newsList.toString());
        for (NewsEntity newsEntity : newsList) {
            model.addAttribute(newsList);
        }

        return newsList;

    }

}
