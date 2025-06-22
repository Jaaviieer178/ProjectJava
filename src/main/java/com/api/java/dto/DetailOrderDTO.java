package com.api.java.dto;
import com.api.java.models.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DetailOrderDTO {
    private Long id;
    private Long UserOrder;
    private ProductDTO productOrder;
    private Integer amount;
    private OrderStatus status;
}
