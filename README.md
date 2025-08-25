# PhoenixGame

A simple game project built with Gradle and Groovy build scripts. The project is organized as a multi-module Gradle build with separate `Engine` and `Game` modules.

## Modules

- **Engine**: Core engine logic. Located at `Engine/src/main/com/absolutephoenix/engine`.
- **Game**: Game application entry point. Located at `Game/src/main/com/absolutephoenix/game` and depends on the Engine module.

## Building

Use the Gradle wrapper to build the project:

```bash
./gradlew build
```

> **Note:** The `gradle-wrapper.jar` binary is not included in this repository. Add it manually or run `gradle wrapper` to generate it before using `./gradlew`. Alternatively, you can invoke the system Gradle installation with `gradle build`.

This will compile all modules and run any tests.
