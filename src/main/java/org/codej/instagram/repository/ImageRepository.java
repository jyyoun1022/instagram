package org.codej.instagram.repository;

import org.codej.instagram.model.Images;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.*;
import java.util.List;

public interface ImageRepository extends JpaRepository<Images,Integer> {
    List<Images> findByUserId(int UserId);

    @Query(value = "select * from images where userId in (select toUserId from follow where fromUserId = ?1) or userId = ?1", nativeQuery = true)
    Page<Images> findImage(int userId, Pageable pageable);
}
