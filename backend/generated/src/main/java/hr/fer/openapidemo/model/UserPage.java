package hr.fer.openapidemo.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import hr.fer.openapidemo.model.PageMetadata;
import hr.fer.openapidemo.model.UserResponse;
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
 * UserPage
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-21T18:23:10.966472300+01:00[Europe/Zagreb]", comments = "Generator version: 7.20.0")
public class UserPage {

  @Valid
  private List<@Valid UserResponse> content = new ArrayList<>();

  private @Nullable PageMetadata page;

  public UserPage content(List<@Valid UserResponse> content) {
    this.content = content;
    return this;
  }

  public UserPage addContentItem(UserResponse contentItem) {
    if (this.content == null) {
      this.content = new ArrayList<>();
    }
    this.content.add(contentItem);
    return this;
  }

  /**
   * Get content
   * @return content
   */
  @Valid 
  @Schema(name = "content", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("content")
  public List<@Valid UserResponse> getContent() {
    return content;
  }

  public void setContent(List<@Valid UserResponse> content) {
    this.content = content;
  }

  public UserPage page(@Nullable PageMetadata page) {
    this.page = page;
    return this;
  }

  /**
   * Get page
   * @return page
   */
  @Valid 
  @Schema(name = "page", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("page")
  public @Nullable PageMetadata getPage() {
    return page;
  }

  public void setPage(@Nullable PageMetadata page) {
    this.page = page;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserPage userPage = (UserPage) o;
    return Objects.equals(this.content, userPage.content) &&
        Objects.equals(this.page, userPage.page);
  }

  @Override
  public int hashCode() {
    return Objects.hash(content, page);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserPage {\n");
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
    sb.append("    page: ").append(toIndentedString(page)).append("\n");
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

