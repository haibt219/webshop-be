package vn.dungnt.webshop_be.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.dungnt.webshop_be.dto.CustomerDTO;
import vn.dungnt.webshop_be.dto.request.CustomerCreateRequest;
import vn.dungnt.webshop_be.dto.request.CustomerUpdateRequest;
import vn.dungnt.webshop_be.entity.Account;

public interface CustomerService {

  CustomerDTO createCustomer(CustomerCreateRequest createRequest);

  CustomerDTO updateCustomer(Long id, CustomerUpdateRequest updateRequest);

  void deleteCustomer(Long id);

  CustomerDTO getCustomerById(Long id);

  Page<CustomerDTO> searchCustomers(String searchTerm, Account.Status status, Pageable pageable);

  CustomerDTO changeCustomerStatus(Long id, Account.Status status);

  boolean isEmailUnique(String email);

  boolean isUsernameUnique(String username);

  boolean isPhoneUnique(String phone);
}
