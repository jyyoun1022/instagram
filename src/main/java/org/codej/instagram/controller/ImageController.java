package org.codej.instagram.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codej.instagram.model.Images;
import org.codej.instagram.model.Tags;
import org.codej.instagram.model.Users;
import org.codej.instagram.repository.ImageRepository;
import org.codej.instagram.repository.TagRepository;
import org.codej.instagram.repository.UserRepository;
import org.codej.instagram.uitl.UtilParser;
import org.codej.instagram.uitl.UtilTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ImageController {

    private static String UPLOADED_FOLDER = "/Users/YOUNJY/Downloads/instagram/src/main/resources/static/images";
    private final ImageRepository imageRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    @GetMapping("/image/{id}")
    public Images imageDetail(@PathVariable int id) {
        Optional<Images> imageOptional = imageRepository.findById(id);
        if(imageOptional.isPresent()) {
            Images image = imageOptional.get();

            return image;
        }else {
            return null;
        }
    }
    @PostMapping("/image/upload")
    public Images imageUpload(@RequestParam("file") MultipartFile file, String caption, String location, String tags) throws IOException {
        Path filePath = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
        log.info("filePath ={}",filePath);
        log.info("file.getByte={}",file.getBytes());
        log.info("file.getContext={}",file.getContentType());


        Files.write(filePath, file.getBytes());

        Users user = UtilTest.getUser();

        List<String> tagList = UtilParser.tagsParser(tags);

        Images image = Images.builder()
                .caption(caption)
                .location(location)
                .user(user)
                .mimeType(file.getContentType())
                .fileName(file.getOriginalFilename())
                .filePath(filePath.toString())
                .build();





        for(String t : tagList) {
            Tags tag = new Tags();
            tag.setName(t);
            tag.setImage(image);
            tag.setUser(user);
            //스프링은 JDK1.8부터 LocalDate를 권장합니다.
            tag.setRegDate(LocalDateTime.now());
            tag.setModDate(LocalDateTime.now());
            image.getTags().add(tag);
        }


        imageRepository.save(image);

        return image;
    }

    @PostMapping("/test/image/upload")
    public ResponseEntity<Resource> imageUpload(@RequestParam("file") MultipartFile file) throws IOException {

        Images image = new Images();
        image.setRegDate(LocalDateTime.now());
        image.setModDate(LocalDateTime.now());
        imageRepository.save(image);

        Tags tag1 = new Tags();
        Tags tag2 = new Tags();
        tag1.setName("Git");
        tag1.setImage(image);
        tag2.setName("버전관리");
        tag2.setImage(image);
        tagRepository.save(tag1);
        tagRepository.save(tag2);

        return  ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getOriginalFilename() + "\"")
                .body(new ByteArrayResource(file.getBytes()));
    }

    @PostMapping("/image/upload")
    public ResponseEntity<Resource> uploadFile(@RequestParam("file")MultipartFile file,String caption,String location,String tags) throws IOException {


        Path filePath = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
        log.info("filePath ={}",filePath);
        log.info("file.getByte={}",file.getBytes());
        Files.write(filePath,file.getBytes());
        log.info("file.getContext={}",file.getContentType());
        Users user = UtilTest.getUser();
        List<String> tagList = UtilParser.tagsParser(tags);

        Images image = Images.builder()
                .caption(caption)
                .location(location)
                .user(user)
                .mimeType(file.getContentType())
                .fileName(file.getOriginalFilename())
                .filePath(filePath.toString())
                .build();

        for(String t : tagList) {
            Tags tag = new Tags();
            tag.setName(t);
            tag.setImage(image);
            tag.setUser(user);

            tag.setRegDate(LocalDateTime.now());
            tag.setModDate(LocalDateTime.now());
            image.getTags().add(tag);
        }


        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+file.getOriginalFilename() + "\"")
                .body(new ByteArrayResource(file.getBytes()));
    }


}
