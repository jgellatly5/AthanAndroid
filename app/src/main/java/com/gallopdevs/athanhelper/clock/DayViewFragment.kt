package com.gallopdevs.athanhelper.clock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gallopdevs.athanhelper.R
import com.gallopdevs.athanhelper.databinding.FragmentDayviewBinding
import com.gallopdevs.athanhelper.model.PrayTime

class DayViewFragment : Fragment() {

    private var _binding: FragmentDayviewBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ClockViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDayviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        PrayTime.timeFormat = PrayTime.time12

        viewModel = ViewModelProvider(this).get(ClockViewModel::class.java).apply {
            binding.dayTextView.text = formatDate(requireArguments())
            val formattedTimes = formatTimes(requireArguments())
            updateText(formattedTimes)

            val nextTimeIndex = getNextTimeIndex()
            setOvalVisibility(nextTimeIndex)
        }
    }

    private fun updateText(formattedTimes: List<Array<String>>) {
        binding.apply {
            dawnTimeTextView.text = formattedTimes[0][0]
            dawnPostFix.text = formattedTimes[0][1]
            middayTimeTextView.text = formattedTimes[1][0]
            middayPostFix.text = formattedTimes[1][1]
            afternoonTimeTextView.text = formattedTimes[2][0]
            afternoonPostFix.text = formattedTimes[2][1]
            sunsetTimeTextView.text = formattedTimes[3][0]
            sunsetPostFix.text = formattedTimes[3][1]
            nightTimeTextView.text = formattedTimes[4][0]
            nightPostFix.text = formattedTimes[4][1]
        }
    }

    private fun setOvalVisibility(i: Int) {
        binding.apply {
            var item = i

            val timeViewList = arrayListOf(dawnTextView, middayTextView, afternoonTextView, sunsetTextView, nightTextView)

            if (item >= 5) {
                item -= 5
            }

            timeViewList[item].addDrawable(R.drawable.green_oval)
        }
    }
}

fun TextView.addDrawable(drawable: Int) {
    val imgDrawable = ContextCompat.getDrawable(context, drawable)
    compoundDrawablePadding = 30
    setCompoundDrawablesWithIntrinsicBounds(imgDrawable, null, null, null)
}
