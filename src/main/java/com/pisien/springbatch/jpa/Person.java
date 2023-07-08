package com.pisien.springbatch.jpa;

import lombok.*;

import javax.persistence.*;
import javax.sound.midi.MetaMessage;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity @Getter @Setter
public class Person {

    @Id
    private int username;
    private String password;
    private String createdDt;

}
