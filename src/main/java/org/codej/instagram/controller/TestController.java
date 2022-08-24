package org.codej.instagram.controller;

import org.codej.instagram.model.Images;
import org.codej.instagram.model.Users;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TestController {

    @GetMapping("/test/home")
    public String testHome(){
        return "home";
    }

    @ResponseBody
    @GetMapping("/test/user")
    public Users getUser(){
        Users user = new Users();
        user.setId(1);
        user.setUsername("codej");
        user.setName("윤재열");
        user.setEmail("jyyoun1021@gmail.com");
        user.setProfileImage("my.jpg");

        Images img1 = Images.builder()
                .id(1)
                .caption("가족 사진")
                .location("인천논현")
                .postImage("family.jpg")
                .user(user)
                .build();

        Images img2 = Images.builder()
                .id(2)
                .caption("증명 사진")
                .location("소래포구")
                .postImage("my.jpg")
                .user(user)
                .build();

        List<Images> images = new ArrayList<>();
        images.add(img1);
        images.add(img2);
        user.setImages(images);
        return user;
    }
    @GetMapping("/test/image")
    public @ResponseBody Images getImages(){
        Users user = new Users();
        user.setId(1);
        user.setUsername("codej");
        user.setName("윤재열");
        user.setEmail("jyyoun1021@gmail.com");
        user.setProfileImage("my.jpg");

        Images img1 = Images.builder()
                .id(1)
                .caption("가족 사진")
                .location("인천논현")
                .postImage("family.jpg")
                .user(user)
                .build();
        return img1;
    }
}
