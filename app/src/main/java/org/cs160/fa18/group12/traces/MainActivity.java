package org.cs160.fa18.group12.traces;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences settingStore;
    private SharedPreferences entryStore;
    private CalendarView mCalandarView;

    /* *********
     * onCreate.
     * *********/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Show stuff.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Initiate Calendar
        mCalandarView = (CalendarView) findViewById(R.id.calendarView);

        // Add event handlers.
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        getAddEntryButton().setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddEntryActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Initialize the data stores.
        settingStore = getSharedPreferences("settings", MODE_PRIVATE);
        entryStore = getSharedPreferences("entries", MODE_PRIVATE);

        // Store and read back some dummy data (just to test).
        // Feel free to edit the dummy data to whatever days you feel are necessary
        // Use this website to convert to TimeStamps and dont forget to add L after timestamp to convert to Long value:
        // https://currentmillis.com/
        Set<Entry> entries = new HashSet<Entry>();
        entries.add(new Entry(System.currentTimeMillis(), 0.5f, "bla", "note"));
        entries.add(new Entry(1542362400000L, 0.2f, "bla2", "note2"));
        entries.add(new Entry(1541149200000L, 0.9f, "bla2", "note2"));
        entries.add(new Entry(1541412000000L, 0.9f, "bla2", "note2"));
        entries.add(new Entry(1542189600000L, 0.9f, "bla2", "note2"));
        entries.add(new Entry(1541584800000L, 0.2f, "bla2", "note2"));
        entries.add(new Entry(1541671200000L, 0.2f, "bla2", "note2"));
        entries.add(new Entry(1541930400000L, 0.2f, "bla2", "note2"));
        entries.add(new Entry(1542535200000L, 0.9f, "bla2", "note2"));
        entries.add(new Entry(1542016800000L, 0.1f, "bla2", "note2"));
        entries.add(new Entry(1541235600000L, 0.1f, "bla2", "note2"));


        setEntries(entries);

        Set<Entry> entry = getEntries();
        List<EventDay> events = new ArrayList<>();

        // Loops through each entry and adds it to the calendar
        for (Entry e : entry) {

            // Get time stamp from Entry
            String[] split = e.toString().split("\\|");
            Long ts = Long.parseLong(split[0]);

            //Get the severity
            Float severity = Float.parseFloat(split[1]);
            Timestamp stamp = new Timestamp(ts);
            Date date = new Date(stamp.getTime());

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);

            EventDay temp = new EventDay(cal, R.drawable.heart);
            Calendar tempcal = temp.getCalendar();

            //Month is indexed starting at 0. So November would be 10
            int month = tempcal.get(Calendar.MONTH);

            // Day and Month are indexed starting at 1 so year would be 2018 and day 21
            int day = tempcal.get(Calendar.DAY_OF_MONTH);
            int year = tempcal.get(Calendar.YEAR);

            // or you can create a date object from the calendar object.

            Date tempdate = tempcal.getTime();


            //Based on how high severity is, will categorize whether panic attack or note
            if (severity >= 0.8f) {
                //Panic Attack is displayed as HEART icon
                events.add(new EventDay(cal, R.drawable.heart));
            }
            else {
                //Note is displayed as NOTE icon
                events.add(new EventDay(cal, R.drawable.note));
            }

        }
        mCalandarView.setEvents(events);


        // This is where we will monitor when a day is clicked!
        mCalandarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {

                // Whenever you click on a day, its numerical month and day will be displayed
                // Just something temporary to test if clicking on certain days works
                // Feel free to delete
                Calendar clickedDayCalender = eventDay.getCalendar();
                String month = String.valueOf(clickedDayCalender.get(Calendar.MONTH));
                String day = String.valueOf(clickedDayCalender.get(Calendar.DAY_OF_MONTH));
                String temp_clicked_phrase = "CLICKED DAY: " + month + ", " + day;
                Context context = getApplicationContext();
                Toast.makeText(context, temp_clicked_phrase, Toast.LENGTH_SHORT).show();

            }
        });

        Log.d("bla", getEntries().toString());
    }

    /* ***************
     * Event handlers.
     * ***************/
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            /* Called when a tab of the navigation bar is clicked. */

            switch (item.getItemId()) {
                case R.id.navigation_history:
                    // Hide the other panes.
                    getDashboardContainer().setVisibility(View.GONE);
                    getSettingsContainer().setVisibility(View.GONE);
                    // Show the history pane.
                    getHistoryContainer().setVisibility(View.VISIBLE);
                    // Absorb the event.
                    return true;

                case R.id.navigation_dashboard:
                    // Hide the other panes.
                    getHistoryContainer().setVisibility(View.GONE);
                    getSettingsContainer().setVisibility(View.GONE);
                    // Show the history pane.
                    getDashboardContainer().setVisibility(View.VISIBLE);
                    // Absorb the event.
                    return true;

                case R.id.navigation_settings:
                    // Hide the other panes.
                    getHistoryContainer().setVisibility(View.GONE);
                    getDashboardContainer().setVisibility(View.GONE);
                    // Show the history pane.
                    getSettingsContainer().setVisibility(View.VISIBLE);
                    // Absorb the event.
                    return true;
            }

            // Do not absorb the event.
            return false;
        }
    };

    /* ********************************************
     * Layout/view getters, purely for convenience.
     * ********************************************/
    private ConstraintLayout getHistoryContainer() {
        /* Gets the history_container. */
        return (ConstraintLayout) findViewById(R.id.history_container);
    }

    private ConstraintLayout getDashboardContainer() {
        /* Gets the dashboard_container. */
        return (ConstraintLayout) findViewById(R.id.dashboard_container);
    }

    private ConstraintLayout getSettingsContainer() {
        /* Gets the settings_container.
         * */
        return (ConstraintLayout) findViewById(R.id.settings_container);
    }

    private FloatingActionButton getAddEntryButton() {
        return (FloatingActionButton) findViewById(R.id.add_entry_button);
    }

    /* *******************************
     * Data store interaction methods.
     * *******************************/
    private Set<Entry> getEntries() {
        /* Get the entries from the data store. Note that changes to the returned set will NOT be
         *  persisted in the data store.
         */
        // Retrieve the serialized entries from the data store.
        Set<String> serializedEntries = entryStore.getStringSet("entries", null);
        if (serializedEntries == null) {
            serializedEntries = new HashSet<String>();
        }

        // Deserialize them.
        Set<Entry> entries = new HashSet<Entry>();
        for (String serialized : serializedEntries) {
            entries.add(Entry.fromString(serialized));
        }

        return entries;
    }

    private void setEntries(Set<Entry> entries) {
        /* Set the entries in the data store.
         *  entries: The entries to store.
         */
        // Serialize them.
        Set<String> serializedEntries = new HashSet<String>();
        for (Entry entry : entries) {
            serializedEntries.add(entry.toString());
        }

        // Store the serialized entries into the data store.
        SharedPreferences.Editor editor = entryStore.edit();
        editor.putStringSet("entries", serializedEntries);
        editor.apply();
    }

}
