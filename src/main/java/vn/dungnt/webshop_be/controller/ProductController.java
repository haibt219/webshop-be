package vn.dungnt.webshop_be.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.dungnt.webshop_be.dto.ProductDTO;
import vn.dungnt.webshop_be.dto.ProductDetailDTO;
import vn.dungnt.webshop_be.dto.request.ProductCreateRequest;
import vn.dungnt.webshop_be.dto.request.ProductUpdateRequest;
import vn.dungnt.webshop_be.exception.NotFoundException;
import vn.dungnt.webshop_be.service.ProductDetailService;
import vn.dungnt.webshop_be.service.ProductService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/products")
public class ProductController {

  @Autowired public ProductService productService;
  @Autowired public ProductDetailService productDetailService;

  @GetMapping("/search")
  public ResponseEntity<Page<ProductDTO>> searchProducts(
      @RequestParam(required = false) String name,
      @RequestParam(required = false) Boolean active,
      @RequestParam(required = false) BigDecimal minPrice,
      @RequestParam(required = false) BigDecimal maxPrice,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "createdAt") String sortBy,
      @RequestParam(defaultValue = "desc") String sortDirection) {
    Sort sort =
        Sort.by(sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
    PageRequest pageRequest = PageRequest.of(page, size, sort);

    Page<ProductDTO> products =
        productService.searchProducts(name, active, minPrice, maxPrice, pageRequest);

    return ResponseEntity.ok(products);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
    return productService
        .getProductById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<ProductDetailDTO> createProduct(
      @Valid @RequestBody ProductCreateRequest productCreateRequest) {
    ProductDetailDTO createdProduct = productDetailService.createProduct(productCreateRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
  }

  @PutMapping("/{productId}")
  public ResponseEntity<ProductDetailDTO> updateProduct(
      @PathVariable Long productId, @Valid @RequestBody ProductUpdateRequest productUpdateRequest) {
    try {
      ProductDetailDTO updatedProduct =
          productDetailService.updateProduct(productId, productUpdateRequest);
      return ResponseEntity.ok(updatedProduct);
    } catch (NotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
    productService.deleteProduct(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/detail/{productId}")
  public ResponseEntity<ProductDetailDTO> getProductDetail(@PathVariable Long productId) {
    return productDetailService
        .findByProductId(productId)
        .map(productDetail -> ResponseEntity.ok(productDetail))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }
}
