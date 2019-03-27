package com.example.calendarapp;

import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.RectF;
import android.os.Bundle;

import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.EventLongPressListener, WeekView.EmptyViewLongPressListener {
    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;

    private WeekView mWeekView;

    private HashMap<String,List<WeekViewEvent>> allEvents = new HashMap<String,List<WeekViewEvent>>();
    private List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
    boolean eventFlag = false;
    boolean addFlag = false;
    private int mWeekViewType = TYPE_THREE_DAY_VIEW;
    private String mName;
    private int mYear, mMonth, mDay, mStartHour, mStartMin, mEndHour, mEndMin;


    @Override
    public void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);

        mName = inState.getString("name");
        mYear = inState.getInt("year");
        mMonth = inState.getInt("month");
        mDay = inState.getInt("day");
        mStartHour =  inState.getInt("startHour");
        mStartMin = inState.getInt("startMin");
        mEndHour = inState.getInt("endHour");
        mEndMin = inState.getInt("endMin");
    }


    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        outState.putString("name", mName);
        outState.putInt("year", mYear);
        outState.putInt("month", mMonth);
        outState.putInt("day", mDay);
        outState.putInt("startHour", mStartHour);
        outState.putInt("startMin", mStartMin);
        outState.putInt("endHour", mEndHour);
        outState.putInt("endMin", mEndMin);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                mName = null;
                mYear = 0;
                mMonth = 0;
                mDay = 0;
                mStartHour = 0;
                mStartMin = 0;
                mEndHour = 0;
                mEndMin = 0;
            } else {
                mName = extras.getString("name");
                mYear = extras.getInt("year");
                mMonth = extras.getInt("month");
                mDay = extras.getInt("day");
                mStartHour = extras.getInt("startHour");
                mStartMin = extras.getInt("startMin");
                mEndHour = extras.getInt("endHour");
                mEndMin = extras.getInt("endMin");
            }
        }

        mWeekView = findViewById(R.id.weekView);
        mWeekView.setOnEventClickListener(this);
        mWeekView.setMonthChangeListener(this);
        mWeekView.setEventLongPressListener(this);
        mWeekView.setEmptyViewLongPressListener(this);
        mWeekView.setShowNowLine(true);
        mWeekView.goToHour(9);

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        if (prefs != null) {
            String name = prefs.getString("name", "");
            int year = prefs.getInt("year", 0);
            int month = prefs.getInt("month", 0);
            int day = prefs.getInt("day", 0);
            int startHour = prefs.getInt("startHour", 0);
            int endHour = prefs.getInt("endHour", 0);
            int startMin = prefs.getInt("startMin", 0);
            int endMin = prefs.getInt("endMin", 0);

            Log.i("pref hour:", Integer.toString(startHour));

            //addEvent(name, year, month, day, startHour, endHour, startMin, endMin);

        }

        setupDateTimeInterpreter(false);
    }

    public void onStop() {
        super.onStop();

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();

        prefsEditor.putString("name", mName);
        prefsEditor.putInt("year", mYear);
        prefsEditor.putInt("month", mMonth);
        prefsEditor.putInt("day", mDay);
        prefsEditor.putInt("startHour", mStartHour);
        prefsEditor.putInt("startMin", mStartMin);
        prefsEditor.putInt("endHour", mEndHour);
        prefsEditor.putInt("endMin", mEndMin);

        prefsEditor.apply();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        setupDateTimeInterpreter(id == R.id.action_week_view);
        switch (id){
            case R.id.action_today:
                mWeekView.goToToday();
                mWeekView.setShowNowLine(true);
                mWeekView.goToHour(9);
                return true;
            case R.id.action_day_view:
                if (mWeekViewType != TYPE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(1);
                    mWeekView.setShowNowLine(true);
                    mWeekView.goToHour(9);
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                }
                return true;
            case R.id.action_three_day_view:
                if (mWeekViewType != TYPE_THREE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_THREE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(3);
                    mWeekView.setShowNowLine(true);
                    mWeekView.goToHour(9);
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                }
                return true;
            case R.id.action_week_view:
                if (mWeekViewType != TYPE_WEEK_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_WEEK_VIEW;
                    mWeekView.setNumberOfVisibleDays(7);
                    mWeekView.setShowNowLine(true);
                    mWeekView.goToHour(9);
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Set up a date time interpreter which will show short date values when in week view and long
     * date values otherwise.
     * @param shortDate True if the date values should be short.
     */
    private void setupDateTimeInterpreter(final boolean shortDate) {
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
                SimpleDateFormat format = new SimpleDateFormat(" M/d", Locale.getDefault());

                // All android api level do not have a standard way of getting the first letter of
                // the week day name. Hence we get the first char programmatically.
                // Details: http://stackoverflow.com/questions/16959502/get-one-letter-abbreviation-of-week-day-of-a-date-in-java#answer-16959657
                if (shortDate)
                    weekday = String.valueOf(weekday.charAt(0));
                return weekday.toUpperCase() + format.format(date.getTime());
            }

            @Override
            public String interpretTime(int hour) {
                return hour > 11 ? (hour - 12) + " PM" : (hour == 0 ? "12 AM" : hour + " AM");
            }
        });
    }

    protected String getEventTitle(Calendar time) {
        return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH)+1, time.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(this, "Clicked " + event.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        events.remove(event);
        mWeekView.notifyDatasetChanged();
        Toast.makeText(this, "Deleted: " + event.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {
        Toast.makeText(this, "Empty view long pressed: " + getEventTitle(time), Toast.LENGTH_SHORT).show();
    }

    public WeekView getWeekView() {
        return mWeekView;
    }

    public void addEvent(String name, int year, int month, int day, int startHour, int endHour, int startMin, int endMin) {

        Calendar startCal = Calendar.getInstance();
        startCal.set(Calendar.DAY_OF_MONTH, day);
        startCal.set(Calendar.HOUR_OF_DAY, startHour);
        startCal.set(Calendar.MINUTE, startMin);
        startCal.set(Calendar.MONTH, month-1);
        startCal.set(Calendar.YEAR, year);
        Calendar endCal = (Calendar) startCal.clone();
        endCal.set(Calendar.HOUR_OF_DAY, endHour);
        endCal.set(Calendar.MINUTE, endMin);
        WeekViewEvent event = new WeekViewEvent(3, name, startCal, endCal);
        event.setColor(getResources().getColor(R.color.event_color_02));
        events.add(event);

        allEvents.put(mMonth + "-" + mYear, events);

        addFlag = true;
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {

        if(!eventFlag) {
            if (allEvents.containsKey(mMonth + "-" + mYear)) {
                eventFlag = true;
                return allEvents.get(mMonth + "-" + mYear);
            }
        }

        if(addFlag) {
            if (allEvents.containsKey(mMonth + "-" + mYear)) {
                addFlag = false;
                Log.i("Saved state:", "Called");
                return allEvents.get(mMonth + "-" + mYear);
            }
        }

        events =  new ArrayList<WeekViewEvent>();

        Calendar startCal = Calendar.getInstance();
        startCal.set(Calendar.DAY_OF_MONTH, mDay);
        startCal.set(Calendar.HOUR_OF_DAY, mStartHour);
        startCal.set(Calendar.MINUTE, mStartMin);
        startCal.set(Calendar.MONTH, mMonth-1);
        startCal.set(Calendar.YEAR, mYear);
        Calendar endCal = (Calendar) startCal.clone();
        endCal.set(Calendar.HOUR_OF_DAY, mEndHour);
        endCal.set(Calendar.MINUTE, mEndMin);
        WeekViewEvent event = new WeekViewEvent(2, mName, startCal, endCal);
        event.setColor(getResources().getColor(R.color.event_color_02));
        events.add(event);

        allEvents.put(mMonth + "-" + mYear, events);
        Log.d("List:", "List returned");

        events =  new ArrayList<WeekViewEvent>();
        return events;
    }

    private boolean eventMatches(WeekViewEvent event, int year, int month) {
        return (event.getStartTime().get(Calendar.YEAR) == year && event.getStartTime().get(Calendar.MONTH) == month - 1) || (event.getEndTime().get(Calendar.YEAR) == year && event.getEndTime().get(Calendar.MONTH) == month - 1);
    }
}
