package com.decoutkhanqindev.aodamn.presentation.components.aod.editor

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.AppOptionRow
import com.decoutkhanqindev.aodamn.presentation.components.LocalTag
import com.decoutkhanqindev.aodamn.presentation.components.onClick
import com.decoutkhanqindev.aodamn.presentation.components.settings.AppSectionLabel
import com.decoutkhanqindev.aodamn.presentation.components.sheet.AppSheet
import com.decoutkhanqindev.aodamn.presentation.components.sheet.AppSheetToolHeader
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodZoneOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodZoneRegionValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodZoneUiModel
import com.decoutkhanqindev.aodamn.presentation.theme.BodyLarge
import com.decoutkhanqindev.aodamn.presentation.theme.BodyMedium
import com.decoutkhanqindev.aodamn.presentation.theme.Grey6E
import com.decoutkhanqindev.aodamn.presentation.theme.Grey9A
import com.decoutkhanqindev.aodamn.presentation.theme.GreyED
import com.decoutkhanqindev.aodamn.presentation.theme.LabelMedium
import com.decoutkhanqindev.aodamn.presentation.theme.Mint
import com.decoutkhanqindev.aodamn.presentation.theme.Neutral12
import com.decoutkhanqindev.aodamn.presentation.theme.NeutralVariant30
import com.decoutkhanqindev.aodamn.presentation.theme.RoundedCornerShape12dp
import com.decoutkhanqindev.aodamn.presentation.theme.RoundedCornerShape16dp
import com.decoutkhanqindev.aodamn.presentation.theme.TitleMedium
import com.decoutkhanqindev.aodamn.utils.collectCatching
import com.decoutkhanqindev.aodamn.utils.withContextCatching
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.util.TimeZone

@Composable
fun AodZonePicker(
    option: AodZoneOption,
    config: AodThemeConfig,
    onOptionChange: (key: String, raw: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isSheetShown by rememberSaveable { mutableStateOf(false) }
    val zoneId = config[option]
    val locale = LocalConfiguration.current.locales[0]
    val city = remember(zoneId, locale) { AodZoneUiModel.cityOf(zoneId, locale) }
    val offset = remember(zoneId) {
        AodZoneUiModel.offsetTextOf(TimeZone.getTimeZone(zoneId).getOffset(System.currentTimeMillis()) / 60_000)
    }

    Column(modifier = modifier) {
        AppSectionLabel(text = stringResource(option.labelRes))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape16dp)
                .background(Neutral12)
                .onClick { isSheetShown = true }
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = city,
                    color = GreyED,
                    maxLines = 1,
                    style = TitleMedium,
                )
                Text(
                    text = offset,
                    color = Grey9A,
                    style = BodyMedium,
                )
            }

            Icon(
                painter = painterResource(R.drawable.ic_caret_right),
                contentDescription = null,
                tint = GreyED,
            )
        }

        AodZoneSheet(
            isVisible = isSheetShown,
            selectedId = zoneId,
            onSelect = { id ->
                onOptionChange(option.key, option.encode(id))
                isSheetShown = false
            },
            onDismiss = { isSheetShown = false },
        )
    }
}

@Composable
private fun AodZoneSheet(
    isVisible: Boolean,
    selectedId: String,
    onSelect: (id: String) -> Unit,
    onDismiss: () -> Unit,
) {
    AppSheet(
        isVisible = isVisible,
        onDismissRequest = onDismiss,
    ) {
        AppSheetToolHeader(
            iconRes = R.drawable.ic_globe_hemisphere_west,
            title = stringResource(R.string.zone_picker_title),
        )
        ZoneList(selectedId = selectedId, onSelect = onSelect)
    }
}

@OptIn(ExperimentalFoundationApi::class, FlowPreview::class)
@Composable
private fun ZoneList(
    selectedId: String,
    onSelect: (id: String) -> Unit,
) {
    val locale = LocalConfiguration.current.locales[0]
    val tag = LocalTag.current
    val zones by produceState<ImmutableList<AodZoneUiModel>?>(initialValue = null, locale) {
        value = withContextCatching(
            context = Dispatchers.Default,
            block = { AodZoneUiModel.catalog(locale, System.currentTimeMillis()) },
            catch = { e ->
                Timber.tag(tag).e(e.stackTraceToString())
                persistentListOf()
            },
        )
    }
    val regionLabels = AodZoneRegionValue.entries.associateWith { region -> stringResource(region.labelRes) }
    val selectedLabel = stringResource(R.string.zone_selected)
    val emptyLabel = stringResource(R.string.zone_search_empty)
    val listHeight = with(LocalDensity.current) {
        (LocalWindowInfo.current.containerSize.height * 0.6f).toDp()
    }
    val queryState = rememberTextFieldState()
    var query by remember { mutableStateOf("") }

    LaunchedEffect(queryState) {
        snapshotFlow { queryState.text.toString() }
            .map { text -> AodZoneUiModel.searchKeyOf(text) }
            .debounce { key -> if (key.isEmpty()) 0L else 300L }
            .distinctUntilChanged()
            .collectCatching(
                block = { key -> query = key },
                catch = { e -> Timber.tag(tag).e(e.stackTraceToString()) },
            )
    }

    ZoneSearchField(
        state = queryState,
        modifier = Modifier.padding(bottom = 12.dp),
    )

    val zoneItems = zones
    if (zoneItems == null) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(listHeight),
        )
        return
    }

    val selected = remember(zoneItems, selectedId) { zoneItems.find { zone -> zone.id == selectedId } }
    val groups = remember(zoneItems, selectedId, query) {
        zoneItems
            .filter { zone -> if (query.isEmpty()) zone.id != selectedId else query in zone.searchKey }
            .groupBy { zone -> zone.region }
    }
    val listState = rememberLazyListState()

    LaunchedEffect(query) {
        listState.scrollToItem(0)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(listHeight)
            .selectableGroup(),
        state = listState,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        if (query.isEmpty() && selected != null) {
            stickyHeader(key = "selected", contentType = "region") {
                ZoneHeader(text = selectedLabel)
            }

            item(key = "selected_${selected.id}", contentType = "zone") {
                ZoneRow(zone = selected, isSelected = true, onSelect = onSelect)
            }
        }

        if (groups.isEmpty()) {
            item(key = "empty", contentType = "empty") {
                Text(
                    text = emptyLabel,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    color = Grey9A,
                    textAlign = TextAlign.Center,
                    style = BodyMedium,
                )
            }
        }

        groups.forEach { (region, items) ->
            stickyHeader(key = region.name, contentType = "region") {
                ZoneHeader(text = regionLabels.getValue(region))
            }

            items(items = items, key = { zone -> zone.id }, contentType = { "zone" }) { zone ->
                ZoneRow(zone = zone, isSelected = zone.id == selectedId, onSelect = onSelect)
            }
        }
    }
}

@Composable
private fun ZoneHeader(text: String) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .background(Neutral12)
            .padding(vertical = 8.dp),
        color = Grey9A,
        style = LabelMedium,
    )
}

@Composable
private fun ZoneRow(
    zone: AodZoneUiModel,
    isSelected: Boolean,
    onSelect: (id: String) -> Unit,
) {
    AppOptionRow(
        label = zone.city,
        isSelected = isSelected,
        onClick = { onSelect(zone.id) },
    ) {
        Text(
            text = zone.offsetText,
            modifier = Modifier.width(72.dp),
            color = Grey9A,
            style = LabelMedium,
        )
    }
}

@Composable
private fun ZoneSearchField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
) {
    BasicTextField(
        state = state,
        modifier = modifier.fillMaxWidth(),
        textStyle = BodyLarge.copy(color = GreyED),
        cursorBrush = SolidColor(Mint),
        keyboardOptions = KeyboardOptions(
            autoCorrectEnabled = false,
            imeAction = ImeAction.Search,
        ),
        lineLimits = TextFieldLineLimits.SingleLine,
        decorator = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .border(width = 1.dp, color = NeutralVariant30, shape = RoundedCornerShape12dp)
                    .padding(start = 14.dp, end = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_magnifying_glass),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Grey9A,
                )

                Box(modifier = Modifier.weight(1f)) {
                    if (state.text.isEmpty()) {
                        Text(
                            text = stringResource(R.string.zone_search_hint),
                            color = Grey6E,
                            maxLines = 1,
                            style = BodyLarge,
                        )
                    }

                    innerTextField()
                }

                AnimatedVisibility(
                    visible = state.text.isNotEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .onClick(shape = CircleShape, ripple = false) { state.clearText() },
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_x),
                            contentDescription = stringResource(R.string.zone_search_clear),
                            modifier = Modifier.size(18.dp),
                            tint = Grey9A,
                        )
                    }
                }
            }
        },
    )
}
