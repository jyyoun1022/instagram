package org.codej.instagram.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codej.instagram.model.Images;
import org.codej.instagram.model.Likes;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.awt.*;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ImageController {

    private final ImageRepository imageRepository;
    private final TagRepository tagRepository;
    private final LikesRepository likesRepository;

    @Value("file.path")
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
        return "/image/explore";
    }
    @PostMapping("/image/like/{id}")
    public @ResponseBody String imageLike(@PathVariable int id,
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
    public @ResponseBody List<Images> imageFeedScroll(@AuthenticationPrincipal CustomUserDetails userDetails,
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



}
