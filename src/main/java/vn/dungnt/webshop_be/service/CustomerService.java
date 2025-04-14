package vn.dungnt.webshop_be.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.dungnt.webshop_be.entity.Customer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CustomerService {

    Page<Customer> searchCustomersWithPagination(String keyword, Customer.Status status, Pageable pageable);

    Customer getCustomerById(Long id);

    Optional<Customer> getCustomerByEmail(String email);

    List<Customer> getCustomersByStatus(Customer.Status status);

    Customer createCustomer(Customer customer);

    Customer updateCustomer(Long id, Customer customerDetails);

    Customer updateCustomerStatus(Long id, Customer.Status status);

    void deleteCustomer(Long id);

    Customer updateOrderInformation(Long customerId, BigDecimal orderAmount);

    Long countCustomersByStatus(Customer.Status status);

    List<Customer> getTopSpenders(BigDecimal minAmount);
}
