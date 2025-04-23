package vn.dungnt.webshop_be.service;

import vn.dungnt.webshop_be.dto.ProductDetailDTO;
import vn.dungnt.webshop_be.dto.request.ProductCreateRequest;
import vn.dungnt.webshop_be.dto.request.ProductUpdateRequest;

import java.util.Optional;

public interface ProductDetailService {
  Optional<ProductDetailDTO> findByProductId(Long productId);

  ProductDetailDTO createProduct(ProductCreateRequest productCreateRequest);

  ProductDetailDTO updateProduct(Long productId, ProductUpdateRequest productUpdateRequest);
}
