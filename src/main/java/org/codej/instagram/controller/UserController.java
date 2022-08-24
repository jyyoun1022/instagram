package org.codej.instagram.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codej.instagram.model.Images;
import org.codej.instagram.model.Users;
import org.codej.instagram.repository.FollowRepository;
import org.codej.instagram.repository.LikesRepository;
import org.codej.instagram.repository.UserRepository;
import org.codej.instagram.service.CustomUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Controller
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final LikesRepository likesRepository;

    @Value("file.path")
    private String fileRealPath;


    @GetMapping("/")
    public String home(){
        return "auth/join";
    }
    @GetMapping("/auth/login")
    public String authLogin(){
        return "/auth/login";
    }
    @GetMapping("/auth/join")
    public String authJoin(){
        return "/auth/join";
    }
    @PostMapping("/auth/joinProc")
    public String joinProc(Users user){
        String rawPassword = user.getPassword();
        String encPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encPassword);

        userRepository.save(user);

        return "redirect:/auth/login";
    }

    /**
     * 1. imageCount
     * 2. followCount
     * 3. follwingCount
     * 4. User Object(Image (likeCount) collection)
     * 5. followCheck follow YN ( 1 : follow , !1 : unfollow)
     */
    @GetMapping("/user/{id}/")
    public String profile(@PathVariable int id,
                          @AuthenticationPrincipal CustomUserDetails userDetails,
                          Model model){
        Optional<Users> oUser = userRepository.findById(id);
        Users users = oUser.get();

        //1. imageCount
        int imageCount = users.getImages().size();
        model.addAttribute("imageCount",imageCount);

        //2.followCount (나를 추가한 사람)
        int followCount = followRepository.countByFromUserId(users.getId());
        model.addAttribute("followCount",followCount);

        //3.followerCount
        int followerCount = followRepository.countByToUserId(users.getId());
        model.addAttribute("followerCount",followerCount);

        //4.likeCount
        for(Images item : users.getImages()){
            int likeCount = likesRepository.countByImageId(item.getId());
            item.setLikeCount(likeCount);
        }

        model.addAttribute("user",users);

        //5.
        Users principal = userDetails.getUser();

        int followCheck = followRepository.countByFromUserIdAndToUserId(principal.getId(), id);
        log.info("followCheck : {}",followCheck);
        model.addAttribute("followCheck",followCheck);

        return "user/profile";


    }
    @PostMapping("/user/profileUpload")
    public String userProfileUpload(@RequestParam("profileImage")MultipartFile file,
                                    @AuthenticationPrincipal CustomUserDetails userDetails) throws IOException {
        Users principal = userDetails.getUser();

        //파일 처리
        UUID uuid = UUID.randomUUID();
        String uuidFilename = uuid + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(fileRealPath + uuidFilename);
        Files.write(filePath,file.getBytes());

        //영소화
        Optional<Users> oUser = userRepository.findById(principal.getId());
        Users users = oUser.get();

        //값 변경
        users.setProfileImage(uuidFilename);

        //다시 영속화 및 저장
        userRepository.save(users);
        return "redirect:/user/"+principal.getId();
    }
    @GetMapping("/user/edit")
    public String userEdit(@AuthenticationPrincipal CustomUserDetails userDetails,
                           Model model){
        Optional<Users> oUser = userRepository.findById(userDetails.getUser().getId());
        Users users = oUser.get();
        model.addAttribute("user",users);
        return "user/profile_edit";
    }
    @PutMapping("/user/editProc")
    public String userEditProc(Users requestUser,
                               @AuthenticationPrincipal CustomUserDetails userDetails){

        //영속화
        Optional<Users> oUser = userRepository.findById(userDetails.getUser().getId());
        Users users = oUser.get();

        //값 변경
        users.setName(requestUser.getName());
        users.setUsername(requestUser.getUsername());
        users.setWebsite(requestUser.getWebsite());
        users.setBio(requestUser.getBio());
        users.setEmail(requestUser.getEmail());
        users.setPhone(requestUser.getPhone());
        users.setGender(requestUser.getGender());

        //다시 영속화 및 flush
        userRepository.save(users);

        return "redirect:/user" + userDetails.getUser().getId();

    }






}
