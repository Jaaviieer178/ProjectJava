package com.api.java.controllers;
import com.api.java.dto.CategoryDTO;
import com.api.java.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "Categoria", description = "Operaciones relacionadas con las categorías de los productos")
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(summary = "Crear una nueva categoría", description = "Registra una nueva categoría en el sistema con su nombre y propiedades")
    @ApiResponse(responseCode = "200", description = "Categoría creada exitosamente") @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO con los datos de la categoría a crear", required = true)
    @PostMapping
    public ResponseEntity<CategoryDTO> createNewCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok(categoryService.createNewCategory(categoryDTO));
    }

    @Operation(summary = "Obtener todas las categorías", description = "Devuelve una lista completa de todas las categorías registradas")
    @ApiResponse(responseCode = "200", description = "Categorías recuperadas exitosamente")
    @GetMapping
    public List<CategoryDTO> getCategotires() { return categoryService.getCategories(); }

    @Operation(summary = "Buscar una categoría por ID", description = "Obtiene una categoría específica según su identificador único")
    @ApiResponse(responseCode = "200", description = "Categoría encontrada")
    @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@Parameter(description = "ID de la categoría a buscar", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @Operation(summary = "Buscar categorías por texto", description = "Busca categorías que coincidan parcial o completamente con un texto dado")
    @ApiResponse(responseCode = "200", description = "Búsqueda realizada con éxito")
    @GetMapping("/search")
    public ResponseEntity<List<CategoryDTO>> searchCategory(
            @Parameter(description = "Texto a buscar en los nombres de las categorías", required = true)
            @RequestParam String categoryProducts) {
        return ResponseEntity.ok(categoryService.searchCategory(categoryProducts));
    }

    @Operation(summary = "Eliminar una categoría por ID", description = "Elimina de forma permanente una categoría usando su identificador")
    @ApiResponse(responseCode = "200", description = "Categoría eliminada correctamente")
    @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategoryById(
            @Parameter(description = "ID de la categoría a eliminar", required = true)
            @PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return ResponseEntity.ok("Categoría eliminada");
    }
}
