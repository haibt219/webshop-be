package vn.dungnt.webshop_be.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.dungnt.webshop_be.dto.OrderDTO;
import vn.dungnt.webshop_be.service.OrderService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

  @Autowired private OrderService orderService;

  @GetMapping
  public ResponseEntity<Page<OrderDTO>> getAllOrders(
      @RequestParam(required = false) String search,
      @RequestParam(required = false) String status,
      @RequestParam(required = false) String startDate,
      @RequestParam(required = false) String endDate,
      @RequestParam(required = false) String paymentStatus,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "createdAt") String sortBy,
      @RequestParam(defaultValue = "desc") String sortDirection) {

    // Tạo đối tượng Sort
    Sort sort =
        Sort.by(
            sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC,
            sortBy);

    // Tạo đối tượng Pageable
    PageRequest pageRequest = PageRequest.of(page, size, sort);

    // Xử lý ngày tháng nếu có
    LocalDateTime startDateTime = null;
    LocalDateTime endDateTime = null;
    if (startDate != null && !startDate.isEmpty()) {
      startDateTime = LocalDateTime.parse(startDate + "T00:00:00");
    }
    if (endDate != null && !endDate.isEmpty()) {
      endDateTime = LocalDateTime.parse(endDate + "T23:59:59");
    }

    // Gọi service để tìm kiếm đơn hàng
    Page<OrderDTO> orderPage =
        orderService.searchOrders(
            search, status, paymentStatus, startDateTime, endDateTime, pageRequest);

    return ResponseEntity.ok(orderPage);
  }

  @GetMapping("/{id}")
  public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
    OrderDTO orderDTO = orderService.getOrderById(id);
    return ResponseEntity.ok(orderDTO);
  }

  @PostMapping
  public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
    OrderDTO createdOrder = orderService.createOrder(orderDTO);
    return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<OrderDTO> updateOrder(
      @PathVariable Long id, @RequestBody OrderDTO orderDTO) {
    OrderDTO updatedOrder = orderService.updateOrder(id, orderDTO);
    return ResponseEntity.ok(updatedOrder);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
    orderService.deleteOrder(id);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/{id}/status")
  public ResponseEntity<OrderDTO> updateOrderStatus(
      @PathVariable Long id, @RequestParam String status) {
    OrderDTO updatedOrder = orderService.updateOrderStatus(id, status);
    return ResponseEntity.ok(updatedOrder);
  }

  @PostMapping("/{id}/cancel")
  public ResponseEntity<OrderDTO> cancelOrder(@PathVariable Long id, @RequestParam String reason) {
    OrderDTO cancelledOrder = orderService.cancelOrder(id, reason);
    return ResponseEntity.ok(cancelledOrder);
  }

  @GetMapping("/customer/{customerId}")
  public ResponseEntity<Map<String, Object>> getOrdersByCustomerId(@PathVariable Long customerId) {
    Map<String, Object> response = new HashMap<>();
    response.put("orders", orderService.getOrdersByCustomerId(customerId));
    return ResponseEntity.ok(response);
  }

  @GetMapping("/statistics/status")
  public ResponseEntity<Map<String, Long>> getOrderCountByStatus() {
    Map<String, Long> stats = orderService.getOrderCountByStatus();
    return ResponseEntity.ok(stats);
  }

  @GetMapping("/statistics/revenue")
  public ResponseEntity<Map<String, Object>> getRevenueByDateRange(
      @RequestParam String startDate, @RequestParam String endDate) {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime start = LocalDateTime.parse(startDate + " 00:00:00", formatter);
    LocalDateTime end = LocalDateTime.parse(endDate + " 23:59:59", formatter);

    Map<String, Object> revenueData = orderService.getRevenueByDateRange(start, end);
    return ResponseEntity.ok(revenueData);
  }

  @GetMapping("/date-range")
  public ResponseEntity<Map<String, Object>> getOrdersByDateRange(
      @RequestParam String startDate, @RequestParam String endDate) {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime start = LocalDateTime.parse(startDate + " 00:00:00", formatter);
    LocalDateTime end = LocalDateTime.parse(endDate + " 23:59:59", formatter);

    Map<String, Object> response = new HashMap<>();
    response.put("orders", orderService.getOrdersByDateRange(start, end));
    return ResponseEntity.ok(response);
  }
}
