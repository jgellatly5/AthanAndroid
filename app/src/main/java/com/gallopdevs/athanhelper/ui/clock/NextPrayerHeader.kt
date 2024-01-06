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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gallopdevs.athanhelper.R
import com.gallopdevs.athanhelper.data.PreferencesManagerImpl.Companion.ENABLE_NOTIFICATIONS
import com.gallopdevs.athanhelper.ui.theme.AthanHelperTheme
import com.gallopdevs.athanhelper.viewmodel.ClockViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import kotlin.time.Duration.Companion.seconds

@Composable
fun NextPrayerHeader(
    clockViewModel: ClockViewModel = hiltViewModel()
) {
    var timerCountDown by remember { mutableLongStateOf(clockViewModel.getNextTimeMillis()) }
    LaunchedEffect(timerCountDown) {
        while (timerCountDown != 0L) {
            delay(1.seconds)
            timerCountDown -= 1000
        }
    }
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
            if (timerCountDown != 0L) {
                val offset = SimpleDateFormat("HH:mm:ss", Locale.US)
                offset.timeZone = TimeZone.getTimeZone("GMT")
                Text(
                    text = stringResource(
                        id = R.string.count_down_time,
                        offset.format(timerCountDown)
                    ),
                    fontSize = dimensionResource(id = R.dimen.prayer_timer_text_size).value.sp
                )
            } else {
                Text(
                    text = stringResource(id = R.string.end_time),
                    fontSize = dimensionResource(id = R.dimen.prayer_timer_text_size).value.sp
                )
                val enableNotifications = clockViewModel.getBoolean(ENABLE_NOTIFICATIONS, false)
                //        if (enableNotifications) createNotification()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NextPrayerHeaderPreview() {
    AthanHelperTheme {
        NextPrayerHeader()
    }
}
