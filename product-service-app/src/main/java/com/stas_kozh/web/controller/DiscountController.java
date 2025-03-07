package com.stas_kozh.web.controller;

import com.stas_kozh.domain.exception.ResourceNotFoundException;
import com.stas_kozh.domain.service.DiscountService;
import com.stas_kozh.web.dto.DiscountDto;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.DiscountsServiceApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class DiscountController implements DiscountsServiceApi {

    private final DiscountService discountService;

    @Override
    public ResponseEntity<List<DiscountDto>> getAllDiscounts() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(discountService.getAllDiscounts());
    }

    @Override
    public ResponseEntity<DiscountDto> getDiscountById(
            Long id
    ) {
        return discountService.getDiscountById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Discount not found with id " + id)
                );
    }

    @Override
    public ResponseEntity<DiscountDto> createDiscount(
            DiscountDto discountDto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        discountService.createDiscount(discountDto)
                );
    }

    @Override
    public ResponseEntity<Void> updateDiscount(
            Long id,
            DiscountDto discountDto) {
        discountService.updateDiscount(id, discountDto);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteDiscount(Long id) {
        discountService.deleteDiscount(id);
        return ResponseEntity.ok().build();
    }
}
