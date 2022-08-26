package org.codej.instagram.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codej.instagram.model.Likes;
import org.codej.instagram.model.Users;
import org.codej.instagram.repository.LikesRepository;
import org.codej.instagram.service.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LikesController {

    private final LikesRepository likesRepository;

    @GetMapping("/like/notification")
    public List<Likes> likeNotification(@AuthenticationPrincipal CustomUserDetails userDetails){
        Users principal = userDetails.getUser();
        List<Likes> likesList = likesRepository.findLikeNotification(principal.getId());
        return likesList;
    }
}
