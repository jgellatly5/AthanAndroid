package com.gallopdevs.athanhelper.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.gallopdevs.athanhelper.compose.SettingsScreen

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                SettingsScreen()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.apply {
//            val listDataHeader = resources.getStringArray(R.array.method_settings).toMutableList()
//            val listDataChild: HashMap<String, List<String>> = HashMap()
//            listDataChild[listDataHeader[0]] =
//                resources.getStringArray(R.array.calculation_methods).toMutableList()
//            listDataChild[listDataHeader[1]] =
//                resources.getStringArray(R.array.asr_methods).toMutableList()
//            listDataChild[listDataHeader[2]] =
//                resources.getStringArray(R.array.latitudes_method).toMutableList()
//
//            val adapter = CustomELVAdapter(context, listDataHeader, listDataChild)
//            expandableListView.setAdapter(adapter)
//
//        }
    }
}
