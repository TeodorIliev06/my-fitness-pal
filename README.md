[![pipeline status](https://gitlab.com/clean-code-fmi/MyFitnessPal2026/badges/main/pipeline.svg)](https://gitlab.com/clean-code-fmi/MyFitnessPal2026/-/pipelines/latest)
[![coverage report](https://gitlab.com/clean-code-fmi/MyFitnessPal2026/badges/main/coverage.svg)](https://gitlab.com/clean-code-fmi/MyFitnessPal2026/-/pipelines/latest)

# Intro
A simple project with java 17, gradle, junit and checkstyle. You only need JDK 17 or later to run the project.

## Run Checkstyle
```shell
.\gradlew checkstyleMain checkstyleTest
```

## Run unit tests
```shell
.\gradlew test
```

## Code coverage
```shell
.\gradlew jacocoTestReport jacocoTestCoverageVerification
```
After running the scripts, HTML code coverage report is created in `build/reports/jacoco/test/html/index.html`.

### Code coverage in CI
The `jacoco` job verifies that there is at least 80% branch coverage and 80% line coverage.
It produces the following reports:

#### HTML report
The HTML report is in the `jacoco` job's artifacts. It is the same as the one generated locally when running jacoco.

#### Line coverage in MR diff
![](https://us1.discourse-cdn.com/gitlab/original/3X/c/f/cf611804de660ec4ec97f6d79f24ce2c9e0c828e.png)