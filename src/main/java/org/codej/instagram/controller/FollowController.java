package org.codej.instagram.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codej.instagram.model.Follow;
import org.codej.instagram.model.Users;
import org.codej.instagram.repository.FollowRepository;
import org.codej.instagram.repository.UserRepository;
import org.codej.instagram.service.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class FollowController {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    @PostMapping("/follow/{id}")
    @ResponseBody
    public String follow(@AuthenticationPrincipal CustomUserDetails userDetails,
                         @PathVariable int id){
        Users fromUser = userDetails.getUser();
        Optional<Users> oToUser = userRepository.findById(id);
        Users toUser = oToUser.get();

        Follow follow = new Follow();
        follow.setToUser(toUser);
        follow.setFromUser(fromUser);

        followRepository.save(follow);

        return "ok";
    }

    @DeleteMapping("/follow/{id}")
    @ResponseBody
    public String unfollow(@AuthenticationPrincipal CustomUserDetails userDetails,
                           @PathVariable int id){
        Users fromUser = userDetails.getUser();
        Optional<Users> oToUser = userRepository.findById(id);
        Users toUser = oToUser.get();

        followRepository.deleteByFromUserIdAndToUserId(fromUser.getId(), toUser.getId());

        List<Follow> follows = followRepository.findAll();
        return "ok";
    }
    @GetMapping("/follow/follower/{id}")
    public String followFollower(@PathVariable int id,
                                 @AuthenticationPrincipal CustomUserDetails userDetails,
                                 Model model){

        //팔로워 리스트
        List<Follow> followers = followRepository.findByToUserId(id);

        //팔로우 리스트
        List<Follow> principalFollows = followRepository.findByFromUserId(userDetails.getUser().getId());

        for(Follow f1 : followers){//3번 iter
            for(Follow f2 : principalFollows){
                if(f1.getFromUser().getId() == f2.getToUser().getId()){
                    f1.setFollowState(true);
                }
            }
        }

        model.addAttribute("followers",followers);
        return "follow/follower";
    }

    @GetMapping("/follow/follow/{id}")
    public String followFollow(@PathVariable int id,
                               @AuthenticationPrincipal CustomUserDetails userDetails,
                               Model model){
        //팔로우 리스트
        List<Follow> follows = followRepository.findByFromUserId(id);

        //팔로우 리스트
        List<Follow> principalFollows = followRepository.findByToUserId(userDetails.getUser().getId());

        for (Follow f1 : follows) { // 3번 돈다.
            for (Follow f2 : principalFollows) {
                if (f1.getToUser().getId() == f2.getToUser().getId()) {
                    f1.setFollowState(true);
                }
            }
        }
        model.addAttribute("follows",follows);

        return "follow/follow";

    }
}
