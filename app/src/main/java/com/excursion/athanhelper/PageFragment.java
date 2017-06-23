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

    final int DEFAULT_CALC_METHOD = 2;
    final int DEFAULT_JURISTIC_METHOD = 0;
    final int DEFAULT_HIGH_LATITUDES = 0;
    final int DEFAULT_TIME_FORMAT = 1;

    final String KEY_PREF_CALC_METHOD = "calculation_method";
    final String KEY_PREF_JURISTIC_METHOD = "juristic_method";
    final String KEY_PREF_HIGH_LATITUDES = "high_latitudes";
    final String KEY_PREF_TIME_FORMATS = "time_formats";

    int calcMethod = 0;
    int juristicMethod = 0;
    int highLatitudes = 0;
    int timeFormat = 0;

    public PageFragment() {
        // Required empty public constructor
    }

//    SharedPreferences sharedPreferences;
//    SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
//        @Override
//        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//            switch(key) {
//                case KEY_PREF_CALC_METHOD:
//                    String calcMethodString = sharedPreferences.getString(KEY_PREF_CALC_METHOD, "");
//                    calcMethod = Integer.parseInt(calcMethodString);
//                    prayerTime.setCalcMethod(calcMethod);
//                    newTimes = prayerTime.getPrayerTimes(c, 32.8, -117.2, -7);
////                    newTimes = prayerTime.getPrayerTimes(c, latitude, longitude, -7);
//                    Log.i("prayer times", String.valueOf(newTimes));
//                    nextDayTimes = prayerTime.getPrayerTimes(nextDay, 32.8, -117.2, -7);
////                    nextDayTimes = prayerTime.getPrayerTimes(nextDay, latitude, longitude, -7);
//                    Log.i("prayer times next day", String.valueOf(nextDayTimes));
//                    break;
//                case KEY_PREF_JURISTIC_METHOD:
//                    String juristicMethodString = sharedPreferences.getString(KEY_PREF_JURISTIC_METHOD, "");
//                    juristicMethod = Integer.parseInt(juristicMethodString);
//                    prayerTime.setAsrJuristic(juristicMethod);
//                    newTimes = prayerTime.getPrayerTimes(c, 32.8, -117.2, -7);
//                    Log.i("prayer times", String.valueOf(newTimes));
//                    nextDayTimes = prayerTime.getPrayerTimes(nextDay, 32.8, -117.2, -7);
//                    Log.i("prayer times next day", String.valueOf(nextDayTimes));
//                    break;
//                case KEY_PREF_HIGH_LATITUDES:
//                    String highLatitudesString = sharedPreferences.getString(KEY_PREF_HIGH_LATITUDES, "");
//                    highLatitudes = Integer.parseInt(highLatitudesString);
//                    prayerTime.setAdjustHighLats(highLatitudes);
//                    newTimes = prayerTime.getPrayerTimes(c, 32.8, -117.2, -7);
//                    Log.i("prayer times", String.valueOf(newTimes));
//                    nextDayTimes = prayerTime.getPrayerTimes(nextDay, 32.8, -117.2, -7);
//                    Log.i("prayer times next day", String.valueOf(nextDayTimes));
//                    break;
//                case KEY_PREF_TIME_FORMATS:
//                    //TODO set up time format to be compatible with get timer difference
//                    String timeFormatsString = sharedPreferences.getString(KEY_PREF_TIME_FORMATS, "");
//                    timeFormat = Integer.parseInt(timeFormatsString);
//                    break;
//            }
//        }
//    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        dateTextView = (TextView) view.findViewById(R.id.textView);
        Bundle bundle = getArguments();

//        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);

        //TODO update UI with shared prefs

        prayerTime.setAsrJuristic(DEFAULT_JURISTIC_METHOD);
        prayerTime.setCalcMethod(DEFAULT_CALC_METHOD);
        prayerTime.setAdjustHighLats(DEFAULT_HIGH_LATITUDES);
        prayerTime.setTimeFormat(DEFAULT_TIME_FORMAT);

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

        Calendar nextDay = Calendar.getInstance();
        nextDay.set(year, month, dayOfMonth + count);
        ArrayList<String> nextDayTimes = prayerTime.getPrayerTimes(nextDay, 32.8, -117.2, -7);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");

        dawnTimeTextView.setText(nextDayTimes.get(1));
        middayTimeTextView.setText(nextDayTimes.get(2));
        afternoonTimeTextView.setText(nextDayTimes.get(3));
        sunsetTimeTextView.setText(nextDayTimes.get(5));
        nightTimeTextView.setText(nextDayTimes.get(6));
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
