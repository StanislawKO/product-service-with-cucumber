package com.stas_kozh.domain.service.impl;

import com.stas_kozh.domain.exception.ResourceNotFoundException;
import com.stas_kozh.domain.model.Product;
import com.stas_kozh.domain.repository.ProductRepository;
import com.stas_kozh.domain.service.ProductService;
import com.stas_kozh.web.dto.ProductDto;
import com.stas_kozh.web.dto.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    @Transactional
    @Override
    public Page<ProductDto> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::toDto);
    }

    @Transactional
    @Override
    public Optional<ProductDto> getProductById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toDto);
    }

    @Transactional
    @Override
    public ProductDto createProduct(ProductDto productDto) {
        log.info("Saved product : {}", productDto);
        return Optional.ofNullable(productDto)
                .map(productMapper::toEntity)
                .map(productRepository::saveAndFlush)
                .map(productMapper::toDto)
                .orElseThrow(() -> new IllegalStateException("Error save product"));

    }


    @Transactional
    @Override
    public void updateActiveDiscountProduct(Long id, Boolean active) {
        productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        productRepository.updateActiveDiscountById(id, active);
        log.info("Update active={} product by id: {}", active, id);
    }

    @Transactional
    @Override
    public void updateProduct(Long id, ProductDto productDto) {
        Product productUpdate = productMapper.toEntity(productDto);
        Product product = productRepository.findById(id).orElseThrow();
        product.setDuration(productUpdate.getDuration());
        product.setDescription(productUpdate.getDescription());
        product.setPrice(productUpdate.getPrice());
        product.setActive(productUpdate.isActive());
        product.setSummary(productUpdate.getSummary());
        product.setDiscount(productUpdate.getDiscount());
        productRepository.save(product);
        log.info("Update product with ID: {}", productDto.getId());
    }

    @Transactional
    @Override
    public void updatePriceProduct(Long id, BigDecimal price) {
        productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id=" + id + " not found"));
        productRepository.updatePriceById(id, price);
        log.info("Update price={} product by id: {}", price, id);
    }

    @Transactional
    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
        log.info("Delete product with ID: {}", id);
    }

}
