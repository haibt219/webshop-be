package vn.dungnt.webshop_be.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 255)
  private String name;

  @Column(length = 1000)
  private String description;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal price;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column(nullable = false)
  private Boolean active = true;

  @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
  private ProductDetail productDetail;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id")
  private Category category;

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ProductDiscount> discounts = new ArrayList<>();

  public Product() {}

  public Product(String name, String description, BigDecimal price) {
    this.name = name;
    this.description = description;
    this.price = price;
    this.createdAt = LocalDateTime.now();
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

  public ProductDetail getProductDetail() {
    return productDetail;
  }

  public void setProductDetail(ProductDetail productDetail) {
    this.productDetail = productDetail;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public List<ProductDiscount> getDiscounts() {
    return discounts;
  }

  public void setDiscounts(List<ProductDiscount> discounts) {
    this.discounts = discounts;
  }

  public void addDiscount(ProductDiscount discount) {
    discounts.add(discount);
    discount.setProduct(this);
  }

  public void removeDiscount(ProductDiscount discount) {
    discounts.remove(discount);
    discount.setProduct(null);
  }

  public ProductDiscount getActiveDiscount() {
    if (discounts == null || discounts.isEmpty()) {
      return null;
    }

    LocalDate now = LocalDate.now();
    return discounts.stream()
        .filter(
            d ->
                d.getActive()
                    && d.getStartDate().compareTo(now) <= 0
                    && d.getEndDate().compareTo(now) >= 0)
        .findFirst()
        .orElse(null);
  }

  public BigDecimal getCurrentPrice() {
    ProductDiscount activeDiscount = getActiveDiscount();
    if (activeDiscount != null && activeDiscount.getDiscountPrice() != null) {
      return activeDiscount.getDiscountPrice();
    }
    return price;
  }

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }
}
