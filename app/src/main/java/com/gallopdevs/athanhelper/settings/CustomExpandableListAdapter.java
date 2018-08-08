package com.gallopdevs.athanhelper.settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gallopdevs.athanhelper.R;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    @BindView(R.id.arrow_right)
    ImageView arrowDown;

    private Context context;
    private List<String> expandableListHeader;
    private HashMap<String, List<String>> expandableListDetail;

    public CustomExpandableListAdapter(Context context, List<String> expandableListHeader, HashMap<String, List<String>> expandableListDetail) {
        this.context = context;
        this.expandableListHeader = expandableListHeader;
        this.expandableListDetail = expandableListDetail;
    }

    public void setImageVisibility() {

        View convertView = LayoutInflater.from(context).inflate(R.layout.list_settings_items, null);
        ImageView indicator = convertView.findViewById(R.id.selection_indicator);
        indicator.setVisibility(View.VISIBLE);
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
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_settings_items, null);
        }

//        ImageView imageView = convertView.findViewById(R.id.selection_indicator);
//        imageView.setVisibility(View.VISIBLE);
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

        TextView lblListHeader = convertView.findViewById(R.id.header_settings);
        ImageView imageIcon = convertView.findViewById(R.id.image_header);
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
        int[] drawables = {R.drawable.sum_icon, R.drawable.sun_icon, R.drawable.bell_icon};
        return drawables[index];
    }
}
