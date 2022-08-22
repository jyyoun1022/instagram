package org.codej.instagram.repository;

import org.codej.instagram.model.Images;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Images,Integer> {

}
