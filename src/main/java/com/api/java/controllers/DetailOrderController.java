package com.api.java.controllers;
import com.api.java.dto.DetailOrderDTO;
import com.api.java.services.DetailOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/detail-order")
@RequiredArgsConstructor
@Tag(name = "Detalle de Orden", description = "Operaciones relacionadas con los ítems individuales de una orden de compra")
public class DetailOrderController {
    private final DetailOrderService detailOrderService;

    @Operation(summary = "Listar todos los detalles de orden", description = "Devuelve una lista completa de los productos solicitados en todas las órdenes")
    @ApiResponse(responseCode = "200", description = "Lista de detalles obtenida exitosamente")
    @GetMapping
    public List<DetailOrderDTO> getDetailOrders() { return detailOrderService.getDetailOrders();  }

    @Operation(summary = "Crear nuevo detalle de orden", description = "Registra un nuevo ítem dentro de una orden de compra")
    @ApiResponse(responseCode = "200", description = "Detalle de orden creado con éxito")
    @ApiResponse(responseCode = "400", description = "Error de validación en los campos", content = @Content)
    @PostMapping
    public ResponseEntity<DetailOrderDTO> newDetailOrder(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO con los datos del producto, cantidad y estado", required = true, content = @Content(schema = @Schema(implementation = DetailOrderDTO.class))) @Valid @RequestBody DetailOrderDTO detailOrderDTO) {
        return ResponseEntity.ok(detailOrderService.newDetailOrder(detailOrderDTO));
    }

    @Operation(summary = "Obtener detalle por ID", description = "Devuelve un ítem específico de una orden mediante su ID")
    @ApiResponse(responseCode = "200", description = "Detalle encontrado")
    @ApiResponse(responseCode = "404", description = "Detalle no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<DetailOrderDTO> getDetailById(@Parameter(description = "ID del detalle de orden", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(detailOrderService.getDetailById(id));
    }

    @Operation(summary = "Buscar detalles por usuario", description = "Devuelve todos los ítems de órdenes realizadas por un usuario específico")
    @ApiResponse(responseCode = "200", description = "Detalles obtenidos para el usuario")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado o sin órdenes")
    @GetMapping("/user/{userOrderId}")
    public ResponseEntity<List<DetailOrderDTO>> getDetailUserById(@Parameter(description = "ID del usuario", required = true) @PathVariable Long userOrderId) {
        return ResponseEntity.ok(detailOrderService.getDetailUserById(userOrderId));
    }

    @Operation(summary = "Actualizar detalle de orden", description = "Modifica los datos de un ítem de orden existente según su ID")
    @ApiResponse(responseCode = "200", description = "Detalle actualizado correctamente")
    @ApiResponse(responseCode = "404", description = "Detalle no encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<DetailOrderDTO> updateDetailById(@Parameter(description = "ID del detalle a actualizar", required = true) @PathVariable Long id, @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Nuevos datos del detalle de orden", required = true) @Valid @RequestBody DetailOrderDTO detailOrderDTO) {
        return ResponseEntity.ok(detailOrderService.updateDetailById(id, detailOrderDTO));
    }

    @Operation(summary = "Eliminar detalle de orden", description = "Borra permanentemente un ítem de orden usando su ID")
    @ApiResponse(responseCode = "200", description = "Detalle eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Detalle no encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@Parameter(description = "ID del detalle a eliminar", required = true) @PathVariable Long id) {
        detailOrderService.deleteById(id);
        return ResponseEntity.ok("Detalle de orden eliminado");
    }
}
