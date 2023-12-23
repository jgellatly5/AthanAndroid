package com.gallopdevs.athanhelper.ui.dayview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.gallopdevs.athanhelper.ui.theme.AthanHelperTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DayViewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                arguments?.apply {
                    AthanHelperTheme {
                        DayViewScreen(
                            pageIndex = getInt(DayViewAdapter.PAGE_INDEX)
                        )
                    }
                }
            }
        }
    }
}
