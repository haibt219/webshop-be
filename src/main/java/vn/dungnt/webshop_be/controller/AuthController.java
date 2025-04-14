package vn.dungnt.webshop_be.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.dungnt.webshop_be.config.jwt.JwtTokenProvider;
import vn.dungnt.webshop_be.dto.request.LoginRequest;
import vn.dungnt.webshop_be.dto.request.RegisterRequest;
import vn.dungnt.webshop_be.dto.response.LoginResponse;
import vn.dungnt.webshop_be.entity.Account;
import vn.dungnt.webshop_be.entity.RoleEnum;
import vn.dungnt.webshop_be.repository.AccountRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new LoginResponse(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity registerUser(@RequestBody RegisterRequest registerRequest) {
        if (accountRepository.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body("Username đã được sử dụng!");
        }

        if (accountRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body("Email đã được đăng ký!");
        }

        RoleEnum role = RoleEnum.valueOf(registerRequest.getRole().toUpperCase());

        Account newAccount = new Account();
        newAccount.setUsername(registerRequest.getUsername());
        newAccount.setEmail(registerRequest.getEmail());
        newAccount.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newAccount.setRole(role);

        newAccount.setEnabled(true);
        newAccount.setAccountNonExpired(true);
        newAccount.setAccountNonLocked(true);
        newAccount.setCredentialsNonExpired(true);

        accountRepository.save(newAccount);

        return ResponseEntity.status(HttpStatus.CREATED).body("Đăng ký thành công!");
    }

}
