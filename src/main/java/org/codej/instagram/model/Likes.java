package org.codej.instagram.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.awt.*;
import java.sql.Timestamp;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "userId")
    @JsonIgnoreProperties({"images", "password", "name", "website", "bio", "email", "phone", "gender", "createDate", "updateDate"})
    private Users user;//id , username, profileImage

    @ManyToOne
    @JoinColumn(name = "imageId")
    @JsonIgnoreProperties({"tags","user","likes"})
    private Images image;// 기본 : image_id

    @CreationTimestamp // 자동으로 현재 시간이 세팅
    private Timestamp createDate;
    @CreationTimestamp // 자동으로 현재 시간이 세팅
    private Timestamp updateDate;
}
