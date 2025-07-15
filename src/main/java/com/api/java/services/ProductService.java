package com.api.java.services;
import com.api.java.dto.ProductDTO;
import com.api.java.mapper.ProductMapper;
import com.api.java.models.ProductModel;
import com.api.java.repositories.IProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.stream.Collectors;

/** Servicio responsable de gestionar la lógica de negocio relacionada con productos.
 * Ofrece operaciones CRUD y búsquedas filtradas. */
@Service
@RequiredArgsConstructor
public class ProductService {
    private final IProductRepository productRepository; // Repositorio para acceder a los datos de la entidad ProductModel
    private final ProductMapper productMapper; // Mapper para convertir entre ProductDTO y ProductModel

    /** Crea un nuevo producto en la base de datos.
     * @param productDTO DTO recibido desde el controlador
     * @return el producto guardado en formato DTO */

    public ProductDTO createProduct(ProductDTO productDTO) {
        ProductModel entity = productMapper.productDtoToProduct(productDTO);    // Convierte el DTO a entidad JPA
        ProductModel saved = productRepository.save(entity);    // Guarda la entidad en la base de datos
        return productMapper.productToProductDto(saved);    // Convierte la entidad guardada de nuevo a DTO
    }

    /** Devuelve todos los productos existentes.
     * @return lista de productos en formato DTO */

    public List<ProductDTO> getProducts() {
        return productRepository.findAll().stream() // Obtiene todos los productos, los mapea a DTO y retorna la lista
                .map(productMapper::productToProductDto)
                .collect(Collectors.toList());
    }

    /** Busca y retorna un producto por su ID.
     * @param id ID del producto
     * @return producto correspondiente en formato DTO */

    public ProductDTO getProductById(Long id) {
        ProductModel product = throwResponse(id);   // Busca la entidad o lanza una excepción 404 si no existe
        return productMapper.productToProductDto(product);      // Mapea la entidad a DTO y la retorna
    }

    /** Busca productos por nombre exacto.
     * @param nameProduct nombre del producto a buscar
     * @return lista de productos coincidentes */

    public List<ProductDTO> searchProduct(String nameProduct) {
        return productRepository.findByNameProduct(nameProduct).stream() // Consulta el repositorio y mapea los resultados a DTO
                .map(productMapper::productToProductDto)
                .collect(Collectors.toList());
    }

    /** Obtiene productos filtrados por categoría.
     * @param categoryId ID de la categoría
     * @return lista de productos en esa categoría */

    public List<ProductDTO> getProdCategoryById(Long categoryId) {
        return productRepository.findByCategoryProduct_Id(categoryId).stream()  // Filtra por ID de categoría y convierte a DTO
                .map(productMapper::productToProductDto)
                .collect(Collectors.toList());
    }

    /** Actualiza los datos de un producto existente.
     * @param id ID del producto a actualizar
     * @param updateProd DTO con los nuevos valores
     * @return producto actualizado en formato DTO */

    @Transactional
    public ProductDTO updateProdById(Long id, ProductDTO updateProd) {
        ProductModel existing = throwResponse(id);      // Valida la existencia del producto original
        productMapper.updateProductFromDto(updateProd, existing);   // Aplica los cambios del DTO sobre la entidad existente
        return productMapper.productToProductDto(productRepository.save(existing));     // Guarda la entidad actualizada y convierte a DTO antes de retornar
    }

    /** Reactiva un producto previamente desactivado, marcándolo como disponible para la venta.
     *
     * @param id ID del producto a reactivar */

    public void activateProdById(Long id) {
        ProductModel product = throwResponse(id); // Reutiliza el metodo auxiliar para validar existencia
        product.setActivo(true);                 // Marca el producto como activo
        productRepository.save(product);         // Persiste el cambio
    }

    /** Desactiva un producto de la base de datos por su ID.
     *
     * @param id ID del producto */

    public void desactivateProdById(Long id) {
        ProductModel product = throwResponse(id);    // Reutiliza el metodo auxiliar para validar existencia
        product.setActivo(false);   // Marca el producto como desactivado
        productRepository.save(product);    // Persiste el cambio

    }

    /** Metodo auxiliar privado que recupera un producto por ID o lanza una excepción 404 si no existe.
     * @param id ID del producto a buscar
     * @return entidad ProductModel */

    private ProductModel throwResponse(Long id) {  return productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto con ID " + id + " no encontrado"));  }
}