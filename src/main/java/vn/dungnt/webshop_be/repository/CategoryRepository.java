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

  List<Category> findByParentId(Long parentId);

  Page<Category> findByParentId(Long parentId, Pageable pageable);

  Page<Category> findByParentIdIsNull(Pageable pageable);

  Page<Category> findByNameContainingIgnoreCase(String name, Pageable pageable);

  boolean existsByParentId(Long parentId);
}
