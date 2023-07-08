package com.pisien.springbatch.writer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@AllArgsConstructor
public class Person {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int username;
    private String password;
    private String createdDt;
}
