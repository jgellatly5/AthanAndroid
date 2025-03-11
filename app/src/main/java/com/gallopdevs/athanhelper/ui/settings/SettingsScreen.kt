package com.gallopdevs.athanhelper.ui.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gallopdevs.athanhelper.R
import com.gallopdevs.athanhelper.data.SharedPreferencesLocalDataSource.Companion.ASR_METHOD
import com.gallopdevs.athanhelper.data.SharedPreferencesLocalDataSource.Companion.CALCULATION_METHOD
import com.gallopdevs.athanhelper.data.SharedPreferencesLocalDataSource.Companion.ENABLE_NOTIFICATIONS
import com.gallopdevs.athanhelper.data.SharedPreferencesLocalDataSource.Companion.LATITUDES_METHOD
import com.gallopdevs.athanhelper.ui.clock.DayViewScreenConstants
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
        LazyColumn {
            items(expandableItems.size) { index ->
                ExpandableListItem(item = expandableItems[index])
            }
        }
    }
}

@Composable
private fun NotificationsOption(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
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

data class ExpandableItem(
    val title: String,
    val drawableId: Int,
    val options: List<String>,
    var selectedOption: Int,
    val onSelectedOption: (Int) -> Unit,
    var isExpanded: Boolean = false
)

@Composable
private fun ExpandableListItem(item: ExpandableItem) {
    var isExpanded by remember { mutableStateOf(item.isExpanded) }
    var selectedOption by remember { mutableIntStateOf(item.selectedOption) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
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
                tint = MaterialTheme.colorScheme.primary
            )
        }
        HorizontalDivider()
        if (isExpanded) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                item.options.forEachIndexed { index, option ->
                    val isHighlighted = selectedOption == index
                    HighlightedSetting(
                        settingName = option,
                        isHighlighted = isHighlighted,
                        modifier = Modifier
                            .clickable {
                                item.onSelectedOption(index)
                                if (selectedOption != index) {
                                    selectedOption = index
                                }
                            }
                    )
                }
            }
        }
    }
}

@Composable
private fun HighlightedSetting(
    settingName: String,
    isHighlighted: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        if (isHighlighted) {
            Image(
                painterResource(id = R.drawable.green_oval),
                contentDescription = DayViewScreenConstants.NEXT_PRAYER,
                modifier = Modifier
                    .size(25.dp)
                    .padding(end = 20.dp)
            )
        }
        Text(
            text = settingName,
            fontSize = dimensionResource(id = R.dimen.prayer_name_text_size).value.sp,
            color = colorResource(id = R.color.colorPrimaryDark)
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
