package com.gallopdevs.athanhelper.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gallopdevs.athanhelper.R
import com.gallopdevs.athanhelper.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            val listDataHeader = resources.getStringArray(R.array.method_settings).toMutableList()
            val listDataChild: HashMap<String, List<String>> = HashMap()
            listDataChild[listDataHeader[0]] = resources.getStringArray(R.array.calculation_methods).toMutableList()
            listDataChild[listDataHeader[1]] = resources.getStringArray(R.array.asr_methods).toMutableList()
            listDataChild[listDataHeader[2]] = resources.getStringArray(R.array.latitudes_method).toMutableList()

            val adapter = CustomELVAdapter(context, listDataHeader, listDataChild)
            expandableListView.setAdapter(adapter)

            val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
            notificationSwitch.isChecked = sharedPref.getBoolean("enableNotifications", false)

            val editor = sharedPref.edit()
            with(editor) {
                putBoolean("enableNotifications", notificationSwitch.isChecked)
                apply()
            }

            notificationSwitch.setOnCheckedChangeListener { _, b ->
                editor.putBoolean("enableNotifications", b)
                editor.apply()
            }
        }
    }
}
