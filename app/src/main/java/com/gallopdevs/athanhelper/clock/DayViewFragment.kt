package com.gallopdevs.athanhelper.clock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gallopdevs.athanhelper.compose.DayViewScreen

class DayViewFragment : Fragment() {

    private lateinit var viewModel: ClockViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                arguments?.getInt(DayViewAdapter.PAGE_INDEX)?.let {
                    DayViewScreen(pageIndex = it)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[ClockViewModel::class.java].apply {
            setTimeFormat()

//            binding.dayTextView.text = formatDate(requireArguments())

//            val formattedTimes = formatTimes(requireArguments())
//            updateText(formattedTimes)
//
//            setOvalVisibility(getNextTimeIndex())
//
//            timerCountDown.observe(viewLifecycleOwner) {
//                if (it.equals(0L)) {
//                    setOvalVisibility(getNextTimeIndex())
//                }
//            }
        }
    }

//    private fun updateText(formattedTimes: List<Array<String>>) {
//        binding.apply {
//            dawnTimeTextView.text = formattedTimes[0][0]
//            dawnPostFix.text = formattedTimes[0][1]
//            middayTimeTextView.text = formattedTimes[1][0]
//            middayPostFix.text = formattedTimes[1][1]
//            afternoonTimeTextView.text = formattedTimes[2][0]
//            afternoonPostFix.text = formattedTimes[2][1]
//            sunsetTimeTextView.text = formattedTimes[3][0]
//            sunsetPostFix.text = formattedTimes[3][1]
//            nightTimeTextView.text = formattedTimes[4][0]
//            nightPostFix.text = formattedTimes[4][1]
//        }
//    }

//    private fun setOvalVisibility(i: Int) {
//        binding.apply {
//            val timeViewList = arrayListOf(
//                dawnTextView,
//                middayTextView,
//                afternoonTextView,
//                sunsetTextView,
//                nightTextView
//            )
//            timeViewList[i].addDrawable(R.drawable.green_oval)
//        }
//    }
}

fun TextView.addDrawable(drawable: Int) {
    val imgDrawable = ContextCompat.getDrawable(context, drawable)
    compoundDrawablePadding = 30
    setCompoundDrawablesWithIntrinsicBounds(imgDrawable, null, null, null)
}
