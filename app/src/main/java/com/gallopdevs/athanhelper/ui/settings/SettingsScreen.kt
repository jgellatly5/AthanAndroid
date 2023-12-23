package com.gallopdevs.athanhelper.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import com.gallopdevs.athanhelper.R
import com.gallopdevs.athanhelper.settings.PreferencesManager
import com.gallopdevs.athanhelper.settings.PreferencesManagerImpl.Companion.SETTINGS

@Composable
fun SettingsScreen(preferencesManager: PreferencesManager) {
    var isChecked by remember {
        mutableStateOf(preferencesManager.getBoolean(SETTINGS, false))
    }
    val expandableItems = listOf(
        ExpandableItem(
            title = stringResource(id = R.string.calculation_method),
            drawableId = R.drawable.sum_icon,
            options = stringArrayResource(id = R.array.calculation_methods).toList()
        ),
        ExpandableItem(
            title = stringResource(id = R.string.asr_method),
            drawableId = R.drawable.sun_icon,
            options = stringArrayResource(id = R.array.asr_methods).toList()
        ),
        ExpandableItem(
            title = stringResource(id = R.string.latitudes_method),
            drawableId = R.drawable.compass_icon,
            options = stringArrayResource(id = R.array.latitudes_methods).toList()
        )
    )
    Column {
        NotificationsOption(
            checked = isChecked,
            onCheckedChange = { enableNotifications ->
                isChecked = enableNotifications
                preferencesManager.saveBoolean(SETTINGS, enableNotifications)
            }
        )
        ExpandableList(items = expandableItems)
    }
}
