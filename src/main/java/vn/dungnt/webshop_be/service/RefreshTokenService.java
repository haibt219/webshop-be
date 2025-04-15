package vn.dungnt.webshop_be.service;

import vn.dungnt.webshop_be.entity.RefreshToken;

public interface RefreshTokenService {
  RefreshToken createRefreshToken(String username);

  RefreshToken verifyRefreshToken(String refreshToken);

  void revokeRefreshToken(String refreshToken);
}
