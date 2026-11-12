# My Fitness Pal

[![Pipeline status](https://gitlab.com/TeodorIliev06/MyFitnessPal2026/badges/main/pipeline.svg)](https://gitlab.com/TeodorIliev06/MyFitnessPal2026/-/pipelines/latest)
[![Coverage report](https://gitlab.com/TeodorIliev06/MyFitnessPal2026/badges/main/coverage.svg)](https://gitlab.com/TeodorIliev06/MyFitnessPal2026/-/pipelines/latest)

A command-line fitness and nutrition tracker built in **Java 17** for the **Clean Code** course at Sofia University, Faculty of Mathematics and Informatics.

The application manages food intake, recipes, meals, water consumption, exercises, and calorie goals. It provides daily and weekly summaries, charts, multiple user profiles, and optional JSON-based persistence.

The project was developed incrementally through course assignments, with changes submitted for lecturer review. Its focus is not just functionality, but also modular design, testability, and maintainable code.

## Features

### Nutrition and meals
- Create and manage foods, meals, and recipes.
- Log food consumption by date and meal time.
- Calculate calories and macronutrients, including nutrition per recipe serving.
- Review daily food logs and daily or weekly nutrition summaries.
- View nutrient breakdowns in pie charts.

### Fitness and hydration
- Create cardio and strength exercises and group exercises into workouts.
- Log exercises and review daily activity or weekly cardio summaries.
- Record and manage water intake.
- Set a daily calorie goal and compare it against consumption using a bar chart.

### Users and data management
- Create user profiles and switch between users; the application starts with a guest session.
- Store user-specific nutrition and hydration records.
- Support in-memory operation and file-backed JSON storage through separate implementations.
- Import or export supported user data on demand.
- Query weekly data by an explicit week number and year, or by a calendar date.

## Tech stack

| Area | Technologies |
| --- | --- |
| Language / build | Java 17, Gradle |
| Tests | JUnit 5, Mockito |
| Quality checks | Checkstyle, JaCoCo |
| Data storage | JSON, Jackson, file-based repositories |
| Visualization | XChart |
| CI | GitLab CI |

## Getting started

### Requirements

- **JDK 17 or later**
- A terminal (the Gradle Wrapper is included, so a separate Gradle installation is not required)

### Run

```bash
git clone https://github.com/TeodorIliev06/my-fitness-pal.git
cd my-fitness-pal
./gradlew run
```

On Windows PowerShell or Command Prompt, use `.\gradlew.bat run` instead.

The program starts an interactive CLI. Enter `help` to see available commands and `exit` to close it.

### Example commands

These illustrate the CLI syntax; user-provided identifiers and values should be adapted to your data.

```text
help
create-food Example Oats 100 389 7 17 66
add-food today breakfast Example Oats 1
show-weekly-calories 46 2026
add-water today 250
show-daily-food-log today
exit
```

The date format is `dd.MM.yyyy`, with `today` also accepted. Weekly summaries accept either a date or a week number followed by a year (for example, `46 2026`). Use `help` for a full list of commands and expected parameters.

## Design and architecture

The source code separates interactive command handling from domain logic and data storage.

- **Command-driven CLI:** Commands implement shared contracts and are resolved through `ExecutableCommandRegistry`, `CommandParser`, and `CommandExecutor`. Individual features live in dedicated command classes.
- **Domain model:** Separate packages represent food, recipes, nutrition summaries, exercises, workouts, water entries, users, and calorie goals.
- **Persistence abstraction:** Interfaces, factories, DTOs, and mappers separate application data models from JSON serialization and file access. In-memory implementations are also available.
- **User-aware storage:** Session switching connects the active user to their corresponding food and water diaries.
- **Visualization abstraction:** Chart interfaces allow reporting commands to build chart data without depending directly on window implementations.

Key directories:

```text
src/
├── main/java/com/fmi/myfitnesspal/
│   ├── cli/           # Application entry point and interactive menu
│   ├── command/       # Command parsing, registration, and execution
│   ├── food/          # Foods, meals, recipes, and nutrition tracking
│   ├── exercise/      # Exercises, workouts, and activity logs
│   ├── water/         # Hydration tracking
│   ├── calorie/       # Calorie goals
│   ├── user/          # Profiles and sessions
│   ├── persistence/   # Repositories, DTOs, mapping, and JSON storage
│   └── chart/         # Chart interfaces and data structures
└── test/java/com/fmi/myfitnesspal/
    └── ...            # Tests for commands, domain logic, and persistence
```

## Testing and code quality

Run the unit tests:

```bash
./gradlew test
```

Run Checkstyle for the application and tests:

```bash
./gradlew checkstyleMain checkstyleTest
```

Generate a JaCoCo HTML report and verify configured coverage thresholds:

```bash
./gradlew test jacocoTestReport jacocoTestCoverageVerification
```

The HTML report is generated at `build/reports/jacoco/test/html/index.html`.

JaCoCo verification is **configured** to require at least **80% line coverage and 80% branch coverage** for included classes. The configuration excludes the `Main` class and the `org/external` package. These are build thresholds, not a claim about the current measured coverage.

The GitLab CI pipeline contains jobs for compilation, unit tests, Checkstyle, and JaCoCo reporting/verification. Test and quality reports are published as job artifacts.

## Course context

This repository was developed for the **Clean Code** course at **Sofia University (FMI)**. The application evolved across weekly assignments: functionality was extended, earlier behavior was corrected when requirements changed, and code was refined through lecturer-reviewed merge requests.

The project illustrates practical experience with object-oriented modeling, modular command handling, persistence boundaries, automated testing, and iterative code review.
