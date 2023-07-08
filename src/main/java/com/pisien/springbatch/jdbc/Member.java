package com.pisien.springbatch.jdbc;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data @Getter @Setter
@Entity
public class Member {
    @Id @GeneratedValue
    private String username;
    private String password;
    private LocalDateTime createdDt;

}
