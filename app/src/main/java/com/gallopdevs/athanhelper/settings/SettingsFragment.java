package com.gallopdevs.athanhelper.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gallopdevs.athanhelper.R;
import com.gallopdevs.athanhelper.utils.CalendarPrayerTimes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SettingsFragment extends Fragment {

    private static final String TAG = "SettingsFragment";

    private static final String KEY_PREF_CALC_METHOD = "calculation_method";
    private static final String KEY_PREF_JURISTIC_METHOD = "juristic_method";
    private static final String KEY_PREF_HIGH_LATITUDES = "high_latitudes";

    @BindView(R.id.expandable_list_view)
    ExpandableListView expandableListView;

    Unbinder unbinder;

    private CustomExpandableListAdapter adapter;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;

    private String chosenMethod;
    private String setMethodText;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        unbinder = ButterKnife.bind(this, view);

        prepareListData();

        initAdapter();

        return view;
    }

    private void initAdapter() {
        adapter = new CustomExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
        expandableListView.setAdapter(adapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
                View convertView = LayoutInflater.from(getActivity()).inflate(R.layout.list_settings_items, null);
                View childView = adapter.getChildView(groupPosition, childPosition, false, convertView, null);

                TextView textView = childView.findViewById(R.id.item);
                setMethodText = textView.getText().toString();

                switch (groupPosition) {
                    case 0:
                        chosenMethod = KEY_PREF_CALC_METHOD;
                        break;
                    case 1:
                        chosenMethod = KEY_PREF_JURISTIC_METHOD;
                        break;
                    case 2:
                        chosenMethod = KEY_PREF_HIGH_LATITUDES;
                        break;
                }
                Log.d(TAG, "onChildClick: chosenMethod: " + chosenMethod);
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(chosenMethod, setMethodText);
                editor.commit();
                return false;
            }
        });
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        listDataHeader.add("Calculation Method");
        listDataHeader.add("Asr Method");
        listDataHeader.add("Latitudes Method");

        List<String> calculationMethodItems = new ArrayList<>();
        calculationMethodItems.add("Jafari");
        calculationMethodItems.add("Karachi");
        calculationMethodItems.add("Islamic Society of North America");
        calculationMethodItems.add("Muslim World League");
        calculationMethodItems.add("Makkah");
        calculationMethodItems.add("Egypt");
        calculationMethodItems.add("Tehran");

        List<String> asrMethodItems = new ArrayList<>();
        asrMethodItems.add("Shafii");
        asrMethodItems.add("Hanafi");

        List<String> latitudesMethodItems = new ArrayList<>();
        latitudesMethodItems.add("None");
        latitudesMethodItems.add("MidNight");
        latitudesMethodItems.add("One/Seventh");
        latitudesMethodItems.add("Angle Based (angle/60th)");

        listDataChild.put(listDataHeader.get(0), calculationMethodItems);
        listDataChild.put(listDataHeader.get(1), asrMethodItems);
        listDataChild.put(listDataHeader.get(2), latitudesMethodItems);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
