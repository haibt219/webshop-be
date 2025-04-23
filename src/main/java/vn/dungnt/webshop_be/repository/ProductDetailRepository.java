package vn.dungnt.webshop_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.dungnt.webshop_be.entity.ProductDetail;

import java.util.Optional;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {
  Optional<ProductDetail> findByProductId(Long productId);
}
