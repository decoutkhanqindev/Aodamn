# Test Patterns (Aodamn)

JUnit 4 + `kotlinx-coroutines-test`. MockK / Turbine are not dependencies (adding them needs the user's OK), so write
hand fakes at the Repository interface. No comments in test code either (CLAUDE.md): the test name says what it checks.

## Class template

```kotlin
package com.decoutkhanqindev.aodamn.data.local.datastore

import kotlinx.serialization.KSerializer
import org.junit.Assert.assertEquals
import org.junit.Test

class PrefsJsonTest {

    @Test
    fun `empty json gives the defaults for every group`() {
        assertEquals(AodOptionsPrefs.DEFAULT, decode(AodOptionsPrefs.serializer(), "{}"))
    }

    private fun <T> decode(serializer: KSerializer<T>, raw: String): T =
        requireNotNull(PrefsJson.decodeOrNull(serializer, raw)) { raw }
}
```

- Name: backtick sentence of the behaviour (`stored gesture actions override defaults and unknown entries are skipped`).
- One behaviour per test; group several asserts only when they describe the same rule (all entries of one enum).
- Assert messages carry the case id when looping (`assertEquals(gesture.id, expected, ui.actionOf(gesture))`).

## DataStore JSON (`data/local/datastore`)

Settings have no domain model: each group is one `@Serializable XxxPrefs` with non-null defaults and `DEFAULT`, decoded
with the app's `PrefsJson`. Every group gets these cases in `PrefsJsonTest`:

| Case | Expectation |
|---|---|
| `"{}"` | equals `XxxPrefs.DEFAULT` |
| `DEFAULT` values | pinned literally in `defaults match the product decisions`; a changed default is a product decision, so the test changes on purpose |
| Non-default prefs → encode → decode | equals the original, every field |
| Only some fields stored | stored ones kept, the rest from `DEFAULT` |
| Unknown keys | ignored |
| `null` on a non-null field | the field's default (`coerceInputValues`) |
| Broken JSON, empty string, wrong type (`"bright"` for an Int, array for a map) | `PrefsJson.decodeOrNull` returns `null`; the manager then logs and uses `DEFAULT` |
| Encode | every field written (`encodeDefaults`), so a saved choice equal to today's default survives a later default change |

A new group adds: empty JSON → `DEFAULT`, the defaults pin, a round trip.

## Prefs → UiModel (`presentation/model`)

- UiModel defaults equal the mapped Prefs defaults: `assertEquals(AodOptionsPrefs.DEFAULT.toUiModel(), AodOptionsUiModel())`.
- Every field mapped: build Prefs with no default values and compare to the expected UiModel.
- Enums stored by `id`: pin the id list literally (`listOf("always", "plugged", "on_battery")`) so renaming an entry cannot
  change what is stored; `fromId(entry.id) == entry` for every entry; unknown and `null` ids fall back (`ALWAYS`) or are
  skipped (`fromIdOrNull` → the gesture's `defaultAction`).
- Writes back to Prefs (`withAction`, `withThemeConfig`, `withComponents`): the stored ids, other entries untouched, and a
  save → `toUiModel()` round trip for every entry.

## Cache / DTO → domain (`data/mapper`)

Business data from outside the app (weather, events) still maps to domain in `data/mapper`: incomplete input → `null` or
the documented fallback, optional fields defaulted (`WeatherCacheMapperTest`).

## Pure rules and helpers

- Boundary values: start included / end excluded, wrap past midnight, `null` inputs (`isPlugged = null`), equal bounds.
- Collections: keys complete after a merge (`AodGestureValue.entries.toSet()`), other entries untouched after an update.

## Theme storage round trip

Use `testing/StoredThemeConfig.kt` › `AodThemeConfig.storedAndRestored(theme)`: it goes through
`AodAppearancePrefs.DEFAULT.withThemeConfig` → JSON → `PrefsJson.decodeOrNull` → `themeConfigOf(theme)`, the same path
as the app.

## ViewModel (Intent → State / Effect)

Only ViewModels whose constructor takes UseCases can run on the JVM; fake the Repository under the real UseCase.

```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
class XxxViewModelTest {

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `select item updates state`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val viewModel = XxxViewModel(GetXxxUseCase(FakeXxxRepository()))

        viewModel.onIntent(XxxIntent.SelectItem(item))
        advanceUntilIdle()

        assertEquals(item, viewModel.state.value.selectedItem)
    }
}
```

Clustered screens send the full path (`CustomizeIntent.History.UndoChange`). Managers (`DataStoreManager`,
`PermissionManager`…) are concrete Android classes: a ViewModel that needs one is checked on device (`androidTest`), or
its rule moves into the UiModel / Prefs helper and is unit-tested there.
