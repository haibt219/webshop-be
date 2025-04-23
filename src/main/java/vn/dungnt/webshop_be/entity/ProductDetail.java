package vn.dungnt.webshop_be.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "product_details")
public class ProductDetail {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @Column(name = "brand", length = 100)
  private String brand;

  @Column(name = "model", length = 255)
  private String model;

  @Column(name = "processor", length = 255)
  private String processor;

  @Column(name = "ram")
  private Integer ram;

  @Column(name = "storage")
  private Integer storage;

  @Enumerated(EnumType.STRING)
  @Column(name = "storage_type")
  private StorageType storageType;

  @Column(name = "screen_size", precision = 4, scale = 1)
  private BigDecimal screenSize;

  @Column(name = "screen_resolution", length = 50)
  private String screenResolution;

  @Column(name = "battery_capacity")
  private Integer batteryCapacity;

  @Column(name = "operating_system", length = 100)
  private String operatingSystem;

  @Column(name = "release_date")
  private LocalDate releaseDate;

  @Column(name = "color", length = 50)
  private String color;

  @Column(name = "warranty_period")
  private Integer warrantyPeriodMonths;

  public ProductDetail() {}

  public ProductDetail(Product product) {
    this.product = product;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
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
