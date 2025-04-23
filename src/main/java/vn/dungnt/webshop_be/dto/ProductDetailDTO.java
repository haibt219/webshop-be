package vn.dungnt.webshop_be.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import vn.dungnt.webshop_be.entity.StorageType;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDetailDTO implements Serializable {

  @Serial private static final long serialVersionUID = 1L;

  private Long id;
  private String name;
  private String description;
  private BigDecimal price;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Boolean active;
  private String brand;
  private String model;
  private String processor;
  private Integer ram;
  private Integer storage;
  private StorageType storageType;
  private BigDecimal screenSize;
  private String screenResolution;
  private Integer batteryCapacity;
  private String operatingSystem;
  private LocalDate releaseDate;
  private String color;
  private Integer warrantyPeriodMonths;

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
