package com.stas_kozh.utils;

import com.stas_kozh.domain.model.Discount;
import com.stas_kozh.domain.model.Product;

import java.math.BigDecimal;
import java.time.Instant;

public class ProductBuilder {

    public static Product buildProduct(Discount discount) {

        return new Product(
                "Hello",
                "world",
                BigDecimal.valueOf(45),
                (short) 10,
                discount,
                true,
                Instant.now(),
                Instant.now());
    }
}
