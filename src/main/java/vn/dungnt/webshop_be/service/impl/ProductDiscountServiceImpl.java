package vn.dungnt.webshop_be.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.dungnt.webshop_be.dto.ProductDiscountDTO;
import vn.dungnt.webshop_be.entity.Product;
import vn.dungnt.webshop_be.entity.ProductDiscount;
import vn.dungnt.webshop_be.exception.NotFoundException;
import vn.dungnt.webshop_be.repository.ProductDiscountRepository;
import vn.dungnt.webshop_be.repository.ProductRepository;
import vn.dungnt.webshop_be.service.ProductDiscountService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductDiscountServiceImpl implements ProductDiscountService {
  @Autowired private ProductDiscountRepository discountRepository;
  @Autowired private ProductRepository productRepository;

  @Override
  public Page<ProductDiscountDTO> getAllDiscounts(Pageable pageable) {
    return discountRepository.findAll(pageable).map(this::convertToDTO);
  }

  @Override
  public Optional<ProductDiscountDTO> getDiscountById(Long id) {
    return discountRepository.findById(id).map(this::convertToDTO);
  }

  @Override
  public List<ProductDiscountDTO> getActiveDiscountsForProduct(Long productId) {
    return discountRepository.findActiveDiscountsByProductId(productId).stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
  }

  @Override
  public ProductDiscountDTO createDiscount(ProductDiscountDTO discountDTO) {
    ProductDiscount discount = convertToEntity(discountDTO);

    // Tìm sản phẩm
    Product product =
        productRepository
            .findById(discountDTO.getProductId())
            .orElseThrow(
                () ->
                    new NotFoundException(
                        "Không tìm thấy sản phẩm có id: " + discountDTO.getProductId()));

    discount.setProduct(product);

    // Lưu discount
    ProductDiscount savedDiscount = discountRepository.save(discount);
    return convertToDTO(savedDiscount);
  }

  @Override
  public ProductDiscountDTO updateDiscount(Long id, ProductDiscountDTO discountDTO) {
    ProductDiscount discount =
        discountRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("Không tìm thấy khuyến mãi có id: " + id));

    // Cập nhật thông tin cơ bản
    discount.setDiscountPrice(discountDTO.getDiscountPrice());
    discount.setPromotionDescription(discountDTO.getPromotionDescription());
    discount.setStartDate(discountDTO.getStartDate());
    discount.setEndDate(discountDTO.getEndDate());
    discount.setActive(discountDTO.getActive());

    // Nếu thay đổi sản phẩm
    if (discountDTO.getProductId() != null
        && !discountDTO.getProductId().equals(discount.getProduct().getId())) {
      Product product =
          productRepository
              .findById(discountDTO.getProductId())
              .orElseThrow(
                  () ->
                      new NotFoundException(
                          "Không tìm thấy sản phẩm có id: " + discountDTO.getProductId()));
      discount.setProduct(product);
    }

    // Lưu thay đổi
    ProductDiscount updatedDiscount = discountRepository.save(discount);
    return convertToDTO(updatedDiscount);
  }

  @Override
  public void deleteDiscount(Long id) {
    ProductDiscount discount =
        discountRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("Không tìm thấy khuyến mãi có id: " + id));

    discountRepository.delete(discount);
  }

  @Override
  public ProductDiscountDTO toggleDiscountStatus(Long id, boolean active) {
    ProductDiscount discount =
        discountRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("Không tìm thấy khuyến mãi có id: " + id));

    discount.setActive(active);
    ProductDiscount updatedDiscount = discountRepository.save(discount);

    return convertToDTO(updatedDiscount);
  }

  @Override
  public Page<ProductDiscountDTO> getAllActiveDiscounts(Pageable pageable) {
    return discountRepository.findAllActiveDiscounts(pageable).map(this::convertToDTO);
  }

  @Override
  public Page<ProductDiscountDTO> getActiveDiscountsByCategory(Long categoryId, Pageable pageable) {
    return discountRepository
        .findActiveDiscountsByCategory(categoryId, pageable)
        .map(this::convertToDTO);
  }

  private ProductDiscountDTO convertToDTO(ProductDiscount entity) {
    ProductDiscountDTO dto = new ProductDiscountDTO();

    dto.setId(entity.getId());
    dto.setProductId(entity.getProduct().getId());
    dto.setProductName(entity.getProduct().getName());
    dto.setDiscountPrice(entity.getDiscountPrice());
    dto.setPromotionDescription(entity.getPromotionDescription());
    dto.setStartDate(entity.getStartDate());
    dto.setEndDate(entity.getEndDate());
    dto.setCreatedAt(entity.getCreatedAt());
    dto.setUpdatedAt(entity.getUpdatedAt());
    dto.setActive(entity.getActive());

    return dto;
  }

  private ProductDiscount convertToEntity(ProductDiscountDTO dto) {
    ProductDiscount entity = new ProductDiscount();

    if (dto.getId() != null) {
      entity.setId(dto.getId());
    }

    entity.setDiscountPrice(dto.getDiscountPrice());
    entity.setPromotionDescription(dto.getPromotionDescription());
    entity.setStartDate(dto.getStartDate());
    entity.setEndDate(dto.getEndDate());
    entity.setActive(dto.getActive() != null ? dto.getActive() : true);

    return entity;
  }
}
