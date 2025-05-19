package vn.dungnt.webshop_be.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductCreateRequest implements Serializable {
  @Serial private static final long serialVersionUID = 1L;

  // Product
  @NotNull(message = "Tên sản phẩm không được để trống")
  @Size(max = 255, message = "Tên sản phẩm không được vượt quá 255 ký tự")
  private String name;

  @Size(max = 1000, message = "Độ dài mô tả không được vượt quá 1000 ký tự")
  private String description;

  @Size(max = 1000, message = "Độ dài hình ảnh không được vượt quá 1000 ký tự")
  private String image;

  @NotNull(message = "Giá sản phẩm không được để trống")
  @Positive(message = "Giá sản phẩm phải lớn hơn 0")
  private BigDecimal price;

  @Positive(message = "Giá sản phẩm phải lớn hơn 0")
  private BigDecimal discountPrice;

  @NotNull(message = "Trạng thái sản phẩm không được để trống")
  private Boolean active;

  // Category
  private Long categoryId;

  // ProductDetail
  private String brand;
  private String model;
  private String processor;

  @Positive(message = "RAM phải lớn hơn 0")
  private Integer ram;

  @Positive(message = "Dung lượng lưu trữ phải lớn hơn 0")
  private Integer storage;

  @NotNull(message = "Loại lưu trữ không được để trống")
  private String storageType;

  @Positive(message = "Kích thước màn hình phải lớn hơn 0")
  private BigDecimal screenSize;

  private String screenResolution;

  @Positive(message = "Dung lượng pin phải lớn hơn 0")
  private Integer batteryCapacity;

  private String operatingSystem;
  private LocalDate releaseDate;
  private String color;

  @Positive(message = "Thời gian bảo hành phải lớn hơn 0")
  private Integer warrantyPeriodMonths;

  private LocalDate promotionStartDate;

  private LocalDate promotionEndDate;

  private String promotionDescription;

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

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public String getProcessor() {
    return processor;
  }

  public void setProcessor(String processor) {
    this.processor = processor;
  }

  public Integer getRam() {
    return ram;
  }

  public void setRam(Integer ram) {
    this.ram = ram;
  }

  public Integer getStorage() {
    return storage;
  }

  public void setStorage(Integer storage) {
    this.storage = storage;
  }

  public String getStorageType() {
    return storageType;
  }

  public void setStorageType(String storageType) {
    this.storageType = storageType;
  }

  public BigDecimal getScreenSize() {
    return screenSize;
  }

  public void setScreenSize(BigDecimal screenSize) {
    this.screenSize = screenSize;
  }

  public String getScreenResolution() {
    return screenResolution;
  }

  public void setScreenResolution(String screenResolution) {
    this.screenResolution = screenResolution;
  }

  public Integer getBatteryCapacity() {
    return batteryCapacity;
  }

  public void setBatteryCapacity(Integer batteryCapacity) {
    this.batteryCapacity = batteryCapacity;
  }

  public String getOperatingSystem() {
    return operatingSystem;
  }

  public void setOperatingSystem(String operatingSystem) {
    this.operatingSystem = operatingSystem;
  }

  public LocalDate getReleaseDate() {
    return releaseDate;
  }

  public void setReleaseDate(LocalDate releaseDate) {
    this.releaseDate = releaseDate;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public Integer getWarrantyPeriodMonths() {
    return warrantyPeriodMonths;
  }

  public void setWarrantyPeriodMonths(Integer warrantyPeriodMonths) {
    this.warrantyPeriodMonths = warrantyPeriodMonths;
  }

  public Long getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Long categoryId) {
    this.categoryId = categoryId;
  }

  public BigDecimal getDiscountPrice() {
    return discountPrice;
  }

  public void setDiscountPrice(BigDecimal discountPrice) {
    this.discountPrice = discountPrice;
  }

  public LocalDate getPromotionStartDate() {
    return promotionStartDate;
  }

  public void setPromotionStartDate(LocalDate promotionStartDate) {
    this.promotionStartDate = promotionStartDate;
  }

  public LocalDate getPromotionEndDate() {
    return promotionEndDate;
  }

  public void setPromotionEndDate(LocalDate promotionEndDate) {
    this.promotionEndDate = promotionEndDate;
  }

  public String getPromotionDescription() {
    return promotionDescription;
  }

  public void setPromotionDescription(String promotionDescription) {
    this.promotionDescription = promotionDescription;
  }
}
