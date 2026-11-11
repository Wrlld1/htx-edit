# Contributing

## Setup
We're using Java 21 and Maven (UTF-8). 

When you open the `text-editor` folder in IntelliJ, click **Reload All Maven Projects** in the Maven sidebar so it downloads JUnit 6.

Useful commands:
```bash
# Compile code
mvn test-compile

# Run unit tests
mvn test
```

## Pushing to `main`
Since we push directly to `main`, please stick to these rules so we don't break the build:

1. Run `mvn test` before committing. Make sure everything compiles and passes before you push.
2. Pull with rebase right before pushing to avoid messy merge commits:
   ```bash
   git pull --rebase origin main
   git push origin main
   ```
3. Commit your unit tests (`src/test/...`) together with your code (`src/main/...`).
4. Keep `target/`, `.idea/`, and `*.iml` files out of Git.

## Commit Messages
Use short prefixes for commits:

* `feat:` - new classes or patterns (e.g., `feat: add ProcessingOptions builder`)
* `fix:` - bug fixes
* `test:` - adding or updating tests
* `docs:` - README, UML diagrams, or report files
* `build:` - `pom.xml` or folder structure changes