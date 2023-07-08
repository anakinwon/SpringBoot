package com.pisien.springbatch.entity;


import javax.persistence.Entity;
import javax.persistence.Id;

public class Member {

    @Id
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
