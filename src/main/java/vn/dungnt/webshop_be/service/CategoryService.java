package vn.dungnt.webshop_be.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.dungnt.webshop_be.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
  Page<CategoryDTO> getCategories(String name, Pageable pageable);

  CategoryDTO getCategoryById(Long id);

  CategoryDTO createCategory(CategoryDTO categoryDTO);

  CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);

  void deleteCategory(Long id);

  Page<CategoryDTO> getParentCategories(Pageable pageable);

  Page<CategoryDTO> getSubcategoriesByParentId(Long parentId, Pageable pageable);

  List<Long> getAllCategoryIdsIncludingChildren(Long categoryId);
}
