package vn.dungnt.webshop_be.dto.response;

import vn.dungnt.webshop_be.dto.CustomerDTO;

public class LoginResponse {
  private String accessToken;
  private String refreshToken;
  private Long expiresAt;
  private CustomerDTO user;

  LoginResponse() {}

  public LoginResponse(String accessToken, String refreshToken, Long expiresAt, CustomerDTO user) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.expiresAt = expiresAt;
    this.user = user;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public Long getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(Long expiresAt) {
    this.expiresAt = expiresAt;
  }

  public CustomerDTO getUser() {
    return user;
  }

  public void setUser(CustomerDTO user) {
    this.user = user;
  }
}
