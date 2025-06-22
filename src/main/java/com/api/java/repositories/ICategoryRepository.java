package com.api.java.repositories;
import com.api.java.models.CategoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICategoryRepository extends JpaRepository<CategoryModel, Long> {
    List<CategoryModel> findByCategoryProducts(String categoryProducts);
}
