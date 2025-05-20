package vn.dungnt.webshop_be.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import vn.dungnt.webshop_be.config.jwt.JwtTokenProvider;
import vn.dungnt.webshop_be.dto.CustomerDTO;
import vn.dungnt.webshop_be.dto.request.LoginRequest;
import vn.dungnt.webshop_be.dto.request.RegisterRequest;
import vn.dungnt.webshop_be.dto.response.ErrorResponse;
import vn.dungnt.webshop_be.dto.response.LoginResponse;
import vn.dungnt.webshop_be.dto.response.MessageResponse;
import vn.dungnt.webshop_be.dto.response.RefreshTokenResponse;
import vn.dungnt.webshop_be.entity.Customer;
import vn.dungnt.webshop_be.entity.RoleEnum;
import vn.dungnt.webshop_be.exception.NotFoundException;
import vn.dungnt.webshop_be.repository.AccountRepository;
import vn.dungnt.webshop_be.service.AuthService;
import vn.dungnt.webshop_be.service.CustomerService;
import vn.dungnt.webshop_be.service.RefreshTokenService;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired private AuthenticationManager authenticationManager;
  @Autowired private JwtTokenProvider tokenProvider;
  @Autowired private AuthService authService;
  @Autowired private RefreshTokenService refreshTokenService;
  @Autowired private CustomerService customerService;
  @Autowired private AccountRepository accountRepository;

  @PostMapping("/login-admin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    try {
      Authentication authentication =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                  loginRequest.getUsername(), loginRequest.getPassword()));

      boolean isAdmin =
          authentication.getAuthorities().stream()
              .anyMatch(
                  grantedAuthority ->
                      RoleEnum.ADMIN.name().equals(grantedAuthority.getAuthority()));

      if (!isAdmin) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(
                new ErrorResponse(
                    HttpStatus.FORBIDDEN.value(),
                    "Bạn không có quyền truy cập hệ thống",
                    null,
                    LocalDateTime.now()));
      }

      SecurityContextHolder.getContext().setAuthentication(authentication);

      String accessToken = tokenProvider.generateToken(authentication);
      String refreshToken = tokenProvider.generateRefreshToken(authentication);

      return ResponseEntity.ok(
          new LoginResponse(
              accessToken,
              refreshToken,
              System.currentTimeMillis() + tokenProvider.getAccessTokenExpiration(),
              null));

    } catch (BadCredentialsException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(
              new ErrorResponse(
                  HttpStatus.UNAUTHORIZED.value(),
                  "Tên đăng nhập hoặc mật khẩu không đúng",
                  null,
                  LocalDateTime.now()));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(
              new ErrorResponse(
                  HttpStatus.INTERNAL_SERVER_ERROR.value(),
                  "Đã xảy ra lỗi: " + e.getMessage(),
                  null,
                  LocalDateTime.now()));
    }
  }

  @PostMapping("/login")
  public ResponseEntity<?> authenticateCustomer(@Valid @RequestBody LoginRequest loginRequest) {
    try {
      Authentication authentication =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                  loginRequest.getUsername(), loginRequest.getPassword()));

      SecurityContextHolder.getContext().setAuthentication(authentication);

      String accessToken = tokenProvider.generateToken(authentication);
      String refreshToken = tokenProvider.generateRefreshToken(authentication);
      Long expiresAt = System.currentTimeMillis() + tokenProvider.getAccessTokenExpiration();

      // Lấy thông tin người dùng
      UserDetails userDetails = (UserDetails) authentication.getPrincipal();
      String username = userDetails.getUsername();

      // Tìm Customer trong database
      Customer customer =
          (Customer)
              accountRepository
                  .findByUsername(username)
                  .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng"));

      // Chuyển đổi Customer thành CustomerDTO
      CustomerDTO customerDTO = customerService.convertToDTO(customer);

      // Trả về token và thông tin người dùng
      LoginResponse response = new LoginResponse(accessToken, refreshToken, expiresAt, customerDTO);

      return ResponseEntity.ok(response);

    } catch (BadCredentialsException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(
              new ErrorResponse(
                  HttpStatus.UNAUTHORIZED.value(),
                  "Tên đăng nhập hoặc mật khẩu không đúng",
                  null,
                  LocalDateTime.now()));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(
              new ErrorResponse(
                  HttpStatus.INTERNAL_SERVER_ERROR.value(),
                  "Đã xảy ra lỗi: " + e.getMessage(),
                  null,
                  LocalDateTime.now()));
    }
  }

  @PostMapping("/register")
  public ResponseEntity<MessageResponse> registerUser(
      @Valid @RequestBody RegisterRequest registerRequest) {
    authService.registerUser(registerRequest);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new MessageResponse("Đăng ký thành công!"));
  }

  @PostMapping("/refresh-token")
  public ResponseEntity<LoginResponse> refreshToken(
      @RequestHeader("Authorization") String refreshToken) {
    if (refreshToken.startsWith("Bearer ")) {
      refreshToken = refreshToken.substring(7);
    }

    RefreshTokenResponse tokenResponse = tokenProvider.refreshToken(refreshToken);

    return ResponseEntity.ok(
        new LoginResponse(
            tokenResponse.getAccessToken(), refreshToken, tokenResponse.getExpiresAt(), null));
  }
}
