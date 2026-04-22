package hr.fer.openapidemo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Zahtjev za kreiranje nove narudžbe")
public class CreateOrderRequest {

    @Schema(description = "ID korisnika koji naručuje", example = "1", required = true)
    private Long userId;

    @Schema(description = "Lista stavki narudžbe", required = true)
    private List<OrderItemRequest> items;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public List<OrderItemRequest> getItems() { return items; }
    public void setItems(List<OrderItemRequest> items) { this.items = items; }
}