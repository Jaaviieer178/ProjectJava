package com.api.java.mapper;
import com.api.java.dto.DetailOrderDTO;
import com.api.java.models.DetailOrderModel;
import org.mapstruct.*;


/** Mapper de MapStruct encargado de convertir entre entidades DetailOrderModel y DTOs DetailOrderDTO.
 * - Se registra como componente Spring gracias a `componentModel = "spring"`.
 * - Ignora automáticamente propiedades nulas al mapear (`nullValuePropertyMappingStrategy = IGNORE`).
 * - Utiliza otros mappers relacionados para objetos anidados: UserMapper y ProductMapper. */

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {UserMapper.class, ProductMapper.class})
public interface DetailOrderMapper {
    /** Convierte una entidad DetailOrderModel en su representación DTO.
     * Extrae el ID del usuario (userOrder.id) y lo asigna al campo simple userOrder del DTO.
     *
     * @param detailOrderModel entidad JPA del detalle de orden
     * @return DTO correspondiente al detalle de orden */

    @Mapping(source = "userOrder.id", target = "userOrder")
    DetailOrderDTO detailOrderToDetailOrderDto(DetailOrderModel detailOrderModel);

    /** Convierte un DTO de detalle de orden en una entidad JPA.
     * Toma el campo userOrder del DTO como un ID, y lo asigna al campo anidado userOrder.id de la entidad.
     * Se nombra como base para la herencia de configuración en otros métodos.
     *
     * @param detailOrderDTO DTO de entrada
     * @return entidad JPA construida */
    @Named("detailOrderDtoToDetailOrder")
    @Mapping(source = "userOrder", target = "userOrder.id")
    DetailOrderModel detailOrderDtoToDetailOrder(DetailOrderDTO detailOrderDTO);

    /** Actualiza una entidad DetailOrderModel existente con los datos del DTO.
     * - Reutiliza la configuración del metodo "detailOrderDtoToDetailOrder".
     * - Ignora la propiedad "id" para evitar sobrescribir el identificador en una actualización parcial.
     *
     * @param dto    DTO que contiene los valores nuevos
     * @param entity entidad existente que será modificada */

    @InheritConfiguration(name = "detailOrderDtoToDetailOrder")
    @Mapping(target = "id", ignore = true)
    void updateDetailOrderFromDto(DetailOrderDTO dto, @MappingTarget DetailOrderModel entity);
}