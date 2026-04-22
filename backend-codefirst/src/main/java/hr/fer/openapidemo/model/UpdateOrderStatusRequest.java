package hr.fer.openapidemo.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Zahtjev za ažuriranje statusa narudžbe")
public class UpdateOrderStatusRequest {

    @Schema(
        description = "Novi status narudžbe",
        example = "PAID",
        allowableValues = {"CREATED", "PAID", "SHIPPED", "DELIVERED", "CANCELLED"},
        required = true
    )
    private String status;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}