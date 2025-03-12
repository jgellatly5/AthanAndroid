package com.gallopdevs.athanhelper.ui.clock

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.gallopdevs.athanhelper.MainActivity
import com.gallopdevs.athanhelper.R
import com.gallopdevs.athanhelper.domain.NextPrayer
import com.gallopdevs.athanhelper.domain.NextPrayerTime
import com.gallopdevs.athanhelper.test
import com.gallopdevs.athanhelper.ui.theme.AthanHelperTheme
import com.gallopdevs.athanhelper.utilities.CHANNEL_ID
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import kotlin.time.Duration.Companion.seconds

@Composable
fun NextPrayerHeader(
    nextPrayerTime: NextPrayerTime,
    enableNotifications: Boolean
) {
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
                .weight(1f)
        )
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(id = R.string.next_prayer),
                fontSize = 18.sp
            )
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                when {
                    timerCountDown != 0L -> {
                        val offset = SimpleDateFormat("H:mm:ss", Locale.US)
                        offset.timeZone = TimeZone.getTimeZone("GMT")
                        Text(
                            text = offset.format(timerCountDown),
                            fontSize = 40.sp
                        )
                    }

                    else -> {
                        if (enableNotifications) createNotification(
                            LocalContext.current,
                            nextPrayerTime.nextPrayer.index
                        )
                        Text(
                            text = stringResource(id = R.string.end_time),
                            fontSize = 40.sp
                        )
                    }
                }
            }
        }
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

@Preview(showBackground = true)
@Composable
private fun NextPrayerHeaderPreview() {
    AthanHelperTheme {
        val nextPrayerTime = NextPrayerTime.test(
            nextPrayerTimeMillis = 10000,
            nextPrayer = NextPrayer.test(
                name = "Fajr",
                index = 0
            )
        )
        NextPrayerHeader(
            nextPrayerTime = nextPrayerTime,
            enableNotifications = false
        )
    }
}
