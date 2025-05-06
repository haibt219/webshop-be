package vn.dungnt.webshop_be.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.dungnt.webshop_be.dto.OrderDTO;
import vn.dungnt.webshop_be.entity.Order;
import vn.dungnt.webshop_be.exception.NotFoundException;
import vn.dungnt.webshop_be.exception.ResourceNotFoundException;
import vn.dungnt.webshop_be.repository.OrderRepository;
import vn.dungnt.webshop_be.service.OrderService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

  @Autowired private OrderRepository orderRepository;

  @Override
  public OrderDTO getOrderById(Long orderId) {
    Order order =
        orderRepository
            .findById(orderId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Đơn hàng không tồn tại với ID: " + orderId));
    return OrderDTO.fromEntity(order);
  }

  @Override
  @Transactional
  public OrderDTO createOrder(OrderDTO orderDTO) {
    Order order = orderDTO.toEntity();

    // Tính toán lại tổng tiền để đảm bảo tính chính xác
    order.calculateTotalAmount();

    Order savedOrder = orderRepository.save(order);
    return OrderDTO.fromEntity(savedOrder);
  }

  @Override
  @Transactional
  public OrderDTO updateOrder(Long orderId, OrderDTO orderDTO) {
    Order existingOrder =
        orderRepository
            .findById(orderId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Đơn hàng không tồn tại với ID: " + orderId));

    // Cập nhật thông tin đơn hàng
    Order updatedOrder = orderDTO.toEntity();
    updatedOrder.setOrderId(existingOrder.getOrderId());
    updatedOrder.setCreatedAt(existingOrder.getCreatedAt());

    // Tính toán lại tổng tiền để đảm bảo tính chính xác
    updatedOrder.calculateTotalAmount();

    Order savedOrder = orderRepository.save(updatedOrder);
    return OrderDTO.fromEntity(savedOrder);
  }

  @Override
  @Transactional
  public void deleteOrder(Long orderId) {
    if (!orderRepository.existsById(orderId)) {
      throw new NotFoundException("Đơn hàng không tồn tại với ID: " + orderId);
    }
    orderRepository.deleteById(orderId);
  }

  public Page<OrderDTO> searchOrders(
      String searchTerm,
      String status,
      String paymentStatus,
      LocalDateTime startDate,
      LocalDateTime endDate,
      Pageable pageable) {

    // Tạo Specification để xây dựng truy vấn động
    Specification<Order> spec = Specification.where(null);

    // Tìm kiếm theo từ khóa (tên khách hàng hoặc ID đơn hàng)
    if (searchTerm != null && !searchTerm.trim().isEmpty()) {
      spec =
          spec.and(
              (root, query, cb) -> {
                String likePattern = "%" + searchTerm.toLowerCase() + "%";
                return cb.or(
                    cb.like(cb.lower(root.get("customerName")), likePattern),
                    cb.like(root.get("orderId").as(String.class), likePattern),
                    cb.like(cb.lower(root.get("customerPhone")), likePattern),
                    cb.like(cb.lower(root.get("customerEmail")), likePattern));
              });
    }

    // Lọc theo trạng thái đơn hàng
    if (status != null && !status.trim().isEmpty()) {
      spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
    }

    // Lọc theo trạng thái thanh toán
    if (paymentStatus != null && !paymentStatus.trim().isEmpty()) {
      spec = spec.and((root, query, cb) -> cb.equal(root.get("paymentStatus"), paymentStatus));
    }

    // Lọc theo ngày bắt đầu
    if (startDate != null) {
      spec =
          spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("createdAt"), startDate));
    }

    // Lọc theo ngày kết thúc
    if (endDate != null) {
      spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("createdAt"), endDate));
    }

    // Thực hiện truy vấn với Specification
    Page<Order> orderPage = orderRepository.findAll(spec, pageable);

    // Chuyển đổi các entity thành DTO
    return orderPage.map(OrderDTO::fromEntity);
  }

  @Override
  @Transactional
  public OrderDTO updateOrderStatus(Long orderId, String newStatus) {
    Order order =
        orderRepository
            .findById(orderId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Đơn hàng không tồn tại với ID: " + orderId));

    order.setStatus(newStatus);
    Order updatedOrder = orderRepository.save(order);
    return OrderDTO.fromEntity(updatedOrder);
  }

  @Override
  public List<OrderDTO> getOrdersByCustomerId(Long customerId) {
    List<Order> orders = orderRepository.findByCustomerId(customerId);
    return orders.stream().map(OrderDTO::fromEntity).collect(Collectors.toList());
  }

  @Override
  public List<OrderDTO> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
    List<Order> orders = orderRepository.findByCreatedAtBetween(startDate, endDate);
    return orders.stream().map(OrderDTO::fromEntity).collect(Collectors.toList());
  }

  @Override
  public Map<String, Object> getRevenueByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
    List<Order> orders = orderRepository.findByCreatedAtBetween(startDate, endDate);

    // Nhóm đơn hàng theo ngày và tính tổng doanh thu
    Map<String, Object> result = new HashMap<>();
    Map<LocalDate, Double> revenueByDate = new HashMap<>();

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    orders.forEach(
        order -> {
          LocalDate orderDate = order.getCreatedAt().toLocalDate();
          Double orderAmount = order.getTotalAmount().doubleValue();

          revenueByDate.merge(orderDate, orderAmount, Double::sum);
        });

    // Chuyển đổi kết quả thành định dạng phù hợp để trả về
    Map<String, Double> formattedResult = new HashMap<>();
    revenueByDate.forEach(
        (date, amount) -> {
          formattedResult.put(date.format(formatter), amount);
        });

    result.put("revenueByDate", formattedResult);
    result.put(
        "totalRevenue", formattedResult.values().stream().mapToDouble(Double::doubleValue).sum());
    result.put("orderCount", orders.size());

    return result;
  }

  @Override
  public Map<String, Long> getOrderCountByStatus() {
    Map<String, Long> result = new HashMap<>();

    // Các trạng thái chính của đơn hàng
    List<String> statuses =
        List.of(
            "Chờ xác nhận",
            "Đang chờ xử lý",
            "Đang chuẩn bị",
            "Đang giao hàng",
            "Đã hoàn thành",
            "Đã hủy");

    // Đếm số lượng đơn hàng theo từng trạng thái
    for (String status : statuses) {
      Long count = orderRepository.countByStatus(status);
      result.put(status, count);
    }

    // Tổng số đơn hàng
    Long totalOrders = orderRepository.count();
    result.put("Tổng cộng", totalOrders);

    return result;
  }

  @Override
  @Transactional
  public OrderDTO cancelOrder(Long orderId, String reason) {
    Order order =
        orderRepository
            .findById(orderId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Đơn hàng không tồn tại với ID: " + orderId));

    // Kiểm tra xem đơn hàng có thể hủy không
    if (order.getStatus().equals("Đã hoàn thành") || order.getStatus().equals("Đã hủy")) {
      throw new IllegalStateException(
          "Không thể hủy đơn hàng với trạng thái: " + order.getStatus());
    }

    order.setStatus("Đã hủy");
    order.setNotes(
        order.getNotes() != null
            ? order.getNotes() + " | Lý do hủy: " + reason
            : "Lý do hủy: " + reason);

    Order updatedOrder = orderRepository.save(order);
    return OrderDTO.fromEntity(updatedOrder);
  }
}
