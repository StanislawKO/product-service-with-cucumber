package com.stas_kozh.service;

import com.stas_kozh.domain.repository.DiscountRepository;
import com.stas_kozh.domain.service.DiscountService;
import com.stas_kozh.web.dto.DiscountDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class DiscountServiceTest {

    @Autowired
    private DiscountService discountService;

    @Autowired
    private DiscountRepository  discountRepository;

    @BeforeEach
    void setUp() {
        discountRepository.deleteAll();
    }

    @Test
    public void createDiscount_ReturnNotNull() {

        // given
        DiscountDto discountDto = getDiscountDto();

        // when
        DiscountDto discountSaved = discountService.createDiscount(discountDto);

        // then
        assertThat(discountSaved).isNotNull();
    }

    @Test
    public void updateDiscount_ReturnNotNull() {

        // given
        DiscountDto discountDto = getDiscountDto();
        DiscountDto discountDtoUpdated = discountService.createDiscount(discountDto);

        // when
        discountDto.setAmount((short) 10);
        discountService.updateDiscount(discountDtoUpdated.getId(), discountDto);

        // then
        DiscountDto discountDtoSaved = discountService.getDiscountById(discountDtoUpdated.getId()).get();
        assertThat(discountDtoSaved).isNotNull();
        assertThat(discountDtoSaved.getAmount()).isEqualTo(discountDto.getAmount());
    }

    @Test
    public void deleteDiscount_ReturnNull() {

        // given
        DiscountDto discountDto = getDiscountDto();
        DiscountDto discountDtoUpdated = discountService.createDiscount(discountDto);

        // when
        discountService.deleteDiscount(discountDtoUpdated.getId());

        // then
        DiscountDto discountDtoSaved = discountService.getDiscountById(discountDtoUpdated.getId()).orElse(null);
        assertThat(discountDtoSaved).isNull();
    }

    @Test
    public void getAllDiscounts_ReturnNotNull() {

        // given
        DiscountDto discountDto = getDiscountDto();
        discountService.createDiscount(discountDto);
        discountService.createDiscount(discountDto);

        // when
        List<DiscountDto> discountDtoList = discountService.getAllDiscounts();

        // then
        assertThat(discountDtoList.size()).isEqualTo(2);
        assertThat(discountDtoList.get(0).getId()).isNotEqualTo(discountDtoList.get(1).getId());
    }

    private static DiscountDto getDiscountDto() {
        return DiscountDto.builder()
                .amount((short) 30)
                .startTime(Instant.now())
                .endTime(Instant.now())
                .build();
    }

}
