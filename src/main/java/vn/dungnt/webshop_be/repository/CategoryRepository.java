package vn.dungnt.webshop_be.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.dungnt.webshop_be.entity.Category;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

  /**
   * Tìm kiếm danh mục theo tên với phân trang
   *
   * @param name Tên danh mục cần tìm (không phân biệt hoa thường)
   * @param pageable Thông tin phân trang
   * @return Trang chứa các đối tượng Category phù hợp
   */
  Page<Category> findByNameContainingIgnoreCase(String name, Pageable pageable);

  /**
   * Tìm tất cả danh mục cha (các danh mục không có parentId) với phân trang
   *
   * @param pageable Thông tin phân trang
   * @return Trang chứa các đối tượng Category
   */
  Page<Category> findByParentIdIsNull(Pageable pageable);

  /**
   * Tìm tất cả danh mục con của một danh mục cha với phân trang
   *
   * @param parentId ID của danh mục cha
   * @param pageable Thông tin phân trang
   * @return Trang chứa các đối tượng Category
   */
  Page<Category> findByParentId(Long parentId, Pageable pageable);

  boolean existsByNameIgnoreCase(String name);

  boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);

  @Query("SELECT COUNT(c) FROM Category c WHERE c.parentId = ?1")
  int countChildCategories(Long parentId);
}
