package vn.dungnt.webshop_be.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String token;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "account_id", nullable = false)
  private Account account;

  @Column(nullable = false)
  private LocalDateTime expiryDate;

  @Column(nullable = false)
  private boolean revoked = false;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  public boolean isExpired() {
    return LocalDateTime.now().isAfter(expiryDate) || revoked;
  }

  public void revoke() {
    this.revoked = true;
  }

  public static RefreshToken create(Account account, String token, LocalDateTime expiryDate) {
    RefreshToken refreshToken = new RefreshToken();
    refreshToken.setAccount(account);
    refreshToken.setToken(token);
    refreshToken.setCreatedAt(LocalDateTime.now());
    refreshToken.setExpiryDate(expiryDate);
    refreshToken.setRevoked(false);
    return refreshToken;
  }

  RefreshToken() {}

  public RefreshToken(
      Long id,
      String token,
      Account account,
      LocalDateTime expiryDate,
      boolean revoked,
      LocalDateTime createdAt) {
    this.id = id;
    this.token = token;
    this.account = account;
    this.expiryDate = expiryDate;
    this.revoked = revoked;
    this.createdAt = createdAt;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Account getAccount() {
    return account;
  }

  public void setAccount(Account account) {
    this.account = account;
  }

  public LocalDateTime getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(LocalDateTime expiryDate) {
    this.expiryDate = expiryDate;
  }

  public boolean isRevoked() {
    return revoked;
  }

  public void setRevoked(boolean revoked) {
    this.revoked = revoked;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }
}
