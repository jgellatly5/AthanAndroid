package com.gallopdevs.athanhelper.ui.clock

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import com.gallopdevs.athanhelper.MainActivity
import com.gallopdevs.athanhelper.R
import com.gallopdevs.athanhelper.R.string.end_time
import com.gallopdevs.athanhelper.data.PreferencesManagerImpl.Companion.ENABLE_NOTIFICATIONS
import com.gallopdevs.athanhelper.databinding.FragmentClockBinding
import com.gallopdevs.athanhelper.ui.dayview.DayViewAdapter
import com.gallopdevs.athanhelper.ui.dayview.DayViewScreen
import com.gallopdevs.athanhelper.ui.theme.AthanHelperTheme
import com.gallopdevs.athanhelper.viewmodel.ClockViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

@AndroidEntryPoint
class ClockFragment : Fragment() {

    private var _binding: FragmentClockBinding? = null
    private val binding get() = _binding!!

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var dayViewAdapter: DayViewAdapter

    private val clockViewModel: ClockViewModel by viewModels()

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.forEach { actionMap ->
                when (actionMap.key) {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION -> {
                        activity?.let { activity ->
                            if (actionMap.value) {
//                                getLocation(activity)
                            } else {
                                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                                    Toast.makeText(
                                        activity,
                                        "Location permissions denied.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AthanHelperTheme {
                    ClockScreen()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let { activity ->
            dayViewAdapter = DayViewAdapter(activity)

//            getLocation(activity)
        }
    }

    private fun getLocation(fragmentActivity: FragmentActivity) {
        if (ActivityCompat.checkSelfPermission(
                fragmentActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                fragmentActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val permissions = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            requestPermissionLauncher.launch(permissions)
        } else {
            fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(fragmentActivity)
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                binding.apply {
                    if (location != null) {
                        clockViewModel.setLocation(location.latitude, location.longitude)

                        progressBar.visibility = ProgressBar.INVISIBLE
                        moonIcon.visibility = ImageView.VISIBLE
                        prayerTimerText.visibility = TextView.VISIBLE
                        nextPrayerText.visibility = TextView.VISIBLE

                        clockViewModel.startNewTimer()
                        clockViewModel.timerCountDown.observe(viewLifecycleOwner) {
                            if (!it.equals(0L)) {
                                val offset = SimpleDateFormat("HH:mm:ss", Locale.US)
                                offset.timeZone = TimeZone.getTimeZone("GMT")
                                prayerTimerText.text =
                                    getString(R.string.count_down_time, offset.format(it))
                            } else {
                                prayerTimerText.text = getString(end_time)
                                val enableNotifications =
                                    clockViewModel.getBoolean(ENABLE_NOTIFICATIONS, false)
                                if (enableNotifications) createNotification()
                            }
                        }

                        viewPagerFragment.adapter = dayViewAdapter
                        TabLayoutMediator(tabDots, viewPagerFragment, true) { _, _ -> }.attach()
                    } else {
                        Toast.makeText(
                            fragmentActivity,
                            "We cannot find your location. Please enable in settings.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(
                    fragmentActivity,
                    "We cannot find your location. Please enable in settings.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun createNotification() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(requireContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            .setSmallIcon(R.drawable.moon)
            .setContentTitle("Athan")
            .setContentText("Next prayer time: ${clockViewModel.getNextPrayerName()}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        val notificationManager = NotificationManagerCompat.from(requireContext())
        notificationManager.notify(0, builder.build())
    }

    companion object {
        private const val CHANNEL_ID = "Notification"
    }
}
