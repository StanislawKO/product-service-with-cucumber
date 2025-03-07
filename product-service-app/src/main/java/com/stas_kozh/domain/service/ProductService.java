package com.stas_kozh.domain.service;

import com.stas_kozh.web.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Optional;

public interface ProductService {

    Page<ProductDto> getAllProducts(Pageable pageable);

    ProductDto createProduct(ProductDto productDto);

    void updatePriceProduct(Long id, BigDecimal price);

    void updateProduct(Long id, ProductDto productDto);

    void updateActiveDiscountProduct(Long id, Boolean active);

    Optional<ProductDto> getProductById(Long id);

    void deleteProduct(Long id);

}
