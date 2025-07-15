package com.api.java.services;
import com.api.java.dto.CategoryDTO;
import com.api.java.mapper.CategoryMapper;
import com.api.java.models.CategoryModel;
import com.api.java.repositories.ICategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.stream.Collectors;

/** Servicio que encapsula la lógica de negocio relacionada con las categorías de productos.
 *  Proporciona operaciones para crear, obtener, buscar y eliminar categorías. */

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final ICategoryRepository categoryRepository; // Repositorio para acceder a los datos de la entidad CategoryModel
    private final CategoryMapper categoryMapper; // Mapper para convertir entre CategoryDTO y CategoryModel

    /** Crea una nueva categoría a partir del DTO recibido.
     * @param categoryDTO DTO con los datos a registrar
     * @return DTO con los datos de la categoría creada (incluye ID) */

    public CategoryDTO createNewCategory(CategoryDTO categoryDTO) {
        CategoryModel categoryModel = categoryMapper.categoryDtoToCategory(categoryDTO); // Convierte el DTO en entidad
        return categoryMapper.categoryToCategoryDto(categoryRepository.save(categoryModel)); // Guarda la entidad en la base de datos y convierte la entidad guardada de nuevo en DTO para retornar
    }

    /** Obtiene todas las categorías registradas.
     * @return lista de categorías convertidas a DTO */

    public List<CategoryDTO> getCategories() {
        return categoryRepository.findAll().stream() // Obtiene todas las entidades y las convierte en un Stream
                .map(categoryMapper::categoryToCategoryDto) // Mapea cada CategoryModel a CategoryDTO
                .collect(Collectors.toList()); // Recolecta los DTOs en una lista
    }

    /** Busca una categoría por su ID.
     *
     * @param id identificador único de la categoría
     * @return DTO de la categoría encontrada
     * @throws ResponseStatusException si la categoría no existe */

    public CategoryDTO getCategoryById(Long id) {
        return categoryMapper.categoryToCategoryDto(throwResponse(id));// Si la encuentra, mapea la entidad a DTO y la retorna
    }

    /** Busca categorías cuyo nombre sea exactamente igual al parámetro recibido.
     *
     * @param categoryProducts nombre exacto de la categoría a buscar
     * @return lista de categorías encontradas como DTO */

    public List<CategoryDTO> searchCategory(String categoryProducts) {
        return categoryRepository.findByCategoryProducts(categoryProducts) // Ejecuta la búsqueda en el repositorio y transforma directamente el resultado a DTOs
                .stream() // Convierte la lista en un flujo
                .map(categoryMapper::categoryToCategoryDto) // Mapea cada entidad a DTO
                .collect(Collectors.toList()); // Recolecta el resultado en una lista
    }

    /** Elimina una categoría por su ID si existe.
     * @param id identificador de la categoría a eliminar
     * @throws ResponseStatusException si la categoría no existe */

    public void deleteCategoryById(Long id) {
        if (!categoryRepository.existsById(id)) { // Verifica si la categoría existe
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría con ID " + id + " no encontrada");
        }
        categoryRepository.deleteById(id); // Si existe, la elimina de la base de datos
    }

    /** Metodo reutilizable que obtiene una categoría por su ID o lanza una excepción 404 si no existe.
     * @param id ID de la categoría
     * @return entidad encontrada
     * @throws ResponseStatusException si no se encuentra */

    private CategoryModel throwResponse(Long id) {  return categoryRepository.findById(id) .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría con ID " + id + " no encontrada"));  }
}