package com.gallopdevs.athanhelper.ui.dayview

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.gallopdevs.athanhelper.R
import com.gallopdevs.athanhelper.ui.theme.AthanHelperTheme

@Composable
fun PrayerTime(
    prayerTime: String,
    prayerTimePostFix: String
) {
    Row {
        Text(
            text = prayerTime,
            fontSize = dimensionResource(id = R.dimen.prayer_name_text_size).value.sp,
            color = colorResource(id = R.color.colorPrimaryDark)
        )
        Text(
            text = prayerTimePostFix,
            fontSize = dimensionResource(id = R.dimen.postfix_text_size).value.sp,
            color = colorResource(id = R.color.colorPrimaryDark)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PrayerTimePreview() {
    AthanHelperTheme {
        PrayerTime(
            prayerTime = stringResource(id = R.string.dawn_time_placeholder),
            prayerTimePostFix = stringResource(id = R.string.postfix_am)
        )
    }
}
