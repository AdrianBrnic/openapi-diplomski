package hr.fer.openapidemo.controller;

import hr.fer.openapidemo.model.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import hr.fer.openapidemo.model.CreateOrderRequest;
import hr.fer.openapidemo.model.UpdateOrderStatusRequest;

@RestController
@RequestMapping("/v1/orders")
@Tag(name = "Orders", description = "Upravljanje narudžbama")
public class OrderController {

    @GetMapping
    @Operation(
        summary = "Dohvati sve narudžbe",
        description = "Vraća listu narudžbi s opcionalnim filtriranjem po statusu",
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista narudžbi uspješno dohvaćena"),
            @ApiResponse(responseCode = "401", description = "Korisnik nije autenticiran",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        }
    )
    public ResponseEntity<?> getOrders(
        @Parameter(description = "Broj stranice, počinje od 0") @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Broj elemenata po stranici") @RequestParam(defaultValue = "20") int size,
        @Parameter(description = "Filter po statusu", schema = @Schema(allowableValues = {"CREATED", "PAID", "SHIPPED", "DELIVERED", "CANCELLED"}))
        @RequestParam(required = false) String status
    ) {
        return ResponseEntity.ok(null);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Dohvati narudžbu po ID-u",
        responses = {
            @ApiResponse(responseCode = "200", description = "Narudžba pronađena"),
            @ApiResponse(responseCode = "404", description = "Narudžba nije pronađena",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        }
    )
    public ResponseEntity<?> getOrderById(
        @Parameter(description = "ID narudžbe", required = true) @PathVariable Long id
    ) {
        return ResponseEntity.ok(null);
    }

    @PostMapping
    @Operation(
        summary = "Kreiraj novu narudžbu",
        description = "Kreira novu narudžbu i automatski smanjuje zalihe proizvoda",
        responses = {
            @ApiResponse(responseCode = "201", description = "Narudžba kreirana"),
            @ApiResponse(responseCode = "400", description = "Neispravan zahtjev",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "Nedovoljno zaliha",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        }
    )
	@SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest request) {
        return ResponseEntity.status(201).body(null);
    }

    @PatchMapping("/{id}/status")
    @Operation(
        summary = "Ažuriraj status narudžbe",
        description = "Mijenja status narudžbe — dozvoljen redoslijed: CREATED -> PAID -> SHIPPED -> DELIVERED",
        responses = {
            @ApiResponse(responseCode = "200", description = "Status ažuriran"),
            @ApiResponse(responseCode = "400", description = "Neispravan status",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Narudžba nije pronađena",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        }
    )
	@SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> updateOrderStatus(
    @Parameter(description = "ID narudžbe", required = true) @PathVariable Long id,
    @RequestBody UpdateOrderStatusRequest request
    ) {
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Otkaži narudžbu",
        responses = {
            @ApiResponse(responseCode = "204", description = "Narudžba otkazana"),
            @ApiResponse(responseCode = "404", description = "Narudžba nije pronađena",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        }
    )
	@SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> deleteOrder(
        @Parameter(description = "ID narudžbe", required = true) @PathVariable Long id
    ) {
        return ResponseEntity.noContent().build();
    }
}