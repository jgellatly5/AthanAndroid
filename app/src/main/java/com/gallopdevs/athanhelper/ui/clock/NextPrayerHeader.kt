package com.gallopdevs.athanhelper.ui.clock

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.gallopdevs.athanhelper.MainActivity
import com.gallopdevs.athanhelper.MainActivity.Companion.CHANNEL_ID
import com.gallopdevs.athanhelper.R
import com.gallopdevs.athanhelper.data.models.TimingsResponse
import com.gallopdevs.athanhelper.domain.NextPrayer
import com.gallopdevs.athanhelper.domain.NextPrayerTime
import com.gallopdevs.athanhelper.domain.PrayerInfo
import com.gallopdevs.athanhelper.domain.PrayerTimes
import com.gallopdevs.athanhelper.test
import com.gallopdevs.athanhelper.ui.clock.NextPrayerHeaderConstants.LOADING_STATE
import com.gallopdevs.athanhelper.ui.shared.ErrorMessage
import com.gallopdevs.athanhelper.ui.shared.LoadingIndicator
import com.gallopdevs.athanhelper.ui.theme.AthanHelperTheme
import com.gallopdevs.athanhelper.viewmodel.PrayerInfoUiState
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import kotlin.time.Duration.Companion.seconds

@Composable
fun NextPrayerHeader(
    prayerInfoUiState: PrayerInfoUiState,
    enableNotifications: Boolean
) {
    when (prayerInfoUiState) {
        PrayerInfoUiState.Loading -> { LoadingIndicator(testTag = LOADING_STATE) }

        is PrayerInfoUiState.Success -> {
            val nextPrayerTime = prayerInfoUiState.prayerInfo.nextPrayerTime
            var timerCountDown by remember { mutableLongStateOf(nextPrayerTime.nextPrayerTimeMillis) }
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
                        if (enableNotifications) createNotification(
                            LocalContext.current,
                            nextPrayerTime.nextPrayer.index
                        )
                        Text(
                            text = stringResource(id = R.string.end_time),
                            fontSize = dimensionResource(id = R.dimen.prayer_timer_text_size).value.sp
                        )
                    }
                }
            }
        }

        is PrayerInfoUiState.Error -> ErrorMessage(message = prayerInfoUiState.message)
    }
}

private fun createNotification(context: Context, nextTimeIndex: Int) {
    val intent = Intent(context, MainActivity::class.java)
    val pendingIntent =
        PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    val prayerNames = context.resources.getStringArray(R.array.prayer_titles)
    val nextPrayerName = prayerNames[nextTimeIndex]
    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.moon)
        .setContentTitle("Athan")
        .setContentText("Next prayer time: $nextPrayerName")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
    val notificationManager = NotificationManagerCompat.from(context)
    notificationManager.notify(0, builder.build())
}

object NextPrayerHeaderConstants {
    const val LOADING_STATE = "LOADING_STATE"
}

@Preview(showBackground = true)
@Composable
private fun NextPrayerHeaderPreview() {
    AthanHelperTheme {
        val prayerInfoUiState = PrayerInfoUiState.Success(
            prayerInfo = PrayerInfo.test(
                nextPrayerTime = NextPrayerTime.test(
                    nextPrayerTimeMillis = 10000,
                    nextPrayer = NextPrayer.test(
                        name = "Fajr",
                        index = 0
                    )
                ),
                prayerTimesList = listOf(
                    PrayerTimes.test(
                        date = "24 Apr 2024",
                        timingsResponse = TimingsResponse.test()
                    )
                )
            )
        )
        NextPrayerHeader(
            prayerInfoUiState = prayerInfoUiState,
            enableNotifications = false
        )
    }
}
