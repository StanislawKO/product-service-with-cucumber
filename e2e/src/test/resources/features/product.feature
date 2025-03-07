Feature: Product API

  Scenario: Get all products
    Given the product service is available
    When I request a list of products with page 0 and size 25
    Then the response status should be 200
    And the response should contain a list of products

  Scenario: Get a product by ID
    Given the product service is available
    When I request a product with ID 1
    Then the response status should be 200
    And the response should contain the product with ID 1

  Scenario: Create a new product
    Given the product service is available
    When I create a new product with the following details
      | summary     | Test Product        |
      | description | This is a test      |
      | price       | 10.0                |
      | duration    | 30                  |
      | discountId  | 1                   |
      | active      | true                |
    Then the response status should be 201
    And the response should contain the created product

  Scenario: Update a product by ID
    Given the product service is available
    When I update the product with ID 1 with the following details
      | summary     | Updated Product     |
      | description | Updated description |
      | price       | 30.0                |
      | duration    | 90                  |
      | discountId  | 1                   |
      | active      | false               |
    Then the response status should be 200

  Scenario: Update product price by ID
    Given the product service is available
    When I update the price of the product with ID 1 to 59.99
    Then the response status should be 200


  Scenario: Update active discount of a product
    Given the product service is available
    When I update the active discount of the product with ID 1 to "true"
    Then the response status should be 200

  Scenario: Delete a product
    Given the product service is available
    When I delete the product with ID 5
    Then the response status should be 200
