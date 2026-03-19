package com.yourapp.product.service;

import com.yourapp.common.security.TenantContext;
import com.yourapp.product.entity.Product;
import com.yourapp.product.dto.CreateProductDto;
import com.yourapp.product.repository.ProductRepository;
import com.yourapp.tenant.entity.Tenant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> listByTenant() {
        Long tenantId = TenantContext.requireTenantId();
        return productRepository.findAllByTenant_Id(tenantId);
    }

    public Product create(CreateProductDto request) {
        Long tenantId = TenantContext.requireTenantId();

        Product product = new Product();
        product.setTenant(tenantRef(tenantId));
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setStock(request.stock());
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }

    public Product update(Long productId, CreateProductDto request) {
        Long tenantId = TenantContext.requireTenantId();

        Product existing = productRepository.findByIdAndTenant_Id(productId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        existing.setName(request.name());
        existing.setDescription(request.description());
        existing.setPrice(request.price());
        existing.setStock(request.stock());
        existing.setUpdatedAt(LocalDateTime.now());
        return productRepository.save(existing);
    }

    public void delete(Long productId) {
        Long tenantId = TenantContext.requireTenantId();
        Product existing = productRepository.findByIdAndTenant_Id(productId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        productRepository.delete(existing);
    }

    private static Tenant tenantRef(Long tenantId) {
        Tenant tenant = new Tenant();
        tenant.setId(tenantId);
        return tenant;
    }
}

