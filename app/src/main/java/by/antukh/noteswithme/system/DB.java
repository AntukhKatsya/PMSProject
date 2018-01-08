package by.antukh.noteswithme.system;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by acdc on 06.01.18.
 */

public class DB {

    private static final String LOG_TAG = "DB_notes";
    private static final String TABLE_NOTES = "Notes";
    private static DB instance;
    private DBHelper dbHelper;

    private DB() {

        dbHelper = new DBHelper(Application.get());
    }

    public static DB getInstance() {
        if (instance == null)
            instance = new DB();
        return instance;
    }


    private final static String ID = "ID",
            NOTE_TEXT = "NOTE_TEXT",
            CREATED_TIME = "CREATED_TIME",
            MODIFED_TIME = "MODIFED_TIME",
            LATITUDE = "LATITUDE",
            LONGITUDE = "LONGITUDE",
            DELETED = "DELETED",
            SYNCHRONIZED = "SYNCHRONIZED";

    private String[] columns = {
            ID,
            NOTE_TEXT,
            CREATED_TIME,
            MODIFED_TIME,
            LATITUDE,
            LONGITUDE,
            DELETED,
            SYNCHRONIZED
    };

    public long addNote(Note note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("NOTE_TEXT", note.getText());
        cv.put("CREATED_TIME", note.getCreatedTime());
        cv.put("MODIFED_TIME", note.getModifiedTime());
        cv.put("LATITUDE", note.getLatitude());
        cv.put("LONGITUDE", note.getLongitude());
        cv.put("DELETED", (!note.isDeleted() ? 0 : 1));
        cv.put("SYNCHRONIZED", (!note.isSynchronised() ? 0 : 1));
        long id = db.insert(TABLE_NOTES, null, cv);
        db.close();
        return id;
    }

    public ArrayList<Note> getStandardNotes() {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = DELETED + " = 0";
        Cursor cursor = db.query(TABLE_NOTES, columns, selection, null, null, null, null, null);
        ArrayList<Note> notes = getNotesFromCursor(cursor);
        cursor.close();
        db.close();
        return notes;
    }

    private static ArrayList<Note> getNotesFromCursor(Cursor cursor) {
        ArrayList<Note> notes = new ArrayList<>();
        if (cursor.moveToFirst()) {
            int _idIndex = cursor.getColumnIndex(ID);
            int _noteText = cursor.getColumnIndex(NOTE_TEXT);
            int _createdTime = cursor.getColumnIndex(CREATED_TIME);
            int _modifedTime = cursor.getColumnIndex(MODIFED_TIME);
            int _latitudeIndex = cursor.getColumnIndex(LATITUDE);
            int _longitudeIndex = cursor.getColumnIndex(LONGITUDE);
            int _deleted = cursor.getColumnIndex(DELETED);
            int _synchronized = cursor.getColumnIndex(SYNCHRONIZED);

            while (!cursor.isAfterLast()) {
                Note note = new Note(
                        cursor.getInt(_idIndex),
                        cursor.getString(_noteText),
                        cursor.getLong(_createdTime),
                        cursor.getLong(_modifedTime),
                        cursor.getDouble(_latitudeIndex),
                        cursor.getDouble(_longitudeIndex),
                        (cursor.getInt(_synchronized) != 0),
                        (cursor.getInt(_deleted) != 0));
                notes.add(note);
                cursor.moveToNext();
            }
        }
        return notes;
    }

    public void deleteNote(int idDeletedNote) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DELETED, 1);
        cv.put(SYNCHRONIZED, 0);
        db.update(TABLE_NOTES, cv, "id = ?", new String[]{String.valueOf(idDeletedNote)});
        db.close();
    }

    public ArrayList<Note> getUnsynkedNotes() {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = SYNCHRONIZED + " = 0";
        Cursor cursor = db.query(TABLE_NOTES, columns, selection, null, null, null, null, null);
        ArrayList<Note> notes = getNotesFromCursor(cursor);
        cursor.close();
        db.close();
        return notes;
    }

    public void setDataToSynhronizedState() {

        // TODO return
        if (1 == 1)
            return;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SYNCHRONIZED, 1);
        db.update(TABLE_NOTES, cv, null, null);
        db.close();
    }

    public ArrayList<Note> getAllNotes() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NOTES, columns, null, null, null, null, null, null);
        ArrayList<Note> notes = getNotesFromCursor(cursor);
        cursor.close();
        db.close();
        return notes;
    }

    public void updateNote(Note noteToEdit) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String where = "id = " + noteToEdit.getId();

        cv.put(NOTE_TEXT, noteToEdit.getText());
        cv.put(MODIFED_TIME, noteToEdit.getModifiedTime());
        cv.put(SYNCHRONIZED, 0);
        db.update(TABLE_NOTES, cv, where, null);
        db.close();
    }


    class DBHelper extends SQLiteOpenHelper {

        DBHelper(Context context) {
            super(context, LOG_TAG, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, "onCreate");
            db.execSQL("create table " + TABLE_NOTES + " ("
                    + ID + "  integer primary key autoincrement,"
                    + NOTE_TEXT + " text,"
                    + CREATED_TIME + " integer,"
                    + MODIFED_TIME + " integer,"
                    + LATITUDE + " long,"
                    + LONGITUDE + " long,"
                    + DELETED + " bool,"
                    + SYNCHRONIZED + " bool"
                    + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
