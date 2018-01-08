package by.antukh.noteswithme.system;

/**
 * Created by acdc on 06.01.18.
 */

public class Note {


    private int id;
    private String text;
    private long createdTime;
    private long modifiedTime;
    private double latitude;
    private double longitude;
    private boolean isSynchronised;
    private boolean isDeleted;


    public Note(String text, double latitude, double longitude) {
        this.text = text;
        this.latitude = latitude;
        this.longitude = longitude;
        createdTime = System.currentTimeMillis();
        modifiedTime = createdTime;
        isSynchronised = false;
    }

    public Note(int id, String text, long createdTime, long modifiedTime, double latitude, double longitude, boolean isSynchronised, boolean isDeleted) {
        this.id = id;
        this.text = text;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isSynchronised = isSynchronised;
        this.isDeleted = isDeleted;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        isSynchronised = false;
        modifiedTime = System.currentTimeMillis();
    }


    public boolean isSynchronised() {
        return isSynchronised;
    }

    public void setSynchronised(boolean synchronised) {
        this.isSynchronised = synchronised;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public long getModifiedTime() {
        return modifiedTime;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }


    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isSynchronised = false;
        isDeleted = deleted;
    }

    public int getId() {
        return id;
    }
}
