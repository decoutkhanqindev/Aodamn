---
name: unit-testing
description: How Aodamn is tested, layer by layer. JVM unit tests (Prefs JSON parse, Prefs / cache → UiModel / domain mappers, enum ids, pure rules, ViewModel Intent → State), Compose @Preview render with the Android CLI to compare UI before / after without a device, on-device theme tests (androidTest + Dropshots via check_theme.sh) and static checks. Use when writing or updating tests, adding logic a unit test can reach, changing UI that needs a before / after image, or deciding which test layer a change needs.
---

# Testing Aodamn

`CLAUDE.md` is the source of truth; when this skill disagrees, follow `CLAUDE.md`. Do the work yourself, no agents
(CLAUDE.md › Skills và agent).

## Which layer a change needs

| Change | Run | Where |
|---|---|---|
| Any change | `./gradlew :app:compileDebugKotlin` + `bash tools/check-rules.sh` (0 vi phạm) | static |
| Logic a JVM test can reach: parse, mapper, enum id, rule, pure helper, Intent → State | write the test in the same change, `./gradlew :app:testDebugUnitTest` must pass | `app/src/test/…`, same package as the source |
| API that androidTest uses (`DataStoreManager`, theme config helpers, `ThemeExpectation`) | `./gradlew :app:compileDebugAndroidTestKotlin` | `app/src/androidTest/…/testing/` |
| Compose UI | render the touched `@Preview`s before and after, look at both | Android CLI, section 2 |
| AOD theme or shared block drawing | `check_theme.sh` fast, then `--device` on an idle emulator | section 3 |
| Release gate | `lint`, every unit test, full device suite | not per change |

Real-device checks (Xiaomi permission pages, lock screen, sensors) are the user's: stop at build + tests and hand over the
commands, never `adb install` on their phone.

## 1. JVM unit tests

- JUnit 4 + `kotlinx-coroutines-test`. MockK / Turbine are not dependencies (adding them needs the user's OK): hand fakes
  at the Repository interface, no mocks of UseCases or final classes.
- No comments in test code; the backtick name says the behaviour.
- Patterns and per-area case tables: `references/test-patterns.md`. Edge-case prompts: `references/edge-case-discovery.md`.
- Gaps: `python scripts/analyze_kotlin.py units <file.kt>` · `python scripts/analyze_kotlin.py coverage <src_dir> <test_dir>`.
- Extend the suite that owns the area instead of starting a parallel one:

| Area | Suite |
|---|---|
| Every DataStore group (`XxxPrefs`): defaults, round trip, broken JSON | `data/local/datastore/PrefsJsonTest` |
| Prefs → UiModel, stored enum ids, writes back to Prefs | `presentation/model/aod/settings/AodSettingsUiMapperTest` |
| Schedule / charging / battery rules | `presentation/model/aod/settings/rules/AodRulesUiModelTest` |
| Theme config stored in `AodAppearancePrefs`, components | `presentation/model/aod/theme/AodAppearanceMappingTest` |
| Theme specs, options, contract, landscape plan | `AodThemeSpecTest`, `AodThemeContractTest`, `AodColorOptionTest`, `AodZoneOptionTest`, `AodLandscapePlanTest` |
| Every theme option / pair / reset / storage (JVM side of the device harness) | `testing/AodThemeExpectationTest` |
| Theme geometry helpers | `presentation/components/aod/theme/<theme>/*MetricsTest` |
| Weather cache → domain | `data/mapper/WeatherCacheMapperTest` |

Code that touches Android (DataStore, Context, Manager) is split so the pure part is testable: `PrefsJson.decodeOrNull`,
`XxxPrefs.toUiModel()`, `AodInteractionPrefs.withAction`, `AodAppearancePrefs.withThemeConfig`. A ViewModel that needs a
Manager is a concrete Android dependency: move its rule into the UiModel / Prefs helper and test it there.

## 2. Compose preview render (Android CLI)

The `android` CLI builds the module and renders `@Preview` functions to PNG, optionally with a component-tree JSON, with
no device or emulator ([preview render](https://developer.android.com/tools/agents/android-cli/commands/preview_render),
[preview list](https://developer.android.com/tools/agents/android-cli/commands/preview_list),
[preview](https://developer.android.com/tools/agents/android-cli/commands/preview)). Checked with CLI 1.1.16717390
(`$ANDROID_HOME/cmdline-tools/latest/bin/android`), Nov 2026.

```bash
android preview list :app
android preview render :app --fqn=com.decoutkhanqindev.aodamn.presentation.screens.settings.SettingsContentKt.SettingsContentPreview --output=<dir>
android preview render :app --fqn=<fqn1> --fqn=<fqn2> --component-tree --output=<dir>
```

- `--fqn` is `<package>.<File>Kt.<PreviewFunction>`, repeatable. `list` shows `private` previews too (about 20 s here).
- Output: `<dir>/<package path>/<File>Kt/<Preview>_0.png`, plus `_0.json` with `--component-tree` (bounds, component
  names, source locations, semantics, parameters, modifiers). `--no-image` only works with `--component-tree`.
  `-f json` gives a machine-readable result.
- `--daemon` keeps the renderer up until 10 minutes of inactivity. Its process holds stdout open, so never pipe it
  (`| tail`, `| sed`): the command never returns. Run it unpiped and read the files under `--output`.
- Before / after: render into `<scratchpad>/previews/before`, make the change, render the same FQNs into `…/after`, open
  both PNGs with Read and compare; for spacing / size compare `bounds` in the two JSON trees. Renders are never committed.
- A state with no preview cannot be rendered: add a `private` preview for each state the change touches (Content
  previews wrap `Theme`; AOD screen previews do not, CLAUDE.md › Compose và theme).
- This is a look-and-compare step, not a regression suite: nothing is stored as a golden. Theme goldens live in Dropshots
  (section 3). Roborazzi / the screenshot-test plugin were tried and removed; adding one back needs the user's OK.
- Background: Compose previews ([docs](https://developer.android.com/develop/ui/compose/tooling/previews)); the Android Dev
  Summit post of 28 Oct 2026 reports agents using `android preview` finished UI tasks 77% faster with up to 3x fewer
  tokens ([blog](https://developer.android.com/blog/posts/android-dev-summit-26-developer-tools-performance)).

## 3. On device (androidTest)

- Harness in `app/src/{debug,androidTest}/java/com/decoutkhanqindev/aodamn/testing/`: `ThemeExpectation` (debug source,
  read by JVM and device tests), `ThemeDeviceRule`, `ThemeTestActivity`, Dropshots screenshot tests, flow / editor /
  behaviour / motion tests.
- Run through `bash .claude/skills/aodamn-aod-theme-ref/scripts/check_theme.sh <pkg> "<Tên Việt>"` with `--device`,
  `--editor`, `--blocks`, `--record…` on an idle API 37 emulator; groups, pass stamps and record rules:
  `.claude/skills/aodamn-aod-theme-ref/references/testing.md`. Never call `connectedDebugAndroidTest` bare for one theme.
- Record goldens only after looking at the new images; a failing image is never recorded as the new truth.

## 4. Static

`compileDebugKotlin` and `check-rules.sh` after every change; `compileDebugAndroidTestKotlin` when a shared API moved;
`lint` at the release gate. A new rule from the user goes into CLAUDE.md plus a grep check in `tools/check-rules.sh`.
