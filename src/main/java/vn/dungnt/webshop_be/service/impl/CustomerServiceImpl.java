package vn.dungnt.webshop_be.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.dungnt.webshop_be.dto.CustomerDTO;
import vn.dungnt.webshop_be.entity.Account;
import vn.dungnt.webshop_be.entity.Customer;
import vn.dungnt.webshop_be.entity.RoleEnum;
import vn.dungnt.webshop_be.exception.ResourceNotFoundException;
import vn.dungnt.webshop_be.exception.ValidationException;
import vn.dungnt.webshop_be.repository.CustomerRepository;
import vn.dungnt.webshop_be.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {
  @Autowired private CustomerRepository customerRepository;
  @Autowired private PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public CustomerDTO createCustomer(CustomerDTO customerDTO) {
    // Kiểm tra tính duy nhất
    if (!isUsernameUnique(customerDTO.getUsername())) {
      throw new ValidationException("Tên đăng nhập đã tồn tại");
    }

    if (!isEmailUnique(customerDTO.getEmail())) {
      throw new ValidationException("Email đã được sử dụng");
    }

    if (!isPhoneUnique(customerDTO.getPhone())) {
      throw new ValidationException("Số điện thoại đã được sử dụng");
    }

    // Tạo khách hàng mới
    Customer customer =
        new Customer(
            customerDTO.getName(),
            customerDTO.getUsername(),
            passwordEncoder.encode(customerDTO.getPassword()),
            customerDTO.getEmail(),
            RoleEnum.CUSTOMER,
            customerDTO.getPhone(),
            customerDTO.getAddress());

    // Lưu và trả về DTO
    Customer savedCustomer = customerRepository.save(customer);
    return convertToDTO(savedCustomer);
  }

  @Override
  @Transactional
  public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
    // Tìm khách hàng
    Customer existingCustomer =
        customerRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách hàng"));

    // Cập nhật thông tin
    existingCustomer.setName(customerDTO.getName());
    existingCustomer.setPhone(customerDTO.getPhone());
    existingCustomer.setAddress(customerDTO.getAddress());

    // Kiểm tra và cập nhật email nếu thay đổi
    if (!existingCustomer.getEmail().equals(customerDTO.getEmail())) {
      if (!isEmailUnique(customerDTO.getEmail())) {
        throw new ValidationException("Email đã được sử dụng");
      }
      existingCustomer.setEmail(customerDTO.getEmail());
    }

    // Lưu và trả về
    Customer updatedCustomer = customerRepository.save(existingCustomer);
    return convertToDTO(updatedCustomer);
  }

  @Override
  @Transactional
  public void deleteCustomer(Long id) {
    Customer customer =
        customerRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách hàng"));

    customerRepository.delete(customer);
  }

  @Override
  @Transactional(readOnly = true)
  public CustomerDTO getCustomerById(Long id) {
    Customer customer =
        customerRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách hàng"));

    return convertToDTO(customer);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CustomerDTO> searchCustomers(
      String searchTerm, Account.Status status, Pageable pageable) {
    Page<Customer> customerPage = customerRepository.findCustomers(searchTerm, status, pageable);

    return customerPage.map(this::convertToDTO);
  }

  @Override
  @Transactional
  public CustomerDTO changeCustomerStatus(Long id, Account.Status status) {
    Customer customer =
        customerRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách hàng"));

    customer.setStatus(status);
    Customer updatedCustomer = customerRepository.save(customer);

    return convertToDTO(updatedCustomer);
  }

  @Override
  public boolean isEmailUnique(String email) {
    return !customerRepository.existsByEmail(email);
  }

  @Override
  public boolean isUsernameUnique(String username) {
    return !customerRepository.existsByUsername(username);
  }

  @Override
  public boolean isPhoneUnique(String phone) {
    return !customerRepository.existsByPhone(phone);
  }

  private CustomerDTO convertToDTO(Customer customer) {
    CustomerDTO dto = new CustomerDTO();
    dto.setId(customer.getId());
    dto.setName(customer.getName());
    dto.setUsername(customer.getUsername());
    dto.setEmail(customer.getEmail());
    dto.setPhone(customer.getPhone());
    dto.setAddress(customer.getAddress());
    dto.setStatus(customer.getStatus());
    dto.setRole(customer.getRole());
    dto.setTotalOrders(customer.getTotalOrders());
    dto.setTotalSpent(customer.getTotalSpent());
    dto.setLastOrderDate(customer.getLastOrderDate());

    return dto;
  }
}
