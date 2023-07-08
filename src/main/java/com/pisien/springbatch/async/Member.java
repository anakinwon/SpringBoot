package com.pisien.springbatch.async;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Member {
    private int username;
    private String password;
    private String createdDt;
}
