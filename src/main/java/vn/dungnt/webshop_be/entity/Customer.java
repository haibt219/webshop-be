package vn.dungnt.webshop_be.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Customer extends Account {

  @Column(length = 15, nullable = false)
  private String phone;

  @Column(nullable = false)
  private String address;

  @Column(name = "total_orders")
  private Integer totalOrders = 0;

  @Column(name = "total_spent", precision = 10, scale = 2)
  private BigDecimal totalSpent = BigDecimal.ZERO;

  @Column(name = "last_order_date")
  private LocalDateTime lastOrderDate;

  public Customer() {
    super();
  }

  public Customer(
      String name,
      String username,
      String password,
      String email,
      RoleEnum role,
      String phone,
      String address) {
    super(
        null,
        name,
        username,
        password,
        email,
        role,
        Account.Status.ACTIVE,
        true,
        true,
        true,
        true,
        LocalDateTime.now());
    this.phone = phone;
    this.address = address;
    this.totalOrders = 0;
    this.totalSpent = BigDecimal.ZERO;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public Integer getTotalOrders() {
    return totalOrders;
  }

  public void setTotalOrders(Integer totalOrders) {
    this.totalOrders = totalOrders;
  }

  public BigDecimal getTotalSpent() {
    return totalSpent;
  }

  public void setTotalSpent(BigDecimal totalSpent) {
    this.totalSpent = totalSpent;
  }

  public LocalDateTime getLastOrderDate() {
    return lastOrderDate;
  }

  public void setLastOrderDate(LocalDateTime lastOrderDate) {
    this.lastOrderDate = lastOrderDate;
  }

  public void incrementTotalOrders() {
    this.totalOrders++;
  }

  public void addToTotalSpent(BigDecimal amount) {
    if (amount == null) {
      return;
    }

    if (this.totalSpent == null) {
      this.totalSpent = amount;
    } else {
      this.totalSpent = this.totalSpent.add(amount);
    }

    this.lastOrderDate = LocalDateTime.now();
    this.incrementTotalOrders();
  }

  public boolean isVip() {
    return this.totalOrders > 5 && this.totalSpent.compareTo(BigDecimal.valueOf(1000)) > 0;
  }
}
