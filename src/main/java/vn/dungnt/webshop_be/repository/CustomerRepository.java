package vn.dungnt.webshop_be.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.dungnt.webshop_be.entity.Account;
import vn.dungnt.webshop_be.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

  @Query(
      "SELECT c FROM Customer c WHERE "
          + "(:searchTerm IS NULL OR "
          + "LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR "
          + "LOWER(c.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR "
          + "LOWER(c.phone) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND "
          + "(:status IS NULL OR c.status = :status)")
  Page<Customer> findCustomers(
      @Param("searchTerm") String searchTerm,
      @Param("status") Account.Status status,
      Pageable pageable);

  Customer findByEmail(String email);

  Customer findByUsername(String username);

  Customer findByPhone(String phone);

  long countByStatus(Account.Status status);

  boolean existsByEmail(String email);

  boolean existsByUsername(String username);

  boolean existsByPhone(String phone);
}
