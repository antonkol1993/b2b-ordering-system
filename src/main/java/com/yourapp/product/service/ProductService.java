package com.yourapp.product.service;

import com.yourapp.product.entity.Product;
import com.yourapp.product.repository.ProductRepository;
import com.yourapp.tenant.entity.Tenant;
import com.yourapp.tenant.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final TenantRepository tenantRepository;

    public List<Product> listByTenant(Long tenantId) {
        return productRepository.findAllByTenant_Id(tenantId);
    }

    public Product create(Long tenantId, CreateProductRequest request) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found"));

        Product product = new Product();
        product.setTenant(tenant);
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }

    public Product update(Long tenantId, Long productId, CreateProductRequest request) {
        Product existing = productRepository.findByIdAndTenant_Id(productId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        existing.setName(request.name());
        existing.setDescription(request.description());
        existing.setPrice(request.price());
        existing.setUpdatedAt(LocalDateTime.now());
        return productRepository.save(existing);
    }

    public void delete(Long tenantId, Long productId) {
        Product existing = productRepository.findByIdAndTenant_Id(productId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        productRepository.delete(existing);
    }

    public record CreateProductRequest(String name, String description, java.math.BigDecimal price) {}
}

