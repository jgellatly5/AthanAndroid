package com.gallopdevs.athanhelper.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;

import com.gallopdevs.athanhelper.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SettingsFragment extends Fragment {

    private static final String TAG = "SettingsFragment";

    @BindView(R.id.expandable_list_view)
    ExpandableListView expandableListView;
    @BindView(R.id.notification_switch)
    SwitchCompat notificationSwitch;

    private Unbinder unbinder;

    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;

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

        initSwitchNotificationListener();

        return view;
    }

    private void initSwitchNotificationListener() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();
        notificationSwitch.setChecked(sharedPref.getBoolean("enableNotifications", false));
        editor.putBoolean("enableNotifications", notificationSwitch.isChecked());
        editor.apply();
        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                editor.putBoolean("enableNotifications", b);
                editor.apply();
            }
        });
    }

    private void initAdapter() {
        CustomELVAdapter adapter = new CustomELVAdapter(getActivity(), listDataHeader, listDataChild);
        expandableListView.setAdapter(adapter);
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
