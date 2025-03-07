package com.stas_kozh.web.dto.mapper;

import com.stas_kozh.domain.model.Discount;
import com.stas_kozh.web.dto.DiscountDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DiscountMapper extends Mappable<Discount, DiscountDto> {

    @Override
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Discount toEntity(DiscountDto dto);

}
