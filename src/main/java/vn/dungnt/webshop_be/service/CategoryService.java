package vn.dungnt.webshop_be.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.dungnt.webshop_be.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
  /**
   * Lấy danh sách danh mục có phân trang và tìm kiếm theo tên
   *
   * @param name Tên danh mục để tìm kiếm
   * @param pageable Thông tin phân trang
   * @return Trang chứa các đối tượng CategoryDTO
   */
  Page<CategoryDTO> getCategories(String name, Pageable pageable);

  /** Lấy thông tin danh mục theo ID */
  CategoryDTO getCategoryById(Long id);

  /** Tạo danh mục mới */
  CategoryDTO createCategory(CategoryDTO categoryDTO);

  /** Cập nhật thông tin danh mục */
  CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);

  /** Xóa danh mục */
  void deleteCategory(Long id);

  /**
   * Lấy danh mục cha có phân trang
   *
   * @param pageable Thông tin phân trang
   * @return Trang chứa các đối tượng CategoryDTO
   */
  Page<CategoryDTO> getParentCategories(Pageable pageable);

  /**
   * Lấy danh mục con của một danh mục cha có phân trang
   *
   * @param parentId ID của danh mục cha
   * @param pageable Thông tin phân trang
   * @return Trang chứa các đối tượng CategoryDTO
   */
  Page<CategoryDTO> getSubcategoriesByParentId(Long parentId, Pageable pageable);
}
