package com.stas_kozh.utils;

import com.stas_kozh.domain.model.Discount;

import java.time.Instant;

public class DiscountBuilder {

    public static Discount buildDiscount() {
        return new Discount(
                (short) 1,
                Instant.now(),
                Instant.now(),
                Instant.now(),
                Instant.now());
    }

}
