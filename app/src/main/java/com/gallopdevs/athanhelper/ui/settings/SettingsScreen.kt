package com.gallopdevs.athanhelper.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.gallopdevs.athanhelper.R
import com.gallopdevs.athanhelper.ui.settings.PreferencesManagerImpl.Companion.ENABLE_NOTIFICATIONS
import com.gallopdevs.athanhelper.viewmodel.ClockViewModel

@Composable
fun SettingsScreen(
    clockViewModel: ClockViewModel = hiltViewModel()
) {
    var areNotificationsEnabled by remember {
        mutableStateOf(clockViewModel.getBoolean(ENABLE_NOTIFICATIONS, false))
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
            checked = areNotificationsEnabled,
            onCheckedChange = { enableNotifications ->
                areNotificationsEnabled = enableNotifications
                clockViewModel.saveBoolean(ENABLE_NOTIFICATIONS, enableNotifications)
            }
        )
        ExpandableList(items = expandableItems)
    }
}
