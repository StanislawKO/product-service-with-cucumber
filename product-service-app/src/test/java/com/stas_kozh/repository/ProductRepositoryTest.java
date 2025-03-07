package com.stas_kozh.repository;

import com.stas_kozh.domain.model.Discount;
import com.stas_kozh.domain.model.Product;
import com.stas_kozh.domain.repository.DiscountRepository;
import com.stas_kozh.domain.repository.ProductRepository;
import com.stas_kozh.utils.DiscountBuilder;
import com.stas_kozh.utils.ProductBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Transactional
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private DiscountRepository discountRepository;

    static Discount discount;

    @BeforeAll
    public void setUpBeforeClass() {
        discount = discountRepository.save(DiscountBuilder.buildDiscount());
    }

    @Test
    public void save_ReturnsSave() {

        // given
        Product product = ProductBuilder.buildProduct(discount);

        // when
        Product productSaved = productRepository.save(product);

        // then
        assertThat(productSaved).isNotNull();
    }

    @Test
    public void getById_ReturnsProduct() {

        // given
        Product product = ProductBuilder.buildProduct(discount);
        productRepository.save(product);

        // when
        Product productById = productRepository.findById(product.getId()).get();

        // then
        assertThat(productById).isNotNull();
    }

    @Test
    public void deleteById_ReturnsNull() {

        // given
        Product product = ProductBuilder.buildProduct(discount);
        productRepository.save(product);

        // when
        productRepository.deleteById(product.getId());

        // then
        Product productById = productRepository.findById(product.getId()).orElse(null);
        assertThat(productById).isNull();
    }

    @Test
    public void updatePrice_ReturnsUpdatedPrice() {

        // given
        Product product = ProductBuilder.buildProduct(discount);
        Product productSaved = productRepository.save(product);

        // when
        productRepository.updatePriceById(productSaved.getId(), BigDecimal.valueOf(55));

        // then
        Product productById = productRepository.findById(product.getId()).orElse(null);
        assertThat(productById).isNotNull();
        assertThat(productById.getPrice()).isEqualTo(BigDecimal.valueOf(55.00).setScale(2));
    }

    @Test
    public void updateActiveDiscountById_ReturnsUpdatedActive() {

        // given
        Product product = ProductBuilder.buildProduct(discount);
        productRepository.save(product);

        // when
        productRepository.updateActiveDiscountById(product.getId(), false);

        // then
        Product productById = productRepository.findById(product.getId()).orElse(null);
        assertThat(productById).isNotNull();
        assertThat(productById.isActive()).isFalse();
    }

}
