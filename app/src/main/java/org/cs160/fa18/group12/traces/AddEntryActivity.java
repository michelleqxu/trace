package org.cs160.fa18.group12.traces;

import android.graphics.Typeface;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class AddEntryActivity extends AppCompatActivity {
    private static SeekBar seek_bar;
    private static TextView seek_bar_stat;
    private int progress_value;
    /* *********
     * onCreate.
     * *********/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //fonts
        Typeface semibold = Typeface.createFromAsset(getAssets(), "fonts/JosefinSans-SemiBold.ttf");
        Typeface regular = Typeface.createFromAsset(getAssets(), "fonts/JosefinSans-Regular.ttf");
        // Show stuff.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry2);
        TextView sevq = (TextView)findViewById(R.id.severity_question);
        sevq.setTypeface(semibold);
        TextView causeq = (TextView)findViewById(R.id.cause_question);
        causeq.setTypeface(semibold);
        TextView noteq = (TextView)findViewById(R.id.note_question);
        noteq.setTypeface(semibold);
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
        getSeverityCauseTab().setTypeface(regular);
        getCauseSeverityTab().setOnClickListener(severityClickListener);
        getCauseSeverityTab().setTypeface(regular);
        getNoteSeverityTab().setOnClickListener(severityClickListener);
        getNoteSeverityTab().setTypeface(regular);
        getSeverityCauseTab().setOnClickListener(causeClickListener);
        getSeverityCauseTab().setTypeface(regular);
        getCauseCauseTab().setOnClickListener(causeClickListener);
        getCauseCauseTab().setTypeface(regular);
        getNoteCauseTab().setOnClickListener(causeClickListener);
        getNoteCauseTab().setTypeface(regular);
        getSeverityNoteTab().setOnClickListener(noteClickListener);
        getSeverityNoteTab().setTypeface(regular);
        getCauseNoteTab().setOnClickListener(noteClickListener);
        getCauseNoteTab().setTypeface(regular);
        getNoteNoteTab().setOnClickListener(noteClickListener);
        getNoteNoteTab().setTypeface(regular);

        seek_bar_stat.setTypeface(regular);
        seek();
    }

    public void seek() {
        seek_bar = (SeekBar)findViewById(R.id.seekBar);
        seek_bar_stat = (TextView)findViewById(R.id.textView);

        seek_bar_stat.setText(seek_bar.getProgress() + "%");

        seek_bar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progress_value = progress;
                        seek_bar_stat.setText(progress + "%");
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        seek_bar_stat.setText(seek_bar.getProgress() + "%");
                    }
                }
        );
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
