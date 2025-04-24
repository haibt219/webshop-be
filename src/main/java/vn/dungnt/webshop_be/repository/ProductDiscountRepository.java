package vn.dungnt.webshop_be.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.dungnt.webshop_be.entity.ProductDiscount;

import java.util.List;

@Repository
public interface ProductDiscountRepository extends JpaRepository<ProductDiscount, Long> {

  /**
   * Tìm khuyến mãi đang active cho một sản phẩm
   *
   * @param productId
   * @return
   */
  @Query(
      "SELECT pd FROM ProductDiscount pd WHERE pd.product.id = :productId "
          + "AND pd.active = true "
          + "AND pd.startDate <= CURRENT_DATE "
          + "AND pd.endDate >= CURRENT_DATE")
  List<ProductDiscount> findActiveDiscountsByProductId(@Param("productId") Long productId);

  /**
   * Tìm khuyến mãi đang active theo danh mục
   *
   * @param categoryId
   * @param pageable
   * @return
   */
  @Query(
      "SELECT pd FROM ProductDiscount pd WHERE "
          + "pd.product.category.id = :categoryId "
          + "AND pd.active = true "
          + "AND pd.startDate <= CURRENT_DATE "
          + "AND pd.endDate >= CURRENT_DATE")
  Page<ProductDiscount> findActiveDiscountsByCategory(
      @Param("categoryId") Long categoryId, Pageable pageable);

  /**
   * Tìm tất cả khuyến mãi đang active
   *
   * @param pageable
   * @return
   */
  @Query(
      "SELECT pd FROM ProductDiscount pd WHERE "
          + "pd.active = true "
          + "AND pd.startDate <= CURRENT_DATE "
          + "AND pd.endDate >= CURRENT_DATE")
  Page<ProductDiscount> findAllActiveDiscounts(Pageable pageable);
}
