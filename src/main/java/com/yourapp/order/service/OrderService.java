package com.yourapp.order.service;

import com.yourapp.common.AppConstants;
import com.yourapp.order.entity.Order;
import com.yourapp.order.entity.OrderItem;
import com.yourapp.order.repository.OrderRepository;
import com.yourapp.product.entity.Product;
import com.yourapp.product.repository.ProductRepository;
import com.yourapp.tenant.entity.Tenant;
import com.yourapp.tenant.repository.TenantRepository;
import com.yourapp.user.entity.User;
import com.yourapp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;

    public Order create(Long tenantId, Long userId, CreateOrderRequest request) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!tenantId.equals(user.getTenant().getId())) {
            throw new IllegalArgumentException("User doesn't belong to tenant");
        }

        Order order = new Order();
        order.setTenant(tenant);
        order.setUser(user);
        order.setStatus(AppConstants.ORDER_STATUS_CREATED);
        order.setComment(request.comment());
        order.setCreatedAt(LocalDateTime.now());

        List<OrderItem> items = new ArrayList<>();
        for (CreateOrderItem itemReq : request.items()) {
            Product product = productRepository.findByIdAndTenant_Id(itemReq.productId(), tenantId)
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + itemReq.productId()));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemReq.quantity());
            orderItem.setUnitPrice(product.getPrice());
            orderItem.setCreatedAt(LocalDateTime.now());
            items.add(orderItem);
        }

        order.setItems(items);
        return orderRepository.save(order);
    }

    public List<Order> listByTenant(Long tenantId) {
        return orderRepository.findAllByTenant_Id(tenantId);
    }

    public java.util.Optional<Order> findByTenantAndId(Long tenantId, Long orderId) {
        return orderRepository.findByIdAndTenant_Id(orderId, tenantId);
    }

    public record CreateOrderRequest(List<CreateOrderItem> items, String comment) {}

    public record CreateOrderItem(Long productId, Integer quantity) {}
}

