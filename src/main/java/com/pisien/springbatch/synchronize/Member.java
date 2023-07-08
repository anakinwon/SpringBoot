package com.pisien.springbatch.synchronize;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    private int username;
    private String password;
    private String createdDt;
}
