package hr.fer.openapidemo.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Podaci o proizvodu")
public class ProductResponse {

    @Schema(description = "Jedinstveni identifikator proizvoda", example = "1")
    private Long id;

    @Schema(description = "Naziv proizvoda", example = "Bežični miš")
    private String name;

    @Schema(description = "Opis proizvoda", example = "Bežični miš s USB prijemnikom")
    private String description;

    @Schema(description = "Cijena proizvoda u eurima", example = "10.00")
    private Double price;

    @Schema(description = "Broj dostupnih komada na zalihama", example = "10")
    private Integer stock;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
}