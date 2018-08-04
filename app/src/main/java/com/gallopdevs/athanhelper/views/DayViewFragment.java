package com.gallopdevs.athanhelper.views;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.gallopdevs.athanhelper.R;
import com.gallopdevs.athanhelper.model.PrayTime;
import com.gallopdevs.athanhelper.utils.CalendarPrayerTimes;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class DayViewFragment extends Fragment {

    private static final String TAG = "DayViewFragment";

    private static final int DEFAULT_TIME_FORMAT = 1;

    private int count = 0;

    private PrayTime prayerTime;

    private ArrayList<String> nextDayTimes = new ArrayList<>();

    @BindView(R.id.dawnTextView)
    TextView dawnTextView;
    @BindView(R.id.dawnTimeTextView)
    TextView dawnTimeTextView;
    @BindView(R.id.middayTextView)
    TextView middayTextView;
    @BindView(R.id.middayTimeTextView)
    TextView middayTimeTextView;
    @BindView(R.id.afternoonTextView)
    TextView afternoonTextView;
    @BindView(R.id.afternoonTimeTextView)
    TextView afternoonTimeTextView;
    @BindView(R.id.sunsetTextView)
    TextView sunsetTextView;
    @BindView(R.id.sunsetTimeTextView)
    TextView sunsetTimeTextView;
    @BindView(R.id.nightTextView)
    TextView nightTextView;
    @BindView(R.id.nightTimeTextView)
    TextView nightTimeTextView;
    @BindView(R.id.table_layout)
    TableLayout tableLayout;
    @BindView(R.id.dayTextView)
    TextView dayTextView;
    @BindView(R.id.dawn_post_fix)
    TextView dawnPostFix;
    @BindView(R.id.midday_post_fix)
    TextView middayPostFix;
    @BindView(R.id.afternoon_post_fix)
    TextView afternoonPostFix;
    @BindView(R.id.sunset_post_fix)
    TextView sunsetPostFix;
    @BindView(R.id.night_post_fix)
    TextView nightPostFix;

    Unbinder unbinder;


    public DayViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        unbinder = ButterKnife.bind(this, view);

        prayerTime = PrayTime.getInstance();
        prayerTime.setTimeFormat(DEFAULT_TIME_FORMAT);

        Bundle bundle = getArguments();
        setDate(bundle);
        updateTimes(bundle);

        return view;
    }

    private void updateTimes(Bundle bundle) {
        count = bundle.getInt("count");
        nextDayTimes = CalendarPrayerTimes.getNextDayTimes(count);

        String newDawnTime = nextDayTimes.get(0).replaceFirst("^0+(?!$)", "");
        String newMiddayTime = nextDayTimes.get(2).replaceFirst("^0+(?!$)", "");
        String newAfternoonTime = nextDayTimes.get(3).replaceFirst("^0+(?!$)", "");
        String newSunsetTime = nextDayTimes.get(5).replaceFirst("^0+(?!$)", "");
        String newNightTime = nextDayTimes.get(6).replaceFirst("^0+(?!$)", "");

        String[] splitDawnTime = newDawnTime.split(" ");
        String[] splitMiddayTime = newMiddayTime.split(" ");
        String[] splitAfternoonTime = newAfternoonTime.split(" ");
        String[] splitSunsetTime = newSunsetTime.split(" ");
        String[] splitNightTime = newNightTime.split(" ");

        if (prayerTime.getTimeFormat() == 0) {
            dawnTimeTextView.setText(nextDayTimes.get(0));
            middayTimeTextView.setText(nextDayTimes.get(2));
            afternoonTimeTextView.setText(nextDayTimes.get(3));
            sunsetTimeTextView.setText(nextDayTimes.get(5));
            nightTimeTextView.setText(nextDayTimes.get(6));
        } else {
            dawnTimeTextView.setText(splitDawnTime[0]);
            dawnPostFix.setText(splitDawnTime[1]);
            middayTimeTextView.setText(splitMiddayTime[0]);
            middayPostFix.setText(splitMiddayTime[1]);
            afternoonTimeTextView.setText(splitAfternoonTime[0]);
            afternoonPostFix.setText(splitAfternoonTime[1]);
            sunsetTimeTextView.setText(splitSunsetTime[0]);
            sunsetPostFix.setText(splitSunsetTime[1]);
            nightTimeTextView.setText(splitNightTime[0]);
            nightPostFix.setText(splitNightTime[1]);
        }
    }

    private void setDate(Bundle bundle) {
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        int year = c.get(Calendar.YEAR);

        Calendar nextDay = Calendar.getInstance();
        int count = bundle.getInt("count");
        nextDay.set(year, month, dayOfMonth + count);

        int numberDay = nextDay.get(Calendar.DAY_OF_MONTH);
        String numberString = String.valueOf(numberDay);

        String monthString;
        int monthDay = nextDay.get(Calendar.MONTH) + 1;
        if (monthDay < 10) {
            monthString = "0" + String.valueOf(monthDay);
        } else {
            monthString = String.valueOf(monthDay);
        }

        int weekDay = bundle.getInt("day");
        if (weekDay >= 8) {
            weekDay = weekDay - 7;
        }
        String weekDayString;
        switch (weekDay) {
            case 1:
                weekDayString = "Sunday";
                break;
            case 2:
                weekDayString = "Monday";
                break;
            case 3:
                weekDayString = "Tuesday";
                break;
            case 4:
                weekDayString = "Wednesday";
                break;
            case 5:
                weekDayString = "Thursday";
                break;
            case 6:
                weekDayString = "Friday";
                break;
            case 7:
                weekDayString = "Saturday";
                break;
            default:
                weekDayString = "This is not a day";
                break;
        }
        if (numberDay < 10) {
            dayTextView.setText(weekDayString + ", " + monthString + "/0" + numberString);
        } else {
            dayTextView.setText(weekDayString + ", " + monthString + "/" + numberString);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
