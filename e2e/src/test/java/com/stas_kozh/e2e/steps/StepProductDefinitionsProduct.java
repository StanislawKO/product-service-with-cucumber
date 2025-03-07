package com.stas_kozh.e2e.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import org.openapitools.client.api.ProductControllerApi;
import org.openapitools.client.model.ProductDto;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class StepProductDefinitionsProduct {
    private final ProductControllerApi productApi = new ProductControllerApi();
    private ProductDto requestProductDto;
    private ProductDto responseProductDto;
    private HttpStatusCode statusCode;
    private ResponseEntity<List<ProductDto>> responseEntity;

    @Given("the product service is available")
    public void theProductServiceIsAvailable() {
        RestAssured.baseURI = System.getProperty("app.url", "http://example:8888");
    }

    @When("I request a list of products with page {int} and size {int}")
    public void iRequestAListOfProductsWithPageAndSize(int page, int size) {
        responseEntity = productApi.getAllProductsWithHttpInfo(page, size);
        statusCode = responseEntity.getStatusCode();
    }

    @When("I create a new product with the following details")
    public void iCreateANewProductWithTheFollowingDetails(Map<String, String> productData) {
        requestProductDto = new ProductDto();
        requestProductDto.setSummary(productData.get("summary"));
        requestProductDto.setDescription(productData.get("description"));
        requestProductDto.setPrice(new BigDecimal(productData.get("price")));
        requestProductDto.setDuration(Integer.valueOf(productData.get("duration")));
        requestProductDto.setDiscountId(Long.parseLong(productData.get("discountId")));
        requestProductDto.setActive(Boolean.parseBoolean(productData.get("active")));

        ResponseEntity<ProductDto> withHttpInfo = productApi.createProductWithHttpInfo(requestProductDto);
        responseProductDto = withHttpInfo.getBody();
        statusCode = withHttpInfo.getStatusCode();
    }

    @When("I update the product with ID {int} with the following details")
    public void iUpdateTheProductWithIdWithTheFollowingDetails(int id, Map<String, String> productData) {
        requestProductDto = new ProductDto();
        requestProductDto.setSummary(productData.get("summary"));
        requestProductDto.setDescription(productData.get("description"));
        requestProductDto.setPrice(new BigDecimal(productData.get("price")));
        requestProductDto.setDuration(Integer.valueOf(productData.get("duration")));
        requestProductDto.setDiscountId(Long.parseLong(productData.get("discountId")));
        requestProductDto.setActive(Boolean.parseBoolean(productData.get("active")));

        ResponseEntity<Void> withHttpInfo = productApi.updateProductWithHttpInfo((long) id, requestProductDto);
        statusCode = withHttpInfo.getStatusCode();
    }

    @When("I update the price of the product with ID {int} to {double}")
    public void iUpdateThePriceOfTheProductWithIdTo(int id, double price) {
        ResponseEntity<Void> withHttpInfo = productApi.updatePriceProductWithHttpInfo((long) id, new BigDecimal(price));
        statusCode = withHttpInfo.getStatusCode();
    }

    @When("I request a product with ID {int}")
    public void iRequestAProductWithId(int id) {
        ResponseEntity<ProductDto> withHttpInfo = productApi.getProductByIdWithHttpInfo((long) id);
        responseProductDto = withHttpInfo.getBody();
        statusCode = withHttpInfo.getStatusCode();
    }

    @When("I update the active discount of the product with ID {int} to {string}")
    public void iUpdateTheActiveDiscountOfTheProductWithIdTo(int id, String active) {
        ResponseEntity<Void> withHttpInfo = productApi
                .updateActiveDiscountProductWithHttpInfo((long) id, Boolean.valueOf(active));
        statusCode = withHttpInfo.getStatusCode();
    }

    @When("I delete the product with ID {int}")
    public void iDeleteTheProductWithId(int id) {
        ResponseEntity<Void> withHttpInfo = productApi.deleteProductWithHttpInfo((long) id);
        statusCode = withHttpInfo.getStatusCode();
    }

    @Then("the response status should be {int}")
    public void theResponseStatusShouldBe(int statusCodeValue) {
        assertThat(statusCode.value()).isEqualTo(statusCodeValue);
    }

    @Then("the response should contain a list of products")
    public void theResponseShouldContainAListOfProducts() {
        List<ProductDto> products = responseEntity.getBody();
        assertThat(products).isNotNull();
        assertThat(products.size()).isGreaterThan(0);
    }

    @Then("the response should contain the created product")
    public void theResponseShouldContainTheCreatedProduct() {
        assertThat(responseProductDto.getId()).isNotNull();
        assertThat(responseProductDto.getSummary()).isEqualTo(requestProductDto.getSummary());
        assertThat(responseProductDto.getDescription()).isEqualTo(requestProductDto.getDescription());
        assertThat(responseProductDto.getPrice()).isEqualTo(requestProductDto.getPrice());
        assertThat(responseProductDto.getDuration()).isEqualTo(requestProductDto.getDuration());
        assertThat(responseProductDto.getDiscountId()).isEqualTo(requestProductDto.getDiscountId());
        assertThat(responseProductDto.getActive()).isEqualTo(requestProductDto.getActive());
    }

    @Then("the response should contain the product with ID {int}")
    public void theResponseShouldContainTheProductWithId(int id) {
        assertThat(responseProductDto).isNotNull();
        assertThat(responseProductDto.getId()).isEqualTo(id);
    }
}
