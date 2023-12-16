package com.gallopdevs.athanhelper.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gallopdevs.athanhelper.R

@Composable
private fun NotificationsLabel() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.bell_icon),
            contentDescription = stringResource(id = R.string.notifications),
            modifier = Modifier
                .size(60.dp)
                .padding(start = 30.dp, end = 15.dp)
        )
        Text(
            text = stringResource(id = R.string.notifications),
            modifier = Modifier.padding(top = 30.dp, bottom = 30.dp, end = 30.dp),
            color = colorResource(id = R.color.colorPrimaryDark)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationsLabelPreview() {
    NotificationsLabel()
}

@Composable
fun NotificationsOption(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        NotificationsLabel()
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

@Composable
fun SettingsScreen() {
    NotificationsOption(checked = false, onCheckedChange = {})
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    SettingsScreen()
}
