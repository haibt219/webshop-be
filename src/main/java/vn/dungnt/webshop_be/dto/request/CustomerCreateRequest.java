package vn.dungnt.webshop_be.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import vn.dungnt.webshop_be.entity.Account;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerCreateRequest implements Serializable {
  @Serial private static final long serialVersionUID = 1L;

  @NotBlank(message = "Tên không được để trống")
  @Size(max = 100, message = "Tên không được vượt quá 100 ký tự")
  private String name;

  @NotBlank(message = "Tên đăng nhập không được để trống")
  @Size(min = 4, max = 50, message = "Tên đăng nhập phải từ 4 đến 50 ký tự")
  private String username;

  @NotBlank(message = "Mật khẩu không được để trống")
  @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
  private String password;

  @NotBlank(message = "Xác nhận mật khẩu không được để trống")
  @Size(min = 6, message = "Xác nhận mật khẩu phải có ít nhất 6 ký tự")
  private String confirmPassword;

  @NotBlank(message = "Email không được để trống")
  @Email(message = "Email không hợp lệ")
  private String email;

  @NotBlank(message = "Số điện thoại không được để trống")
  @Pattern(regexp = "^(0[1-9][0-9]{8})$", message = "Số điện thoại không hợp lệ")
  private String phone;

  @Size(max = 255, message = "Địa chỉ không được vượt quá 255 ký tự")
  private String address;

  private Account.Status status;

  public CustomerCreateRequest() {}

  public boolean isPasswordMatching() {
    return password != null && password.equals(confirmPassword);
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

  public String getConfirmPassword() {
    return confirmPassword;
  }

  public void setConfirmPassword(String confirmPassword) {
    this.confirmPassword = confirmPassword;
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
}
