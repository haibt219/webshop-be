package vn.dungnt.webshop_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.dungnt.webshop_be.entity.Account;
import vn.dungnt.webshop_be.entity.RefreshToken;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByToken(String token);

  boolean existsByToken(String token);

  void deleteByAccount(Account account);
}
