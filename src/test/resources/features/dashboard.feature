@dashboard @regression
Feature: Dashboard

  Background:
    Given the user is on the login page
    When the user logs in with username "John Smith" and password "12345"

  @smoke
  Scenario: Dashboard is displayed after login
    Then the dashboard should be displayed
    And the greeting should contain "John"
