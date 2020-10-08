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
    private val TAG = "SettingsFragment"
    private lateinit var listDataHeader: MutableList<String>
    private lateinit var listDataChild: HashMap<String, List<String>>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        prepareListData()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initSwitchNotificationListener()
    }

    private fun initSwitchNotificationListener() {
        val sharedPref = activity!!.getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        notification_switch.isChecked = sharedPref.getBoolean("enableNotifications", false)
        editor.putBoolean("enableNotifications", notification_switch.isChecked)
        editor.apply()
        notification_switch.setOnCheckedChangeListener { compoundButton, b ->
            editor.putBoolean("enableNotifications", b)
            editor.apply()
        }
    }

    private fun initAdapter() {
        val adapter = CustomELVAdapter(context, listDataHeader, listDataChild)
        expandable_list_view.setAdapter(adapter)
    }

    private fun prepareListData() {
        listDataHeader = ArrayList()
        listDataChild = HashMap()

        listDataHeader.add("Calculation Method")
        listDataHeader.add("Asr Method")
        listDataHeader.add("Latitudes Method")

        val calculationMethodItems = ArrayList<String>()
        calculationMethodItems.add("Jafari")
        calculationMethodItems.add("Karachi")
        calculationMethodItems.add("Islamic Society of North America")
        calculationMethodItems.add("Muslim World League")
        calculationMethodItems.add("Makkah")
        calculationMethodItems.add("Egypt")
        calculationMethodItems.add("Tehran")

        val asrMethodItems = ArrayList<String>()
        asrMethodItems.add("Shafii")
        asrMethodItems.add("Hanafi")

        val latitudesMethodItems = ArrayList<String>()
        latitudesMethodItems.add("None")
        latitudesMethodItems.add("MidNight")
        latitudesMethodItems.add("One/Seventh")
        latitudesMethodItems.add("Angle Based (angle/60th)")

        listDataChild[listDataHeader[0]] = calculationMethodItems
        listDataChild[listDataHeader[1]] = asrMethodItems
        listDataChild[listDataHeader[2]] = latitudesMethodItems
    }
}
