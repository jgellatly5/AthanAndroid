package com.excursion.athanhelper;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
    PrayTime prayerTime = new PrayTime();

    public PageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        dateTextView = (TextView) view.findViewById(R.id.textView);
        Bundle bundle = getArguments();

        prayerTime.setAsrJuristic(0);
        prayerTime.setCalcMethod(2);

        dawnTimeTextView = (TextView) view.findViewById(R.id.dawnTimeTextView);
        middayTimeTextView = (TextView) view.findViewById(R.id.middayTimeTextView);
        afternoonTimeTextView = (TextView) view.findViewById(R.id.afternoonTimeTextView);
        sunsetTimeTextView = (TextView) view.findViewById(R.id.sunsetTimeTextView);
        nightTimeTextView = (TextView) view.findViewById(R.id.nightTimeTextView);

        String strDate = getCurrentDay();
        formatDate(bundle, strDate);
        formatPrayers(bundle, strDate);
        return view;
    }

    private void formatPrayers(Bundle bundle, String strDate) {
        int count = bundle.getInt("count");
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        int year = c.get(Calendar.YEAR);
        Log.i("monthtimes", String.valueOf(month));
        Log.i("daytimes", String.valueOf(dayOfMonth));
        Log.i("yeartimes", String.valueOf(year));

        Calendar nextDay = Calendar.getInstance();
        nextDay.set(year, month, dayOfMonth + count);
        ArrayList<String> nextDayTimes = new ArrayList<>();
        nextDayTimes =  prayerTime.getPrayerTimes(nextDay, 32.8, -117.2, -7);
        Log.i("PageFragnextDayTimes", String.valueOf(nextDayTimes));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");

        dawnTimeTextView.setText(prayerTime.floatToTime12(convertTimeToDouble(nextDayTimes.get(1)), false));
        middayTimeTextView.setText(prayerTime.floatToTime12(convertTimeToDouble(nextDayTimes.get(2)), false));
        afternoonTimeTextView.setText(prayerTime.floatToTime12(convertTimeToDouble(nextDayTimes.get(3)), false));
        sunsetTimeTextView.setText(prayerTime.floatToTime12(convertTimeToDouble(nextDayTimes.get(4)), false));
        nightTimeTextView.setText(prayerTime.floatToTime12(convertTimeToDouble(nextDayTimes.get(5)), false));
    }

    private double convertTimeToDouble(String time) {
        String[] parts = time.split(":", 0);
        Double hours = Double.parseDouble(parts[0]);
        Double mins = Double.parseDouble(parts[1])/60;
        return hours + mins;
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
        Log.i("day", String.valueOf(day));
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
