package org.cs160.fa18.group12.traces;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
//import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    // The default causes that appear in the cause list.
    private static String[] DEFAULT_CAUSES = {"Work", "Family", "CS 160"};

    Calendar cal = Calendar.getInstance();

    // The number of milliseconds in a day.
    private static long MS_PER_DAY = 1_000 * 60 * 60 * 24;

    // The number of days in the past that the line chart should extend.
    private static long LINE_CHART_MAX_AGE = 21;

    private SharedPreferences settingStore;
    private SharedPreferences entryStore;
    private SharedPreferences causeStore;
    private CalendarView mCalandarView;
    private int clickedMonth = cal.get(Calendar.MONTH);
    private int clickedDay = cal.get(Calendar.DAY_OF_MONTH);
    private long clickedTs = cal.getTimeInMillis();

    //fonts
    //Typeface semibold = Typeface.createFromAsset(getAssets(), "fonts/JosefinSans-SemiBold.ttf");
    //Typeface regular = Typeface.createFromAsset(getAssets(), "fonts/JosefinSans-Regular.ttf");
    //Typeface light = Typeface.createFromAsset(getAssets(), "fonts/JosefinSans-Light.ttf");

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

        //Initialize Line Graph
        LineChart lineChart = (LineChart) findViewById(R.id.linechart);

        //Initialize Doughnut Chart
        PieChart pieChart = (PieChart) findViewById(R.id.piechart);


        // Add event handlers.
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        getAddEntryButton().setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddEntryActivity.class);
                intent.putExtra("month", clickedMonth);
                intent.putExtra("day", clickedDay);
                intent.putExtra("timestamp", clickedTs);
                intent.putExtra("entry", getEntry(clickedTs));
                if (getCauses() != null) {
                    intent.putStringArrayListExtra("causeList", (ArrayList<String>) getCauses());
                }
                startActivity(intent);
                finish();
            }
        });

        // Initialize the data stores.
        settingStore = getSharedPreferences("settings", MODE_PRIVATE);
        entryStore = getSharedPreferences("entries", MODE_PRIVATE);
        causeStore = getSharedPreferences("causes", MODE_PRIVATE);

        // Store and read back some dummy data (just to test).
        // Feel free to edit the dummy data to whatever days you feel are necessary
        // Use this website to convert to TimeStamps and dont forget to add L after timestamp to convert to Long value:
        // https://currentmillis.com/

        Bundle b = getIntent().getExtras();
        String s = "";
        final Set<Entry> entries = getEntries();
        if (b == null) {
            s = "nothing passed back";
        } else {
            int entryMonth = b.getInt("month");
            int entryDay = b.getInt("day");
            long timestamp = b.getLong("timestamp");
            int severity = b.getInt("severity");
            String cause = b.getString("cause");
            String entry = b.getString("entry");
            ArrayList<String> causes = getIntent().getStringArrayListExtra("causeList");
            if (causes != null) {
                setCauses(causes);
            }
            Date date = new Date();
            date.setMonth(entryMonth);
            date.setDate(entryDay);
            Entry e = new Entry(timestamp, severity, cause, entry);
            entries.remove(e);
            entries.add(e);
        }
        setEntries(entries);

        Set<Entry> entry = getEntries();
        final List<EventDay> events = new ArrayList<>();

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
                clickedMonth = clickedDayCalender.get(Calendar.MONTH);
                clickedDay = clickedDayCalender.get(Calendar.DAY_OF_MONTH);
                clickedTs = clickedDayCalender.getTimeInMillis();
                String m = String.valueOf(clickedMonth + 1);
                String temp_clicked_phrase = "CLICKED DAY: " + m + ", " + clickedDay;
                Context context = getApplicationContext();
                Toast.makeText(context, temp_clicked_phrase, Toast.LENGTH_SHORT).show();
//                Toast.makeText(context, eventDay, Toast.LENGTH_SHORT).show();
                TextView timeday = (TextView) findViewById(R.id.daytime);
                TextView severity = (TextView) findViewById(R.id.severity);
                TextView cause = (TextView) findViewById(R.id.cause);
                TextView note = (TextView) findViewById(R.id.note);

                //set typefaces
                //timeday.setTypeface(light);
                //severity.setTypeface(light);
                //cause.setTypeface(light);
                //note.setTypeface(light);

                for (Entry e : entries) {
                    Log.d("string of entry", e.toString());
                    String[] split = e.toString().split("\\|");
                    Long ts = Long.parseLong(split[0]);
                    Timestamp stamp = new Timestamp(ts);
                    Date date = new Date(stamp.getTime());
                    Log.d("bla", " SLSKJFDSKLFJDSLFDSK " + date.toString());
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    int emonth = cal.get(Calendar.MONTH);
                    int eday = cal.get(Calendar.DAY_OF_MONTH);

                    Log.d("bla", "made it here");

                    if (clickedMonth == emonth
                            && clickedDay == eday) {
//                        Toast.makeText(context, date.toString(), Toast.LENGTH_SHORT);

                        Float sev = Float.parseFloat(split[1]);
                        String caus = split[2];
                        String not = split[3];
                        String d = String.valueOf(emonth + 1) + "/" + String.valueOf(eday);
                        timeday.setText(d);
                        severity.setText(String.format(sev.toString()));
                        cause.setText(caus);
                        note.setText(not);
                        break;
                    }
                }
                // check if event exists, show if it does
//                if (events.contains(eventDay)) {
//                    timeday.setText(month + day);
//                    severity.setText(eventDay.severity);
//                    cause.setText(eventDay.cause);
//                    note.setText(eventDay.note);
//                }
            }
        });

        Log.d("bla", getEntries().toString());

        // Set and get some dummy causes (just to test).
        /*List<String> causes = new ArrayList<String>();
        causes.add("cause1");
        causes.add("cause3");
        causes.add("cause2");
        setCauses(causes);
        Log.d("Causes", getCauses().toString());*/

        // Gather line chart data.
        ArrayList<com.github.mikephil.charting.data.Entry> lineChartEntries = new ArrayList<>();
        final long nowTs = new Date().getTime();
        final List<Entry> sortedEntries = new ArrayList<>(getEntries());
        sortedEntries.sort(new Comparator<Entry>() {
            @Override
            public int compare(Entry o1, Entry o2) {
                return (Long.valueOf(o1.ts)).compareTo(o2.ts);
            }
        });

        for (final Entry e : sortedEntries) {
            final Timestamp ts = new Timestamp(e.ts);
            final long relativeTimeMs = ts.getTime() - nowTs;
            final double relativeTimeDays = relativeTimeMs / MS_PER_DAY;

            if (relativeTimeDays < -LINE_CHART_MAX_AGE) {
                continue;
            }

            Log.d("line chart entry","(" + relativeTimeDays + ", " + e.severity + ")");

            final com.github.mikephil.charting.data.Entry lineChartEntry =
                    new com.github.mikephil.charting.data.Entry((float)relativeTimeDays, e.severity);
            lineChartEntries.add(lineChartEntry);

        }

        LineDataSet lineDataSet = new LineDataSet(lineChartEntries, "Days Ago vs. Entry Severity Level");
        lineDataSet.setFillAlpha(110);
        lineDataSet.setColor(Color.RED);
        lineDataSet.setCircleColor(Color.BLACK);
        lineDataSet.setCircleHoleColor(Color.BLACK);

        ArrayList<ILineDataSet> linedataSets = new ArrayList<>();
        linedataSets.add(lineDataSet);

        LineData lineData = new LineData(linedataSets);
        lineChart.setData(lineData);


        //Doughnut Chart Data
        pieChart.setDragDecelerationFrictionCoef(0.5f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(60f);

        final Map<String, Integer> counts = new HashMap<>();
        int total = 0;
        for (final Entry e : getEntries()) {
            if (!counts.containsKey(e.cause)) {
                counts.put(e.cause, 0);
            }
            counts.put(e.cause, counts.get(e.cause) + 1);
            total += 1;
        }
        final List<Map.Entry<String, Integer>> countsSorted = new ArrayList<>(counts.entrySet());
        countsSorted.sort(new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return (Integer.valueOf(o1.getValue())).compareTo(o2.getValue());
            }
        });
        final List<PieEntry> pieEntries = new ArrayList<>();
        for (final Map.Entry<String, Integer> e : countsSorted) {
            final float percent = (float)e.getValue() / total * 100;

            Log.d("pie chart entry","(" + e.getKey() + ", " + percent + "%)");

            pieEntries.add(new PieEntry(percent, e.getKey()));
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setValueTextSize(10f);
        pieDataSet.setColors(MATERIAL_COLORS);

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueTextSize(10f);
        pieData.setValueTextColor(Color.BLACK);
        pieData.setValueFormatter(new PercentFormatter());

        pieChart.setUsePercentValues(true);
        pieChart.setData(pieData);
        pieChart.animateY(2000, Easing.EasingOption.EaseInOutCubic);
        pieChart.invalidate();
    }

    public static final int[] MATERIAL_COLORS = {

            Color.rgb(46, 204, 113), Color.rgb(241, 195, 15), Color.rgb(231, 76, 60),
            Color.rgb(52, 152, 219), Color.rgb(226, 108, 240)
    };

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

    private String getEntry(long timeStamp) {
        Set<Entry> entries = getEntries();
        for (Entry e : entries) {
            String[] split = e.toString().split("\\|");
            Long ts = Long.parseLong(split[0]);
            if (ts == timeStamp) {
                return split[3];
            }
        }
        return null;
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

    private List<String> getCauses() {
        /* Retrieve the user's saved causes from the data store. For use in the cause pane of the
         *      add-entry interaction flow.
         *
         *  Changes to the returned list will not be persisted to the data store. Use setCauses to
         *      save a new list of causes.
         *
         *  Returns: The causes, as a lexicographically-sorted list of strings.
         */
        // Retrieve them.
        Set<String> causes = causeStore.getStringSet("causes", null);
        if (causes == null) {
            causes = new HashSet<>();
            causes.addAll(Arrays.asList(DEFAULT_CAUSES));
        }

        // Sort them.
        List<String> sortedCauses = new ArrayList<>(causes);
        Collections.sort(sortedCauses);

        return sortedCauses;
    }

    private void setCauses(List<String> causes) {
        /* Save the user's saved causes to the data store. For use in the cause pane of the
         *      add-entry interaction flow.
         *
         * causes: The causes to save.
         */
        // Store the serialized entries into the data store.
        SharedPreferences.Editor editor = causeStore.edit();
        editor.putStringSet("causes", new HashSet<>(causes));
        editor.apply();
    }

}
