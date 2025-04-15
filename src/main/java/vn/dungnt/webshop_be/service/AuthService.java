package vn.dungnt.webshop_be.service;

import vn.dungnt.webshop_be.dto.request.RegisterRequest;

public interface AuthService {
  void registerUser(RegisterRequest registerRequest);

  boolean isUsernameUnique(String username);

  boolean isEmailUnique(String email);
}
