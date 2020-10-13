package com.gallopdevs.athanhelper.settings

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import com.gallopdevs.athanhelper.R
import com.gallopdevs.athanhelper.model.PrayTime
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
        val selectionIndicator = convertView?.findViewById<ImageView>(R.id.selection_indicator)
        if (lastGroupPosition == groupPosition) {
            if (lastChildPosition != childPosition) {
                selectionIndicator?.visibility = View.INVISIBLE
            }
        }

        val sharedPreferences = context?.getSharedPreferences("settings", Context.MODE_PRIVATE)
        // set default indicators
        when (groupPosition) {
            0 -> {
                if (sharedPreferences?.getInt("calcMethod", 0) == childPosition) {
                    selectionIndicator?.visibility = View.VISIBLE
                }
            }
            1 -> {
                if (sharedPreferences?.getInt("asrMethod", 0) == childPosition) {
                    selectionIndicator?.visibility = View.VISIBLE
                }
            }
            2 -> {
                if (sharedPreferences?.getInt("latitudes", 0) == childPosition) {
                    selectionIndicator?.visibility = View.VISIBLE
                }
            }
        }

        convertView?.setOnClickListener {
            selectionIndicator?.visibility = View.VISIBLE
            val editor = sharedPreferences?.edit()
            when (groupPosition) {
                0 -> {
                    PrayTime.calcMethod = childPosition
                    editor?.putInt("calcMethod", childPosition)
                }
                1 -> {
                    PrayTime.asrJuristic = childPosition
                    editor?.putInt("asrMethod", childPosition)
                }
                2 -> {
                    PrayTime.adjustHighLats = childPosition
                    editor?.putInt("latitudes", childPosition)
                }
            }
            editor?.apply()
            lastChildPosition = childPosition
            lastGroupPosition = groupPosition
            notifyDataSetChanged()
        }
        val textListChild = convertView?.findViewById<TextView>(R.id.item)
        val childText = getChild(groupPosition, childPosition) as String
        textListChild?.text = childText
        return convertView
    }

    override fun getGroupCount(): Int {
        return expandableListHeader.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return expandableListDetail[expandableListHeader[groupPosition]]!!.size
    }

    override fun getGroup(groupPosition: Int): Any {
        return expandableListHeader[groupPosition]
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
            val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.list_settings_header, null)
        }

        val arrowDown = convertView?.findViewById<ImageView>(R.id.arrow_right)
        if (isExpanded) {
            arrowDown?.setImageResource(R.drawable.arrow_down)
        } else {
            arrowDown?.setImageResource(R.drawable.arrow_right)
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
