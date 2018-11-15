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

import com.gallopdevs.athanhelper.R;
import com.gallopdevs.athanhelper.utils.CalendarPrayerTimes;

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

    private int lastChildPosition = 0;
    private int lastGroupPosition = 0;

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
        if (lastGroupPosition == groupPosition) {
            if (lastChildPosition != childPosition) {
                imageView.setVisibility(View.INVISIBLE);
            }
        }

        // TODO test this
        SharedPreferences sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        if (groupPosition == 0) {
            if (sharedPreferences.getInt("calcMethod", 0) == childPosition) {
                imageView.setVisibility(View.VISIBLE);
            }
        }
        if (groupPosition == 1) {
            if (sharedPreferences.getInt("asrMethod", 0) == childPosition) {
                imageView.setVisibility(View.VISIBLE);
            }
        }
        if (groupPosition == 2) {
            if (sharedPreferences.getInt("latitudes", 0) == childPosition) {
                imageView.setVisibility(View.VISIBLE);
            }
        }


        final String childText = (String) getChild(groupPosition, childPosition);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setVisibility(View.VISIBLE);
                SharedPreferences sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                switch (groupPosition) {
                    case 0:
                        CalendarPrayerTimes.updateCalcMethod(childPosition);
                        editor.putInt("calcMethod", childPosition);
                        editor.apply();
                        lastChildPosition = childPosition;
                        lastGroupPosition = groupPosition;
                        notifyDataSetChanged();
                        break;
                    case 1:
                        CalendarPrayerTimes.updateAsrJuristic(childPosition);
                        editor.putInt("asrMethod", childPosition);
                        editor.apply();
                        lastChildPosition = childPosition;
                        lastGroupPosition = groupPosition;
                        notifyDataSetChanged();
                        break;
                    case 2:
                        CalendarPrayerTimes.updateHighLats(childPosition);
                        editor.putInt("latitudes", childPosition);
                        editor.apply();
                        lastChildPosition = childPosition;
                        lastGroupPosition = groupPosition;
                        notifyDataSetChanged();
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
