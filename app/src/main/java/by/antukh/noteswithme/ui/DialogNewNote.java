package by.antukh.noteswithme.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import by.antukh.noteswithme.system.GPSTracker;
import by.antukh.noteswithme.R;
import by.antukh.noteswithme.system.DB;
import by.antukh.noteswithme.system.Note;
import by.antukh.noteswithme.system.Utils;

/**
 * Created by acdc on 06.01.18.
 */

public class DialogNewNote extends Dialog {

    private static final String LOG_TAG = "DialogNewNote";
    private OnNotesChangeListener listener;

    public DialogNewNote(@NonNull Context context, OnNotesChangeListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_new_note);

        final GPSTracker tracker = new GPSTracker(this.getContext());

        final EditText edtNewNote = findViewById(R.id.edtNewNote);
        edtNewNote.setText(Utils.getLocationName());
        findViewById(R.id.btnAddNewNote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                double longitude = tracker.getLongitude();
                double latitude = tracker.getLatitude();
                String noteText = edtNewNote.getText().toString();
                if (noteText.equals("")) {
                    noteText = getContext().getString(R.string.no_text);
                }
                Note newNote = new Note(noteText, latitude, longitude);
                long id = DB.getInstance().addNote(newNote);

                Log.d(LOG_TAG, "Inserted to " + id);
                if (listener != null)
                    listener.onNotesChanged();
                dismiss();
            }
        });
    }

}
