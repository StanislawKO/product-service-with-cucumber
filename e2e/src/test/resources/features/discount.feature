Feature: Discount API

  Scenario: Get all discounts
    Given the discount service is available
    When I request a list of discounts
    Then the response status discount should be 200
    And the response should contain a list of discounts

  Scenario: Get discount by ID
    Given the discount service is available
    When I request a discount with ID 1
    Then the response status discount should be 200
    And the response should contain the discount with ID 1

  Scenario: Create a new discount
    Given the discount service is available
    When I create a new discount with the following details
      | amount | startTime           | endTime             |
      | 10     | 2024-09-01T00:00:00Z | 2024-09-30T23:59:59Z |
    Then the response status discount should be 201
    And the response should contain the created discount

  Scenario: Update a discount
    Given the discount service is available
    When I update the discount with ID 1 with the following details
      | amount | startTime           | endTime             |
      | 15     | 2024-10-01T00:00:00Z | 2024-10-31T23:59:59Z |
    Then the response status discount should be 200

  Scenario: Delete a discount
    Given the discount service is available
    When I delete the discount with ID 5
    Then the response status discount should be 200
