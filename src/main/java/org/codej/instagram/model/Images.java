package org.codej.instagram.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Images {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

//    @Column(length=1024000)
//    private byte[] file;
//
//    private String filePath;
//    private String mimeType;
//    private String fileName;

    private String location;//사진찍은 위치
    private String caption;//사진 설명
    private String postImage;//포스팅 사진 경로+이름

    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonIgnoreProperties({"password","images"})
    private Users user;

    //(1) Like List
    @OneToMany(mappedBy = "image")
    @Builder.Default private List<Tags> tags = new ArrayList<>();

    @OneToMany(mappedBy = "image")
    @JsonManagedReference
    @Builder.Default private List<Likes> likes = new ArrayList<>();

    @Transient
    private int likeCount;

    @CreationTimestamp // 자동으로 현재 시간이 세팅
    private Timestamp createDate;
    @CreationTimestamp // 자동으로 현재 시간이 세팅
    private Timestamp updateDate;


}
