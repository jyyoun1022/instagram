package org.codej.instagram.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.*;
import java.time.LocalDateTime;

/*
 * @RequiredArgsConstructor를 사용하면 @NonNull 이 있는 애들만 생성자에 파라메터를 만들어 준다.
 * @@NoArgsConstructor가 없으면 기본 생성자가 없기 때문에 스프링 리플렉션이 발동할 때 객체 생성이 되지 않는다.
 */
@Data
@Entity
@RequiredArgsConstructor
@NoArgsConstructor
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "from_user")//DB에 FK 변수명이됨
    @NonNull private Users from_user;

    @ManyToOne
    @JoinColumn(name = "to_user")
    @NonNull private Users to_user;

    @CreationTimestamp
    private LocalDateTime regDate;
    @CreationTimestamp
    private LocalDateTime modDate;


}
