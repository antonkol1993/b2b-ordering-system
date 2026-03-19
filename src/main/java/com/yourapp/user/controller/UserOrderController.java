package com.yourapp.user.controller;

import com.yourapp.order.entity.Order;
import com.yourapp.order.service.OrderService;
import com.yourapp.session.SessionService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class UserOrderController {

    private final OrderService orderService;
    private final SessionService sessionService;

    @PostMapping
    public Order create(@RequestBody OrderService.CreateOrderRequest request, HttpSession session) {
        Long tenantId = sessionService.requireTenantId(session);
        Long userId = sessionService.requireUserId(session);
        return orderService.create(tenantId, userId, request);
    }
}

