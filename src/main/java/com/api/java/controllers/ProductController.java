package com.api.java.controllers;
import com.api.java.dto.ProductDTO;
import com.api.java.services.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Tag(name = "Producto", description = "Operaciones relacionadas con los productos")
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO){
        return ResponseEntity.ok(productService.createProduct(productDTO));
    }

    @GetMapping
    public List<ProductDTO> getProducts(){
        return productService.getProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id){
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductDTO>> getProdCategoryById(@PathVariable @Valid Long categoryId){
        return ResponseEntity.ok(productService.getProdCategoryById(categoryId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchProduct(@RequestParam @Valid  String nameProduct){
        return ResponseEntity.ok(productService.searchProduct(nameProduct));
    }
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProdById(@PathVariable Long id,@Valid @RequestBody ProductDTO productDTO){
        return ResponseEntity.ok(productService.updateProdById(id, productDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProdById(@PathVariable Long id){
        productService.deleteProdById(id);
        return ResponseEntity.ok("Producto Eliminado");
    }
    }