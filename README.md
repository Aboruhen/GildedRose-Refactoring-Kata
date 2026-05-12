# Gilded Rose — Java Refactoring (Tech Assessment)

This repository contains a Java solution to the [Gilded Rose Refactoring Kata](https://github.com/emilybache/GildedRose-Refactoring-Kata) submitted as a technical assessment.

## Task Summary

The original `GildedRose.updateQuality()` method contained a single deeply nested `if/else` chain covering all item types. The assessment required:

1. Refactoring the legacy code without altering the `Item` class (kata constraint).
2. Adding support for the new **Conjured** item category.
3. Writing a comprehensive test suite that covers all item types and edge cases.

See [GildedRoseRequirements.md](GildedRoseRequirements.md) for the full business specification.

---

## Solution Approach

The monolithic conditional logic was replaced with the **Strategy pattern**:

- `ItemUpdater` — a single-method interface (`void update(Item item)`).
- One strategy class per item type in the `strategy` sub-package: `NormalItemUpdater`, `AgedBrieUpdater`, `SulfurasUpdater`, `BackstagePassUpdater`, `ConjuredItemUpdater`.
- `ItemUpdaterFactory` — resolves the correct strategy via a `switch` expression at runtime. Item names are configurable via JVM system properties or environment variables so the system can adapt to different locales/tenants without recompilation.
- `GildedRose` — reduced to a pure orchestrator; contains no business logic.

This structure satisfies the **Open/Closed Principle**: adding a new item type requires only a new strategy class and a single factory registration — no existing class is modified.

---

## Documentation

| Document | Description |
|----------|-------------|
| [docs/Architecture.md](docs/Architecture.md) | Full architecture reference: module layout, domain model, class responsibilities, quality rules table, factory configuration, and test design |
| [GildedRoseRequirements.md](GildedRoseRequirements.md) | Original business requirements specification |

## Running the Project

All commands should be run from the `Java/` directory.

### Run tests

```bash
./gradlew test
```

### Run the text-test fixture (approval / golden-master)

```bash
./gradlew -q text
```

For a specific number of days (e.g. 10):

```bash
./gradlew -q text --args 10
```

---

## Test Coverage

Tests are in `GildedRoseTest` (JUnit 5), organised with `@Nested` classes — one per item type plus a `GlobalInvariants` group:

```
GildedRoseTest
├── NormalItem        — 7 tests: quality/sellIn decrement, floor boundary
├── AgedBrie          — 6 tests: quality increment, ceiling boundary
├── Sulfuras          — 3 tests: no-op invariants
├── BackstagePasses   — 8 tests (parametrised): tier boundaries, ceiling, post-concert drop
├── ConjuredItems     — 7 tests: double-degradation, floor boundary
└── GlobalInvariants  — 3 tests: quality never negative, never > 50, multi-item pass
```

`@ParameterizedTest` with `@CsvSource` is used for boundary value analysis at sellIn thresholds.

---

## About the Kata

The Gilded Rose kata was originally created by Terry Hughes and is widely used for practicing legacy code refactoring. This solution is based on the Java starting position provided by [Emily Bache's repository](https://github.com/emilybache/GildedRose-Refactoring-Kata).
