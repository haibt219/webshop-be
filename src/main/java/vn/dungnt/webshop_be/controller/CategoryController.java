package vn.dungnt.webshop_be.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import vn.dungnt.webshop_be.dto.CategoryDTO;
import vn.dungnt.webshop_be.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

  @Autowired private CategoryService categoryService;

  /** Lấy danh sách danh mục có phân trang và tìm kiếm theo tên */
  @GetMapping
  public ResponseEntity<Page<CategoryDTO>> getCategories(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(required = false) String name,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "ASC") String direction) {

    Sort sort =
        direction.equalsIgnoreCase("DESC")
            ? Sort.by(sortBy).descending()
            : Sort.by(sortBy).ascending();

    Pageable pageable = PageRequest.of(page, size, sort);
    Page<CategoryDTO> categories = categoryService.getCategories(name, pageable);

    return ResponseEntity.ok(categories);
  }

  /** Lấy thông tin danh mục theo ID */
  @GetMapping("/{id}")
  public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
    CategoryDTO category = categoryService.getCategoryById(id);
    return ResponseEntity.ok(category);
  }

  /** Tạo danh mục mới */
  @PostMapping
  public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
    CategoryDTO createdCategory = categoryService.createCategory(categoryDTO);
    return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
  }

  /** Cập nhật thông tin danh mục */
  @PutMapping("/{id}")
  public ResponseEntity<CategoryDTO> updateCategory(
      @PathVariable Long id, @Valid @RequestBody CategoryDTO categoryDTO) {
    CategoryDTO updatedCategory = categoryService.updateCategory(id, categoryDTO);
    return ResponseEntity.ok(updatedCategory);
  }

  /** Xóa danh mục */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
    categoryService.deleteCategory(id);
    return ResponseEntity.noContent().build();
  }

  /** Lấy danh mục cha có phân trang */
  @GetMapping("/parents")
  public ResponseEntity<Page<CategoryDTO>> getParentCategories(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "ASC") String direction) {

    Sort sort =
        direction.equalsIgnoreCase("DESC")
            ? Sort.by(sortBy).descending()
            : Sort.by(sortBy).ascending();

    Pageable pageable = PageRequest.of(page, size, sort);
    Page<CategoryDTO> parentCategories = categoryService.getParentCategories(pageable);

    return ResponseEntity.ok(parentCategories);
  }

  /** Lấy danh mục con của một danh mục cha có phân trang */
  @GetMapping("/parent/{parentId}/subcategories")
  public ResponseEntity<Page<CategoryDTO>> getSubcategoriesByParentId(
      @PathVariable Long parentId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "ASC") String direction) {

    Sort sort =
        direction.equalsIgnoreCase("DESC")
            ? Sort.by(sortBy).descending()
            : Sort.by(sortBy).ascending();

    Pageable pageable = PageRequest.of(page, size, sort);
    Page<CategoryDTO> subcategories =
        categoryService.getSubcategoriesByParentId(parentId, pageable);

    return ResponseEntity.ok(subcategories);
  }

  @GetMapping("/{id}/all-category-ids")
  public ResponseEntity<List<Long>> getAllCategoryIdsIncludingChildren(@PathVariable Long id) {
    try {
      List<Long> allIds = categoryService.getAllCategoryIdsIncludingChildren(id);
      return ResponseEntity.ok(allIds);
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }
}
