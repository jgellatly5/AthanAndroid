package com.gallopdevs.athanhelper.settings;

import android.content.SharedPreferences;

import com.gallopdevs.athanhelper.model.PrayTime;

public class SharedPrefsUtil {

    private static PrayTime prayerTime = new PrayTime();
    private static final String KEY_PREF_CALC_METHOD = "calculation_method";
    private static final String KEY_PREF_JURISTIC_METHOD = "juristic_method";
    private static final String KEY_PREF_HIGH_LATITUDES = "high_latitudes";

    private SharedPrefsUtil() {

    }

    public static PrayTime getPrayerTime() {
        return prayerTime;
    }

    public static SharedPreferences.OnSharedPreferenceChangeListener getSharedPrefsInstance() {
        SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                switch (key) {
                    case KEY_PREF_CALC_METHOD:
                        String calcMethod = sharedPreferences.getString(KEY_PREF_CALC_METHOD, "");
                        prayerTime.setCalcMethod(Integer.parseInt(calcMethod));
                        break;
                    case KEY_PREF_JURISTIC_METHOD:
                        String juristicMethod = sharedPreferences.getString(KEY_PREF_JURISTIC_METHOD, "");
                        prayerTime.setAsrJuristic(Integer.parseInt(juristicMethod));
                        break;
                    case KEY_PREF_HIGH_LATITUDES:
                        String highLatitudes = sharedPreferences.getString(KEY_PREF_HIGH_LATITUDES, "");
                        prayerTime.setAdjustHighLats(Integer.parseInt(highLatitudes));
                        break;
                }
            }
        };
        return listener;
    }
}
