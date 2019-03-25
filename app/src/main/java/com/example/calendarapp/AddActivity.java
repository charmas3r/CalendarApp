package com.example.calendarapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class AddActivity extends AppCompatActivity {

    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;

    private EditText mETName;
    private DatePickerDialog mDatePicker;
    private TimePickerDialog mStartTimePicker, mEndTimePicker;
    private Button mBtnDate, mBtnStartTime, mBtnEndTime;
    private TextView mEventDate, mStartTV, mEndTV;

    private String mName;
    private int mYear, mMonth, mDay, mStartHour, mStartMin, mEndHour, mEndMin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mStartTV = findViewById(R.id.pickStartTv);
        mEndTV = findViewById(R.id.pickEndTv);
        mEventDate = findViewById(R.id.text_event_date);
        mETName = findViewById(R.id.edit_name);
        mBtnDate = findViewById(R.id.date_picker_btn);
        mBtnStartTime = findViewById(R.id.start_time_picker_btn);
        mBtnEndTime = findViewById(R.id.end_time_picker_btn);

        final Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mStartHour = calendar.get(Calendar.HOUR_OF_DAY);
        mEndHour = calendar.get(Calendar.HOUR_OF_DAY);
        mStartMin = calendar.get(Calendar.MINUTE);
        mEndMin = calendar.get(Calendar.MINUTE);


        mBtnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePicker = new DatePickerDialog(AddActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                mYear = year;
                                mMonth = monthOfYear + 1;
                                mDay = dayOfMonth;

                                mEventDate.setText("Selected date: " + mMonth + "/" + mDay + "/" + mYear);
                            }
                        }, mYear, mMonth, mDay);
                mDatePicker.show();
            }
        });

        mBtnStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStartTimePicker = new TimePickerDialog(AddActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                mStartHour = hourOfDay;
                                mStartMin = minute;

                                DateFormat input = new SimpleDateFormat("HH:mm");
                                DateFormat output = new SimpleDateFormat("h:mmaa");

                                Date date = null;
                                String outputStr = null;
                                String inputStr = mStartHour + ":" + mStartMin;

                                try {
                                    date = input.parse(inputStr);
                                    outputStr = output.format(date);
                                }
                                catch(ParseException e){
                                    e.printStackTrace();
                                }

                                mStartTV.setText("Selected time: " + outputStr);

                            }
                        }, mStartHour, mStartMin, false);
                mStartTimePicker.show();
            }
        });

        mBtnEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEndTimePicker = new TimePickerDialog(AddActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                mEndHour = hourOfDay;
                                mEndMin = minute;

                                DateFormat input = new SimpleDateFormat("HH:mm");
                                DateFormat output = new SimpleDateFormat("h:mmaa");

                                Date date = null;
                                String outputStr = null;
                                String inputStr = mEndHour + ":" + mEndMin;

                                try {
                                    date = input.parse(inputStr);
                                    outputStr = output.format(date);
                                }
                                catch(ParseException e){
                                    e.printStackTrace();
                                }

                                mEndTV.setText("Selected time: " + outputStr);

                            }
                        }, mEndHour, mEndMin, false);
                mEndTimePicker.show();
            }
        });

    }

    public void addEvent(View v) {

        mName = mETName.getText().toString();

        Intent intent = new Intent(this, MainActivity.class);

        intent.putExtra("name", mName);
        intent.putExtra("year", mYear);
        intent.putExtra("month", mMonth);
        intent.putExtra("day", mDay);
        intent.putExtra("startHour", mStartHour);
        intent.putExtra("startMin", mStartMin);
        intent.putExtra("endHour", mEndHour);
        intent.putExtra("endMin", mEndMin);

        Log.i("year", Integer.toString(mYear));
        Log.i("month", Integer.toString(mMonth));
        Log.i("day", Integer.toString(mDay));
        Log.i("startHour", Integer.toString(mStartHour));
        Log.i("startMin", Integer.toString(mStartMin));
        Log.i("endHour", Integer.toString(mEndHour));
        Log.i("endMin", Integer.toString(mEndMin));

        startActivity(intent);
    }

}

