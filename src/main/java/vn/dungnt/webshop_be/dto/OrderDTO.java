package vn.dungnt.webshop_be.dto;

import vn.dungnt.webshop_be.entity.Order;
import vn.dungnt.webshop_be.entity.OrderDetail;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDTO {
  private Long orderId;
  private String customerName;
  private String customerEmail;
  private String customerPhone;
  private String shippingAddress;
  private String status;
  private BigDecimal totalAmount;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private String paymentMethod;
  private String paymentStatus;
  private String shippingMethod;
  private BigDecimal shippingCost;
  private String notes;
  private Long customerId;
  private List<OrderDetailDTO> items = new ArrayList<>();

  // Chuyển đổi từ Entity sang DTO
  public static OrderDTO fromEntity(Order order) {
    OrderDTO dto = new OrderDTO();
    dto.setOrderId(order.getOrderId());
    dto.setCustomerName(order.getCustomerName());
    dto.setCustomerEmail(order.getCustomerEmail());
    dto.setCustomerPhone(order.getCustomerPhone());
    dto.setShippingAddress(order.getShippingAddress());
    dto.setStatus(order.getStatus());
    dto.setTotalAmount(order.getTotalAmount());
    dto.setCreatedAt(order.getCreatedAt());
    dto.setUpdatedAt(order.getUpdatedAt());
    dto.setPaymentMethod(order.getPaymentMethod());
    dto.setPaymentStatus(order.getPaymentStatus());
    dto.setShippingMethod(order.getShippingMethod());
    dto.setShippingCost(order.getShippingCost());
    dto.setNotes(order.getNotes());
    dto.setCustomerId(order.getCustomerId());

    // Chuyển đổi danh sách chi tiết đơn hàng
    if (order.getItems() != null) {
      dto.setItems(
          order.getItems().stream().map(OrderDetailDTO::fromEntity).collect(Collectors.toList()));
    }

    return dto;
  }

  // Chuyển đổi từ DTO sang Entity
  public Order toEntity() {
    Order order = new Order();
    order.setOrderId(this.orderId);
    order.setCustomerName(this.customerName);
    order.setCustomerEmail(this.customerEmail);
    order.setCustomerPhone(this.customerPhone);
    order.setShippingAddress(this.shippingAddress);
    order.setStatus(this.status);
    order.setTotalAmount(this.totalAmount);
    order.setCreatedAt(this.createdAt);
    order.setUpdatedAt(this.updatedAt);
    order.setPaymentMethod(this.paymentMethod);
    order.setPaymentStatus(this.paymentStatus);
    order.setShippingMethod(this.shippingMethod);
    order.setShippingCost(this.shippingCost);
    order.setNotes(this.notes);
    order.setCustomerId(this.customerId);

    // Chuyển đổi danh sách chi tiết đơn hàng
    if (this.items != null) {
      List<OrderDetail> orderDetails =
          this.items.stream().map(OrderDetailDTO::toEntity).collect(Collectors.toList());

      // Thiết lập quan hệ hai chiều
      orderDetails.forEach(item -> item.setOrder(order));
      order.setItems(orderDetails);
    }

    return order;
  }

  public Long getOrderId() {
    return orderId;
  }

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public String getCustomerEmail() {
    return customerEmail;
  }

  public void setCustomerEmail(String customerEmail) {
    this.customerEmail = customerEmail;
  }

  public String getCustomerPhone() {
    return customerPhone;
  }

  public void setCustomerPhone(String customerPhone) {
    this.customerPhone = customerPhone;
  }

  public String getShippingAddress() {
    return shippingAddress;
  }

  public void setShippingAddress(String shippingAddress) {
    this.shippingAddress = shippingAddress;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(BigDecimal totalAmount) {
    this.totalAmount = totalAmount;
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

  public String getPaymentMethod() {
    return paymentMethod;
  }

  public void setPaymentMethod(String paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  public String getPaymentStatus() {
    return paymentStatus;
  }

  public void setPaymentStatus(String paymentStatus) {
    this.paymentStatus = paymentStatus;
  }

  public String getShippingMethod() {
    return shippingMethod;
  }

  public void setShippingMethod(String shippingMethod) {
    this.shippingMethod = shippingMethod;
  }

  public BigDecimal getShippingCost() {
    return shippingCost;
  }

  public void setShippingCost(BigDecimal shippingCost) {
    this.shippingCost = shippingCost;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public Long getCustomerId() {
    return customerId;
  }

  public void setCustomerId(Long customerId) {
    this.customerId = customerId;
  }

  public List<OrderDetailDTO> getItems() {
    return items;
  }

  public void setItems(List<OrderDetailDTO> items) {
    this.items = items;
  }
}
