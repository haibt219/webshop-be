package vn.dungnt.webshop_be.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.dungnt.webshop_be.dto.ProductDiscountDTO;

import java.util.List;
import java.util.Optional;

public interface ProductDiscountService {
  // Lấy tất cả khuyến mãi có phân trang
  Page<ProductDiscountDTO> getAllDiscounts(Pageable pageable);

  // Lấy khuyến mãi theo ID
  Optional<ProductDiscountDTO> getDiscountById(Long id);

  // Lấy khuyến mãi active của một sản phẩm
  List<ProductDiscountDTO> getActiveDiscountsForProduct(Long productId);

  // Tạo mới khuyến mãi
  ProductDiscountDTO createDiscount(ProductDiscountDTO discountDTO);

  // Cập nhật khuyến mãi
  ProductDiscountDTO updateDiscount(Long id, ProductDiscountDTO discountDTO);

  // Xóa khuyến mãi
  void deleteDiscount(Long id);

  // Kích hoạt/Vô hiệu hóa khuyến mãi
  ProductDiscountDTO toggleDiscountStatus(Long id, boolean active);

  // Lấy tất cả khuyến mãi đang active
  Page<ProductDiscountDTO> getAllActiveDiscounts(Pageable pageable);

  // Lấy khuyến mãi theo danh mục
  Page<ProductDiscountDTO> getActiveDiscountsByCategory(Long categoryId, Pageable pageable);
}
