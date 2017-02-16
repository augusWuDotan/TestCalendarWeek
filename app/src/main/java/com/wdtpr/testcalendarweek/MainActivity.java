package com.wdtpr.testcalendarweek;

import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements MonthLoader.MonthChangeListener, WeekView.EventClickListener, WeekView.EventLongPressListener
        ,WeekView.EmptyViewLongPressListener,WeekView.EmptyViewClickListener {
    WeekView mWeekView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) findViewById(R.id.weekView);
        mWeekView.setNumberOfVisibleDays(7);
        mWeekView.goToToday();
        mWeekView.invalidate();


        // Lets change some dimensions to best fit the view.
//        mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
//        mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
//        mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));

//        Calendar today =Calendar.getInstance();
//        today.set(2017,2,16);
//        mWeekView.goToDate(today);

        // Set an action when any event is clicked.
        mWeekView.setOnEventClickListener(this);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(this);

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(this);

        // Set long press listener for empty view
        mWeekView.setEmptyViewLongPressListener(this);

        // Set up a date time interpreter to interpret how the date and time will be formatted in
        // the week view. This is optional.
//        setupDateTimeInterpreter(false);




    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        Log.d("Main",CalendarToDateString(event.getStartTime()) +" 至 "+CalendarToDateString(event.getEndTime())+ " "+ event.getId());
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {

    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        // Populate the week view with some events.
        List<WeekViewEvent> matchedEvents = new ArrayList<WeekViewEvent>();
//        Calendar calendar = Calendar.getInstance();//取得現在的時間
        Calendar calendar = Calendar.getInstance();
        Log.d("YEAR", newYear+" . " + newMonth +" "+(calendar.get(Calendar.MONTH)+1));


//        Log.d("MONTH",calendar.get(Calendar.MONTH)+"");
//        Log.d("DAY_OF_MONTH",calendar.get(Calendar.DAY_OF_MONTH)+"");
//        Log.d("HOUR_OF_DAY",calendar.get(Calendar.HOUR_OF_DAY )+"");
//        Log.d("MINUTE",calendar.get(Calendar.MINUTE)+"");
//        Log.d("YEAR",calendar.get(Calendar.SECOND)+"");
        Long id = 12345L;
        WeekViewEvent event = DateToWeekViewEvent("2017-02-18 06:30:00","2017-02-18 15:30:00",id,"test",getResources().getColor(android.R.color.darker_gray));
        matchedEvents.add(event);
        Log.d("size",matchedEvents.size()+"");
        return matchedEvents;
    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {
        Log.d("Main",CalendarToDateString(time));
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

    @Override
    public void onEmptyViewClicked(Calendar time) {

        Log.d("Main",CalendarToDateString(time));
    }


    //Calendar to DateString
    private String CalendarToDateString (Calendar calendar){
        //
        String dateStr = null;
        Date date = calendar.getTime();
        dateStr =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        //
        return dateStr;
    }

    //DateString to Calendar
    private Calendar DateStringtoCalendar(String DateString){
        //
        Calendar calendar = Calendar.getInstance();
        //
        ParsePosition pos = new ParsePosition(0);//翻轉 字串 為 時間格式 的工具
        Date date =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(DateString,pos);
        calendar.setTime(date);
        //
        return calendar;
    }

    //Date to WeekViewEvent
    private WeekViewEvent DateToWeekViewEvent(String dateStart,String dateEnd,Long eventId,String eventName,int color){
       //
        WeekViewEvent event =null;
        Calendar Startcalendar = DateStringtoCalendar(dateStart);
        Calendar Endcalendar = DateStringtoCalendar(dateEnd);
        event = new WeekViewEvent(eventId,eventName,Startcalendar,Endcalendar);
        event.setColor(color);
        //
        return event ;
    }
}


