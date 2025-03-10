package com.gallopdevs.athanhelper.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.gallopdevs.athanhelper.R
import com.gallopdevs.athanhelper.data.SharedPreferencesLocalDataSource.Companion.ASR_METHOD
import com.gallopdevs.athanhelper.data.SharedPreferencesLocalDataSource.Companion.CALCULATION_METHOD
import com.gallopdevs.athanhelper.data.SharedPreferencesLocalDataSource.Companion.ENABLE_NOTIFICATIONS
import com.gallopdevs.athanhelper.data.SharedPreferencesLocalDataSource.Companion.LATITUDES_METHOD
import com.gallopdevs.athanhelper.ui.theme.AthanHelperTheme
import com.gallopdevs.athanhelper.utilities.JAFARI
import com.gallopdevs.athanhelper.utilities.MIDNIGHT
import com.gallopdevs.athanhelper.utilities.SHAFII
import com.gallopdevs.athanhelper.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    settingsViewModel.apply {
        var selectedCalculationMethod by remember {
            mutableIntStateOf(getInt(CALCULATION_METHOD, JAFARI))
        }
        var selectedAsrMethod by remember {
            mutableIntStateOf(getInt(ASR_METHOD, SHAFII))
        }
        var selectedLatitudesMethod by remember {
            mutableIntStateOf(getInt(LATITUDES_METHOD, MIDNIGHT))
        }
        val expandableItems = listOf(
            ExpandableItem(
                title = stringResource(id = R.string.calculation_method),
                drawableId = R.drawable.sum_icon,
                options = stringArrayResource(id = R.array.calculation_methods).toList(),
                selectedOption = selectedCalculationMethod,
                onSelectedOption = { newCalculationMethod ->
                    selectedCalculationMethod = newCalculationMethod
                    saveInt(CALCULATION_METHOD, newCalculationMethod)
                }
            ),
            ExpandableItem(
                title = stringResource(id = R.string.asr_method),
                drawableId = R.drawable.sun_icon,
                options = stringArrayResource(id = R.array.asr_methods).toList(),
                selectedOption = selectedAsrMethod,
                onSelectedOption = { newAsrMethod ->
                    selectedAsrMethod = newAsrMethod
                    saveInt(ASR_METHOD, newAsrMethod)
                }
            ),
            ExpandableItem(
                title = stringResource(id = R.string.latitudes_method),
                drawableId = R.drawable.compass_icon,
                options = stringArrayResource(id = R.array.latitudes_methods).toList(),
                selectedOption = selectedLatitudesMethod,
                onSelectedOption = { newLatitudesMethod ->
                    selectedLatitudesMethod = newLatitudesMethod
                    saveInt(LATITUDES_METHOD, newLatitudesMethod)
                }
            )
        )
        SettingsScreenContent(
            enableNotifications = getBoolean(ENABLE_NOTIFICATIONS, false),
            onNotificationsEnabled = { enableNotifications ->
                saveBoolean(ENABLE_NOTIFICATIONS, enableNotifications)
            },
            expandableItems = expandableItems
        )
    }
}

@Composable
private fun SettingsScreenContent(
    enableNotifications: Boolean,
    onNotificationsEnabled: (Boolean) -> Unit,
    expandableItems: List<ExpandableItem>
) {
    var areNotificationsEnabled by remember { mutableStateOf(enableNotifications) }
    Column {
        NotificationsOption(
            checked = areNotificationsEnabled,
            onCheckedChange = { enableNotifications ->
                areNotificationsEnabled = enableNotifications
                onNotificationsEnabled(enableNotifications)
            }
        )
        ExpandableList(
            items = expandableItems
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenContentPreview() {
    AthanHelperTheme {
        val expandableItems = listOf(
            ExpandableItem(
                title = stringResource(id = R.string.calculation_method),
                drawableId = R.drawable.sum_icon,
                options = stringArrayResource(id = R.array.calculation_methods).toList(),
                selectedOption = 0,
                onSelectedOption = {}
            ),
            ExpandableItem(
                title = stringResource(id = R.string.asr_method),
                drawableId = R.drawable.sun_icon,
                options = stringArrayResource(id = R.array.asr_methods).toList(),
                selectedOption = 0,
                onSelectedOption = {}
            ),
            ExpandableItem(
                title = stringResource(id = R.string.latitudes_method),
                drawableId = R.drawable.compass_icon,
                options = stringArrayResource(id = R.array.latitudes_methods).toList(),
                selectedOption = 0,
                onSelectedOption = {}
            )
        )
        SettingsScreenContent(
            enableNotifications = false,
            onNotificationsEnabled = {},
            expandableItems = expandableItems
        )
    }
}
