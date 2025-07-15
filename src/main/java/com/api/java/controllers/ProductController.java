package com.api.java.controllers;
import com.api.java.dto.ProductDTO;
import com.api.java.services.ProductService;
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
@RequestMapping("/product")
@Tag(name = "Producto", description = "Operaciones relacionadas con productos")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "Crear producto", description = "Recibe un ProductDTO y crea un nuevo producto en la base de datos.")
    @ApiResponse(responseCode = "200", description = "Producto creado exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos en el cuerpo de la solicitud")
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO con los datos del producto", required = true) @Valid @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.createProduct(productDTO));
    }

    @Operation(summary = "Listar todos los productos", description = "Devuelve una lista con todos los productos registrados.")
    @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente")
    @GetMapping
    public List<ProductDTO> getProducts() {  return productService.getProducts();  }

    @Operation(summary = "Obtener producto por ID", description = "Devuelve un producto específico según su ID.")
    @ApiResponse(responseCode = "200", description = "Producto encontrado")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@Parameter(description = "ID del producto a buscar", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @Operation(summary = "Buscar productos por categoría", description = "Devuelve los productos asociados a un ID de categoría.")
    @ApiResponse(responseCode = "200", description = "Productos encontrados para la categoría")
    @ApiResponse(responseCode = "400", description = "ID de categoría inválido")
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductDTO>> getProdCategoryById(@Parameter(description = "ID de la categoría", required = true) @PathVariable @Valid Long categoryId) {
        return ResponseEntity.ok(productService.getProdCategoryById(categoryId));
    }

    @Operation(summary = "Buscar productos por nombre", description = "Busca productos cuyo nombre coincida (parcial o totalmente).")
    @ApiResponse(responseCode = "200", description = "Búsqueda completada exitosamente")
    @ApiResponse(responseCode = "400", description = "Nombre de producto no válido")
    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchProduct(@Parameter(description = "Nombre (o parte del nombre) del producto", required = true) @RequestParam @Valid String nameProduct) {
        return ResponseEntity.ok(productService.searchProduct(nameProduct));
    }

    @Operation(summary = "Actualizar producto", description = "Modifica un producto existente a partir de su ID.")
    @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProdById(@Parameter(description = "ID del producto a actualizar", required = true) @PathVariable Long id, @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO con los nuevos datos del producto", required = true) @Valid @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.updateProdById(id, productDTO));
    }

    @Operation(summary = "Reactivar un producto", description = "Vuelve a activar un producto previamente desactivado")
    @ApiResponse(responseCode = "200", description = "Producto reactivado correctamente")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    @PatchMapping("/{id}/activar")
    public ResponseEntity<String> activateProductById(@Parameter(description = "ID del producto que se desea reactivar", example = "101", required = true) @PathVariable Long id) {
        productService.activateProdById(id);
        return ResponseEntity.ok("Producto activado");
    }

    @Operation(summary = "Desactivar producto", description = "Desactiva un producto de la base de datos según su ID.")
    @ApiResponse(responseCode = "200", description = "Producto desactivado exitosamente")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    @DeleteMapping("/{id}/desactivar")
    public ResponseEntity<String> desactivateProdById(@Parameter(description = "ID del producto a desactivar", required = true) @PathVariable Long id) {
        productService.desactivateProdById(id);
        return ResponseEntity.ok("Producto desactivado");
    }
}