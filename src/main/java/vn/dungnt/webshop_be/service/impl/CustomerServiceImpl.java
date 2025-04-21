package vn.dungnt.webshop_be.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.dungnt.webshop_be.dto.CustomerDTO;
import vn.dungnt.webshop_be.dto.request.CustomerCreateRequest;
import vn.dungnt.webshop_be.dto.request.CustomerUpdateRequest;
import vn.dungnt.webshop_be.entity.Account;
import vn.dungnt.webshop_be.entity.Customer;
import vn.dungnt.webshop_be.entity.RoleEnum;
import vn.dungnt.webshop_be.exception.ResourceNotFoundException;
import vn.dungnt.webshop_be.repository.CustomerRepository;
import vn.dungnt.webshop_be.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {
  @Autowired private CustomerRepository customerRepository;
  @Autowired private PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public CustomerDTO createCustomer(CustomerCreateRequest createRequest) {
    if (customerRepository.existsByUsername(createRequest.getUsername())) {
      throw new IllegalArgumentException("Tên đăng nhập đã tồn tại");
    }
    if (customerRepository.existsByEmail(createRequest.getEmail())) {
      throw new IllegalArgumentException("Email đã tồn tại");
    }
    Customer customer = new Customer();
    customer.setName(createRequest.getName());
    customer.setUsername(createRequest.getUsername());
    customer.setEmail(createRequest.getEmail());
    customer.setPhone(createRequest.getPhone());
    customer.setAddress(createRequest.getAddress());
    String encodedPassword = passwordEncoder.encode(createRequest.getPassword());
    customer.setPassword(encodedPassword);
    if (createRequest.getStatus() == null) {
      customer.setStatus(Account.Status.ACTIVE);
    } else {
      customer.setStatus(createRequest.getStatus());
    }
    customer.setRole(RoleEnum.CUSTOMER);
    Customer savedCustomer = customerRepository.save(customer);
    return convertToDTO(savedCustomer);
  }

  @Override
  @Transactional
  public CustomerDTO updateCustomer(Long id, CustomerUpdateRequest updateRequest) {
    Customer existingCustomer =
        customerRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách hàng."));
    if (updateRequest.getName() != null) {
      existingCustomer.setName(updateRequest.getName());
    }
    if (updateRequest.getEmail() != null) {
      existingCustomer.setEmail(updateRequest.getEmail());
    }
    if (updateRequest.getPhone() != null) {
      existingCustomer.setPhone(updateRequest.getPhone());
    }
    if (updateRequest.getAddress() != null) {
      existingCustomer.setAddress(updateRequest.getAddress());
    }
    if (updateRequest.getStatus() != null) {
      existingCustomer.setStatus(updateRequest.getStatus());
    }
    if (updateRequest.getPassword() != null && updateRequest.getConfirmPassword() != null) {
      if (!updateRequest.getPassword().equals(updateRequest.getConfirmPassword())) {
        throw new IllegalArgumentException("Mật khẩu xác nhận không khớp.");
      }
      String encodedPassword = passwordEncoder.encode(updateRequest.getPassword());
      existingCustomer.setPassword(encodedPassword);
    }
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
