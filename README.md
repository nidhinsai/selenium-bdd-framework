# selenium-bdd-framework

Production-grade Selenium 4 + Cucumber BDD framework with Page Object Model, parallel execution, reusable utilities, and Jenkins integration.

## Stack

- Java 17
- Selenium 4
- Cucumber 7
- TestNG
- WebDriverManager
- Jenkins
- Log4j2

## Features

- Thread-safe `ThreadLocal` driver management
- Page Object Model structure
- Parallel execution with TestNG + Cucumber
- Environment-based configuration
- Screenshot capture on failure
- Ready for Jenkins pipelines

## Project Structure

```text
src/main/java/com/nidhinsai/framework
├── base
├── pages
└── utils

src/test/java/com/nidhinsai
├── hooks
├── runners
└── steps
```

## Run

```bash
mvn clean test
mvn clean test -Dbrowser=firefox -Dheadless=true
mvn clean test -DsuiteFile=testng-parallel.xml -Dthreads=4
```

## Tags

```bash
mvn clean test -Dcucumber.filter.tags="@smoke"
```

## Notes

- Update `config/qa.properties` with your application URL.
- Locators in sample page objects are intentionally generic placeholders.
- Jenkins pipeline publishes Cucumber JSON artifacts.