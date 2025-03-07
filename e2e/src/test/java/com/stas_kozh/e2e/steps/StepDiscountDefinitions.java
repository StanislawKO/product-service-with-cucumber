package com.stas_kozh.e2e.steps;

import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import org.openapitools.client.api.DiscountControllerApi;
import org.openapitools.client.model.DiscountDto;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class StepDiscountDefinitions {
    private final DiscountControllerApi discountApi = new DiscountControllerApi();
    private DiscountDto requestDiscountDto;
    private DiscountDto responseDiscountDto;
    private HttpStatusCode statusCode;
    private ResponseEntity<List<DiscountDto>> responseEntity;

    @Given("the discount service is available")
    public void theDiscountServiceIsAvailable() {
        RestAssured.baseURI = System.getProperty("app.url", "http://example:8888");
    }

    @When("I request a list of discounts")
    public void iRequestAListOfDiscounts() {
        responseEntity = discountApi.getAllDiscountsWithHttpInfo();
        statusCode = responseEntity.getStatusCode();
    }

    @When("I request a discount with ID {int}")
    public void iRequestADiscountWithId(int id) {
        ResponseEntity<DiscountDto> withHttpInfo = discountApi.getDiscountByIdWithHttpInfo((long) id);
        responseDiscountDto = withHttpInfo.getBody();
        statusCode = withHttpInfo.getStatusCode();
    }

    @DataTableType
    public DiscountDto toDto(Map<String, String> entry) {
        DiscountDto discountDto = new DiscountDto();
        discountDto.setAmount(Integer.parseInt(entry.get("amount")));
        discountDto.setStartTime(OffsetDateTime.parse(entry.get("startTime")));
        discountDto.setEndTime(OffsetDateTime.parse(entry.get("endTime")));
        return discountDto;
    }

    @When("I create a new discount with the following details")
    public void iCreateANewDiscountWithTheFollowingDetails(List<DiscountDto> discountDtos) {
        requestDiscountDto = discountDtos.get(0);

        ResponseEntity<DiscountDto> withHttpInfo = discountApi.createDiscountWithHttpInfo(requestDiscountDto);
        responseDiscountDto = withHttpInfo.getBody();
        statusCode = withHttpInfo.getStatusCode();
    }

    @When("I update the discount with ID {int} with the following details")
    public void iUpdateTheDiscountWithIdWithTheFollowingDetails(int id, List<DiscountDto> discountDtos) {
        requestDiscountDto = discountDtos.get(0);

        ResponseEntity<Void> withHttpInfo = discountApi.updateDiscountWithHttpInfo((long) id, requestDiscountDto);
        statusCode = withHttpInfo.getStatusCode();
    }

    @When("I delete the discount with ID {int}")
    public void iDeleteTheDiscountWithId(int id) {
        ResponseEntity<Void> withHttpInfo = discountApi.deleteDiscountWithHttpInfo((long) id);
        statusCode = withHttpInfo.getStatusCode();
    }

    @Then("the response status discount should be {int}")
    public void theResponseStatusShouldBe(int statusCodeValue) {
        assertThat(statusCode.value()).isEqualTo(statusCodeValue);
    }

    @Then("the response should contain a list of discounts")
    public void theResponseShouldContainAListOfDiscounts() {
        List<DiscountDto> discounts = responseEntity.getBody();
        assertThat(discounts).isNotNull();
        assertThat(discounts.size()).isGreaterThan(0);
    }

    @Then("the response should contain the created discount")
    public void theResponseShouldContainTheCreatedDiscount() {
        assertThat(responseDiscountDto.getId()).isNotNull();
        assertThat(responseDiscountDto.getAmount()).isEqualTo(requestDiscountDto.getAmount());
        assertThat(responseDiscountDto.getStartTime()).isEqualTo(requestDiscountDto.getStartTime());
        assertThat(responseDiscountDto.getEndTime()).isEqualTo(requestDiscountDto.getEndTime());
    }

    @Then("the response should contain the discount with ID {int}")
    public void theResponseShouldContainTheDiscountWithId(int id) {
        assertThat(responseDiscountDto).isNotNull();
        assertThat(responseDiscountDto.getId()).isEqualTo(id);
    }

}
