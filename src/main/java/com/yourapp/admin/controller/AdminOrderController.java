package com.yourapp.admin.controller;

import com.yourapp.order.entity.Order;
import com.yourapp.order.service.OrderService;
import com.yourapp.session.SessionService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

    private final OrderService orderService;
    private final SessionService sessionService;

    @GetMapping
    public List<Order> list(HttpSession session) {
        sessionService.requireAdmin(session);
        Long tenantId = sessionService.requireTenantId(session);
        return orderService.listByTenant(tenantId);
    }

    @GetMapping("/{orderId}")
    public Order get(@PathVariable Long orderId, HttpSession session) {
        sessionService.requireAdmin(session);
        Long tenantId = sessionService.requireTenantId(session);
        return orderService.findByTenantAndId(tenantId, orderId).orElseThrow();
    }
}

