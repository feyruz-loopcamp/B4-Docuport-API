@smoke
Feature: Docuport API login verifications

  @DocuportApi
  Scenario: verify login with valid credentials
    Given User logged in to Docuport api as "advisor" role
    And User sends GET request to "/api/v1/identity/users" with query param "advisor" for EmailAddress
    Then status code should be 200
    And content type is "application/json; charset=utf-8"
    And role is "advisor"


  @ui #API -> UI
  Scenario: verify user details with ui and api
    Given User logged in to Docuport api as "advisor" role
    And User sends GET request to "/api/v1/identity/users" with query param "advisor" for EmailAddress
    And User logged in to Docuport app as "advisor" role
    When User goes to profile page
    Then User should see same info on UI and API