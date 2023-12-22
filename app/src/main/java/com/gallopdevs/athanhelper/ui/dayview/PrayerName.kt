package com.gallopdevs.athanhelper.ui.dayview

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
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
import com.gallopdevs.athanhelper.ui.theme.AthanHelperTheme

@Composable
fun PrayerName(
    prayerTitle: String,
    showHighlighted: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showHighlighted) {
            Image(
                painterResource(id = R.drawable.green_oval),
                contentDescription = DayViewScreenConstants.NEXT_PRAYER,
                modifier = Modifier
                    .size(25.dp)
                    .padding(end = 20.dp)
            )
        }
        Text(
            text = prayerTitle,
            fontSize = dimensionResource(id = R.dimen.prayer_name_text_size).value.sp,
            color = colorResource(id = R.color.colorPrimaryDark)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PrayerNamePreview() {
    AthanHelperTheme {
        PrayerName(
            prayerTitle = stringResource(id = R.string.dawn),
            showHighlighted = true
        )
    }
}
