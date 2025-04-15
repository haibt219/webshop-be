package vn.dungnt.webshop_be.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.dungnt.webshop_be.dto.request.RegisterRequest;
import vn.dungnt.webshop_be.entity.Account;
import vn.dungnt.webshop_be.entity.RoleEnum;
import vn.dungnt.webshop_be.exception.DuplicateResourceException;
import vn.dungnt.webshop_be.repository.AccountRepository;
import vn.dungnt.webshop_be.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {
  @Autowired private AccountRepository accountRepository;
  @Autowired private PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public void registerUser(RegisterRequest registerRequest) {
    if (accountRepository.existsByUsername(registerRequest.getUsername())) {
      throw new DuplicateResourceException("Tên đăng nhập đã được sử dụng");
    }

    if (accountRepository.existsByEmail(registerRequest.getEmail())) {
      throw new DuplicateResourceException("Email đã được đăng ký");
    }

    Account newAccount = new Account();
    newAccount.setName("admin");
    newAccount.setUsername(registerRequest.getUsername());
    newAccount.setEmail(registerRequest.getEmail());
    newAccount.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

    try {
      RoleEnum role = RoleEnum.valueOf(registerRequest.getRole().toUpperCase());
      newAccount.setRole(role);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Vai trò không hợp lệ");
    }

    newAccount.setEnabled(true);
    newAccount.setAccountNonExpired(true);
    newAccount.setAccountNonLocked(true);
    newAccount.setCredentialsNonExpired(true);
    newAccount.setStatus(Account.Status.ACTIVE);

    accountRepository.save(newAccount);
  }

  @Override
  public boolean isUsernameUnique(String username) {
    return !accountRepository.existsByUsername(username);
  }

  @Override
  public boolean isEmailUnique(String email) {
    return !accountRepository.existsByEmail(email);
  }
}
