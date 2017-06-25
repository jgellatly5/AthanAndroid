package com.excursion.athanhelper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;
    TextView prayerTimer;
    CountDownTimer timer;

    String dawnTime = "";
    String middayTime = "";
    String afternoonTime = "";
    String sunsetTime = "";
    String nightTime = "";
    String nextDawnTime = "";

    int currentTimeIndex = 0;

    public int getTimeZoneOffset() {
        return timeZoneOffset;
    }

    int timeZoneOffset = 0;
    int dstOffset = 0;
    int rawOffset = 0;

    ArrayList<String> newTimes = new ArrayList<>();
    ArrayList<String> nextDayTimes = new ArrayList<>();

    long difference1 = 0;
    long difference2 = 0;
    long difference3 = 0;
    long difference4 = 0;
    long difference5 = 0;
    long difference6 = 0;
    long[] differences = {difference1, difference2, difference3, difference4, difference5, difference6};

    double latitude;
    double longitude;

    int calcMethod = 0;
    int juristicMethod = 0;
    int highLatitudes = 0;
    int timeFormat = 0;

    SimpleDateFormat offset;
    PrayTime prayerTime;

    final String API_KEY_GOOGLE_TIMEZONE = "AIzaSyD18gubjQeeZwLRdi2HfgBjzD6y4sKVRg0";

    final String KEY_PREF_CALC_METHOD = "calculation_method";
    final String KEY_PREF_JURISTIC_METHOD = "juristic_method";
    final String KEY_PREF_HIGH_LATITUDES = "high_latitudes";
    final String KEY_PREF_TIME_FORMATS = "time_formats";

    final int DEFAULT_CALC_METHOD = 2;
    final int DEFAULT_JURISTIC_METHOD = 0;
    final int DEFAULT_HIGH_LATITUDES = 0;
    final int DEFAULT_TIME_FORMAT = 1;

    Calendar c = Calendar.getInstance();
    int month = c.get(Calendar.MONTH);
    int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
    int year = c.get(Calendar.YEAR);
    Calendar nextDay = Calendar.getInstance();

    SharedPreferences sharedPreferences;
    SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            switch(key) {
                case KEY_PREF_CALC_METHOD:
                    String calcMethodString = sharedPreferences.getString(KEY_PREF_CALC_METHOD, "");
                    calcMethod = Integer.parseInt(calcMethodString);
                    prayerTime.setCalcMethod(calcMethod);
                    newTimes = prayerTime.getPrayerTimes(c, 32.8, -117.2, timeZoneOffset);
//                    newTimes = prayerTime.getPrayerTimes(c, latitude, longitude, -7);
                    Log.i("prayer times", String.valueOf(newTimes));
                    nextDayTimes = prayerTime.getPrayerTimes(nextDay, 32.8, -117.2, timeZoneOffset);
//                    nextDayTimes = prayerTime.getPrayerTimes(nextDay, latitude, longitude, -7);
                    Log.i("prayer times next day", String.valueOf(nextDayTimes));
                    break;
                case KEY_PREF_JURISTIC_METHOD:
                    String juristicMethodString = sharedPreferences.getString(KEY_PREF_JURISTIC_METHOD, "");
                    juristicMethod = Integer.parseInt(juristicMethodString);
                    prayerTime.setAsrJuristic(juristicMethod);
                    newTimes = prayerTime.getPrayerTimes(c, 32.8, -117.2, timeZoneOffset);
                    Log.i("prayer times", String.valueOf(newTimes));
                    nextDayTimes = prayerTime.getPrayerTimes(nextDay, 32.8, -117.2, timeZoneOffset);
                    Log.i("prayer times next day", String.valueOf(nextDayTimes));
                    break;
                case KEY_PREF_HIGH_LATITUDES:
                    String highLatitudesString = sharedPreferences.getString(KEY_PREF_HIGH_LATITUDES, "");
                    highLatitudes = Integer.parseInt(highLatitudesString);
                    prayerTime.setAdjustHighLats(highLatitudes);
                    newTimes = prayerTime.getPrayerTimes(c, 32.8, -117.2, timeZoneOffset);
                    Log.i("prayer times", String.valueOf(newTimes));
                    nextDayTimes = prayerTime.getPrayerTimes(nextDay, 32.8, -117.2, timeZoneOffset);
                    Log.i("prayer times next day", String.valueOf(nextDayTimes));
                    break;
            }
            timer.cancel();
            startNewTimer();
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
                Log.i("Google Time Zone", rawOffsetInfo);
                Log.i("Google Time Zone", String.valueOf(timeZoneOffset));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            searchPrayerTimes();
            customizeActionBar();
            setupSwipe();
            startNewTimer();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        long timeStamp = c.getTimeInMillis();
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
        Log.i("result", result);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);

        // set default settings
        calcMethod = DEFAULT_CALC_METHOD;
        juristicMethod = DEFAULT_JURISTIC_METHOD;
        highLatitudes = DEFAULT_HIGH_LATITUDES;
        timeFormat = DEFAULT_TIME_FORMAT;

        prayerTime = new PrayTime();
        prayerTime.setCalcMethod(calcMethod);
        prayerTime.setAsrJuristic(juristicMethod);
        prayerTime.setAdjustHighLats(highLatitudes);
//        prayerTime.setIshaAngle(15);
//        prayerTime.setFajrAngle(15);

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
    }

    private void searchPrayerTimes() {
        Log.i("prayer names", String.valueOf(prayerTime.getTimeNames()));
        Log.i("timeZone", String.valueOf(timeZoneOffset));
        newTimes = prayerTime.getPrayerTimes(c, 32.8, -117.2, timeZoneOffset);
//        Log.i("latitude", String.valueOf(latitude));
//        Log.i("longitude", String.valueOf(longitude));
//        newTimes = prayerTime.getPrayerTimes(c, latitude, longitude, -7);
        Log.i("prayer times", String.valueOf(newTimes));
        nextDay.set(year, month, dayOfMonth + 1);
        nextDayTimes = prayerTime.getPrayerTimes(nextDay, 32.8, -117.2, timeZoneOffset);
//        nextDayTimes = prayerTime.getPrayerTimes(nextDay, latitude, longitude, -7);
        Log.i("prayer times next day", String.valueOf(nextDayTimes));
    }

    public long[] getTimerDifference() {
        // get currentTime and set format
        Calendar cal = Calendar.getInstance();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        final String currentTime = simpleDateFormat.format(cal.getTime());

        // instantiate dates
        Date dawnDate = null;
        Date middayDate = null;
        Date afternoonDate = null;
        Date sunsetDate = null;
        Date nightDate = null;
        Date nextDawnDate = null;
        Date currentTimeDate = null;

        // format times received from PrayTime model
        dawnTime = newTimes.get(1)+ ":00";
        middayTime = newTimes.get(2) + ":00";
        afternoonTime = newTimes.get(3) + ":00";
        if (calcMethod == 2) {
            sunsetTime = newTimes.get(5) + ":00";
        } else {
            sunsetTime = newTimes.get(4) + ":00";
        }
        nightTime = newTimes.get(6) + ":00";
        nextDawnTime = nextDayTimes.get(1) + ":00";
        Log.i("nextDawnTime", nextDawnTime);
        try {
            dawnDate = simpleDateFormat.parse(dawnTime);
            long dawnMillis = dawnDate.getTime();
            String formatDawn = simpleDateFormat.format(dawnDate);
            Log.i("dawnTime", formatDawn);
            Log.i("dawnTimeMilliSeconds", String.valueOf(dawnMillis));

            middayDate = simpleDateFormat.parse(middayTime);
            long middayMillis = middayDate.getTime();
            String formatMidday = simpleDateFormat.format(middayDate);
            Log.i("middayTime", formatMidday);
            Log.i("middayTimeMilliSeconds", String.valueOf(middayMillis));

            afternoonDate = simpleDateFormat.parse(afternoonTime);
            long afMillis = afternoonDate.getTime();
            String formatAf = simpleDateFormat.format(afternoonDate);
            Log.i("afTime", formatAf);
            Log.i("afTimeMilliSeconds", String.valueOf(afMillis));

            sunsetDate = simpleDateFormat.parse(sunsetTime);
            long sunsetMillis = sunsetDate.getTime();
            String formatSunset = simpleDateFormat.format(sunsetDate);
            Log.i("sunsetTime", formatSunset);
            Log.i("sunsetTimeMilliSeconds", String.valueOf(sunsetMillis));

            nightDate = simpleDateFormat.parse(nightTime);
            long nightMillis = nightDate.getTime();
            String formatNight = simpleDateFormat.format(nightDate);
            Log.i("nightTime", formatNight);
            Log.i("nightTimeMilliSeconds", String.valueOf(nightMillis));

            nextDawnDate = simpleDateFormat.parse(nextDawnTime);
            long nextDawnMillis = nextDawnDate.getTime();
            String formatNextDawn = simpleDateFormat.format(nextDawnDate);
            Log.i("nextDawnTime", formatNextDawn);
            Log.i("nextDawnMilliSeconds", String.valueOf(nextDawnMillis));

            //get milliseconds from currentTime
            currentTimeDate = simpleDateFormat.parse(currentTime);
            long currentTimeMilliSeconds = currentTimeDate.getTime();
            String formattedCurrent = simpleDateFormat.format(currentTimeDate);
            Log.i("currentTime", formattedCurrent);
            Log.i("currentTimeInMillis", String.valueOf(currentTimeMilliSeconds));

            difference1 = dawnMillis - currentTimeMilliSeconds;
            Log.i("difference1TimeInMillis", String.valueOf(difference1));

            difference2 = middayMillis - currentTimeMilliSeconds;
            Log.i("difference2TimeInMillis", String.valueOf(difference2));

            difference3 = afMillis - currentTimeMilliSeconds;
            Log.i("difference3TimeInMillis", String.valueOf(difference3));

            difference4 = sunsetMillis - currentTimeMilliSeconds;
            Log.i("difference4TimeInMillis", String.valueOf(difference4));

            difference5 = nightMillis - currentTimeMilliSeconds;
            Log.i("difference5TimeInMillis", String.valueOf(difference5));

            difference6 = nextDawnMillis - currentTimeMilliSeconds + 86400000;
            Log.i("difference6TimeInMillis", String.valueOf(difference6));

            // format for prayerTimer
            offset = new SimpleDateFormat("HH:mm:ss", Locale.US);
            offset.setTimeZone(TimeZone.getTimeZone("GMT"));

            differences[0] = difference1;
            differences[1] = difference2;
            differences[2] = difference3;
            differences[3] = difference4;
            differences[4] = difference5;
            differences[5] = difference6;
            return differences;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        differences[0] = difference1;
        differences[1] = difference2;
        differences[2] = difference3;
        differences[3] = difference4;
        differences[4] = difference5;
        differences[5] = difference6;
        return differences;
    }

    private void startNewTimer() {
        timer = new CountDownTimer(getTimerDifference()[getNextTime()], 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Date newDateTimer = new Date();
                newDateTimer.setTime(millisUntilFinished);
                prayerTimer = (TextView) findViewById(R.id.prayerTimer);
                prayerTimer.setText(offset.format(newDateTimer) + "s");
            }

            @Override
            public void onFinish() {
                prayerTimer.setText("00:00:00s");
                Toast.makeText(MainActivity.this, "New prayer starting", Toast.LENGTH_SHORT).show();
                startNewTimer();
            }
        }.start();
    }

    private int getNextTime() {
        for(int i = 0; i < differences.length; i++) {
            if(differences[i] < 0) {
                currentTimeIndex = i + 1;
                Log.i("currentTimeIndex", String.valueOf(currentTimeIndex));
                Log.i("difference values", String.valueOf(differences[i]));
//                if (calcMethod == 2) {
//                    if (currentTimeIndex == 5) {
//                        currentTimeIndex = 6;
//                    }
//                }
                if (currentTimeIndex > 5) {
                    currentTimeIndex = 0;
                }
            }
        }
        return currentTimeIndex;
    }

    private void setupSwipe() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        SwipeAdapter swipeAdapter = new SwipeAdapter(getSupportFragmentManager());
        viewPager.setAdapter(swipeAdapter);
    }

    private void customizeActionBar() {
        //Customize the ActionBar
        final ActionBar abar = getSupportActionBar();
        View viewActionBar = getLayoutInflater().inflate(R.layout.actionbar_titletext_layout, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);
        textviewTitle.setText("Athan");
        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        abar.setDisplayShowTitleEnabled(false);
        abar.setDisplayHomeAsUpEnabled(true);
        abar.setHomeButtonEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
