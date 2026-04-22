package hr.fer.openapidemo.controller;

import hr.fer.openapidemo.model.ErrorResponse;
import hr.fer.openapidemo.model.ProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/products")
@Tag(name = "Products", description = "Upravljanje proizvodima")
public class ProductController {

    @GetMapping
    @Operation(
        summary = "Dohvati sve proizvode",
        description = "Vraća listu proizvoda s opcionalnim filtriranjem po cijeni",
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista proizvoda uspješno dohvaćena",
                content = @Content(schema = @Schema(implementation = ProductResponse.class)))
        }
    )
    public ResponseEntity<List<ProductResponse>> getProducts(
        @Parameter(description = "Broj stranice, počinje od 0") @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Broj elemenata po stranici") @RequestParam(defaultValue = "20") int size,
        @Parameter(description = "Minimalna cijena filtra") @RequestParam(required = false) Double minPrice,
        @Parameter(description = "Maksimalna cijena filtra") @RequestParam(required = false) Double maxPrice
    ) {
        return ResponseEntity.ok(null);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Dohvati proizvod po ID-u",
        responses = {
            @ApiResponse(responseCode = "200", description = "Proizvod pronađen",
                content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "404", description = "Proizvod nije pronađen",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        }
    )
    public ResponseEntity<ProductResponse> getProductById(
        @Parameter(description = "ID proizvoda", required = true) @PathVariable Long id
    ) {
        return ResponseEntity.ok(null);
    }

    @PostMapping
    @Operation(
        summary = "Dodaj novi proizvod",
        responses = {
            @ApiResponse(responseCode = "201", description = "Proizvod kreiran",
                content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "400", description = "Neispravan zahtjev",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Korisnik nije autenticiran",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        }
    )
    public ResponseEntity<ProductResponse> createProduct(
        @RequestBody ProductResponse request
    ) {
        return ResponseEntity.status(201).body(null);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Ažuriraj proizvod",
        responses = {
            @ApiResponse(responseCode = "200", description = "Proizvod ažuriran",
                content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "404", description = "Proizvod nije pronađen",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        }
    )
    public ResponseEntity<ProductResponse> updateProduct(
        @Parameter(description = "ID proizvoda", required = true) @PathVariable Long id,
        @RequestBody ProductResponse request
    ) {
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Obriši proizvod",
        responses = {
            @ApiResponse(responseCode = "204", description = "Proizvod obrisan"),
            @ApiResponse(responseCode = "404", description = "Proizvod nije pronađen",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        }
    )
    public ResponseEntity<Void> deleteProduct(
        @Parameter(description = "ID proizvoda", required = true) @PathVariable Long id
    ) {
        return ResponseEntity.noContent().build();
    }
}