package vn.dungnt.webshop_be.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import vn.dungnt.webshop_be.config.jwt.JwtTokenProvider;
import vn.dungnt.webshop_be.dto.request.LoginRequest;
import vn.dungnt.webshop_be.dto.request.RegisterRequest;
import vn.dungnt.webshop_be.dto.response.LoginResponse;
import vn.dungnt.webshop_be.dto.response.MessageResponse;
import vn.dungnt.webshop_be.dto.response.RefreshTokenResponse;
import vn.dungnt.webshop_be.service.AuthService;
import vn.dungnt.webshop_be.service.RefreshTokenService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired private AuthenticationManager authenticationManager;
  @Autowired private JwtTokenProvider tokenProvider;
  @Autowired private AuthService authService;
  @Autowired private RefreshTokenService refreshTokenService;

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> authenticateUser(
      @Valid @RequestBody LoginRequest loginRequest) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    String accessToken = tokenProvider.generateToken(authentication);

    String refreshToken = tokenProvider.generateRefreshToken(authentication);

    return ResponseEntity.ok(
        new LoginResponse(
            accessToken,
            refreshToken,
            System.currentTimeMillis() + tokenProvider.getAccessTokenExpiration()));
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
            tokenResponse.getAccessToken(), refreshToken, tokenResponse.getExpiresAt()));
  }
}
