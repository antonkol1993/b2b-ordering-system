package com.yourapp.user.controller;

import com.yourapp.product.entity.Product;
import com.yourapp.product.service.ProductService;
import com.yourapp.session.SessionService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class UserProductController {

    private final ProductService productService;
    private final SessionService sessionService;

    @GetMapping
    public List<Product> list(HttpSession session) {
        sessionService.requireUserId(session);
        return productService.listByTenant();
    }
}

