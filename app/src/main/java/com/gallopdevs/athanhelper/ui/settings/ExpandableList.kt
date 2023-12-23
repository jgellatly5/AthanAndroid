package com.gallopdevs.athanhelper.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gallopdevs.athanhelper.R
import com.gallopdevs.athanhelper.ui.theme.AthanHelperTheme

data class ExpandableItem(
    val title: String,
    val drawableId: Int,
    val options: List<String>,
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
                tint = MaterialTheme.colors.primary
            )
        }
        Divider()
        if (isExpanded) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                for (option in item.options) {
                    Text(
                        text = option,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExpandableListItemPreview() {
    AthanHelperTheme {
        ExpandableListItem(
            item = ExpandableItem(
                title = stringResource(id = R.string.calculation_method),
                drawableId = R.drawable.sum_icon,
                options = stringArrayResource(id = R.array.calculation_methods).toList(),
                isExpanded = true
            )
        )
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
    AthanHelperTheme {
        ExpandableList(items = expandableItems)
    }
}
