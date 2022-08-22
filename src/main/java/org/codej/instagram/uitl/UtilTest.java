package org.codej.instagram.uitl;

import org.codej.instagram.model.Users;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UtilTest {
    public static Users getUser() {
        Users user = Users.builder()
                .bio("")
                .email("codej@naver.com")
                .gender("남")
                .name("재열")
                .phone("01022229988")
                .username("cos")
                .website("blog.naver.com/codingspecialist")
                .regDate(LocalDateTime.now())
                .modDate(LocalDateTime.now())
                .build();

        return user;
    }
}