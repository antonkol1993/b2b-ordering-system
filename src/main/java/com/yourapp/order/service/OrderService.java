package com.yourapp.order.service;

import com.yourapp.common.AppConstants;
import com.yourapp.common.security.TenantContext;
import com.yourapp.order.entity.Order;
import com.yourapp.order.entity.OrderItem;
import com.yourapp.order.dto.CreateOrderDto;
import com.yourapp.order.dto.CreateOrderItemDto;
import com.yourapp.order.repository.OrderRepository;
import com.yourapp.product.entity.Product;
import com.yourapp.product.repository.ProductRepository;
import com.yourapp.tenant.entity.Tenant;
import com.yourapp.user.entity.User;
import com.yourapp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public Order create(Long userId, CreateOrderDto request) {
        Long tenantId = TenantContext.requireTenantId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!tenantId.equals(user.getTenant().getId())) {
            throw new IllegalArgumentException("User doesn't belong to tenant");
        }

        Order order = new Order();
        order.setTenant(tenantRef(tenantId));
        order.setUser(user);
        order.setStatus(AppConstants.ORDER_STATUS_CREATED);
        order.setComment(request.comment());
        order.setCreatedAt(LocalDateTime.now());

        List<OrderItem> items = new ArrayList<>();
        for (CreateOrderItemDto itemReq : request.items()) {
            Product product = productRepository.findByIdAndTenant_Id(itemReq.productId(), tenantId)
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + itemReq.productId()));

            if (product.getStock() == null || product.getStock() < itemReq.quantity()) {
                throw new IllegalArgumentException("Not enough stock for product: " + product.getId());
            }

            product.setStock(product.getStock() - itemReq.quantity());
            productRepository.save(product);

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

    public List<Order> listByTenant() {
        Long tenantId = TenantContext.requireTenantId();
        return orderRepository.findAllByTenant_Id(tenantId);
    }

    public java.util.Optional<Order> findByTenantAndId(Long orderId) {
        Long tenantId = TenantContext.requireTenantId();
        return orderRepository.findByIdAndTenant_Id(orderId, tenantId);
    }

    private static Tenant tenantRef(Long tenantId) {
        Tenant tenant = new Tenant();
        tenant.setId(tenantId);
        return tenant;
    }
}

