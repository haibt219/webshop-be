package vn.dungnt.webshop_be.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.dungnt.webshop_be.dto.ProductDTO;
import vn.dungnt.webshop_be.entity.Product;
import vn.dungnt.webshop_be.entity.ProductDiscount;
import vn.dungnt.webshop_be.exception.NotFoundException;
import vn.dungnt.webshop_be.repository.ProductRepository;
import vn.dungnt.webshop_be.service.ProductService;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

  @Autowired private ProductRepository productRepository;

  @Override
  public Page<ProductDTO> searchProducts(
      String name,
      Boolean active,
      BigDecimal minPrice,
      BigDecimal maxPrice,
      Long categoryId,
      Pageable pageable) {
    Page<Product> productPage =
        productRepository.searchProducts(name, active, minPrice, maxPrice, categoryId, pageable);
    return productPage.map(this::mapToProductDTO);
  }

  @Override
  public void deleteProduct(Long id) {
    Product product =
        productRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào có id: " + id));
    productRepository.delete(product);
  }

  @Override
  public Optional<ProductDTO> getProductById(Long id) {
    return productRepository.findById(id).map(this::mapToProductDTO);
  }

  private ProductDTO mapToProductDTO(Product product) {
    ProductDTO productDTO = new ProductDTO();
    productDTO.setId(product.getId());
    productDTO.setName(product.getName());
    productDTO.setDescription(product.getDescription());
    productDTO.setImage(product.getImage());
    productDTO.setPrice(product.getPrice());
    productDTO.setCreatedAt(product.getCreatedAt());
    productDTO.setUpdatedAt(product.getUpdatedAt());
    productDTO.setActive(product.getActive());
    if (product.getCategory() != null) {
      productDTO.setCategoryId(product.getCategory().getId());
      productDTO.setCategoryName(product.getCategory().getName());
    }
    ProductDiscount activeDiscount = product.getActiveDiscount();
    if (activeDiscount != null) {
      productDTO.setDiscountPrice(activeDiscount.getDiscountPrice());
      productDTO.setHasActiveDiscount(true);
    } else {
      productDTO.setHasActiveDiscount(false);
    }
    return productDTO;
  }

  /**
   * mapToEntity
   *
   * @param productDTO
   * @return
   */
  private Product mapToProduct(ProductDTO productDTO) {
    Product product = new Product();
    product.setName(productDTO.getName());
    product.setDescription(productDTO.getDescription());
    product.setImage(productDTO.getImage());
    product.setPrice(productDTO.getPrice());
    product.setCreatedAt(productDTO.getCreatedAt());
    product.setUpdatedAt(productDTO.getUpdatedAt());
    product.setActive(productDTO.getActive());
    if (productDTO.getCreatedAt() != null) {
      product.setCreatedAt(productDTO.getCreatedAt());
    }
    if (productDTO.getUpdatedAt() != null) {
      product.setUpdatedAt(productDTO.getUpdatedAt());
    }
    product.setActive(productDTO.getActive() != null ? productDTO.getActive() : true);
    return product;
  }
}
