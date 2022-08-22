package org.codej.instagram.controller;

import lombok.RequiredArgsConstructor;
import org.codej.instagram.model.Images;
import org.codej.instagram.model.Tags;
import org.codej.instagram.repository.ImageRepository;
import org.codej.instagram.repository.TagRepository;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageRepository imageRepository;
    private final TagRepository tagRepository;

    @PostMapping("/image/upload")
    public ResponseEntity<Resource> uploadFile(@RequestParam("file")MultipartFile file) throws IOException {

        Images image = new Images();
        image.setFile(file.getBytes());
        image.setCaption("git 집필 자료 사진");
        image.setLocation("인천 논현");

        List<Tags> list = new ArrayList<>();
        Tags tag1 = new Tags();
        Tags tag2 = new Tags();
        tag1.setName("Git");
        tag2.setName("버전 관리");
        list.add(tag1);
        list.add(tag2);

        imageRepository.save(image);

        for (Tags tag : list) {
            tagRepository.save(tag);
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+file.getOriginalFilename() + "\"")
                .body(new ByteArrayResource(file.getBytes()));
    }


}
