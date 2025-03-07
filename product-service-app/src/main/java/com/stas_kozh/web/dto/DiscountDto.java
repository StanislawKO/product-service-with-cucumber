package com.stas_kozh.web.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiscountDto {

    private Long id;

    @Min(0)
    @Max(100)
    private Short amount;

    @NotNull
    private Instant startTime;

    @NotNull
    private Instant endTime;

}
