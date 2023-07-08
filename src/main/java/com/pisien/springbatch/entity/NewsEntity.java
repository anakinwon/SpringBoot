package com.pisien.springbatch.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data @Getter @Setter
public class NewsEntity {
    private String title;
    private String journalist;
    private String publisher;
    private String reg_dt;
}
