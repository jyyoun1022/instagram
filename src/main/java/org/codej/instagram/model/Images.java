package org.codej.instagram.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;

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

    @Lob
    @Column(length=1024000)
    private byte[] file;

    private String filePath;
    private String mimeType;
    private String fileName;

    private String location;
    private String caption;

    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonIgnoreProperties({"name", "website", "bio", "email" ,"phone" ,"gender" ,"createDate", "updateDate"})
    private Users user;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    @Builder.Default
    @JsonManagedReference
    private List<Tags> tags = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime regDate;
    @CreationTimestamp
    private LocalDateTime modDate;


}
