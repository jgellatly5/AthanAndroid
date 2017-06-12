package com.excursion.athanhelper;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class PageFragment extends Fragment {

    TextView dateTextView;
    TextView dawnTimeTextView;
    TextView middayTimeTextView;
    TextView afternoonTimeTextView;
    TextView sunsetTimeTextView;
    TextView nightTimeTextView;

    public PageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        dateTextView = (TextView) view.findViewById(R.id.textView);
        Bundle bundle = getArguments();

        dawnTimeTextView = (TextView) view.findViewById(R.id.dawnTimeTextView);
        middayTimeTextView = (TextView) view.findViewById(R.id.middayTimeTextView);
        afternoonTimeTextView = (TextView) view.findViewById(R.id.afternoonTimeTextView);
        sunsetTimeTextView = (TextView) view.findViewById(R.id.sunsetTimeTextView);
        nightTimeTextView = (TextView) view.findViewById(R.id.nightTimeTextView);

        PrayTime prayerTime = new PrayTime();
        Calendar c = Calendar.getInstance();
        ArrayList<String> newTimes = new ArrayList<>();
        newTimes = prayerTime.getPrayerTimes(c, 32.8, -117.2, -7);

        dawnTimeTextView.setText(newTimes.get(1));
        middayTimeTextView.setText(newTimes.get(2));
        afternoonTimeTextView.setText(newTimes.get(3));
        sunsetTimeTextView.setText(newTimes.get(4));
        nightTimeTextView.setText(newTimes.get(5));

        String strDate = getCurrentDay();
        formatDate(bundle, strDate);
        return view;
    }

    private void formatDate(Bundle bundle, String strDate) {
        String[] values = strDate.split("/", 0);
        int day = bundle.getInt("day");
        int count = bundle.getInt("count");
        int numberDay = count + Integer.parseInt(values[2]) - 1;
        String numberString = String.valueOf(numberDay);
        if (day >= 8) {
            day = day - 7;
        }
        String dayString = "";
        switch (day) {
            case 1: dayString = "Sunday";
                    break;
            case 2: dayString = "Monday";
                    break;
            case 3: dayString = "Tuesday";
                    break;
            case 4: dayString = "Wednesday";
                    break;
            case 5: dayString = "Thursday";
                    break;
            case 6: dayString = "Friday";
                    break;
            case 7: dayString = "Saturday";
                    break;
            default: dayString = "This is not a day";
                break;
        }
        if (numberDay < 10) {
            dateTextView.setText(dayString + " " + values[1] + "/0" + numberString);
        } else {
            dateTextView.setText(dayString + " " + values[1] + "/" + numberString);
        }
    }

    private String getCurrentDay() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        return sdf.format(cal.getTime());
    }
}
