package vn.dungnt.webshop_be.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.dungnt.webshop_be.entity.Order;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository
    extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
  // Tìm đơn hàng theo ID khách hàng
  List<Order> findByCustomerId(Long customerId);

  // Tìm đơn hàng theo trạng thái
  List<Order> findByStatus(String status);

  // Tìm đơn hàng theo khoảng thời gian
  List<Order> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

  // Đếm số đơn hàng theo trạng thái
  Long countByStatus(String status);

  // Tìm đơn hàng mới nhất của một khách hàng
  Order findFirstByCustomerIdOrderByCreatedAtDesc(Long customerId);

  // Thống kê số lượng đơn hàng theo ngày
  @Query(
      "SELECT CAST(o.createdAt AS date) as orderDate, COUNT(o) as orderCount "
          + "FROM Order o "
          + "WHERE o.createdAt BETWEEN :startDate AND :endDate "
          + "GROUP BY CAST(o.createdAt AS date) "
          + "ORDER BY orderDate ASC")
  List<Object[]> countOrdersByDate(
      @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
