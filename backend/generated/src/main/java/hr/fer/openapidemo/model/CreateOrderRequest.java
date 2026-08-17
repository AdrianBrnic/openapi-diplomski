package hr.fer.openapidemo.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import hr.fer.openapidemo.model.OrderItemRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CreateOrderRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-21T18:23:10.966472300+01:00[Europe/Zagreb]", comments = "Generator version: 7.20.0")
public class CreateOrderRequest {

  private Long userId;

  @Valid
  private List<@Valid OrderItemRequest> items = new ArrayList<>();

  public CreateOrderRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CreateOrderRequest(Long userId, List<@Valid OrderItemRequest> items) {
    this.userId = userId;
    this.items = items;
  }

  public CreateOrderRequest userId(Long userId) {
    this.userId = userId;
    return this;
  }

  /**
   * Get userId
   * minimum: 1
   * @return userId
   */
  @NotNull @Min(value = 1L) 
  @Schema(name = "userId", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("userId")
  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public CreateOrderRequest items(List<@Valid OrderItemRequest> items) {
    this.items = items;
    return this;
  }

  public CreateOrderRequest addItemsItem(OrderItemRequest itemsItem) {
    if (this.items == null) {
      this.items = new ArrayList<>();
    }
    this.items.add(itemsItem);
    return this;
  }

  /**
   * Get items
   * @return items
   */
  @NotNull @Valid @Size(min = 1) 
  @Schema(name = "items", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("items")
  public List<@Valid OrderItemRequest> getItems() {
    return items;
  }

  public void setItems(List<@Valid OrderItemRequest> items) {
    this.items = items;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateOrderRequest createOrderRequest = (CreateOrderRequest) o;
    return Objects.equals(this.userId, createOrderRequest.userId) &&
        Objects.equals(this.items, createOrderRequest.items);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, items);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateOrderRequest {\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(@Nullable Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

