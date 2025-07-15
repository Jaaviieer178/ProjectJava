package com.api.java.services;
import com.api.java.dto.DetailOrderDTO;
import com.api.java.dto.ProductDTO;
import com.api.java.mapper.DetailOrderMapper;
import com.api.java.models.DetailOrderModel;
import com.api.java.models.ProductModel;
import com.api.java.repositories.IDetailOrderRepository;
import com.api.java.repositories.IProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


/** Servicio que gestiona la lógica de negocio relacionada con los detalles de órdenes.
 * Permite crear, consultar, actualizar y eliminar registros de tipo DetailOrder.*/

@Service
@RequiredArgsConstructor
public class DetailOrderService {
    private final IDetailOrderRepository detailOrderRepository;  //Repositorio JPA que maneja la persistencia de DetailOrderModel.
    private final DetailOrderMapper detailOrderMapper;  // Mapper encargado de convertir entre entidades y DTOs de detalle de orden.
    private final IProductRepository productRepository;

    /**
     * Obtiene todos los detalles de órdenes registrados en el sistema.
     *
     * @return lista de objetos DetailOrderDTO
     */

    public List<DetailOrderDTO> getDetailOrders() {
        return detailOrderRepository.findAll().stream()     // Recupera todas las entidades desde la base de datos
                .map(detailOrderMapper::detailOrderToDetailOrderDto)    // Convierte cada entidad a DTO
                .collect(Collectors.toList());  // Agrupa en una lista
    }

    /**
     * Crea un nuevo detalle de orden en la base de datos.
     *
     * @param detailOrderDTO DTO recibido con los datos a guardar
     * @return DTO del detalle de orden creado
     */

    public DetailOrderDTO newDetailOrder(DetailOrderDTO detailOrderDTO) {
        validateProductActiveStock(detailOrderDTO.getProductOrder(), detailOrderDTO.getAmount());

        DetailOrderModel entity = detailOrderMapper.detailOrderDtoToDetailOrder(detailOrderDTO);    // Convierte el DTO a entidad JPA
        DetailOrderModel saved = detailOrderRepository.save(entity);    // Guarda la entidad en base de datos
        return detailOrderMapper.detailOrderToDetailOrderDto(saved);    // Convierte la entidad guardada nuevamente a DTO para la respuesta
    }

    /**
     * Obtiene un detalle de orden específico por su ID.
     *
     * @param id identificador único del detalle
     * @return DTO correspondiente al ID solicitado
     */

    public DetailOrderDTO getDetailById(Long id) {
        return detailOrderMapper.detailOrderToDetailOrderDto(throwResponse(id));
    }     // Lanza excepción si no existe, si existe lo retorna como DTO


    /**
     * Obtiene todos los detalles de órdenes realizados por un usuario específico.
     *
     * @param userOrderId ID del usuario que hizo la orden
     * @return lista de detalles de órdenes correspondientes al usuario
     */

    public List<DetailOrderDTO> getDetailUserById(Long userOrderId) {
        return detailOrderRepository.findByUserOrder_Id(userOrderId).stream()   // Consulta el repositorio filtrando por ID de usuario
                .map(detailOrderMapper::detailOrderToDetailOrderDto)    // Mapea cada resultado a DTO
                .collect(Collectors.toList());
    }

    /**
     * Actualiza un detalle de orden existente con nuevos valores.
     *
     * @param id             ID del registro a actualizar
     * @param detailOrderDTO DTO con los datos nuevos
     * @return DTO actualizado luego de persistir
     */

    @Transactional
    public DetailOrderDTO updateDetailById(Long id, DetailOrderDTO detailOrderDTO) {
        DetailOrderModel existingDetail = throwResponse(id);    // Obtener el detalle actual desde la base de datos

        // Obtener el producto actualizado desde el DTO
        ProductDTO productDTO = detailOrderDTO.getProductOrder();
        Integer newAmount = detailOrderDTO.getAmount();
        Integer previousAmount = existingDetail.getAmount();

        validateChangeAmount(productDTO, previousAmount, newAmount);   // Validar producto y stock según la diferencia
        detailOrderMapper.updateDetailOrderFromDto(detailOrderDTO, existingDetail);  // Actualizar los campos del detalle con el mapper
        DetailOrderModel saved = detailOrderRepository.save(existingDetail);    // Guardar el detalle actualizado
        adjustStock(productDTO.getId(), previousAmount, newAmount);   // Ajustar el stock del producto
        return detailOrderMapper.detailOrderToDetailOrderDto(saved);
    }

    /**
     * Elimina un detalle de orden según su ID.
     *
     * @param id ID del registro a eliminar
     */

    public void deleteById(Long id) {
        if (!detailOrderRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Detalle de orden con id " + id + " no encontrado");
        }     // Valida existencia del registro antes de eliminar
        detailOrderRepository.deleteById(id);   // Elimina el registro
    }

    /**
     * Metodo auxiliar que lanza una excepción 404 si el ID no existe.
     *
     * @param id ID a buscar
     * @return entidad encontrada
     */

    private DetailOrderModel throwResponse(Long id) {
        return detailOrderRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Detalles de la Orden con ID " + id + " no encontrado"));
    }

    /** Valida que el producto exista, esté activo y tenga stock suficiente.
     * Lanza una excepción si no cumple las condiciones.
     * @param productDTO         DTO del producto a validar
     * @param requestAmount cantidad de unidades que se desean ordenar*/

    private void validateProductActiveStock(ProductDTO productDTO, Integer requestAmount) {
        if (productDTO == null || productDTO.getId() == null) {     // Verifica que el DTO del producto no sea nulo y que contenga un ID válido
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El producto es obligatorio");
        }

        ProductModel product = productRepository.findById(productDTO.getId())   // Busca el producto en la base de datos usando su ID,  si no se encuentra, lanza una excepción 404
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));

        if (!product.isActivo()) {     // Verifica si el producto está marcado como activo y si está inactivo, no se permite realizar pedidos con él
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El producto está inactivo y no puede ser ordenado");
        }

        if (requestAmount == null || requestAmount < 1) {     // Verifica que la cantidad solicitada no sea nula ni menor a 1 ,esto asegura que se solicite al menos una unidad
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cantidad solicitada debe ser al menos 1 unidad");
        }

        if (product.getStockProduct() < requestAmount) {  // Verifica que el stock disponible sea suficiente para cubrir la cantidad solicitada. Si no hay suficiente stock, lanza una excepción con un mensaje detallado
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Stock insuficiente: disponible " + product.getStockProduct() + ", solicitado " + requestAmount);
        }
    }

    /** Valida que el producto esté activo y tenga stock suficiente si se aumenta la cantidad.
     * @param productDTO producto recibido en el DTO
     * @param previousAmount cantidad actual registrada en la base
     * @param newAmount cantidad solicitada en la actualización */

    private void validateChangeAmount(ProductDTO productDTO, int previousAmount, int newAmount) {
        int difference = newAmount - previousAmount;


        if (difference > 0) {   // Solo validamos stock si se está solicitando más unidades
            validateProductActiveStock(productDTO, difference);
        }
    }

    /** Ajusta el stock del producto según la diferencia entre la cantidad anterior y la nueva.
     * @param productId ID del producto
     * @param previousAmount cantidad registrada antes de la actualización
     * @param newAmount nueva cantidad solicitada */

    private void adjustStock(Long productId, int previousAmount, int newAmount) {
        ProductModel product = productRepository.findById(productId)   // Busca el producto en la base de datos usando su ID, si no se encuentra, lanza una excepción 404 (NOT_FOUND).
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));

        int difference = newAmount - previousAmount;      // Calcula la diferencia entre la nueva cantidad y la anterior. Si es positiva, significa que se están solicitando más unidades (hay que descontar más stock) y si es negativa, significa que se están solicitando menos unidades (hay que devolver stock).

        int newStock = product.getStockProduct() - difference;       // Calcula el nuevo stock restando la diferencia al stock actual.

        if (newStock < 0) {   // Verifica que el nuevo stock no sea negativo, si lo es, lanza una excepción 400 (BAD_REQUEST) indicando que no hay suficiente stock.
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Stock insuficiente para actualizar la orden. Disponible: " + product.getStockProduct());
        }

        product.setStockProduct(newStock);   // Actualiza el stock del producto con el nuevo valor calculado.
        productRepository.save(product);   // Persiste el cambio en la base de datos.
    }
}