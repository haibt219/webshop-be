package vn.dungnt.webshop_be.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.dungnt.webshop_be.dto.response.ApiResponse;
import vn.dungnt.webshop_be.entity.Customer;
import vn.dungnt.webshop_be.service.CustomerService;
import vn.dungnt.webshop_be.dto.CustomerDto;
import vn.dungnt.webshop_be.dto.CustomerStatusDto;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // ✅ Lấy danh sách khách hàng có phân trang và lọc + sắp xếp
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status
    ) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            Pageable pageable = PageRequest.of(page, size, sort);

            Customer.Status customerStatus = null;
            if (status != null && !status.isEmpty()) {
                customerStatus = Customer.Status.valueOf(status.toUpperCase());
            }

            Page<Customer> pageCustomers = customerService.searchCustomersWithPagination(keyword, customerStatus, pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("customers", pageCustomers.getContent());
            response.put("currentPage", pageCustomers.getNumber());
            response.put("totalItems", pageCustomers.getTotalElements());
            response.put("totalPages", pageCustomers.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Lấy chi tiết khách hàng theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        Customer customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    // Thêm khách hàng mới
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody CustomerDto customerDto) {
        try {
            Customer customer = new Customer();
            customer.setName(customerDto.getName());
            customer.setEmail(customerDto.getEmail());
            customer.setPhone(customerDto.getPhone());
            customer.setAddress(customerDto.getAddress());
            customer.setStatus(Customer.Status.valueOf(customerDto.getStatus().toUpperCase()));

            Customer createdCustomer = customerService.createCustomer(customer);
            return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Cập nhật thông tin khách hàng
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody CustomerDto customerDto) {
        try {
            Customer customerDetails = new Customer();
            customerDetails.setName(customerDto.getName());
            customerDetails.setEmail(customerDto.getEmail());
            customerDetails.setPhone(customerDto.getPhone());
            customerDetails.setAddress(customerDto.getAddress());
            customerDetails.setStatus(Customer.Status.valueOf(customerDto.getStatus().toUpperCase()));

            Customer updatedCustomer = customerService.updateCustomer(id, customerDetails);
            return ResponseEntity.ok(updatedCustomer);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Cập nhật trạng thái khách hàng
    @PatchMapping("/{id}/status")
    public ResponseEntity<Customer> updateCustomerStatus(@PathVariable Long id, @RequestBody CustomerStatusDto statusDto) {
        try {
            Customer.Status status = Customer.Status.valueOf(statusDto.getStatus().toUpperCase());
            Customer updatedCustomer = customerService.updateCustomerStatus(id, status);
            return ResponseEntity.ok(updatedCustomer);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Xóa khách hàng
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCustomer(@PathVariable Long id) {
        try {
            customerService.deleteCustomer(id);
            ApiResponse response = new ApiResponse(true, "Customer deleted successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse(false, "Failed to delete customer");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    // Thống kê khách hàng
//    @GetMapping("/status")
//    public ResponseEntity<Map<String, Object>> getCustomerStats() {
//        try {
//            Map<String, Object> stats = new HashMap<>();
//            stats.put("totalCustomers", customerService.getAllCustomers(0, 1, "id", "asc").getTotalElements());
//            stats.put("activeCustomers", customerService.countCustomersByStatus(Customer.Status.ACTIVE));
//            stats.put("inactiveCustomers", customerService.countCustomersByStatus(Customer.Status.INACTIVE));
//            stats.put("blockedCustomers", customerService.countCustomersByStatus(Customer.Status.BLOCKED));
//
//            return new ResponseEntity<>(stats, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    // Lấy danh sách khách hàng có tổng chi tiêu lớn
    @GetMapping("/top-spenders")
    public ResponseEntity<List<Customer>> getTopSpenders(@RequestParam(defaultValue = "1000000") BigDecimal minAmount) {
        try {
            List<Customer> topSpenders = customerService.getTopSpenders(minAmount);
            return new ResponseEntity<>(topSpenders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
