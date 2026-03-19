package com.yourapp.admin.controller;

import com.yourapp.product.entity.Product;
import com.yourapp.product.service.ProductService;
import com.yourapp.session.SessionService;
import com.yourapp.product.dto.CreateProductDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/products")
public class AdminProductController {

    private final ProductService productService;
    private final SessionService sessionService;

    @GetMapping
    public List<Product> list(HttpSession session) {
        sessionService.requireAdmin(session);
        return productService.listByTenant();
    }

    @PostMapping
    public Product create(@Valid @RequestBody CreateProductDto request, HttpSession session) {
        sessionService.requireAdmin(session);
        return productService.create(request);
    }

    @PutMapping("/{productId}")
    public Product update(@PathVariable Long productId,
                           @Valid @RequestBody CreateProductDto request,
                           HttpSession session) {
        sessionService.requireAdmin(session);
        return productService.update(productId, request);
    }

    @DeleteMapping("/{productId}")
    public void delete(@PathVariable Long productId, HttpSession session) {
        sessionService.requireAdmin(session);
        productService.delete(productId);
    }
}

