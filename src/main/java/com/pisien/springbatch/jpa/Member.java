package com.pisien.springbatch.jpa;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Id;

@Data
@AllArgsConstructor
public class Member {
    @Id
    private int username;
    private String password;
    private String createdDt;
}
