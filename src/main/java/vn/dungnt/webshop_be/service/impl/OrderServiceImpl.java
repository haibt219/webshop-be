package vn.dungnt.webshop_be.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.dungnt.webshop_be.dto.OrderDTO;
import vn.dungnt.webshop_be.dto.request.OrderCreateRequest;
import vn.dungnt.webshop_be.dto.request.OrderItemRequest;
import vn.dungnt.webshop_be.dto.request.OrderUpdateRequest;
import vn.dungnt.webshop_be.entity.Order;
import vn.dungnt.webshop_be.entity.OrderDetail;
import vn.dungnt.webshop_be.entity.Product;
import vn.dungnt.webshop_be.entity.ProductDiscount;
import vn.dungnt.webshop_be.exception.NotFoundException;
import vn.dungnt.webshop_be.exception.ResourceNotFoundException;
import vn.dungnt.webshop_be.repository.OrderRepository;
import vn.dungnt.webshop_be.repository.ProductRepository;
import vn.dungnt.webshop_be.service.OrderService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

  @Autowired private OrderRepository orderRepository;
  @Autowired private ProductRepository productRepository;

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
  public OrderDTO createOrder(OrderCreateRequest requestDTO) {
    // Tạo đối tượng Order mới
    Order order = new Order();
    order.setCustomerName(requestDTO.getCustomerName());
    order.setCustomerEmail(requestDTO.getCustomerEmail());
    order.setCustomerPhone(requestDTO.getCustomerPhone());
    order.setShippingAddress(requestDTO.getShippingAddress());
    order.setStatus(requestDTO.getStatus());
    order.setPaymentMethod(requestDTO.getPaymentMethod());
    order.setPaymentStatus(requestDTO.getPaymentStatus());
    order.setShippingMethod(requestDTO.getShippingMethod());
    order.setShippingCost(requestDTO.getShippingCost());
    order.setNotes(requestDTO.getNotes());
    order.setCustomerId(requestDTO.getCustomerId());
    order.setTotalAmount(BigDecimal.ZERO);

    // Lấy thông tin sản phẩm từ database và tạo OrderDetail cho mỗi sản phẩm
    for (OrderItemRequest item : requestDTO.getProducts()) {
      Long productId = item.getProductId();
      Integer quantity = item.getQuantity();

      // Lấy thông tin sản phẩm từ database
      Product product =
          productRepository
              .findById(productId)
              .orElseThrow(
                  () ->
                      new ResourceNotFoundException("Sản phẩm không tồn tại với ID: " + productId));

      // Tạo OrderDetail
      OrderDetail orderDetail = new OrderDetail();
      orderDetail.setProductId(product.getId());
      orderDetail.setProductName(product.getName());

      // Đảm bảo productCode không null
      String productCode = "P" + product.getId();
      orderDetail.setProductCode(productCode);

      orderDetail.setProductImage(product.getImage());
      orderDetail.setQuantity(quantity);

      // Sử dụng giá hiện tại của sản phẩm (có thể là giá khuyến mãi)
      BigDecimal currentPrice = product.getCurrentPrice();
      if (currentPrice == null) {
        currentPrice = product.getPrice();
      }
      orderDetail.setPrice(currentPrice);

      // Tính toán discount nếu có
      ProductDiscount activeDiscount = product.getActiveDiscount();
      if (activeDiscount != null) {
        BigDecimal originalPrice = product.getPrice();
        BigDecimal discountPrice = product.getCurrentPrice();
        if (originalPrice != null && discountPrice != null) {
          BigDecimal discountAmount = originalPrice.subtract(discountPrice);

          // Nếu có giảm giá, lưu lại giá trị giảm giá cho item này
          if (discountAmount.compareTo(BigDecimal.ZERO) > 0) {
            // Tổng giảm giá cho số lượng sản phẩm này
            orderDetail.setDiscount(discountAmount.multiply(new BigDecimal(quantity)));
          } else {
            orderDetail.setDiscount(BigDecimal.ZERO);
          }
        } else {
          orderDetail.setDiscount(BigDecimal.ZERO);
        }
      } else {
        orderDetail.setDiscount(BigDecimal.ZERO);
      }

      // Thêm OrderDetail vào Order thông qua phương thức addItem
      order.addItem(orderDetail);
    }

    // Tính toán lại tổng tiền để đảm bảo tính chính xác
    order.calculateTotalAmount();

    // Lưu Order vào database
    Order savedOrder = orderRepository.save(order);

    // Trả về OrderDTO
    return OrderDTO.fromEntity(savedOrder);
  }

  @Override
  @Transactional
  public OrderDTO updateOrder(Long orderId, OrderUpdateRequest requestDTO) {
    // Tìm đơn hàng hiện tại
    Order existingOrder =
        orderRepository
            .findById(orderId)
            .orElseThrow(() -> new NotFoundException("Đơn hàng không tồn tại với ID: " + orderId));

    // Cập nhật thông tin cơ bản
    existingOrder.setCustomerName(requestDTO.getCustomerName());
    existingOrder.setCustomerEmail(requestDTO.getCustomerEmail());
    existingOrder.setCustomerPhone(requestDTO.getCustomerPhone());
    existingOrder.setShippingAddress(requestDTO.getShippingAddress());
    existingOrder.setStatus(requestDTO.getStatus());
    existingOrder.setPaymentMethod(requestDTO.getPaymentMethod());
    existingOrder.setPaymentStatus(requestDTO.getPaymentStatus());
    existingOrder.setShippingMethod(requestDTO.getShippingMethod());
    existingOrder.setShippingCost(requestDTO.getShippingCost());
    existingOrder.setNotes(requestDTO.getNotes());
    existingOrder.setCustomerId(requestDTO.getCustomerId());

    // Xóa tất cả các item hiện tại
    existingOrder.getItems().clear();

    // Thêm các item mới
    for (OrderItemRequest item : requestDTO.getProducts()) {
      Long productId = item.getProductId();
      Integer quantity = item.getQuantity();

      // Lấy thông tin sản phẩm từ database
      Product product =
          productRepository
              .findById(productId)
              .orElseThrow(
                  () -> new NotFoundException("Sản phẩm không tồn tại với ID: " + productId));

      // Tạo OrderDetail
      OrderDetail orderDetail = new OrderDetail();
      orderDetail.setProductId(product.getId());
      orderDetail.setProductName(product.getName());

      // Đảm bảo productCode không null
      String productCode = "P" + product.getId();
      orderDetail.setProductCode(productCode);

      orderDetail.setProductImage(product.getImage());
      orderDetail.setQuantity(quantity);

      // Sử dụng giá hiện tại của sản phẩm (có thể là giá khuyến mãi)
      BigDecimal currentPrice = product.getCurrentPrice();
      if (currentPrice == null) {
        currentPrice = product.getPrice(); // Sử dụng giá gốc nếu không có giá hiện tại
      }
      orderDetail.setPrice(currentPrice);

      // Tính toán discount nếu có
      ProductDiscount activeDiscount = product.getActiveDiscount();
      if (activeDiscount != null) {
        BigDecimal originalPrice = product.getPrice();
        BigDecimal discountPrice = product.getCurrentPrice();
        if (originalPrice != null && discountPrice != null) {
          BigDecimal discountAmount = originalPrice.subtract(discountPrice);

          // Nếu có giảm giá, lưu lại giá trị giảm giá cho item này
          if (discountAmount.compareTo(BigDecimal.ZERO) > 0) {
            // Tổng giảm giá cho số lượng sản phẩm này
            orderDetail.setDiscount(discountAmount.multiply(new BigDecimal(quantity)));
          } else {
            orderDetail.setDiscount(BigDecimal.ZERO);
          }
        } else {
          orderDetail.setDiscount(BigDecimal.ZERO);
        }
      } else {
        orderDetail.setDiscount(BigDecimal.ZERO);
      }

      // Thêm OrderDetail vào Order
      existingOrder.addItem(orderDetail);
    }

    // Tính toán lại tổng tiền để đảm bảo tính chính xác
    existingOrder.calculateTotalAmount();

    // Lưu Order cập nhật vào database
    Order savedOrder = orderRepository.save(existingOrder);

    // Trả về OrderDTO
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
