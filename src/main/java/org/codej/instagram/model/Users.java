package org.codej.instagram.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bytebuddy.asm.Advice;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;
    private String name;
    private String website;
    private String bio;
    private String email;
    private String phone;
    private String gender;

    // Users객체에서 Follow 연관관계를 검색하고 싶다면 아래 추가
// mappedBy는 연관관계의 주인이 아님을 설정
// mappedBy가 있다는 것은 양방향 관계라는 뜻
// mappedBy가 없는 쪽이 FK가 생김
// mappedBy에 들어가는 값은 연관 관계에 있는 Follow 객체의 변수명이다.
//	@OneToMany(mappedBy = "from_user")
//	private List<Follow> from_user;
//
//	@OneToMany(mappedBy = "to_user")
//	private List<Follow> to_user;

    @CreationTimestamp
    private LocalDateTime regDate;
    @CreationTimestamp
    private LocalDateTime modDate;



}
