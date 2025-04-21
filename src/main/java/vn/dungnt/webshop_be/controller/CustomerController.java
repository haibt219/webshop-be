package vn.dungnt.webshop_be.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.dungnt.webshop_be.dto.CustomerDTO;
import vn.dungnt.webshop_be.dto.request.CustomerCreateRequest;
import vn.dungnt.webshop_be.dto.request.CustomerUpdateRequest;
import vn.dungnt.webshop_be.entity.Account;
import vn.dungnt.webshop_be.service.CustomerService;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
  @Autowired private CustomerService customerService;

  @PostMapping
  public ResponseEntity<CustomerDTO> createCustomer(
      @Valid @RequestBody CustomerCreateRequest createRequest) {
    if (!createRequest.isPasswordMatching()) {
      throw new IllegalArgumentException("Mật khẩu xác nhận không khớp");
    }
    CustomerDTO createdCustomer = customerService.createCustomer(createRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
  }

  @PutMapping("/{id}")
  public ResponseEntity<CustomerDTO> updateCustomer(
      @PathVariable Long id, @Valid @RequestBody CustomerUpdateRequest customerDTO) {
    CustomerDTO updatedCustomer = customerService.updateCustomer(id, customerDTO);
    return ResponseEntity.ok(updatedCustomer);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
    customerService.deleteCustomer(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{id}")
  public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
    CustomerDTO customer = customerService.getCustomerById(id);
    return ResponseEntity.ok(customer);
  }

  @GetMapping("/search")
  public ResponseEntity<Page<CustomerDTO>> searchCustomers(
      @RequestParam(required = false) String searchTerm,
      @RequestParam(required = false) Account.Status status,
      @PageableDefault(size = 10, sort = "id") Pageable pageable) {
    Page<CustomerDTO> customers = customerService.searchCustomers(searchTerm, status, pageable);
    return ResponseEntity.ok(customers);
  }

  @PatchMapping("/{id}/status")
  public ResponseEntity<CustomerDTO> changeCustomerStatus(
      @PathVariable Long id, @RequestParam Account.Status status) {
    CustomerDTO updatedCustomer = customerService.changeCustomerStatus(id, status);
    return ResponseEntity.ok(updatedCustomer);
  }

  @GetMapping("/check-email")
  public ResponseEntity<Boolean> checkEmailUnique(@RequestParam String email) {
    return ResponseEntity.ok(customerService.isEmailUnique(email));
  }

  @GetMapping("/check-username")
  public ResponseEntity<Boolean> checkUsernameUnique(@RequestParam String username) {
    return ResponseEntity.ok(customerService.isUsernameUnique(username));
  }

  @GetMapping("/check-phone")
  public ResponseEntity<Boolean> checkPhoneUnique(@RequestParam String phone) {
    return ResponseEntity.ok(customerService.isPhoneUnique(phone));
  }
}
