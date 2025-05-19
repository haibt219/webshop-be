package vn.dungnt.webshop_be.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.dungnt.webshop_be.entity.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  @Query(
      "SELECT p FROM Product p "
          + "WHERE (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) "
          + "AND (:active IS NULL OR p.active = :active) "
          + "AND (:minPrice IS NULL OR p.price >= :minPrice) "
          + "AND (:maxPrice IS NULL OR p.price <= :maxPrice) "
          + "AND (:categoryIds IS NULL OR p.category.id IN :categoryIds)")
  Page<Product> searchProducts(
      @Param("name") String name,
      @Param("active") Boolean active,
      @Param("minPrice") BigDecimal minPrice,
      @Param("maxPrice") BigDecimal maxPrice,
      @Param("categoryIds") List<Long> categoryIds,
      Pageable pageable);
}
