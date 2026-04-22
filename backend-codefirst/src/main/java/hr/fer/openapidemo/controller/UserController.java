package hr.fer.openapidemo.controller;

import hr.fer.openapidemo.model.ErrorResponse;
import hr.fer.openapidemo.model.UserResponse;
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
@RequestMapping("/v1/users")
@Tag(name = "Users", description = "Upravljanje korisnicima")
public class UserController {

    @GetMapping
    @Operation(
        summary = "Dohvati sve korisnike",
        description = "Vraća listu svih korisnika u sustavu",
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista korisnika uspješno dohvaćena",
                content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "401", description = "Korisnik nije autenticiran",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        }
    )
    public ResponseEntity<List<UserResponse>> getUsers(
        @Parameter(description = "Broj stranice, počinje od 0") @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Broj elemenata po stranici") @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(null);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Dohvati korisnika po ID-u",
        responses = {
            @ApiResponse(responseCode = "200", description = "Korisnik pronađen",
                content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "Korisnik nije pronađen",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        }
    )
    public ResponseEntity<UserResponse> getUserById(
        @Parameter(description = "ID korisnika", required = true) @PathVariable Long id
    ) {
        return ResponseEntity.ok(null);
    }

    @PostMapping
    @Operation(
        summary = "Kreiraj novog korisnika",
        responses = {
            @ApiResponse(responseCode = "201", description = "Korisnik uspješno kreiran",
                content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Neispravan zahtjev",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Korisnik s tim emailom već postoji",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        }
    )
    public ResponseEntity<UserResponse> createUser(
        @RequestBody UserResponse request
    ) {
        return ResponseEntity.status(201).body(null);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Ažuriraj korisnika",
        responses = {
            @ApiResponse(responseCode = "200", description = "Korisnik ažuriran",
                content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "Korisnik nije pronađen",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        }
    )
    public ResponseEntity<UserResponse> updateUser(
        @Parameter(description = "ID korisnika", required = true) @PathVariable Long id,
        @RequestBody UserResponse request
    ) {
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Obriši korisnika",
        responses = {
            @ApiResponse(responseCode = "204", description = "Korisnik obrisan"),
            @ApiResponse(responseCode = "404", description = "Korisnik nije pronađen",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        }
    )
    public ResponseEntity<Void> deleteUser(
        @Parameter(description = "ID korisnika", required = true) @PathVariable Long id
    ) {
        return ResponseEntity.noContent().build();
    }
}