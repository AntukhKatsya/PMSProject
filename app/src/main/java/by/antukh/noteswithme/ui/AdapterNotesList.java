package by.antukh.noteswithme.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import by.antukh.noteswithme.R;
import by.antukh.noteswithme.system.Application;
import by.antukh.noteswithme.system.DB;
import by.antukh.noteswithme.system.Note;

/**
 * Created by acdc on 06.01.18.
 */

public class AdapterNotesList extends BaseAdapter {

    private ArrayList<Note> notes;
    private OnNotesChangeListener listener;

    public AdapterNotesList(ArrayList<Note> notes, OnNotesChangeListener listener) {
        this.notes = notes;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Object getItem(int i) {
        return notes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return notes.get(i).getId();
    }

    @Override
    public View getView(final int i, View convertView, final ViewGroup parent) {
        final Note note = notes.get(i);

        LayoutInflater inflater = (LayoutInflater) Application.get()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView;

        if (convertView == null)
            rowView = inflater.inflate(R.layout.item_list_note, parent, false);
        else rowView = convertView;

        @SuppressLint("DefaultLocale")
        String noteText = String.format("[%d] %s", note.getId(), note.getText());

        ((TextView) rowView.findViewById(R.id.tvNoteText)).setText(noteText);

        rowView.findViewById(R.id.btnDeleteNote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DB.getInstance().deleteNote(notes.get(i).getId());
                if (listener != null)
                    listener.onNotesChanged();
            }
        });

        rowView.findViewById(R.id.tvNoteText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DialogEditNote(parent.getContext(), note, listener).show();
            }
        });
        return rowView;
    }
}
