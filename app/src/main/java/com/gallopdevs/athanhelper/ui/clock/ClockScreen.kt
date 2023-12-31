package com.gallopdevs.athanhelper.ui.clock

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gallopdevs.athanhelper.R
import com.gallopdevs.athanhelper.ui.theme.AthanHelperTheme
import com.gallopdevs.athanhelper.viewmodel.ClockViewModel

@Composable
fun ClockScreen(
//    clockViewModel: ClockViewModel = hiltViewModel()
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.moon),
            contentDescription = "Moon",
            modifier = Modifier
                .width(150.dp)
                .height(150.dp)
        )
        Column {
            Text(
                text = stringResource(id = R.string.next_prayer),
                fontSize = dimensionResource(id = R.dimen.next_prayer_text_size).value.sp
            )
            Text(
                text = "00:12:40s",
                fontSize = dimensionResource(id = R.dimen.prayer_timer_text_size).value.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClockScreenPreview() {
    AthanHelperTheme {
        ClockScreen()
    }
}
