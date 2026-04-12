Feature: Login

  @smoke
  Scenario: Successful login
    Given the user is on the login page
    When the user logs in with username "John Smith" and password "12345"
    Then the greeting should contain "John"