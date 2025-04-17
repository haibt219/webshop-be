package vn.dungnt.webshop_be.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoryDTO {
  private Long id;

  @NotBlank(message = "Tên danh mục không được để trống")
  @Size(max = 255, message = "Tên danh mục không được vượt quá 255 ký tự")
  private String name;

  private String description;

  private Long parentId;

  public CategoryDTO() {}

  public CategoryDTO(Long id, String name, String description, Long parentId) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.parentId = parentId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Long getParentId() {
    return parentId;
  }

  public void setParentId(Long parentId) {
    this.parentId = parentId;
  }
}
