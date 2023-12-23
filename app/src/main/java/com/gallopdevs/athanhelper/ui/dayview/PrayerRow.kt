package com.gallopdevs.athanhelper.ui.dayview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gallopdevs.athanhelper.R
import com.gallopdevs.athanhelper.ui.theme.AthanHelperTheme

@Composable
fun PrayerRow(
    prayerTitle: String,
    prayerTime: String,
    prayerTimePostFix: String,
    showHighlighted: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 60.dp, end = 60.dp)
        ) {
            PrayerName(
                prayerTitle = prayerTitle,
                showHighlighted = showHighlighted
            )
            PrayerTime(
                prayerTime = prayerTime,
                prayerTimePostFix = prayerTimePostFix
            )
        }
        if (prayerTitle != stringResource(id = R.string.night)) {
            Divider(
                modifier = modifier
                    .padding(start = 60.dp, end = 60.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PrayerRowPreview() {
    AthanHelperTheme {
        PrayerRow(
            prayerTitle = stringResource(id = R.string.dawn),
            prayerTime = stringResource(id = R.string.dawn_time_placeholder),
            prayerTimePostFix = stringResource(id = R.string.postfix_am),
            showHighlighted = true
        )
    }
}