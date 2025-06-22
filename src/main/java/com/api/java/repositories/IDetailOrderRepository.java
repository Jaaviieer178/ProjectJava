package com.api.java.repositories;

import com.api.java.models.DetailOrderModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IDetailOrderRepository extends JpaRepository<DetailOrderModel, Long> {
    List<DetailOrderModel> findByUserOrder_Id(Long userOrderId);
}
