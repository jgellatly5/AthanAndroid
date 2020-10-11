package com.gallopdevs.athanhelper.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gallopdevs.athanhelper.R
import kotlinx.android.synthetic.main.fragment_settings.*
import java.util.*

class SettingsFragment : Fragment() {
    private lateinit var listDataHeader: List<String>
    private lateinit var listDataChild: HashMap<String, List<String>>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        listDataHeader = resources.getStringArray(R.array.method_settings).toMutableList()
        listDataChild = HashMap()
        listDataChild[listDataHeader[0]] = resources.getStringArray(R.array.calculation_methods).toMutableList()
        listDataChild[listDataHeader[1]] = resources.getStringArray(R.array.asr_methods).toMutableList()
        listDataChild[listDataHeader[2]] = resources.getStringArray(R.array.latitudes_method).toMutableList()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = CustomELVAdapter(context, listDataHeader, listDataChild)
        expandable_list_view.setAdapter(adapter)

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)

        notification_switch.isChecked = sharedPref!!.getBoolean("enableNotifications", false)

        val editor = sharedPref.edit()
        with (editor) {
            putBoolean("enableNotifications", notification_switch.isChecked)
            apply()
        }

        notification_switch.setOnCheckedChangeListener { _, b ->
            editor.putBoolean("enableNotifications", b)
            editor.apply()
        }
    }
}
