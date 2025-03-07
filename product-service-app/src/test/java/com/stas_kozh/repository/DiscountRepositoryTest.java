package com.stas_kozh.repository;

import com.stas_kozh.domain.model.Discount;
import com.stas_kozh.domain.repository.DiscountRepository;
import com.stas_kozh.utils.DiscountBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class DiscountRepositoryTest {

    @Autowired
    private DiscountRepository discountRepository;

    @Test
    public void saveById_returnSavedDiscount() {

        // given
        Discount discount = DiscountBuilder.buildDiscount();

        // when
        Discount savedDiscount = discountRepository.save(discount);

        // then
        assertThat(savedDiscount).isNotNull();
    }

    @Test
    public void findById_returnSavedDiscount() {

        // given
        Discount discount = DiscountBuilder.buildDiscount();
        discountRepository.save(discount);

        // when
        Discount savedDiscount = discountRepository.findById(discount.getId()).get();

        // then
        assertThat(savedDiscount).isNotNull();
    }

    @Test
    public void deleteById_returnDeletedDiscount() {

        // given
        Discount discount = DiscountBuilder.buildDiscount();
        discountRepository.save(discount);

        // when
        discountRepository.deleteById(discount.getId());

        // then
        Optional<Discount> deletedDiscount = discountRepository.findById(discount.getId());
        assertThat(deletedDiscount).isEmpty();
    }
}
