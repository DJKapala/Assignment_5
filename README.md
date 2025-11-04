# Assignment 5 – Testing and CI Automation

## Overview
SE 333 – Software Testing and Quality Assurance 
writing, organizing, and automating tests using JUnit 5, Mockito, Checkstyle, JaCoCo, and GitHub Actions.

### Part 1 – BarnesAndNoble Testing
- Implemented Specification-Based and Structural-Based unit tests for the `BarnesAndNoble` class.
- Verified behavior for valid, invalid, and edge cases in cart pricing.

### Part 2 – Continuous Integration (CI)
- Added GitHub Actions workflow:  
  `.github/workflows/SE333_CI.yml`
- CI Pipeline includes:
    - Checkstyle Analysis during Maven validate phase
    - JUnit 5 Tests with coverage reporting via JaCoCo
    - Artifact Uploads for both `checkstyle.xml` and `jacoco.xml`

- Build Status:  
  ![Build Status](https://github.com/DJKapala/Assignment_5/actions/workflows/SE333_CI.yml/badge.svg)

- The workflow runs on every push to `main` and all steps pass.

###  Part 3 – Amazon Testing
- Created Unit Tests (`AmazonUnitTest.java`) using Mockito to isolate dependencies.
    - Tested interaction between `Amazon` and mocked `ShoppingCart` / `PriceRule`.
- Created Integration Tests (`AmazonIntegrationTest.java`) to validate full behavior with a database.
- Confirmed both test suites execute successfully in GitHub Actions.


---

## Workflow
All workflow steps have completed in the most recent GitHub Actions run.  
You can view the workflow results here:  
 [View Workflow on GitHub](https://github.com/YourGitHubUsername/Assignment_5/actions/workflows/SE333_CI.yml)

---