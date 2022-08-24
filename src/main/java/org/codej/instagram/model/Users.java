package org.codej.instagram.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bytebuddy.asm.Advice;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.awt.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;//사용자 아이디
    private String password;
    private String name;// 사용자이름
    private String website;//홈페이지 주소
    private String bio;//자기 소개
    private String email;
    private String phone;
    private String gender;
    private String profileImage;//프로필 사진 경로+이름


    //findById() 때만 동작
    //findByUserInfo() 제외
    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties({"users","tags","likes"})
    private List<Images> images = new ArrayList<>();

    @CreationTimestamp // 자동으로 현재 시간이 세팅
    private Timestamp createDate;
    @CreationTimestamp // 자동으로 현재 시간이 세팅
    private Timestamp updateDate;





}
