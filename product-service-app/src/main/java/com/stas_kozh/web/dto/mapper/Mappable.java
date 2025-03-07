package com.stas_kozh.web.dto.mapper;

public interface Mappable<E, D> {

    E toEntity(D dto);

    D toDto(E entity);

}
