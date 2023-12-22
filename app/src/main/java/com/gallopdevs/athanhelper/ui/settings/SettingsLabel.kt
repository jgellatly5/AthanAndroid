package com.gallopdevs.athanhelper.ui.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gallopdevs.athanhelper.R
import com.gallopdevs.athanhelper.ui.theme.AthanHelperTheme

@Composable
fun SettingsLabel(settingsIcon: Painter, settingsHeader: String) {
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
    AthanHelperTheme {
        SettingsLabel(
            settingsIcon = painterResource(id = R.drawable.bell_icon),
            settingsHeader = stringResource(id = R.string.notifications)
        )
    }
}
