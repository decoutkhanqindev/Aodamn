package com.decoutkhanqindev.aodamn.presentation.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.theme.GreyED
import com.decoutkhanqindev.aodamn.presentation.theme.Theme

@Composable
fun AppTopBar(
    title: String,
    modifier: Modifier = Modifier,
    onNavigateBack: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    AppTopBar(
        title = { Text(text = title) },
        modifier = modifier,
        onNavigateBack = onNavigateBack,
        actions = actions,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onNavigateBack: (() -> Unit)? = null,
    isTitleCentered: Boolean = true,
    actions: @Composable RowScope.() -> Unit = {},
) {
    val navigationIcon = @Composable {
        if (onNavigateBack != null) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_left),
                    contentDescription = stringResource(R.string.action_back),
                    modifier = Modifier.size(26.dp),
                )
            }
        }
    }
    val colors = TopAppBarDefaults.topAppBarColors(
        containerColor = Color.Transparent,
        navigationIconContentColor = GreyED,
        titleContentColor = GreyED,
        actionIconContentColor = GreyED,
    )

    if (isTitleCentered) {
        CenterAlignedTopAppBar(
            title = title,
            modifier = modifier,
            navigationIcon = navigationIcon,
            actions = actions,
            colors = colors,
            expandedHeight = 68.dp,
        )
    } else {
        TopAppBar(
            title = title,
            modifier = modifier,
            navigationIcon = navigationIcon,
            actions = actions,
            colors = colors,
            expandedHeight = 68.dp,
        )
    }
}

@Preview(widthDp = 360)
@Composable
private fun AppTopBarPreview() {
    Theme {
        AppTopBar(
            title = stringResource(R.string.iap_title),
            onNavigateBack = {},
        )
    }
}
