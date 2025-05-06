package vn.dungnt.webshop_be.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.dungnt.webshop_be.dto.OrderDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface OrderService {
  // Lấy đơn hàng theo ID
  OrderDTO getOrderById(Long orderId);

  // Tạo đơn hàng mới
  OrderDTO createOrder(OrderDTO orderDTO);

  // Cập nhật đơn hàng
  OrderDTO updateOrder(Long orderId, OrderDTO orderDTO);

  // Xóa đơn hàng
  void deleteOrder(Long orderId);

  // Tìm kiếm đơn hàng với phân trang
  Page<OrderDTO> searchOrders(
      String searchTerm,
      String status,
      String paymentStatus,
      LocalDateTime startDate,
      LocalDateTime endDate,
      Pageable pageable);

  // Cập nhật trạng thái đơn hàng
  OrderDTO updateOrderStatus(Long orderId, String newStatus);

  // Lấy đơn hàng theo ID khách hàng
  List<OrderDTO> getOrdersByCustomerId(Long customerId);

  // Lấy đơn hàng theo khoảng thời gian
  List<OrderDTO> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate);

  // Thống kê doanh thu theo ngày
  Map<String, Object> getRevenueByDateRange(LocalDateTime startDate, LocalDateTime endDate);

  // Thống kê số lượng đơn hàng theo trạng thái
  Map<String, Long> getOrderCountByStatus();

  // Hủy đơn hàng
  OrderDTO cancelOrder(Long orderId, String reason);
}
