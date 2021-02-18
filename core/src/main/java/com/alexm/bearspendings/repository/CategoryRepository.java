package com.alexm.bearspendings.repository;

import com.alexm.bearspendings.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author AlexM
 * Date: 2/17/21
 **/
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findOneByName(String name);
}
