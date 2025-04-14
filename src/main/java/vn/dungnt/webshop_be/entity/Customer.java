package vn.dungnt.webshop_be.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(length = 15)
    private String phone;

    @Column(length = 255)
    private String address;

    @Column(name = "total_orders")
    private Integer totalOrders;

    // Sử dụng BigDecimal cho totalSpent để đảm bảo độ chính xác
    @Column(name = "total_spent", precision = 10, scale = 2)
    private BigDecimal totalSpent;

    // Lưu trữ ngày tháng của lần đặt hàng cuối cùng
    @Column(name = "last_order_date")
    private LocalDateTime lastOrderDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    // Cột createdAt sẽ tự động được tạo bởi Hibernate khi tạo đối tượng
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    // Enum cho trạng thái khách hàng
    public enum Status {
        ACTIVE,
        INACTIVE,
        BLOCKED
    }

    // Constructor mặc định cho JPA
    public Customer() {
    }

    // Constructor đầy đủ cho khách hàng với các trường bắt buộc
    public Customer(String name, String email, Status status) {
        this.name = name;
        this.email = email;
        this.status = status;
        this.createdAt = LocalDateTime.now();  // Tự động gán ngày giờ hiện tại
        this.totalOrders = 0;
        this.totalSpent = BigDecimal.ZERO;  // Khởi tạo giá trị mặc định cho totalSpent
    }

    // Getter và Setter cho các thuộc tính
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Các phương thức nghiệp vụ (business methods)
    public void incrementTotalOrders() {
        if (this.totalOrders == null) {
            this.totalOrders = 1;
        } else {
            this.totalOrders++;
        }
    }

    // Phương thức để cộng thêm tiền vào totalSpent
    public void addToTotalSpent(BigDecimal amount) {
        if (this.totalSpent == null) {
            this.totalSpent = amount;
        } else {
            this.totalSpent = this.totalSpent.add(amount);
        }
        this.lastOrderDate = LocalDateTime.now();
    }
}
