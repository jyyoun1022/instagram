package org.codej.instagram.repository;

import org.codej.instagram.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow,Integer> {

    //unfollow
    @Transactional
    int deleteByFromUserIdAndToUserId(int fromUserId, int toUserId);

    //follow YN
    int countByFromUserIdAndToUserId(int fromUserId, int toUserId);

    //follow List
    List<Follow> findByFromUserId(int fromUserId);

    //follower List
    List<Follow> findByToUserId(int toUserId);

    //follow Count
    int countByFromUserId(int fromUserId);

    //follower Count
    int countByToUserId(int toUserId);
}
