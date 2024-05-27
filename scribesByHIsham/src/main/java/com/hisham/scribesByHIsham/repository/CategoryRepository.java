package com.hisham.scribesByHIsham.repository;

import com.hisham.scribesByHIsham.model.Category;
import com.hisham.scribesByHIsham.model.CategoryName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(CategoryName categoryName);
}
