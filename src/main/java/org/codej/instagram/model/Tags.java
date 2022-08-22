package org.codej.instagram.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class Tags {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private Users user;

    @ManyToOne
    @JoinColumn(name="imageId")
    @JsonBackReference
    private Images image;

    @CreationTimestamp
    private LocalDateTime regDate;
    @CreationTimestamp
    private LocalDateTime modDate;

    public void setImage(Images image) {
        this.image = image;
        this.image.getTags().add(this); //이 태그를 넣으면 image객체를 넣을 때 image쪽에 null이던 tag값이 들어간다.
    }

}
