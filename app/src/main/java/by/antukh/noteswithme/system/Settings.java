package by.antukh.noteswithme.system;

import android.content.SharedPreferences;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.GeoPoint;

/**
 * Created by acdc on 06.01.18.
 */

public class Settings {

    private static final String LATITUDE = "LATITUDE";
    private static final String LONGTITUDE = "LONGTITUDE";
    private static final String ZOOM_LEVEL = "ZOOM_LEVEL";
    public static final String USERNAME = "USERNAME";
    public static final String PASSWORD = "PASSWORD";
    public static final String SERVER_HOST = "SERVER_HOST";
    public static final String SERVER_PORT = "SERVER_PORT";
    public static final String SHOW_DELETED = "SHOW_DELETED";
    private static Settings instance;

    private Settings() {
    }

    public static Settings getInstance() {

        if (instance == null)
            instance = new Settings();
        return instance;
    }

    public void saveLastPositionAndZoom(IGeoPoint mapCenter, int zoomLevel) {
        SharedPreferences.Editor editor = Application.getPrefs().edit();
        // sharedPrefs not support double - convert to string
        editor.putString(LATITUDE, String.valueOf(mapCenter.getLatitude()));
        editor.putString(LONGTITUDE, String.valueOf(mapCenter.getLongitude()));
        editor.putInt(ZOOM_LEVEL, zoomLevel);
        editor.apply();
    }

    public IGeoPoint getLastPosition() {
        SharedPreferences sPrefs = Application.getPrefs();
        // sharedPrefs not support double - convert to string
        double lat = Double.parseDouble(sPrefs.getString(LATITUDE, "48.8583"));
        double lon = Double.parseDouble(sPrefs.getString(LONGTITUDE, "2.2944"));
        return new GeoPoint(lat, lon);
    }

    public int getLastZoom() {
        SharedPreferences sPrefs = Application.getPrefs();
        return sPrefs.getInt(ZOOM_LEVEL, 9);
    }

    public String getUsername() {
        SharedPreferences sPrefs = Application.getPrefs();
        return sPrefs.getString(USERNAME, "not set");
    }

    public String getPassword() {
        SharedPreferences sPrefs = Application.getPrefs();
        return sPrefs.getString(PASSWORD, "not set");
    }


    public String getServerHost() {
        SharedPreferences sPrefs = Application.getPrefs();
        return sPrefs.getString(SERVER_HOST, "192.168.100.222");
    }


    public int getServerPort() {
        SharedPreferences sPrefs = Application.getPrefs();
        try {
            return Integer.valueOf(sPrefs.getString(SERVER_PORT, "1000"));
        } catch (Exception e) {
            return -1;
        }
    }

    public boolean isShowDeleted() {
        SharedPreferences sPrefs = Application.getPrefs();
        return sPrefs.getBoolean(SHOW_DELETED, false);
    }
}
