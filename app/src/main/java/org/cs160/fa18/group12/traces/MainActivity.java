package org.cs160.fa18.group12.traces;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    // The default causes that appear in the cause list.
    private static String[] DEFAULT_CAUSES = {"Work", "Family", "CS 160"};

    private SharedPreferences settingStore;
    private SharedPreferences entryStore;
    private SharedPreferences causeStore;

    /* *********
     * onCreate.
     * *********/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Show stuff.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Add event handlers.
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        getAddEntryButton().setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddEntryActivity.class);
                //ArrayList<String> causes = (ArrayList) getCauses();
                //intent.putExtra("causeList", causes);
                startActivity(intent);
                finish();
            }
        });

        // Initialize the data stores.
        settingStore = getSharedPreferences("settings", MODE_PRIVATE);
        entryStore = getSharedPreferences("entries", MODE_PRIVATE);
        causeStore = getSharedPreferences("causes", MODE_PRIVATE);

        // Store and read back some dummy data (just to test).
        Set<Entry> entries = new HashSet<Entry>();
        entries.add(new Entry(100l, 0.5f, "bla", "note"));
        entries.add(new Entry(200l, 0.8f, "bla2", "note2"));
        setEntries(entries);
        Log.d("bla", getEntries().toString());

        // Set and get some dummy causes (just to test).
        List<String> causes = new ArrayList<String>();
        causes.add("cause1");
        causes.add("cause3");
        causes.add("cause2");
        setCauses(causes);
        Log.d("Causes", getCauses().toString());
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
