package com.stas_kozh.domain.service;


import com.stas_kozh.web.dto.DiscountDto;

import java.util.List;
import java.util.Optional;

public interface DiscountService {

    List<DiscountDto> getAllDiscounts();

    DiscountDto createDiscount(DiscountDto discountDto);

    void updateDiscount(Long id, DiscountDto discountDto);

    Optional<DiscountDto> getDiscountById(Long id);

    void deleteDiscount(Long id);

}
