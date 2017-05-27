package com.excursion.athanhelper;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class PageFragment extends Fragment {

    TextView textView;

    public PageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        textView = (TextView) view.findViewById(R.id.textView);
        Bundle bundle = getArguments();
        String message = Integer.toString(bundle.getInt("count"));
//        textView.setText("This is the " + message + "Swip View Page");
        return view;
    }

}
