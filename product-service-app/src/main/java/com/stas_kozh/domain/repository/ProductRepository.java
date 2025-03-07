package com.stas_kozh.domain.repository;

import com.stas_kozh.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Transactional
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Product p SET p.active = :active WHERE p.id = :id")
    void updateActiveDiscountById(Long id, boolean active);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE product p SET price = ?2 WHERE id = ?1", nativeQuery = true)
    void updatePriceById(Long id, BigDecimal price);

}
