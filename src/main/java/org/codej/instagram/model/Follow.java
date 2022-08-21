package org.codej.instagram.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "from_user")//DB에 FK 변수명이됨
    private Users from_user;

    @ManyToOne
    @JoinColumn(name = "to_user")
    private Users to_user;

    private LocalDateTime reg_date;
    private LocalDateTime mod_date;


}
