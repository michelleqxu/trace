package org.cs160.fa18.group12.traces;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.Calendar;

public class Entry implements Comparable<Entry>{
    /* A journal entry. */

    long ts;
    float severity;
    String cause;
    String note;

    public static Entry fromString(String serialized) {
        /* Deserialize this entry from a string.
         *  serialized: The serialized entry.
         */
        String[] split = serialized.split("\\|");
        if (split.length != 4) {
            throw new RuntimeException("You messed up Shea.");
        }
        return new Entry(
                Long.parseLong(split[0]),
                Float.parseFloat(split[1]),
                split[2],
                split[3]
        );
    }

    public Entry(long ts, float severity, String cause, String note) {
        /* Create an entry.
         *  ts: The timestamp at which the entry was created.
         *  severity: The severity of the entry.
         *  cause: The name of the cause of the entry.
         *  note: The content of the note attached to the entry. Must not contain pipe characters.
         */
        this.ts = ts;
        this.severity = severity;
        this.cause = cause;
        /*if (note.contains("|")) {
            throw new RuntimeException("Note contains pipe characters.");
        }*/
        this.note = note;
    }

    public String toString() {
        /* Serialize this entry to a string. */
        return ts + "|" + severity + "|" + cause + "|" + note;
    }

    @Override
    public int compareTo(@NonNull Entry e) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(this.ts);

        int year1 = calendar.get(Calendar.YEAR);
        int month1 = calendar.get(Calendar.MONTH);
        int day1 = calendar.get(Calendar.DAY_OF_MONTH);

        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(e.ts);

        int year2 = calendar.get(Calendar.YEAR);
        int month2 = calendar.get(Calendar.MONTH);
        int day2 = calendar.get(Calendar.DAY_OF_MONTH);
        Log.d("entry month1: ", String.valueOf(month1));
        Log.d("entry month2: ", String.valueOf(month2));
        Log.d("entry day1: ", String.valueOf(day1));
        Log.d("entry day2: ", String.valueOf(day2));


        if (year1 < year2) {
            return -1;
        } else if (month1 < month2) {
            return -1;
        } else if (day1 < day2) {
            return -1;
        } else if (year1 > year2) {
            return 1;
        } else if (month1 > month2) {
            return 1;
        } else if (day1 > day2) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int hashCode() {
        return (int) this.ts;
    }


    @Override
    public boolean equals(Object o) {
//        if (o instanceof Entry && ((Entry) o).compareTo(this) == 0) {
//            return true;
//
//        }
//        return false;
        Log.d("o.timestamp ", String.valueOf(((Entry) o).ts));
        Log.d("this.timestamp ", String.valueOf(this.ts));
        return String.valueOf(((Entry) o).ts).equals(String.valueOf(this.ts));
    }

}
