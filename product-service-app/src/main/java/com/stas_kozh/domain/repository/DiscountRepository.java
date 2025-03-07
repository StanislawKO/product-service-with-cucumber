package com.stas_kozh.domain.repository;

import com.stas_kozh.domain.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
}
