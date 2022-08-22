package org.codej.instagram.repository;

import org.codej.instagram.model.Tags;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tags,Integer> {

}
