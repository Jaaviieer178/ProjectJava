package com.api.java.controllers;
import com.api.java.dto.DetailOrderDTO;
import com.api.java.services.DetailOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/detail-order")
@RequiredArgsConstructor
public class DetailOrderController {
    private final DetailOrderService detailOrderService;

    @GetMapping
    public List<DetailOrderDTO> getDetailOrders(){ return detailOrderService.getDetailOders(); }

    @PostMapping
    public ResponseEntity<DetailOrderDTO> newDetailOrder(@RequestBody DetailOrderDTO detailOrderDTO){
        return ResponseEntity.ok(detailOrderService.newDetailOrder(detailOrderDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetailOrderDTO> getDetailById(@PathVariable Long id){
        return ResponseEntity.ok(detailOrderService.getDetailById(id));
    }

    @GetMapping("/user/{userOrderId}")
    public ResponseEntity<List<DetailOrderDTO>> getDetailUserById(@PathVariable Long userOrderId){
        return  ResponseEntity.ok(detailOrderService.getDetailUserById(userOrderId));
    }
    @PutMapping("/{id}")
    public ResponseEntity<DetailOrderDTO> updateDetailById(@PathVariable Long id, @Valid @RequestBody DetailOrderDTO detailOrderDTO){
        return ResponseEntity.ok(detailOrderService.updateDetailById(id, detailOrderDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id){
        detailOrderService.deleteById(id);
        return  ResponseEntity.ok("Detalle de ordeneliminada");
    }

}