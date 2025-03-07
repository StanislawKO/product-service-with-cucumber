package com.stas_kozh.web.dto.mapper;

import com.stas_kozh.domain.model.Product;
import com.stas_kozh.web.dto.ProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DiscountMapper.class})
public interface ProductMapper extends Mappable<Product, ProductDto> {

    @Override
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "discount.id", source = "discountId")
    Product toEntity(ProductDto dto);

    @Override
    @Mapping(target = "discountId", source = "discount.id")
    ProductDto toDto(Product entity);

}
