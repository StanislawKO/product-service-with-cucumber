package com.stas_kozh.controller;

import com.stas_kozh.domain.service.DiscountService;
import com.stas_kozh.web.dto.DiscountDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DiscountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DiscountService discountService;

    @Test
    public void createDiscountWithValidData_returnsResponseWithStatusOk() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.post("/discounts/v1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "amount": 10,
                          "startTime": "2024-07-05T00:00:00Z",
                          "endTime": "2024-08-20T00:00:00Z"
                        }
                        """);

        // when
        when(discountService.createDiscount(any(DiscountDto.class))).thenReturn(new DiscountDto());
        this.mockMvc.perform(requestBuilder)
                // then
                .andExpect(status().isCreated());
    }

    @Test
    public void getAllDiscounts_returnsResponseWithStatusOk() throws Exception {
        // given
        DiscountDto discount1 = new DiscountDto(1L, (short) 10, Instant.now(), Instant.now().plusSeconds(3600));
        DiscountDto discount2 = new DiscountDto(2L, (short) 20, Instant.now(), Instant.now().plusSeconds(7200));
        when(discountService.getAllDiscounts()).thenReturn(List.of(discount1, discount2));

        // when
        this.mockMvc.perform(MockMvcRequestBuilders.get("/discounts/v1"))
                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].amount").value(10))
                .andExpect(jsonPath("$[1].amount").value(20));
    }

    @Test
    public void getDiscountById_returnsResponseWithStatusOk() throws Exception {
        // given
        when(discountService.getDiscountById(1L)).thenReturn(Optional.of(new DiscountDto()));

        // when
        this.mockMvc.perform(MockMvcRequestBuilders.get("/discounts/v1/1"))
                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getDiscountById_returnsResponseWithStatusNotFound() throws Exception {
        // given
        when(discountService.getDiscountById(9L)).thenReturn(Optional.empty());

        // when
        this.mockMvc.perform(MockMvcRequestBuilders.get("/discounts/v1/9"))
                // then
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateDiscountWithValidData_returnsResponseWithStatusOk() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.put("/discounts/v1/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "amount": 15,
                          "startTime": "2024-02-01T00:00:00Z",
                          "endTime": "2024-03-15T00:00:00Z"
                        }
                        """);

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andExpect(status().isOk());

        verify(discountService).updateDiscount(any(Long.class), any(DiscountDto.class));
    }

    @Test
    public void deleteDiscount_returnsResponseWithStatusOk() throws Exception {
        // when
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/discounts/v1/1"))
                // then
                .andExpect(status().isOk());

        verify(discountService).deleteDiscount(1L);
    }
}

