package vn.dungnt.webshop_be.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import vn.dungnt.webshop_be.entity.StorageType;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductCreateRequest implements Serializable {
  @Serial private static final long serialVersionUID = 1L;

  @NotNull(message = "Tên sản phẩm không được để trống")
  @Size(max = 255, message = "Tên sản phẩm không được vượt quá 255 ký tự")
  private String name;

  @Size(max = 1000, message = "Mô tả không được vượt quá 1000 ký tự")
  private String description;

  @NotNull(message = "Giá sản phẩm không được để trống")
  @Positive(message = "Giá sản phẩm phải lớn hơn 0")
  private BigDecimal price;

  @NotNull(message = "Trạng thái sản phẩm không được để trống")
  private Boolean active;

  private String brand;
  private String model;
  private String processor;

  @Positive(message = "RAM phải lớn hơn 0")
  private Integer ram;

  @Positive(message = "Dung lượng lưu trữ phải lớn hơn 0")
  private Integer storage;

  @NotNull(message = "Loại lưu trữ không được để trống")
  private StorageType storageType;

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

  public StorageType getStorageType() {
    return storageType;
  }

  public void setStorageType(StorageType storageType) {
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
}
