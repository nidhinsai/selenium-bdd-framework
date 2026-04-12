@login
Feature: Login

  Background:
    Given the user is on the login page

  @smoke @positive
  Scenario: Successful login with valid credentials
    When the user logs in with username "John Smith" and password "12345"
    Then the greeting should contain "John"
    And the dashboard should be displayed

  @smoke @regression @parametrised
  Scenario Outline: Login with multiple valid users
    When the user logs in with username "<username>" and password "<password>"
    Then the greeting should contain "<expected_name>"

    Examples:
      | username    | password | expected_name |
      | John Smith  | 12345    | John          |
      | Jane Smith  | 12345    | Jane          |

  @negative @regression
  Scenario: Login fails with blank credentials
    When the user attempts login with username "" and password ""
    Then the login error message should contain "incorrect"

  @negative @regression
  Scenario: Login fails with wrong password
    When the user attempts login with username "John Smith" and password "wrong"
    Then the login error message should contain "incorrect"
