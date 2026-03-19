package com.yourapp.admin.controller;

import com.yourapp.product.entity.Product;
import com.yourapp.product.service.ProductService;
import com.yourapp.session.SessionService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
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
        Long tenantId = sessionService.requireTenantId(session);
        return productService.listByTenant(tenantId);
    }

    @PostMapping
    public Product create(@RequestBody ProductService.CreateProductRequest request, HttpSession session) {
        sessionService.requireAdmin(session);
        Long tenantId = sessionService.requireTenantId(session);
        return productService.create(tenantId, request);
    }

    @PutMapping("/{productId}")
    public Product update(@PathVariable Long productId,
                           @RequestBody ProductService.CreateProductRequest request,
                           HttpSession session) {
        sessionService.requireAdmin(session);
        Long tenantId = sessionService.requireTenantId(session);
        return productService.update(tenantId, productId, request);
    }

    @DeleteMapping("/{productId}")
    public void delete(@PathVariable Long productId, HttpSession session) {
        sessionService.requireAdmin(session);
        Long tenantId = sessionService.requireTenantId(session);
        productService.delete(tenantId, productId);
    }
}

