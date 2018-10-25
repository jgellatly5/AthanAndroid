package com.gallopdevs.athanhelper.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gallopdevs.athanhelper.R;
import com.gallopdevs.athanhelper.clock.ClockFragment;
import com.gallopdevs.athanhelper.clock.DayViewAdapter;
import com.gallopdevs.athanhelper.utils.CalendarPrayerTimes;
import com.gallopdevs.athanhelper.utils.SettingsPagerAdapter;
import com.gallopdevs.athanhelper.utils.SwiperActivity;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomELVAdapter extends BaseExpandableListAdapter {

    private static final String TAG = "CustomELVAdapter";

    @BindView(R.id.arrow_right)
    ImageView arrowDown;
    @BindView(R.id.header_settings)
    TextView lblListHeader;
    @BindView(R.id.image_header)
    ImageView imageIcon;

    private Context context;
    private List<String> expandableListHeader;
    private HashMap<String, List<String>> expandableListDetail;

    public CustomELVAdapter(Context context, List<String> expandableListHeader, HashMap<String, List<String>> expandableListDetail) {
        this.context = context;
        this.expandableListHeader = expandableListHeader;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.expandableListDetail.get(this.expandableListHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, final boolean isLastChild,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_settings_items, null);
        }

        final ImageView imageView = convertView.findViewById(R.id.selection_indicator);
        final String childText = (String) getChild(groupPosition, childPosition);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setVisibility(View.VISIBLE);
                switch (groupPosition) {
                    case 0:
                        Log.w(TAG, "onClick: childPosition: " + childPosition);
                        CalendarPrayerTimes.updateCalcMethod(childPosition);
                        Toast.makeText(context, "Updating Calc Method", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Log.w(TAG, "onClick: childPosition: " + childPosition);
                        CalendarPrayerTimes.updateAsrJuristic(childPosition);
                        Toast.makeText(context, "Updating Juristic Method", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Log.w(TAG, "onClick: childPosition: " + childPosition);
                        CalendarPrayerTimes.updateHighLats(childPosition);
                        Toast.makeText(context, "Updating HighLats", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        TextView textListChild = convertView.findViewById(R.id.item);
        textListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getGroupCount() {
        return this.expandableListHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.expandableListDetail.get(this.expandableListHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.expandableListHeader.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_settings_header, null);
        }
        ButterKnife.bind(this, convertView);

        if (isExpanded) {
            arrowDown.setImageResource(R.drawable.arrow_down);
        } else {
            arrowDown.setImageResource(R.drawable.arrow_right);
        }

        imageIcon.setImageResource(getImageDrawable(groupPosition));
        String headerTitle = (String) getGroup(groupPosition);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private int getImageDrawable(int index) {
        int[] drawables = {R.drawable.sum_icon, R.drawable.sun_icon, R.drawable.compass_icon};
        return drawables[index];
    }
}
