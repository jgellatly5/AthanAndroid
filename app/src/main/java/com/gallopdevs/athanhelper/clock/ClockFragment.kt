package com.gallopdevs.athanhelper.clock

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gallopdevs.athanhelper.R
import com.gallopdevs.athanhelper.databinding.FragmentClockBinding
import com.gallopdevs.athanhelper.home.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.tabs.TabLayoutMediator
import java.text.SimpleDateFormat
import java.util.*

class ClockFragment : Fragment() {

    private var _binding: FragmentClockBinding? = null
    private val binding get() = _binding!!

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var viewModel: ClockViewModel
    private lateinit var dayViewAdapter: DayViewAdapter
    private var timer: CountDownTimer? = null

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        permissions.forEach { actionMap ->
            when (actionMap.key) {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION-> {
                    if (actionMap.value) {
                        getLocation()
                    } else {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                            Toast.makeText(requireContext(), "Location permissions denied.", Toast.LENGTH_SHORT).show()
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
        _binding = FragmentClockBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ClockViewModel::class.java)

        getLocation()

        dayViewAdapter = DayViewAdapter(requireActivity())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            requestPermissionLauncher.launch(permissions)
        } else {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            mFusedLocationClient.lastLocation.addOnSuccessListener { location ->
                binding.apply {
                    if (location != null) {
                        viewModel.setLocation(location.latitude, location.longitude)

                        progressBar.visibility = ProgressBar.INVISIBLE
                        moonIcon.visibility = ImageView.VISIBLE
                        prayerTimerText.visibility = TextView.VISIBLE
                        nextPrayerText.visibility = TextView.VISIBLE

                        val countDownTime = viewModel.getNextTimeMillis()
                        startNewTimer(countDownTime)

                        viewPagerFragment.adapter = dayViewAdapter
                        TabLayoutMediator(tabDots, viewPagerFragment, true) { _, _ -> }.attach()
                    } else {
                        Toast.makeText(requireContext(), "We cannot find your location. Please enable in settings.", Toast.LENGTH_SHORT).show()
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "We cannot find your location. Please enable in settings.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startNewTimer(countDownTime: Long) {
        timer?.cancel()
        timer = object : CountDownTimer(countDownTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val offset = SimpleDateFormat("HH:mm:ss", Locale.US)
                offset.timeZone = TimeZone.getTimeZone("GMT")
                binding.prayerTimerText.text = getString(R.string.count_down_time, offset.format(millisUntilFinished))
            }

            override fun onFinish() {
                binding.prayerTimerText.text = getString(R.string.end_time)
                val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
                if (sharedPref.getBoolean("enableNotifications", false)) createNotification()
                val newCountDownTime = viewModel.getNextTimeMillis()
                startNewTimer(newCountDownTime)
            }
        }.start()
    }

    private fun createNotification() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(requireContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.moon)
                .setContentTitle("Athan")
                .setContentText("Next prayer time: ${viewModel.getNextPrayerName()}")
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
