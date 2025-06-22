package com.api.java.mapper;
import com.api.java.dto.DetailOrderDTO;
import com.api.java.models.DetailOrderModel;
import org.mapstruct.*;


@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {UserMapper.class, ProductMapper.class})
public interface DetailOrderMapper {
    @Mapping(source = "userOrder.id", target = "userOrder")
    DetailOrderDTO detailOrderToDetailOrderDto(DetailOrderModel detailOrderModel);

    @Named("detailOrderDtoToDetailOrder")
    @Mapping(source = "userOrder", target = "userOrder.id")
    DetailOrderModel detailOrderDtoToDetailOrder(DetailOrderDTO detailOrderDTO);

    @InheritConfiguration(name = "detailOrderDtoToDetailOrder")
    @Mapping(target = "id", ignore = true)
    void updateDetailOrderFromDto(DetailOrderDTO dto, @MappingTarget DetailOrderModel entity);
    }