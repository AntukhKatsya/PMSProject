package by.antukh.noteswithme.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ListView;

import java.util.ArrayList;

import by.antukh.noteswithme.R;
import by.antukh.noteswithme.system.DB;
import by.antukh.noteswithme.system.Note;
import by.antukh.noteswithme.system.Settings;
import by.antukh.noteswithme.ui.anim.PushPullAnimation;

public class FragmentNotesList
        extends Fragment
        implements OnNotesChangeListener {


    private ListView lvNotes;

    public FragmentNotesList() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btnCreateNewNote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DialogNewNote(getContext(), FragmentNotesList.this).show();
            }
        });

        view.findViewById(R.id.btnSynchronizeNotes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DialogSynchronize(getContext()).show();
            }
        });


        lvNotes = view.findViewById(R.id.lvNotes);
        updateViews();
    }

    private void updateViews() {
        // update notes list
        ArrayList<Note> notes = Settings.getInstance().isShowDeleted()
                ? DB.getInstance().getAllNotes()
                : DB.getInstance().getStandardNotes();
        lvNotes.setAdapter(new AdapterNotesList(notes, this));
    }

    @Override
    public void onStart() {
        super.onStart();
        updateViews();
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return PushPullAnimation.create(PushPullAnimation.UP, enter, 300);
    }

    @Override
    public void onNotesChanged() {
        updateViews();
    }
}
