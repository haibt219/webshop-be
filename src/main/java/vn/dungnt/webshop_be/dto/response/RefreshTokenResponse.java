package vn.dungnt.webshop_be.dto.response;

import java.io.Serializable;

public class RefreshTokenResponse implements Serializable {
  private String accessToken;
  private Long expiresAt;

  public RefreshTokenResponse() {}

  public RefreshTokenResponse(String accessToken, Long expiresAt) {
    this.accessToken = accessToken;
    this.expiresAt = expiresAt;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public Long getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(Long expiresAt) {
    this.expiresAt = expiresAt;
  }
}
