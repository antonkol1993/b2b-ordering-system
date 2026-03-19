package com.yourapp.user.controller;

import com.yourapp.order.entity.Order;
import com.yourapp.order.service.OrderService;
import com.yourapp.order.dto.CreateOrderDto;
import com.yourapp.session.SessionService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class UserOrderController {

    private final OrderService orderService;
    private final SessionService sessionService;

    @PostMapping
    public Order create(@Valid @RequestBody CreateOrderDto request, HttpSession session) {
        Long userId = sessionService.requireUserId(session);
        return orderService.create(userId, request);
    }
}

