package vn.dungnt.webshop_be.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.dungnt.webshop_be.dto.CategoryDTO;
import vn.dungnt.webshop_be.entity.Category;
import vn.dungnt.webshop_be.exception.ResourceNotFoundException;
import vn.dungnt.webshop_be.repository.CategoryRepository;
import vn.dungnt.webshop_be.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

  @Autowired private CategoryRepository categoryRepository;

  @Override
  public Page<CategoryDTO> getCategories(String name, Pageable pageable) {
    Page<Category> categoriesPage;

    if (name != null && !name.trim().isEmpty()) {
      categoriesPage = categoryRepository.findByNameContainingIgnoreCase(name, pageable);
    } else {
      categoriesPage = categoryRepository.findAll(pageable);
    }

    return categoriesPage.map(this::convertToDTO);
  }

  @Override
  public CategoryDTO getCategoryById(Long id) {
    Category category =
        categoryRepository
            .findById(id)
            .orElseThrow(
                () -> new ResourceNotFoundException("Danh mục không tồn tại với id: " + id));
    return convertToDTO(category);
  }

  @Override
  public CategoryDTO createCategory(CategoryDTO categoryDTO) {
    Category category = convertToEntity(categoryDTO);
    Category savedCategory = categoryRepository.save(category);
    return convertToDTO(savedCategory);
  }

  @Override
  public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
    Category existingCategory =
        categoryRepository
            .findById(id)
            .orElseThrow(
                () -> new ResourceNotFoundException("Danh mục không tồn tại với id: " + id));

    existingCategory.setName(categoryDTO.getName());
    existingCategory.setDescription(categoryDTO.getDescription());
    existingCategory.setParentId(categoryDTO.getParentId());
    Category updatedCategory = categoryRepository.save(existingCategory);
    return convertToDTO(updatedCategory);
  }

  @Override
  public void deleteCategory(Long id) {
    Category category =
        categoryRepository
            .findById(id)
            .orElseThrow(
                () -> new ResourceNotFoundException("Danh mục không tồn tại với id: " + id));
    categoryRepository.delete(category);
  }

  @Override
  public Page<CategoryDTO> getParentCategories(Pageable pageable) {
    Page<Category> parentCategoriesPage = categoryRepository.findByParentIdIsNull(pageable);
    return parentCategoriesPage.map(this::convertToDTO);
  }

  @Override
  public Page<CategoryDTO> getSubcategoriesByParentId(Long parentId, Pageable pageable) {
    Page<Category> subcategoriesPage = categoryRepository.findByParentId(parentId, pageable);
    return subcategoriesPage.map(this::convertToDTO);
  }

  private CategoryDTO convertToDTO(Category category) {
    CategoryDTO dto = new CategoryDTO();
    dto.setId(category.getId());
    dto.setName(category.getName());
    dto.setDescription(category.getDescription());
    dto.setParentId(category.getParentId());
    return dto;
  }

  private Category convertToEntity(CategoryDTO dto) {
    Category category = new Category();
    if (dto.getId() != null) {
      category.setId(dto.getId());
    }
    category.setName(dto.getName());
    category.setDescription(dto.getDescription());
    category.setParentId(dto.getParentId());
    return category;
  }
}
