package org.cs160.fa18.group12.traces;

public class Entry {
    /* A journal entry. */

    long ts;
    float severity;
    String cause;
    private String note;

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
        if (note.contains("|")) {
            throw new RuntimeException("Note contains pipe characters.");
        }
        this.note = note;
    }

    public String toString() {
        /* Serialize this entry to a string. */
        return ts + "|" + severity + "|" + cause + "|" + note;
    }
}
