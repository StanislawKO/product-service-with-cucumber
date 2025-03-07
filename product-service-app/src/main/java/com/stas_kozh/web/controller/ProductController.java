package com.stas_kozh.web.controller;

import com.stas_kozh.domain.exception.ResourceNotFoundException;
import com.stas_kozh.domain.service.ProductService;
import com.stas_kozh.web.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.ProductsServiceApi;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ProductController implements ProductsServiceApi {

    private final ProductService productService;

    @Override
    public ResponseEntity<List<ProductDto>> getAllProducts(
            Integer page,
            Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(
                productService
                        .getAllProducts(pageable)
                        .getContent()
        );
    }

    @Override
    public ResponseEntity<ProductDto> getProductById(Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with id " + id)
                );
    }

    @Override
    public ResponseEntity<ProductDto> createProduct(
            ProductDto productDto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        productService.createProduct(productDto)
                );
    }

    @Override
    public ResponseEntity<Void> updateProduct(
            Long id,
            ProductDto productDto
    ) {
        productService.updateProduct(id, productDto);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> updateActiveDiscountProduct(
            Long id,
            Boolean active) {
        productService.updateActiveDiscountProduct(id, active);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> updatePriceProduct(
            Long id,
            BigDecimal price) {
        productService.updatePriceProduct(id, price);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteProduct(Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
}
