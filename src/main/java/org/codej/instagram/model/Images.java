package org.codej.instagram.model;

import lombok.Data;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class Images {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Lob
    @Column(length=1024000)
    private byte[] file;

    private String location;
    private String caption;

    @ManyToOne
    @JoinColumn(name="user_id")
    private Users user;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name="image_id" )
    private List<Tags> tags;

    private LocalDateTime reg_date;
    private LocalDateTime mod_date;


}
