package com.pisien.springbatch.writer;

import lombok.*;

import javax.persistence.Entity;

@Data
@AllArgsConstructor
public class Member {
    private int username;
    private String password;
    private String createdDt;
}
