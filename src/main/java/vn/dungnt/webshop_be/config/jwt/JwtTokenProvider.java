package vn.dungnt.webshop_be.config.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import vn.dungnt.webshop_be.dto.response.RefreshTokenResponse;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenProvider {
  private final Key secretKey;

  @Value("${jwt.access-token.expiration}")
  private long accessTokenExpiration;

  @Value("${jwt.refresh-token.expiration}")
  private long refreshTokenExpiration;

  public JwtTokenProvider(@Value("${jwt.secret}") String secret) {
    this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
  }

  public String generateToken(Authentication authentication) {
    UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

    // Thêm các claims bổ sung
    Map<String, Object> claims = new HashMap<>();
    claims.put("username", userPrincipal.getUsername());
    claims.put("authorities", userPrincipal.getAuthorities());

    return Jwts.builder()
        .setClaims(claims)
        .setSubject(userPrincipal.getUsername())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
        .signWith(secretKey, SignatureAlgorithm.HS256)
        .compact();
  }

  public String generateRefreshToken(Authentication authentication) {
    UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

    return Jwts.builder()
        .setSubject(userPrincipal.getUsername())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
        .signWith(secretKey, SignatureAlgorithm.HS256)
        .compact();
  }

  public String getUsernameFromToken(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public boolean validateToken(String token, UserDetails userDetails) {
    final String username = getUsernameFromToken(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    try {
      return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    } catch (ExpiredJwtException e) {
      throw new JwtExpiredException("Token đã hết hạn");
    } catch (SignatureException e) {
      throw new JwtInvalidException("Chữ ký token không hợp lệ");
    } catch (JwtException e) {
      throw new JwtInvalidException("Token không hợp lệ");
    }
  }

  public RefreshTokenResponse refreshToken(String refreshToken) {
    try {
      Claims claims = extractAllClaims(refreshToken);

      if (claims.getExpiration().before(new Date())) {
        throw new JwtExpiredException("Refresh token đã hết hạn");
      }

      String username = claims.getSubject();

      String newAccessToken =
          Jwts.builder()
              .setSubject(username)
              .setIssuedAt(new Date())
              .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
              .signWith(secretKey, SignatureAlgorithm.HS256)
              .compact();

      return new RefreshTokenResponse(
          newAccessToken, System.currentTimeMillis() + accessTokenExpiration);
    } catch (JwtException e) {
      throw new JwtInvalidException("Refresh token không hợp lệ");
    }
  }

  public static class JwtExpiredException extends RuntimeException {
    public JwtExpiredException(String message) {
      super(message);
    }
  }

  public static class JwtInvalidException extends RuntimeException {
    public JwtInvalidException(String message) {
      super(message);
    }
  }

  public long getAccessTokenExpiration() {
    return accessTokenExpiration;
  }
}
