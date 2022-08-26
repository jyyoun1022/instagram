package org.codej.instagram.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codej.instagram.Util.UtilParser;
import org.codej.instagram.model.Images;
import org.codej.instagram.model.Likes;
import org.codej.instagram.model.Tags;
import org.codej.instagram.model.Users;
import org.codej.instagram.repository.ImageRepository;
import org.codej.instagram.repository.LikesRepository;
import org.codej.instagram.repository.TagRepository;
import org.codej.instagram.service.CustomUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ImageController {

    private final ImageRepository imageRepository;
    private final TagRepository tagRepository;
    private final LikesRepository likesRepository;

    @Value("${file.path}")
    private String fileRealPath;

    @GetMapping("/image/explore")
    public String imageExplore(Model model,
                               @PageableDefault(size = 9, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        // 알고리즘 ( 내 주변에서 좋아요가 가장 많은 순으로 해보는 것 추천)
        Page<Images> pImages = imageRepository.findAll(pageable);
        List<Images> images = pImages.getContent();

        // 4번 likeCount
        for (Images item : images) {
            int likeCount = likesRepository.countByImageId(item.getId());
            item.setLikeCount(likeCount);
        }

        model.addAttribute("images", images);
        return "image/explore";
    }
    @PostMapping("/image/like/{id}")
    @ResponseBody
    public String imageLike(@PathVariable int id,
                                          @AuthenticationPrincipal CustomUserDetails userDetails){
        Likes oldLike = likesRepository.findByUserIdAndImageId(userDetails.getUser().getId(), id);

        Optional<Images> oImage = imageRepository.findById(id);
        Images images = oImage.get();

        try{
            if(oldLike == null){//좋아요 안한 상태(추가)
                Likes newLike = Likes.builder().image(images).user(userDetails.getUser()).build();

                likesRepository.save(newLike);
                //좋아요 카운트 증가(리턴값 수정)
                return "like";
            }else {//좋아요 한 상태(삭제)
                likesRepository.delete(oldLike);
                //좋아요 카운트 증가(리턴 값 수정)
                return  "unLike";
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return "fail";
    }

    @GetMapping("/image/feed/scroll")
    @ResponseBody
    public List<Images> imageFeedScroll(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                      @PageableDefault(size = 3,sort = "id",direction = Sort.Direction.DESC)Pageable pageable,
                                                      Model model){

        Page<Images> pageImages = imageRepository.findImage(userDetails.getUser().getId(), pageable);
        List<Images> images = pageImages.getContent();

        for (Images image : images) {
            Likes like = likesRepository.findByUserIdAndImageId(userDetails.getUser().getId(), image.getId());
            if(like != null){
                image.setHeart(true);
            }
            int likeCount = likesRepository.countByImageId(image.getId());
            image.setLikeCount(likeCount);;
        }
        return images;
    }
    @GetMapping({"/","/image/feed"})

    public String imageFeed(@AuthenticationPrincipal CustomUserDetails userDetails,
                            @PageableDefault(size = 3,sort = "id",direction = Sort.Direction.DESC)Pageable pageable,
                            Model model){
        //내가 팔로우한 친구들의 사진
        Page<Images> pageImages = imageRepository.findImage(userDetails.getUser().getId(), pageable);
        List<Images> images = pageImages.getContent();

        for (Images item : images) {
            int likeCount = likesRepository.countByImageId(item.getId());
            item.setLikeCount(likeCount);
        }
        model.addAttribute("images",images);

        return "image/feed";
    }

    @GetMapping("/image/upload")
    public String imageUpload(){
        return "image/image_upload";
    }

    @PostMapping("/image/uploadProc")
    public String imageUploadProc(@AuthenticationPrincipal CustomUserDetails userDetails,
                                  @RequestParam("file")MultipartFile file,
                                  @RequestParam("caption")String caption,
                                  @RequestParam("location")String location,
                                  @RequestParam("tags")String tags,
                                  HttpSession session) throws IOException {

        //이미지 업로드 수행
        UUID uuid = UUID.randomUUID();
        String uuidFilename = uuid + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(fileRealPath + uuidFilename);
        Files.write(filePath,file.getBytes());
        log.info("fileRealPath : {}",fileRealPath);
        log.info("session : {}",session.getServletContext().getContext("/"));

        Users principal = userDetails.getUser();

        Images images = new Images();
        images.setCaption(caption);
        images.setLocation(location);
        images.setUser(principal);
        images.setPostImage(uuidFilename);

        imageRepository.save(images);

        List<String> tagList = UtilParser.tagsParser(tags);

        for (String tag : tagList) {
            Tags t = new Tags();
            t.setImage(images);
            t.setName(tag);
            tagRepository.save(t);
            images.getTags().add(t);
        }

        return "redirect:/";


    }




}
