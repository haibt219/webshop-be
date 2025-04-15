package vn.dungnt.webshop_be.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import vn.dungnt.webshop_be.entity.Account;
import vn.dungnt.webshop_be.entity.RoleEnum;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerDTO implements Serializable {

  @Serial private static final long serialVersionUID = 1L;

  private Long id;

  @NotBlank(message = "Tên không được để trống")
  @Size(max = 100, message = "Tên không được vượt quá 100 ký tự")
  private String name;

  @NotBlank(message = "Tên đăng nhập không được để trống")
  @Size(min = 4, max = 50, message = "Tên đăng nhập phải từ 4 đến 50 ký tự")
  private String username;

  @NotBlank(message = "Mật khẩu không được để trống")
  @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
  private String password;

  @NotBlank(message = "Email không được để trống")
  @Email(message = "Email không hợp lệ")
  private String email;

  @NotBlank(message = "Số điện thoại không được để trống")
  @Pattern(regexp = "^(0[1-9][0-9]{8})$", message = "Số điện thoại không hợp lệ")
  private String phone;

  @NotBlank(message = "Địa chỉ không được để trống")
  @Size(max = 255, message = "Địa chỉ không được vượt quá 255 ký tự")
  private String address;

  private Account.Status status;
  private RoleEnum role;

  private Integer totalOrders;
  private BigDecimal totalSpent;
  private LocalDateTime lastOrderDate;

  private String confirmPassword;

  public boolean isPasswordMatching() {
    return password != null && password.equals(confirmPassword);
  }

  public boolean isVip() {
    return totalOrders != null
        && totalSpent != null
        && totalOrders > 5
        && totalSpent.compareTo(BigDecimal.valueOf(1000)) > 0;
  }

  public CustomerDTO() {}

  public CustomerDTO(
      Long id,
      String name,
      String username,
      String password,
      String email,
      String phone,
      String address,
      Account.Status status,
      RoleEnum role,
      Integer totalOrders,
      BigDecimal totalSpent,
      LocalDateTime lastOrderDate,
      String confirmPassword) {
    this.id = id;
    this.name = name;
    this.username = username;
    this.password = password;
    this.email = email;
    this.phone = phone;
    this.address = address;
    this.status = status;
    this.role = role;
    this.totalOrders = totalOrders;
    this.totalSpent = totalSpent;
    this.lastOrderDate = lastOrderDate;
    this.confirmPassword = confirmPassword;
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

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
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

  public Account.Status getStatus() {
    return status;
  }

  public void setStatus(Account.Status status) {
    this.status = status;
  }

  public RoleEnum getRole() {
    return role;
  }

  public void setRole(RoleEnum role) {
    this.role = role;
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

  public String getConfirmPassword() {
    return confirmPassword;
  }

  public void setConfirmPassword(String confirmPassword) {
    this.confirmPassword = confirmPassword;
  }
}
