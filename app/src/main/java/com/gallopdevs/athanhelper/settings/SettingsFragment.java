package com.gallopdevs.athanhelper.settings;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.gallopdevs.athanhelper.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SettingsFragment extends Fragment {

    @BindView(R.id.expandable_list_view)
    ExpandableListView expandableListView;

    Unbinder unbinder;

    private CustomExpandableListAdapter adapter;
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

        adapter = new CustomExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
        expandableListView.setAdapter(adapter);


        return view;
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        listDataHeader.add("Calculation Method");
        listDataHeader.add("Asr Method");
        listDataHeader.add("Latitudes Method");

        List<String> calculationMethodItems = new ArrayList<String>();
        calculationMethodItems.add("Jafari");
        calculationMethodItems.add("Karachi");
        calculationMethodItems.add("Islamic Society of North America");
        calculationMethodItems.add("Muslim World League");
        calculationMethodItems.add("Makkah");
        calculationMethodItems.add("Egypt");
        calculationMethodItems.add("Tehran");

        List<String> asrMethodItems = new ArrayList<String>();
        asrMethodItems.add("Shafii");
        asrMethodItems.add("Hanafi");

        List<String> latitudesMethodItems = new ArrayList<String>();
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
