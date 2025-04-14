package vn.dungnt.webshop_be.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.dungnt.webshop_be.entity.Customer;
import vn.dungnt.webshop_be.repository.CustomerRepository;
import vn.dungnt.webshop_be.service.CustomerService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Page<Customer> searchCustomersWithPagination(String keyword, Customer.Status status, Pageable pageable) {
        if (status != null) {
            return customerRepository.searchCustomersByStatusAndKeyword(keyword, status, pageable);
        } else {
            return customerRepository.searchCustomers(keyword, pageable);
        }
    }

    // Lấy khách hàng theo ID
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with id: " + id));
    }

    // Lấy khách hàng theo email
    public Optional<Customer> getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    // Lấy danh sách khách hàng theo trạng thái
    public List<Customer> getCustomersByStatus(Customer.Status status) {
        return customerRepository.findByStatus(status);
    }

    // Thêm khách hàng mới
    @Transactional
    public Customer createCustomer(Customer customer) {
        // Kiểm tra email đã tồn tại
        if (customerRepository.findByEmail(customer.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Đặt giá trị mặc định nếu cần
        if (customer.getCreatedAt() == null) {
            customer.setCreatedAt(java.time.LocalDateTime.now());  // Đặt giá trị mặc định cho createdAt
        }
        if (customer.getTotalOrders() == null) {
            customer.setTotalOrders(0);
        }
        if (customer.getTotalSpent() == null) {
            customer.setTotalSpent(BigDecimal.ZERO);  // Đặt giá trị mặc định cho totalSpent
        }

        return customerRepository.save(customer);
    }

    // Cập nhật thông tin khách hàng
    @Transactional
    public Customer updateCustomer(Long id, Customer customerDetails) {
        Customer customer = getCustomerById(id);

        // Kiểm tra nếu email đã thay đổi và đã tồn tại
        if (!customer.getEmail().equals(customerDetails.getEmail()) &&
                customerRepository.findByEmail(customerDetails.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Cập nhật thông tin
        customer.setName(customerDetails.getName());
        customer.setEmail(customerDetails.getEmail());
        customer.setPhone(customerDetails.getPhone());
        customer.setAddress(customerDetails.getAddress());
        customer.setStatus(customerDetails.getStatus());

        return customerRepository.save(customer);
    }

    // Cập nhật trạng thái khách hàng
    @Transactional
    public Customer updateCustomerStatus(Long id, Customer.Status status) {
        Customer customer = getCustomerById(id);
        customer.setStatus(status);
        return customerRepository.save(customer);
    }

    // Xóa khách hàng
    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = getCustomerById(id);
        customerRepository.delete(customer);
    }

    // Cập nhật thông tin đơn hàng cho khách hàng
    @Transactional
    public Customer updateOrderInformation(Long customerId, BigDecimal orderAmount) {
        Customer customer = getCustomerById(customerId);

        // Cập nhật số lượng đơn hàng
        customer.incrementTotalOrders();

        // Cập nhật tổng chi tiêu và ngày đặt hàng cuối
        customer.addToTotalSpent(orderAmount);

        return customerRepository.save(customer);
    }

    // Thống kê khách hàng theo trạng thái
    public Long countCustomersByStatus(Customer.Status status) {
        return customerRepository.countByStatus(status);
    }

    // Lấy danh sách khách hàng có tổng chi tiêu lớn hơn minAmount
    public List<Customer> getTopSpenders(BigDecimal minAmount) {
        return customerRepository.findByTotalSpentGreaterThan(minAmount);
    }
}
