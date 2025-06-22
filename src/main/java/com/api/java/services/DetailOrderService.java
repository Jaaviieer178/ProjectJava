package com.api.java.services;
import com.api.java.dto.DetailOrderDTO;
import com.api.java.mapper.DetailOrderMapper;
import com.api.java.models.DetailOrderModel;
import com.api.java.repositories.IDetailOrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class DetailOrderService {
    private final IDetailOrderRepository detailOrderRepository;
    private final DetailOrderMapper detailOrderMapper;

    public List<DetailOrderDTO> getDetailOders(){
        List<DetailOrderModel> details = detailOrderRepository.findAll();
        return details.stream()
                .map(detailOrderMapper::detailOrderToDetailOrderDto)
                .collect(Collectors.toList());
    }

    public DetailOrderDTO newDetailOrder(DetailOrderDTO detailOrderDTO) {
        return detailOrderMapper.detailOrderToDetailOrderDto(detailOrderRepository.save(detailOrderMapper.detailOrderDtoToDetailOrder(detailOrderDTO)));
    }

    public DetailOrderDTO getDetailById(Long id){
        return detailOrderMapper.detailOrderToDetailOrderDto(
                detailOrderRepository.findById(id)
                        .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Detalles de Orden con id" +id+ "no encontrados"))
        );
    }

    public List<DetailOrderDTO> getDetailUserById(Long userOrderId){
        List<DetailOrderModel> userOrder = detailOrderRepository.findByUserOrder_Id(userOrderId);
                return userOrder.stream()
                        .map(detailOrderMapper::detailOrderToDetailOrderDto)
                        .collect(Collectors.toList());
    }

    @Transactional
    public DetailOrderDTO updateDetailById(Long id, DetailOrderDTO detailOrderDTO){
        DetailOrderModel updateDetail = detailOrderRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Detalles de Orden con id" +id+  "no encontradas"));
        detailOrderMapper.updateDetailOrderFromDto(detailOrderDTO, updateDetail);
        return detailOrderMapper.detailOrderToDetailOrderDto(detailOrderRepository.save(updateDetail));
    }

    public void deleteById(Long id){
        if (!detailOrderRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Detalle de orden con id" +id+ "no encontrado");
        }
        detailOrderRepository.deleteById(id);
    }
}