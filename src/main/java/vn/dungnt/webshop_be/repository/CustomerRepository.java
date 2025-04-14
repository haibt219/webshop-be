package vn.dungnt.webshop_be.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.dungnt.webshop_be.entity.Customer;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // Tìm kiếm khách hàng theo email
    Optional<Customer> findByEmail(String email);

    // Tìm kiếm khách hàng theo số điện thoại
    Optional<Customer> findByPhone(String phone);

    // Tìm kiếm khách hàng theo trạng thái
    List<Customer> findByStatus(Customer.Status status);

    // Tìm kiếm khách hàng theo từ khóa (tên, email, phone)
    @Query("SELECT c FROM Customer c WHERE (c.name LIKE %:keyword% OR c.email LIKE %:keyword% OR c.phone LIKE %:keyword%)")
    Page<Customer> searchCustomers(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT c FROM Customer c WHERE (c.name LIKE %:keyword% OR c.email LIKE %:keyword% OR c.phone LIKE %:keyword%) AND c.status = :status")
    Page<Customer> searchCustomersByStatusAndKeyword(@Param("keyword") String keyword, @Param("status") Customer.Status status, Pageable pageable);

    // Lấy danh sách khách hàng có đơn hàng trong khoảng thời gian
    @Query("SELECT c FROM Customer c WHERE c.lastOrderDate BETWEEN :startDate AND :endDate")
    List<Customer> findCustomersByOrderDateBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    // Lấy danh sách khách hàng có tổng chi tiêu lớn hơn một mức cụ thể
    List<Customer> findByTotalSpentGreaterThan(BigDecimal amount);

    // Lấy danh sách khách hàng theo thời gian tạo
    List<Customer> findByCreatedAtBetween(Date startDate, Date endDate);

    // Đếm số lượng khách hàng theo trạng thái
    @Query("SELECT COUNT(c) FROM Customer c WHERE c.status = :status")
    Long countByStatus(@Param("status") Customer.Status status);
}
