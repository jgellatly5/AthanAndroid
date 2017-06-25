package com.excursion.athanhelper;


import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutionException;


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

    int dstOffset = 0;
    int timeZoneOffset = 0;

    final String API_KEY_GOOGLE_TIMEZONE = "AIzaSyD18gubjQeeZwLRdi2HfgBjzD6y4sKVRg0";

    Calendar nextDay = Calendar.getInstance();
    ArrayList<String> nextDayTimes = new ArrayList<>();

    public PageFragment() {
        // Required empty public constructor
    }

    //TODO set up timezone from API, location listener
    SharedPreferences sharedPreferences;
    SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            switch(key) {
                case KEY_PREF_CALC_METHOD:
                    String calcMethodString = sharedPreferences.getString(KEY_PREF_CALC_METHOD, "");
                    calcMethod = Integer.parseInt(calcMethodString);
                    prayerTime.setCalcMethod(calcMethod);
                    nextDayTimes = prayerTime.getPrayerTimes(nextDay, 32.8, -117.2, timeZoneOffset);
//                    nextDayTimes = prayerTime.getPrayerTimes(nextDay, latitude, longitude, -7);
                    break;
                case KEY_PREF_JURISTIC_METHOD:
                    String juristicMethodString = sharedPreferences.getString(KEY_PREF_JURISTIC_METHOD, "");
                    juristicMethod = Integer.parseInt(juristicMethodString);
                    prayerTime.setAsrJuristic(juristicMethod);
                    nextDayTimes = prayerTime.getPrayerTimes(nextDay, 32.8, -117.2, timeZoneOffset);
                    break;
                case KEY_PREF_HIGH_LATITUDES:
                    String highLatitudesString = sharedPreferences.getString(KEY_PREF_HIGH_LATITUDES, "");
                    highLatitudes = Integer.parseInt(highLatitudesString);
                    prayerTime.setAdjustHighLats(highLatitudes);
                    nextDayTimes = prayerTime.getPrayerTimes(nextDay, 32.8, -117.2, timeZoneOffset);
                    break;
                case KEY_PREF_TIME_FORMATS:
                    String timeFormatsString = sharedPreferences.getString(KEY_PREF_TIME_FORMATS, "");
                    timeFormat = Integer.parseInt(timeFormatsString);
                    prayerTime.setTimeFormat(timeFormat);
                    nextDayTimes = prayerTime.getPrayerTimes(nextDay, 32.8, -117.2, timeZoneOffset);
                    break;
            }
            updateView();
        }
    };

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "failed";
            } catch (IOException e) {
                e.printStackTrace();
                return "failed";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                String rawOffsetInfo = jsonObject.getString("rawOffset");
                String dstOffsetInfo = jsonObject.getString("dstOffset");
                dstOffset = Integer.parseInt(dstOffsetInfo)/3600;
                timeZoneOffset = Integer.parseInt(rawOffsetInfo)/3600 + dstOffset;
                Log.i("timezonefrag", rawOffsetInfo);
                Log.i("timezonefrag", String.valueOf(timeZoneOffset));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Bundle bundle = getArguments();
            String strDate = getCurrentDay();
            formatDate(bundle, strDate);
            formatPrayers(bundle, strDate);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        dateTextView = (TextView) view.findViewById(R.id.textView);

        long timeStamp = nextDay.getTimeInMillis();
        String timeStampString = String.valueOf(timeStamp/1000);
        Log.i("timeStamp", timeStampString);

        DownloadTask downloadTask = new DownloadTask();
        String result = null;
        try {
            result = downloadTask.execute("https://maps.googleapis.com/maps/api/timezone/json?location=32.8,-117.2&timestamp=" + timeStampString + "&key=" + API_KEY_GOOGLE_TIMEZONE).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);

        prayerTime.setAsrJuristic(DEFAULT_JURISTIC_METHOD);
        prayerTime.setCalcMethod(DEFAULT_CALC_METHOD);
        prayerTime.setAdjustHighLats(DEFAULT_HIGH_LATITUDES);
        prayerTime.setTimeFormat(DEFAULT_TIME_FORMAT);

        dawnTimeTextView = (TextView) view.findViewById(R.id.dawnTimeTextView);
        middayTimeTextView = (TextView) view.findViewById(R.id.middayTimeTextView);
        afternoonTimeTextView = (TextView) view.findViewById(R.id.afternoonTimeTextView);
        sunsetTimeTextView = (TextView) view.findViewById(R.id.sunsetTimeTextView);
        nightTimeTextView = (TextView) view.findViewById(R.id.nightTimeTextView);

        return view;
    }

    private void formatPrayers(Bundle bundle, String strDate) {
        int count = bundle.getInt("count");
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        int year = c.get(Calendar.YEAR);

        nextDay.set(year, month, dayOfMonth + count);
        nextDayTimes = prayerTime.getPrayerTimes(nextDay, 32.8, -117.2, timeZoneOffset);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");

        updateView();
    }

    private void updateView() {
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

    //TODO fix end of day month bug
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
