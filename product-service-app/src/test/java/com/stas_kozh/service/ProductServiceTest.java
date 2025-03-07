package com.stas_kozh.service;

import com.stas_kozh.domain.model.Discount;
import com.stas_kozh.domain.repository.DiscountRepository;
import com.stas_kozh.domain.service.ProductService;
import com.stas_kozh.utils.DiscountBuilder;
import com.stas_kozh.web.dto.ProductDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Transactional
public class ProductServiceTest {

    @Autowired
    private ProductService productService;
    @Autowired
    private DiscountRepository discountRepository;

    static Discount discount;

    @BeforeAll
    public void setUpBeforeClass() {
        discount = discountRepository
                .save(DiscountBuilder.buildDiscount());
    }

    @Test
    public void createProduct_ReturnNotNull() {

        // given
        ProductDto productDto = getProductDto();

        // when
        ProductDto productSaved = productService.createProduct(productDto);

        // then
        assertThat(productSaved).isNotNull();
    }

    @Test
    public void updateProduct_ReturnNotNull() {

        // given
        ProductDto productDto = getProductDto();
        ProductDto productDtoUpdated = productService.createProduct(productDto);

        // when
        productDto.setDuration((short) 10);
        productService.updateProduct(productDtoUpdated.getId(), productDto);

        // then
        ProductDto productDtoSaved = productService.getProductById(productDtoUpdated.getId()).get();
        assertThat(productDtoSaved).isNotNull();
        assertThat(productDtoSaved.getDuration()).isEqualTo(productDto.getDuration());
    }

    @Test
    public void deleteProduct_ReturnNotNull() {

        // given
        ProductDto productDto = getProductDto();
        ProductDto productDtoUpdated = productService.createProduct(productDto);

        // when
        productService.deleteProduct(productDtoUpdated.getId());

        // then
        ProductDto productDtoSaved = productService.getProductById(productDtoUpdated.getId()).orElse(null);
        assertThat(productDtoSaved).isNull();
    }

    @Test
    public void getAllProducts_ReturnNotNull() {
        Pageable pageable = PageRequest.of(0, 25);

        // given
        ProductDto productDto = getProductDto();
        productService.createProduct(productDto);
        productService.createProduct(productDto);

        // when
        Page<ProductDto> productDtoList = productService.getAllProducts(pageable);

        // then
        assertThat(productDtoList.getTotalElements()).isEqualTo(2);
        assertThat(productDtoList.getContent().get(0).getId()).isNotEqualTo(productDtoList.getContent().get(1).getId());
    }

    @Test
    public void updatePrice_ReturnUpdatedPrice() {

        // given
        ProductDto productDto = getProductDto();
        ProductDto productDtoUpdated = productService.createProduct(productDto);

        // when
        productService.updatePriceProduct(productDtoUpdated.getId(), BigDecimal.valueOf(1));

        // then
        Optional<ProductDto> productDtoSaved = productService.getProductById(productDtoUpdated.getId());
        assertThat(productDtoSaved.get().getPrice()).isNotEqualByComparingTo(productDto.getPrice());
    }

    private static ProductDto getProductDto() {
        return ProductDto.builder()
                .summary("Java")
                .description("Course")
                .price(BigDecimal.valueOf(45))
                .discountId(discount.getId())
                .duration((short) 5)
                .active(true)
                .build();
    }
}
