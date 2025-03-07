package com.stas_kozh.controller;

import com.stas_kozh.domain.service.ProductService;
import com.stas_kozh.web.dto.ProductDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
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
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    public void createProduct_returnsResponseWithStatusOk() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.post("/products/v1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "summary": "Valid summary",
                          "description": "Sample description",
                          "price": 100.00,
                          "duration": 30,
                          "active": true,
                          "discountId": 1,
                          "createdAt": "2024-07-05T00:00:00Z",
                          "updatedAt": "2024-08-20T00:00:00Z"
                        }
                        """);

        // when
        when(productService.createProduct(any(ProductDto.class))).thenReturn(new ProductDto());
        this.mockMvc.perform(requestBuilder)
                // then
                .andExpect(status().isCreated());
    }

    @Test
    public void createProductWithMissingSummary_returnsResponseWithStatusBadRequest() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.post("/products/v1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "description": "Sample description",
                          "price": 100.00,
                          "duration": 30,
                          "active": true,
                          "createdAt": "2024-07-05T00:00:00Z",
                          "updatedAt": "2024-08-20T00:00:00Z"
                        }
                        """);

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getAllProducts_returnsResponseWithStatusOk() throws Exception {
        // given
        Pageable pageable = PageRequest.of(0, 25);
        ProductDto product1 = new ProductDto(1L, "Product 1", "Description 1",
                new BigDecimal("100.00"), (short) 30, 1L, true);
        ProductDto product2 = new ProductDto(2L, "Product 2", "Description 2",
                new BigDecimal("150.00"), (short) 45, 2L, false);
        when(productService.getAllProducts(pageable)).thenReturn(new PageImpl<>(List.of(product1, product2)));

        // when
        this.mockMvc.perform(MockMvcRequestBuilders.get("/products/v1")
                        .param("page", "0")
                        .param("size", "25"))
                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].summary").value("Product 1"))
                .andExpect(jsonPath("$[0].description").value("Description 1"))
                .andExpect(jsonPath("$[0].price").value(100.00))
                .andExpect(jsonPath("$[1].summary").value("Product 2"))
                .andExpect(jsonPath("$[1].description").value("Description 2"))
                .andExpect(jsonPath("$[1].price").value(150.00));
    }

    @Test
    public void getProductById_returnsResponseWithStatusOk() throws Exception {
        // given
        when(productService.getProductById(1L)).thenReturn(Optional.of(new ProductDto()));

        // when
        this.mockMvc.perform(MockMvcRequestBuilders.get("/products/v1/1"))
                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getProductById_returnsResponseWithStatusNotFound() throws Exception {
        // given
        when(productService.getProductById(9L)).thenReturn(Optional.empty());

        // when
        this.mockMvc.perform(MockMvcRequestBuilders.get("/products/v1/9"))
                // then
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateProduct_returnsResponseWithStatusOk() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.put("/products/v1/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "summary": "Updated summary",
                          "description": "Updated description",
                          "price": 150.00,
                          "duration": 30,
                          "active": true,
                          "discountId": 1,
                          "createdAt": "2024-07-05T00:00:00Z",
                          "updatedAt": "2024-08-20T00:00:00Z"
                        }
                        """);

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andExpect(status().isOk());

        verify(productService).updateProduct(any(Long.class), any(ProductDto.class));
    }

    @Test
    public void deleteProduct_returnsResponseWithStatusOk() throws Exception {
        // when
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/products/v1/1"))
                // then
                .andExpect(status().isOk());

        verify(productService).deleteProduct(1L);
    }
}
