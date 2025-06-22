package com.api.java.controllers;
import com.api.java.dto.CategoryDTO;
import com.api.java.services.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Categoria", description = "Operaciones relacionadas con las categorias de los productos")
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDTO> createNewCategory(@RequestBody @Valid CategoryDTO categoryDTO){
        return ResponseEntity.ok(categoryService.createNewCategory(categoryDTO));
    }

    @GetMapping
    public List<CategoryDTO> getCategotires(){return categoryService.getCategories();}

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id){
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<CategoryDTO>> searchCategory(@RequestParam String categoryProducts){
        return ResponseEntity.ok(categoryService.searchCategory(categoryProducts));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategoryById(@PathVariable Long id){
        categoryService.deleteCategoryById(id);
        return ResponseEntity.ok("Categoria eliminada");
    }

}
