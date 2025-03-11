package com.gallopdevs.athanhelper.ui.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gallopdevs.athanhelper.R
import com.gallopdevs.athanhelper.ui.clock.DayViewScreenConstants
import com.gallopdevs.athanhelper.ui.theme.AthanHelperTheme

@Composable
fun HighlightedSetting(
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
private fun HighlightedSettingPreview() {
    AthanHelperTheme {
        HighlightedSetting(
            settingName = stringResource(id = R.string.dawn),
            isHighlighted = true
        )
    }
}
