package com.gallopdevs.athanhelper.settings

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import com.gallopdevs.athanhelper.R
import com.gallopdevs.athanhelper.model.CalendarPrayerTimes
import java.util.*

class CustomELVAdapter(
        private val context: Context?,
        private val expandableListHeader: List<String>,
        private val expandableListDetail: HashMap<String, List<String>>
) : BaseExpandableListAdapter() {
    private var lastChildPosition = 0
    private var lastGroupPosition = 0

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return expandableListDetail[expandableListHeader[groupPosition]]!!.get(childPosition)
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean,
                              convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        if (convertView == null) {
            val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.list_settings_items, null)
        }
        val imageView = convertView?.findViewById<ImageView>(R.id.selection_indicator)
        if (lastGroupPosition == groupPosition) {
            if (lastChildPosition != childPosition) {
                imageView?.visibility = View.INVISIBLE
            }
        }

        val sharedPreferences = context?.getSharedPreferences("settings", Context.MODE_PRIVATE)
        if (groupPosition == 0) {
            if (sharedPreferences?.getInt("calcMethod", 0) == childPosition) {
                imageView?.visibility = View.VISIBLE
            } else {
                imageView?.visibility = View.INVISIBLE
            }
        }
        if (groupPosition == 1) {
            if (sharedPreferences?.getInt("asrMethod", 0) == childPosition) {
                imageView?.visibility = View.VISIBLE
            } else {
                imageView?.visibility = View.INVISIBLE
            }
        }
        if (groupPosition == 2) {
            if (sharedPreferences?.getInt("latitudes", 0) == childPosition) {
                imageView?.visibility = View.VISIBLE
            } else {
                imageView?.visibility = View.INVISIBLE
            }
        }

        convertView?.setOnClickListener {
            imageView?.visibility = View.VISIBLE
            val sharedPreferences = context?.getSharedPreferences("settings", Context.MODE_PRIVATE)
            val editor = sharedPreferences?.edit()
            when (groupPosition) {
                0 -> {
                    CalendarPrayerTimes.updateCalcMethod(childPosition)
                    editor?.putInt("calcMethod", childPosition)
                    editor?.apply()
                    lastChildPosition = childPosition
                    lastGroupPosition = groupPosition
                    notifyDataSetChanged()
                }
                1 -> {
                    CalendarPrayerTimes.updateAsrJuristic(childPosition)
                    editor?.putInt("asrMethod", childPosition)
                    editor?.apply()
                    lastChildPosition = childPosition
                    lastGroupPosition = groupPosition
                    notifyDataSetChanged()
                }
                2 -> {
                    CalendarPrayerTimes.updateHighLats(childPosition)
                    editor?.putInt("latitudes", childPosition)
                    editor?.apply()
                    lastChildPosition = childPosition
                    lastGroupPosition = groupPosition
                    notifyDataSetChanged()
                }
            }
        }
        val textListChild = convertView?.findViewById<TextView>(R.id.item)
        val childText = getChild(groupPosition, childPosition) as String
        textListChild?.text = childText
        return convertView
    }

    override fun getGroupCount(): Int {
        return this.expandableListHeader.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return this.expandableListDetail[this.expandableListHeader[groupPosition]]!!.size
    }

    override fun getGroup(groupPosition: Int): Any {
        return this.expandableListHeader[groupPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            val inflater = this.context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.list_settings_header, null)
        }

        val arrowDown = convertView?.findViewById<ImageView>(R.id.arrow_right)
        if (isExpanded) {
            arrowDown!!.setImageResource(R.drawable.arrow_down)
        } else {
            arrowDown!!.setImageResource(R.drawable.arrow_right)
        }
        val imageIcon = convertView?.findViewById<ImageView>(R.id.image_header)
        imageIcon?.setImageResource(getImageDrawable(groupPosition))
        val headerTitle = getGroup(groupPosition) as String
        val lblListHeader = convertView?.findViewById<TextView>(R.id.header_settings)
        lblListHeader?.text = headerTitle
        return convertView!!
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    private fun getImageDrawable(index: Int): Int {
        val drawables = intArrayOf(R.drawable.sum_icon, R.drawable.sun_icon, R.drawable.compass_icon)
        return drawables[index]
    }
}
