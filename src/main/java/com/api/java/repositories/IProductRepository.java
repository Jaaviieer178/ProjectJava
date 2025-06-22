package com.api.java.repositories;
import com.api.java.models.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductRepository extends JpaRepository<ProductModel, Long> {
    List<ProductModel> findByNameProduct(String nameProduct);
    List<ProductModel> findByCategoryProduct_Id(Long categoryId);

}
