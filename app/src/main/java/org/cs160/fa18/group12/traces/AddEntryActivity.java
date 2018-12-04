package org.cs160.fa18.group12.traces;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AddEntryActivity extends AppCompatActivity {
    private static SeekBar seek_bar;
    private static TextView seek_bar_stat;
    private int progress_value;
    private String entry;
    private ArrayList<String> causes = new ArrayList<>();
    ImageButton add;
    ImageButton save;
    String currentCause = "";
    /* *********
     * onCreate.
     * *********/
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
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

        //get causes list
        causes = getIntent().getStringArrayListExtra("causeList");


        //add cuases button handling
        add = (ImageButton)findViewById(R.id.imageButton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder popup = new AlertDialog.Builder(AddEntryActivity.this);
                popup.setTitle("Set a new cause");

                final EditText userCause = new EditText(AddEntryActivity.this);
                popup.setView(userCause);

                popup.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LinearLayout causeLayout = findViewById(R.id.causelist);
                        RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 130);
                        buttonParams.topMargin = 20;
                        causes.add(userCause.getText().toString());
                        Button text = new Button(AddEntryActivity.this);
                        text.setText(userCause.getText().toString());
                        text.setBackgroundColor(Color.parseColor("#FBF7E2"));
                        text.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getSeverityContainer().setVisibility(View.GONE);
                                getCauseContainer().setVisibility(View.GONE);

                                getNoteContainer().setVisibility(View.VISIBLE);
                                currentCause = userCause.getText().toString();
                            }
                        });
                        causes.add(currentCause);
                        causeLayout.addView(text, buttonParams);
                    }
                });

                popup.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                });
                AlertDialog a = popup.create();
                a.show();
            }
        });


        final EditText noteEntry = getNoteEntry();
        Bundle b = getIntent().getExtras();
        entry = b.getString("entry");

        if (entry == null) {
            noteEntry.setHint("Type your note here");
        } else {
            noteEntry.setText(entry);
        }

        noteEntry.setTextColor(getResources().getColor(R.color.black));

        save = findViewById(R.id.save_entry_button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = getIntent().getExtras();
                int month = b.getInt("month");
                int day = b.getInt("day");
                long timestamp = b.getLong("timestamp");
                Intent intent = new Intent(AddEntryActivity.this, MainActivity.class);
                if (getNoteEntry().getText().length() > 0) {
                    intent.putExtra("entry", getNoteEntry().getText().toString());
                    intent.putExtra("month", month);
                    intent.putExtra("day", day);
                    intent.putExtra("timestamp", timestamp);
                }
                intent.putExtra("severity", progress_value);
                intent.putExtra("cause", currentCause);
                intent.putStringArrayListExtra("causeList", causes);
                Log.d("entry", "month: " + month);
                Log.d("entry", "day: " + day);
                startActivity(intent);
                finish();
            }
        });
        //seek_bar_stat.setTypeface(regular);
        seek();
        createCause();

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
                        progress_value = seek_bar.getProgress();
                    }
                }
        );
    }

    public void createCause() {
        LinearLayout causeLayout = findViewById(R.id.causelist);
        RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 130);
        buttonParams.topMargin = 20;
        for (int i = 0; i < causes.size(); i++) {
            Button text = new Button(this);
            final String c = causes.get(i);
            text.setText(causes.get(i));
            text.setBackgroundColor(Color.parseColor("#FBF7E2"));
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getSeverityContainer().setVisibility(View.GONE);
                    getCauseContainer().setVisibility(View.GONE);

                    getNoteContainer().setVisibility(View.VISIBLE);
                    currentCause = c;
                }
            });
            causeLayout.addView(text, buttonParams);
            //text.setLayoutParams(new ConstraintLayout.LayoutParams(
            //        ConstraintLayout.LayoutParams.WRAP_CONTENT,
            //        ConstraintLayout.LayoutParams.WRAP_CONTENT
            //));
        }
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

    private EditText getNoteEntry() { return findViewById(R.id.noteInputText); }
}
