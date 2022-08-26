package org.codej.instagram.repository;

import org.codej.instagram.model.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LikesRepository extends JpaRepository<Likes,Integer> {

    //내가 좋아요 한 이미지 찾기 위해
    Likes findByUserIdAndImageId(int userId, int imageId);

    //이미지 좋아요 카운트
    int countByImageId(int imageId);

    //내 이미지를 좋아요 하는 알림 정보
    @Query(value = "select * from likes where imageId in (select id from images where userId=?1) order by id desc limit 5;",nativeQuery = true)
    List<Likes> findLikeNotification(int userId);
}
