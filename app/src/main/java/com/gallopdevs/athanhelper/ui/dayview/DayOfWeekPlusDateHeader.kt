package com.gallopdevs.athanhelper.ui.dayview

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gallopdevs.athanhelper.R
import com.gallopdevs.athanhelper.ui.theme.AthanHelperTheme

@Composable
fun DayOfWeekPlusDateHeader(
    dayOfWeekPlusDate: String
) {
    Text(
        text = dayOfWeekPlusDate,
        fontSize = dimensionResource(id = R.dimen.day_text_size).value.sp,
        color = colorResource(id = R.color.colorPrimaryDark),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(top = 32.dp, bottom = 40.dp)
            .fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
private fun DayOfWeekPlusDateHeaderPreview() {
    AthanHelperTheme {
        DayOfWeekPlusDateHeader(
            dayOfWeekPlusDate = stringResource(id = R.string.day_placeholder)
        )
    }
}
