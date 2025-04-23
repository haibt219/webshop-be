package vn.dungnt.webshop_be.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDTO implements Serializable {
  @Serial private static final long serialVersionUID = 1L;

  private Long id;

  private String name;

  private String description;

  private BigDecimal price;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  private Boolean active = true;

  public ProductDTO() {}

  public ProductDTO(
      Long id,
      String name,
      String description,
      BigDecimal price,
      LocalDateTime createdAt,
      LocalDateTime updatedAt,
      Boolean active) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.price = price;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.active = active;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }
}
