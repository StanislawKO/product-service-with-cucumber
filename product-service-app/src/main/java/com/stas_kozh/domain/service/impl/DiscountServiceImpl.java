package com.stas_kozh.domain.service.impl;

import com.stas_kozh.domain.model.Discount;
import com.stas_kozh.domain.repository.DiscountRepository;
import com.stas_kozh.domain.service.DiscountService;
import com.stas_kozh.web.dto.DiscountDto;
import com.stas_kozh.web.dto.mapper.DiscountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRepository discountRepository;

    private final DiscountMapper discountMapper;

    @Transactional
    @Override
    public List<DiscountDto> getAllDiscounts() {
        return discountRepository.findAll().stream()
                .map(discountMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public Optional<DiscountDto> getDiscountById(Long id) {
        return discountRepository.findById(id)
                .map(discountMapper::toDto);
    }

    @Transactional
    @Override
    public DiscountDto createDiscount(DiscountDto discountDto) {
        return Optional.ofNullable(discountDto)
                .map(discountMapper::toEntity)
                .map(discountRepository::saveAndFlush)
                .map(discountMapper::toDto)
                .orElseThrow(() -> new IllegalStateException("Error save discount"));
    }

    @Transactional
    @Override
    public void updateDiscount(Long id, DiscountDto discountDto) {
        Discount discountUpdate = discountMapper.toEntity(discountDto);
        Discount discount = discountRepository.findById(id).orElseThrow();
        discount.setAmount(discountUpdate.getAmount());
        discount.setStartTime(discountUpdate.getStartTime());
        discount.setEndTime(discountUpdate.getEndTime());
        discountRepository.save(discount);
    }

    @Transactional
    @Override
    public void deleteDiscount(Long id) {
        discountRepository.deleteById(id);
    }

}

