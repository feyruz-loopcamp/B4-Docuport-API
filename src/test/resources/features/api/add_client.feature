@client
Feature: Add new client API and DB validation

  @db
  Scenario: Post new client and verify in database
    Given User logged in to Docuport api as "client" role
    When Users sends POST request to "/api/v1/document/users/me/clients" with following info:
      | clientType | 1           |
      | name       | $randomName |
      | firstName  | Loop        |
      | lastName   | Camp        |
    Then status code should be 200
    And Database should persist same client info