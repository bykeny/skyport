package com.airport.retail.service;

import com.airport.retail.client.CheckInValidationClient;
import com.airport.retail.domain.DutyFreeOrder;
import com.airport.retail.domain.OrderStatus;
import com.airport.retail.domain.Product;
import com.airport.retail.dto.CreateOrderRequest;
import com.airport.retail.dto.CreateProductRequest;
import com.airport.retail.dto.OrderResponse;
import com.airport.retail.dto.ProductResponse;
import com.airport.retail.dto.UpdateOrderStatusRequest;
import com.airport.retail.exception.NotFoundException;
import com.airport.retail.repository.DutyFreeOrderRepository;
import com.airport.retail.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RetailService {

    private final ProductRepository productRepository;
    private final DutyFreeOrderRepository orderRepository;
    private final CheckInValidationClient checkInValidationClient;

    public RetailService(ProductRepository productRepository,
                         DutyFreeOrderRepository orderRepository,
                         CheckInValidationClient checkInValidationClient) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.checkInValidationClient = checkInValidationClient;
    }

    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
        if (productRepository.existsBySku(request.getSku())) {
            throw new IllegalStateException("Product SKU already exists: " + request.getSku());
        }

        Product product = new Product();
        product.setSku(request.getSku());
        product.setName(request.getName());
        product.setCategory(request.getCategory());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setActive(true);
        return toProductResponse(productRepository.save(product));
    }

    public List<ProductResponse> listProducts() {
        return productRepository.findByActiveTrueOrderByNameAsc().stream()
                .map(this::toProductResponse)
                .toList();
    }

    public ProductResponse getProduct(Long id) {
        return toProductResponse(findProduct(id));
    }

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        Long checkInId = checkInValidationClient.validatePassengerCheckedIn(request.getPassengerId(), request.getFlightId());
        Product product = findProduct(request.getProductId());
        if (!Boolean.TRUE.equals(product.getActive())) {
            throw new IllegalStateException("Product is not active: " + product.getId());
        }
        if (product.getStockQuantity() < request.getQuantity()) {
            throw new IllegalStateException("Insufficient stock for product: " + product.getSku());
        }

        product.setStockQuantity(product.getStockQuantity() - request.getQuantity());
        productRepository.save(product);

        DutyFreeOrder order = new DutyFreeOrder();
        order.setPassengerId(request.getPassengerId());
        order.setFlightId(request.getFlightId());
        order.setCheckInId(checkInId);
        order.setProductId(product.getId());
        order.setProductSku(product.getSku());
        order.setProductName(product.getName());
        order.setQuantity(request.getQuantity());
        order.setUnitPrice(product.getPrice());
        order.setTotalAmount(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
        order.setStatus(OrderStatus.CREATED);
        return toOrderResponse(orderRepository.save(order));
    }

    public OrderResponse getOrder(Long id) {
        return toOrderResponse(findOrder(id));
    }

    public List<OrderResponse> getPassengerOrders(Long passengerId) {
        return orderRepository.findByPassengerIdOrderByCreatedAtDesc(passengerId).stream()
                .map(this::toOrderResponse)
                .toList();
    }

    public List<OrderResponse> getFlightOrders(Long flightId) {
        return orderRepository.findByFlightIdOrderByCreatedAtDesc(flightId).stream()
                .map(this::toOrderResponse)
                .toList();
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long id, UpdateOrderStatusRequest request) {
        DutyFreeOrder order = findOrder(id);
        order.setStatus(request.getStatus());
        return toOrderResponse(orderRepository.save(order));
    }

    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));
    }

    private DutyFreeOrder findOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Duty-free order not found with id: " + id));
    }

    private ProductResponse toProductResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setSku(product.getSku());
        response.setName(product.getName());
        response.setCategory(product.getCategory());
        response.setPrice(product.getPrice());
        response.setStockQuantity(product.getStockQuantity());
        response.setActive(product.getActive());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());
        return response;
    }

    private OrderResponse toOrderResponse(DutyFreeOrder order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setPassengerId(order.getPassengerId());
        response.setFlightId(order.getFlightId());
        response.setCheckInId(order.getCheckInId());
        response.setProductId(order.getProductId());
        response.setProductSku(order.getProductSku());
        response.setProductName(order.getProductName());
        response.setQuantity(order.getQuantity());
        response.setUnitPrice(order.getUnitPrice());
        response.setTotalAmount(order.getTotalAmount());
        response.setStatus(order.getStatus());
        response.setCreatedAt(order.getCreatedAt());
        response.setUpdatedAt(order.getUpdatedAt());
        return response;
    }
}
