package com.gallopdevs.athanhelper.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gallopdevs.athanhelper.R
import com.gallopdevs.athanhelper.settings.PreferencesManager
import com.gallopdevs.athanhelper.settings.PreferencesManagerImpl.Companion.ENABLE_NOTIFICATIONS

@Composable
private fun SettingsLabel(settingsIcon: Painter, settingsHeader: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            settingsIcon,
            contentDescription = settingsHeader,
            modifier = Modifier
                .size(60.dp)
                .padding(start = 30.dp, end = 15.dp)
        )
        Text(
            settingsHeader,
            modifier = Modifier.padding(top = 30.dp, bottom = 30.dp, end = 30.dp),
            color = colorResource(id = R.color.colorPrimaryDark)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsLabelPreview() {
    SettingsLabel(
        settingsIcon = painterResource(id = R.drawable.bell_icon),
        settingsHeader = stringResource(id = R.string.notifications)
    )
}

@Composable
fun NotificationsOption(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        SettingsLabel(
            settingsIcon = painterResource(id = R.drawable.bell_icon),
            settingsHeader = stringResource(id = R.string.notifications)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier
                .padding(end = 30.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationsOptionPreview() {
    NotificationsOption(checked = false, onCheckedChange = {})
}

data class ExpandableItem(
    val title: String,
    val drawableId: Int,
    var isExpanded: Boolean = false
)

@Composable
fun ExpandableListItem(item: ExpandableItem) {
    var isExpanded by remember { mutableStateOf(item.isExpanded) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.clickable {
                isExpanded = !isExpanded
            }
        ) {
            SettingsLabel(
                settingsIcon = painterResource(id = item.drawableId),
                settingsHeader = item.title
            )
            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = MaterialTheme.colors.primary
            )
        }

        if (isExpanded) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Additional content goes here...")
        }
    }
}

@Composable
fun ExpandableList(items: List<ExpandableItem>) {
    LazyColumn {
        items(items.size) { index ->
            ExpandableListItem(item = items[index])
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExpandableListPreview() {
    val expandableItems = listOf(
        ExpandableItem(
            title = stringResource(id = R.string.calculation_method),
            drawableId = R.drawable.sum_icon
        ),
        ExpandableItem(
            title = stringResource(id = R.string.asr_method),
            drawableId = R.drawable.sun_icon
        ),
        ExpandableItem(
            title = stringResource(id = R.string.latitudes_method),
            drawableId = R.drawable.compass_icon
        )
    )
    ExpandableList(items = expandableItems)
}

@Composable
fun SettingsScreen(preferencesManager: PreferencesManager) {
    var isChecked by remember {
        mutableStateOf(preferencesManager.getData(ENABLE_NOTIFICATIONS, false))
    }
    val expandableItems = listOf(
        ExpandableItem(
            title = stringResource(id = R.string.calculation_method),
            drawableId = R.drawable.sum_icon
        ),
        ExpandableItem(
            title = stringResource(id = R.string.asr_method),
            drawableId = R.drawable.sun_icon
        ),
        ExpandableItem(
            title = stringResource(id = R.string.latitudes_method),
            drawableId = R.drawable.compass_icon
        )
    )
    Column {
        NotificationsOption(
            checked = isChecked,
            onCheckedChange = { enableNotifications ->
                isChecked = enableNotifications
                preferencesManager.saveData(ENABLE_NOTIFICATIONS, enableNotifications)
            }
        )
        ExpandableList(items = expandableItems)
    }
}
