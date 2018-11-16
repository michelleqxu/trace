package org.cs160.fa18.group12.traces;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AddEntryActivity extends AppCompatActivity {

    /* *********
     * onCreate.
     * *********/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Show stuff.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry2);

        // Add event handlers.
        View.OnClickListener severityClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                getCauseContainer().setVisibility(View.GONE);
                getNoteContainer().setVisibility(View.GONE);

                getSeverityContainer().setVisibility(View.VISIBLE);
            }
        };
        View.OnClickListener causeClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                getSeverityContainer().setVisibility(View.GONE);
                getNoteContainer().setVisibility(View.GONE);

                getCauseContainer().setVisibility(View.VISIBLE);
            }
        };
        View.OnClickListener noteClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                getSeverityContainer().setVisibility(View.GONE);
                getCauseContainer().setVisibility(View.GONE);

                getNoteContainer().setVisibility(View.VISIBLE);
            }
        };
        getSeveritySeverityTab().setOnClickListener(severityClickListener);
        getCauseSeverityTab().setOnClickListener(severityClickListener);
        getNoteSeverityTab().setOnClickListener(severityClickListener);
        getSeverityCauseTab().setOnClickListener(causeClickListener);
        getCauseCauseTab().setOnClickListener(causeClickListener);
        getNoteCauseTab().setOnClickListener(causeClickListener);
        getSeverityNoteTab().setOnClickListener(noteClickListener);
        getCauseNoteTab().setOnClickListener(noteClickListener);
        getNoteNoteTab().setOnClickListener(noteClickListener);
    }

    /* ********************************************
     * Layout/view getters, purely for convenience.
     * ********************************************/
    private ConstraintLayout getSeverityContainer() {
        return (ConstraintLayout) findViewById(R.id.severity_container);
    }

    private ConstraintLayout getCauseContainer() {
        return (ConstraintLayout) findViewById(R.id.cause_container);
    }

    private ConstraintLayout getNoteContainer() {
        return (ConstraintLayout) findViewById(R.id.note_container);
    }

    private TextView getSeveritySeverityTab() {
        return (TextView) findViewById(R.id.severity_severity_tab);
    }

    private TextView getSeverityCauseTab() {
        return (TextView) findViewById(R.id.severity_cause_tab);
    }

    private TextView getSeverityNoteTab() {
        return (TextView) findViewById(R.id.severity_note_tab);
    }

    private TextView getCauseSeverityTab() {
        return (TextView) findViewById(R.id.cause_severity_tab);
    }

    private TextView getCauseCauseTab() {
        return (TextView) findViewById(R.id.cause_cause_tab);
    }

    private TextView getCauseNoteTab() {
        return (TextView) findViewById(R.id.cause_note_tab);
    }

    private TextView getNoteSeverityTab() {
        return (TextView) findViewById(R.id.note_severity_tab);
    }

    private TextView getNoteCauseTab() {
        return (TextView) findViewById(R.id.note_cause_tab);
    }

    private TextView getNoteNoteTab() {
        return (TextView) findViewById(R.id.note_note_tab);
    }
}
