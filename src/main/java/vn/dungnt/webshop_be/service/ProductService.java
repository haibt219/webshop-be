package vn.dungnt.webshop_be.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.dungnt.webshop_be.dto.ProductDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductService {
  Page<ProductDTO> searchProducts(
      String name,
      Boolean active,
      BigDecimal minPrice,
      BigDecimal maxPrice,
      Long categoryId,
      Pageable pageable);

  void deleteProduct(Long id);

  Optional<ProductDTO> getProductById(Long id);
}
