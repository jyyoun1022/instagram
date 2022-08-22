package org.codej.instagram.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.awt.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Data
public class Tags {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="imageId")
    @JsonBackReference
    private Images image;

    @CreationTimestamp
    private LocalDateTime regDate;
    @CreationTimestamp
    private LocalDateTime modDate;

    @CreationTimestamp
    private Timestamp createDate;

}
