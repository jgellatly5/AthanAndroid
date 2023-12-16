package com.gallopdevs.athanhelper.clock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
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
                arguments?.apply {
                    DayViewScreen(
                        weekDay = arguments?.getInt(DayViewAdapter.WEEK_DAY),
                        month = arguments?.getInt(DayViewAdapter.MONTH),
                        dayOfMonth = arguments?.getInt(DayViewAdapter.DAY_OF_MONTH),
                        pageIndex = getInt(DayViewAdapter.PAGE_INDEX)
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[ClockViewModel::class.java].apply {


            timerCountDown.observe(viewLifecycleOwner) {
                if (it.equals(0L)) {
//                    setOvalVisibility(getNextTimeIndex())
                }
            }
        }
    }
}
