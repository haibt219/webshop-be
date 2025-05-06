package vn.dungnt.webshop_be.dto;

import vn.dungnt.webshop_be.entity.OrderDetail;

import java.math.BigDecimal;

public class OrderDetailDTO {
  private Long id;
  private Long orderId;
  private Long productId;
  private String productName;
  private String productCode;
  private String productImage;
  private Integer quantity;
  private BigDecimal price;
  private BigDecimal discount;
  private String variation;
  private BigDecimal subtotal;

  // Chuyển đổi từ Entity sang DTO
  public static OrderDetailDTO fromEntity(OrderDetail entity) {
    OrderDetailDTO dto = new OrderDetailDTO();
    dto.setId(entity.getId());
    dto.setOrderId(entity.getOrder() != null ? entity.getOrder().getOrderId() : null);
    dto.setProductId(entity.getProductId());
    dto.setProductName(entity.getProductName());
    dto.setProductCode(entity.getProductCode());
    dto.setProductImage(entity.getProductImage());
    dto.setQuantity(entity.getQuantity());
    dto.setPrice(entity.getPrice());
    dto.setDiscount(entity.getDiscount());
    dto.setVariation(entity.getVariation());
    dto.setSubtotal(entity.getSubtotal());
    return dto;
  }

  // Chuyển đổi từ DTO sang Entity
  public OrderDetail toEntity() {
    OrderDetail entity = new OrderDetail();
    entity.setId(this.id);
    entity.setProductId(this.productId);
    entity.setProductName(this.productName);
    entity.setProductCode(this.productCode);
    entity.setProductImage(this.productImage);
    entity.setQuantity(this.quantity);
    entity.setPrice(this.price);
    entity.setDiscount(this.discount);
    entity.setVariation(this.variation);

    // Không thiết lập Order ở đây vì sẽ được thiết lập từ OrderDTO
    return entity;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getOrderId() {
    return orderId;
  }

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }

  public Long getProductId() {
    return productId;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public String getProductCode() {
    return productCode;
  }

  public void setProductCode(String productCode) {
    this.productCode = productCode;
  }

  public String getProductImage() {
    return productImage;
  }

  public void setProductImage(String productImage) {
    this.productImage = productImage;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public BigDecimal getDiscount() {
    return discount;
  }

  public void setDiscount(BigDecimal discount) {
    this.discount = discount;
  }

  public String getVariation() {
    return variation;
  }

  public void setVariation(String variation) {
    this.variation = variation;
  }

  public BigDecimal getSubtotal() {
    return subtotal;
  }

  public void setSubtotal(BigDecimal subtotal) {
    this.subtotal = subtotal;
  }
}
