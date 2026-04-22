package hr.fer.openapidemo.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Stavka narudžbe")
public class OrderItemRequest {

    @Schema(description = "ID proizvoda", example = "1", required = true)
    private Long productId;

    @Schema(description = "Količina proizvoda", example = "2", minimum = "1", required = true)
    private Integer quantity;

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}