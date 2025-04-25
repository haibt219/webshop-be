package vn.dungnt.webshop_be.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.dungnt.webshop_be.dto.ProductDetailDTO;
import vn.dungnt.webshop_be.dto.request.ProductCreateRequest;
import vn.dungnt.webshop_be.dto.request.ProductUpdateRequest;
import vn.dungnt.webshop_be.entity.Category;
import vn.dungnt.webshop_be.entity.Product;
import vn.dungnt.webshop_be.entity.ProductDetail;
import vn.dungnt.webshop_be.entity.ProductDiscount;
import vn.dungnt.webshop_be.exception.NotFoundException;
import vn.dungnt.webshop_be.repository.CategoryRepository;
import vn.dungnt.webshop_be.repository.ProductDetailRepository;
import vn.dungnt.webshop_be.repository.ProductDiscountRepository;
import vn.dungnt.webshop_be.repository.ProductRepository;
import vn.dungnt.webshop_be.service.ProductDetailService;

import java.util.Optional;

@Service
@Transactional
public class ProductDetailServiceImpl implements ProductDetailService {

  @Autowired private ProductRepository productRepository;
  @Autowired private ProductDetailRepository productDetailRepository;
  @Autowired private CategoryRepository categoryRepository;
  @Autowired private ProductDiscountRepository discountRepository;

  @Override
  public Optional<ProductDetailDTO> findByProductId(Long productId) {
    return productDetailRepository.findByProductId(productId).map(this::mapToProductDetailDTO);
  }

  @Override
  @Transactional
  public ProductDetailDTO createProduct(ProductCreateRequest productCreateRequest) {
    // Tạo đối tượng Product từ ProductCreateRequest
    Product product = new Product();
    product.setName(productCreateRequest.getName());
    product.setDescription(productCreateRequest.getDescription());
    product.setPrice(productCreateRequest.getPrice());
    product.setActive(productCreateRequest.getActive());
    product.setImage(productCreateRequest.getImage());

    // Thêm category nếu có
    if (productCreateRequest.getCategoryId() != null) {
      Optional<Category> categoryOpt =
          categoryRepository.findById(productCreateRequest.getCategoryId());
      categoryOpt.ifPresent(product::setCategory);
    }

    // Lưu Product vào database
    Product savedProduct = productRepository.save(product);

    // Tạo ProductDetail
    ProductDetail productDetail = new ProductDetail();
    productDetail.setProduct(savedProduct);
    productDetail.setBrand(productCreateRequest.getBrand());
    productDetail.setModel(productCreateRequest.getModel());
    productDetail.setProcessor(productCreateRequest.getProcessor());
    productDetail.setRam(productCreateRequest.getRam());
    productDetail.setStorage(productCreateRequest.getStorage());
    productDetail.setStorageType(productCreateRequest.getStorageType());
    productDetail.setScreenSize(productCreateRequest.getScreenSize());
    productDetail.setScreenResolution(productCreateRequest.getScreenResolution());
    productDetail.setBatteryCapacity(productCreateRequest.getBatteryCapacity());
    productDetail.setOperatingSystem(productCreateRequest.getOperatingSystem());
    productDetail.setReleaseDate(productCreateRequest.getReleaseDate());
    productDetail.setColor(productCreateRequest.getColor());
    productDetail.setWarrantyPeriodMonths(productCreateRequest.getWarrantyPeriodMonths());

    // Lưu ProductDetail vào database
    ProductDetail savedProductDetail = productDetailRepository.save(productDetail);

    // Cập nhật mối quan hệ hai chiều
    savedProduct.setProductDetail(savedProductDetail);

    return mapToProductDetailDTO(savedProductDetail);
  }

  @Override
  @Transactional
  public ProductDetailDTO updateProduct(Long productId, ProductUpdateRequest productUpdateRequest) {
    // Tìm kiếm Product theo ID
    Product product =
        productRepository
            .findById(productId)
            .orElseThrow(
                () -> new NotFoundException("Không tìm thấy sản phẩm nào có id: " + productId));

    // Lấy ra đối tượng ProductDetail liên kết
    ProductDetail productDetail = product.getProductDetail();
    if (productDetail == null) {
      throw new NotFoundException("Sản phẩm này chưa có thông tin chi tiết");
    }

    // Cập nhật thông tin Product
    if (productUpdateRequest.getName() != null) {
      product.setName(productUpdateRequest.getName());
    }
    if (productUpdateRequest.getDescription() != null) {
      product.setDescription(productUpdateRequest.getDescription());
    }
    if (productUpdateRequest.getImage() != null) {
      product.setImage(productUpdateRequest.getImage());
    }
    if (productUpdateRequest.getPrice() != null) {
      product.setPrice(productUpdateRequest.getPrice());
    }
    if (productUpdateRequest.getActive() != null) {
      product.setActive(productUpdateRequest.getActive());
    }

    // Cập nhật category nếu có
    if (productUpdateRequest.getCategoryId() != null) {
      Optional<Category> categoryOpt =
          categoryRepository.findById(productUpdateRequest.getCategoryId());
      categoryOpt.ifPresent(product::setCategory);
    }

    // Lưu cập nhật cho Product
    productRepository.save(product);

    // Cập nhật thông tin ProductDetail
    if (productUpdateRequest.getBrand() != null) {
      productDetail.setBrand(productUpdateRequest.getBrand());
    }
    if (productUpdateRequest.getModel() != null) {
      productDetail.setModel(productUpdateRequest.getModel());
    }
    if (productUpdateRequest.getProcessor() != null) {
      productDetail.setProcessor(productUpdateRequest.getProcessor());
    }
    if (productUpdateRequest.getRam() != null) {
      productDetail.setRam(productUpdateRequest.getRam());
    }
    if (productUpdateRequest.getStorage() != null) {
      productDetail.setStorage(productUpdateRequest.getStorage());
    }
    if (productUpdateRequest.getStorageType() != null) {
      productDetail.setStorageType(productUpdateRequest.getStorageType());
    }
    if (productUpdateRequest.getScreenSize() != null) {
      productDetail.setScreenSize(productUpdateRequest.getScreenSize());
    }
    if (productUpdateRequest.getScreenResolution() != null) {
      productDetail.setScreenResolution(productUpdateRequest.getScreenResolution());
    }
    if (productUpdateRequest.getBatteryCapacity() != null) {
      productDetail.setBatteryCapacity(productUpdateRequest.getBatteryCapacity());
    }
    if (productUpdateRequest.getOperatingSystem() != null) {
      productDetail.setOperatingSystem(productUpdateRequest.getOperatingSystem());
    }
    if (productUpdateRequest.getReleaseDate() != null) {
      productDetail.setReleaseDate(productUpdateRequest.getReleaseDate());
    }
    if (productUpdateRequest.getColor() != null) {
      productDetail.setColor(productUpdateRequest.getColor());
    }
    if (productUpdateRequest.getWarrantyPeriodMonths() != null) {
      productDetail.setWarrantyPeriodMonths(productUpdateRequest.getWarrantyPeriodMonths());
    }

    // Lưu cập nhật cho ProductDetail
    ProductDetail updatedProductDetail = productDetailRepository.save(productDetail);

    // Trả về DTO từ entities đã cập nhật
    return mapToProductDetailDTO(updatedProductDetail);
  }

  private ProductDetailDTO mapToProductDetailDTO(ProductDetail productDetail) {
    ProductDetailDTO dto = new ProductDetailDTO();
    Product product = productDetail.getProduct();

    // Map thông tin từ Product
    dto.setId(product.getId());
    dto.setName(product.getName());
    dto.setDescription(product.getDescription());
    dto.setImage(product.getImage());
    dto.setPrice(product.getPrice());
    dto.setCreatedAt(product.getCreatedAt());
    dto.setUpdatedAt(product.getUpdatedAt());
    dto.setActive(product.getActive());

    // Map thông tin category nếu có
    if (product.getCategory() != null) {
      dto.setCategoryId(product.getCategory().getId());
      dto.setCategoryName(product.getCategory().getName());
    }

    // Map thông tin từ ProductDetail
    dto.setBrand(productDetail.getBrand());
    dto.setModel(productDetail.getModel());
    dto.setProcessor(productDetail.getProcessor());
    dto.setRam(productDetail.getRam());
    dto.setStorage(productDetail.getStorage());
    dto.setStorageType(productDetail.getStorageType());
    dto.setScreenSize(productDetail.getScreenSize());
    dto.setScreenResolution(productDetail.getScreenResolution());
    dto.setBatteryCapacity(productDetail.getBatteryCapacity());
    dto.setOperatingSystem(productDetail.getOperatingSystem());
    dto.setReleaseDate(productDetail.getReleaseDate());
    dto.setColor(productDetail.getColor());
    dto.setWarrantyPeriodMonths(productDetail.getWarrantyPeriodMonths());

    // Map thông tin khuyến mãi
    ProductDiscount activeDiscount = product.getActiveDiscount();
    if (activeDiscount != null) {
      dto.setDiscountId(activeDiscount.getId());
      dto.setDiscountPrice(activeDiscount.getDiscountPrice());
      dto.setPromotionDescription(activeDiscount.getPromotionDescription());
      dto.setPromotionStartDate(activeDiscount.getStartDate());
      dto.setPromotionEndDate(activeDiscount.getEndDate());
      dto.setHasActiveDiscount(true);
    } else {
      dto.setHasActiveDiscount(false);
    }

    return dto;
  }
}
