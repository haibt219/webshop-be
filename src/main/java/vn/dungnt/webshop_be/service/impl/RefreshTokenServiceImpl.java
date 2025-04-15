package vn.dungnt.webshop_be.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.dungnt.webshop_be.entity.Account;
import vn.dungnt.webshop_be.entity.RefreshToken;
import vn.dungnt.webshop_be.exception.TokenException;
import vn.dungnt.webshop_be.repository.AccountRepository;
import vn.dungnt.webshop_be.repository.RefreshTokenRepository;
import vn.dungnt.webshop_be.service.RefreshTokenService;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
  private RefreshTokenRepository refreshTokenRepository;
  private AccountRepository accountRepository;

  @Value("${jwt.refresh-token.expiration-days}")
  private long refreshTokenExpirationDays;

  @Override
  @Transactional
  public RefreshToken createRefreshToken(String username) {
    Account account =
        accountRepository
            .findByUsername(username)
            .orElseThrow(() -> new TokenException("Tài khoản không tồn tại"));

    refreshTokenRepository.deleteByAccount(account);

    RefreshToken refreshToken =
        RefreshToken.create(
            account,
            UUID.randomUUID().toString(),
            LocalDateTime.now().plusDays(refreshTokenExpirationDays));

    return refreshTokenRepository.save(refreshToken);
  }

  @Override
  @Transactional(readOnly = true)
  public RefreshToken verifyRefreshToken(String refreshToken) {
    RefreshToken token =
        refreshTokenRepository
            .findByToken(refreshToken)
            .orElseThrow(() -> new TokenException("Refresh token không tồn tại"));

    if (token.isExpired()) {
      token.revoke();
      refreshTokenRepository.save(token);
      throw new TokenException("Refresh token đã hết hạn");
    }

    return token;
  }

  @Override
  @Transactional
  public void revokeRefreshToken(String refreshToken) {
    RefreshToken token =
        refreshTokenRepository
            .findByToken(refreshToken)
            .orElseThrow(() -> new TokenException("Refresh token không tồn tại"));

    token.revoke();
    refreshTokenRepository.save(token);
  }
}
