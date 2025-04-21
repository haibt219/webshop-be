package vn.dungnt.webshop_be.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import vn.dungnt.webshop_be.entity.Account;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerUpdateRequest implements Serializable {

  @Serial private static final long serialVersionUID = 1L;

  @Size(max = 100, message = "Tên không được vượt quá 100 ký tự")
  private String name;

  @Email(message = "Email không hợp lệ")
  private String email;

  @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
  private String password;

  @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
  private String confirmPassword;

  @Pattern(regexp = "^(0[1-9][0-9]{8})$", message = "Số điện thoại không hợp lệ")
  private String phone;

  @Size(max = 255, message = "Địa chỉ không được vượt quá 255 ký tự")
  private String address;

  private Account.Status status;

  public CustomerUpdateRequest() {}

  public CustomerUpdateRequest(
      String name,
      String email,
      String password,
      String confirmPassword,
      String phone,
      String address,
      Account.Status status) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.confirmPassword = confirmPassword;
    this.phone = phone;
    this.address = address;
    this.status = status;
  }

  public @Size(max = 100, message = "Tên không được vượt quá 100 ký tự") String getName() {
    return name;
  }

  public void setName(@Size(max = 100, message = "Tên không được vượt quá 100 ký tự") String name) {
    this.name = name;
  }

  public @Email(message = "Email không hợp lệ") String getEmail() {
    return email;
  }

  public void setEmail(@Email(message = "Email không hợp lệ") String email) {
    this.email = email;
  }

  public @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự") String getPassword() {
    return password;
  }

  public void setPassword(
      @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự") String password) {
    this.password = password;
  }

  public @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự") String getConfirmPassword() {
    return confirmPassword;
  }

  public void setConfirmPassword(
      @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự") String confirmPassword) {
    this.confirmPassword = confirmPassword;
  }

  public @Pattern(regexp = "^(0[1-9][0-9]{8})$", message = "Số điện thoại không hợp lệ") String
      getPhone() {
    return phone;
  }

  public void setPhone(
      @Pattern(regexp = "^(0[1-9][0-9]{8})$", message = "Số điện thoại không hợp lệ")
          String phone) {
    this.phone = phone;
  }

  public @Size(max = 255, message = "Địa chỉ không được vượt quá 255 ký tự") String getAddress() {
    return address;
  }

  public void setAddress(
      @Size(max = 255, message = "Địa chỉ không được vượt quá 255 ký tự") String address) {
    this.address = address;
  }

  public Account.Status getStatus() {
    return status;
  }

  public void setStatus(Account.Status status) {
    this.status = status;
  }
}
