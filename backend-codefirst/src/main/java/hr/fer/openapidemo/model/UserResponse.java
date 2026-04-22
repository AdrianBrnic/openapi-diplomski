package hr.fer.openapidemo.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Podaci o korisniku")
public class UserResponse {

    @Schema(description = "Jedinstveni identifikator korisnika", example = "1")
    private Long id;

    @Schema(description = "Ime i prezime korisnika", example = "Adrian Brnić")
    private String name;

    @Schema(description = "Email adresa korisnika", example = "adrian.brnic@fer.hr")
    private String email;

    @Schema(description = "Broj mobitela", example = "+38591000000")
    private String phone;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}