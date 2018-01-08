package by.antukh.noteswithme.system;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

/**
 * Created by acdc on 06.01.18.
 */

public class Application extends android.app.Application {

    public static String SETTINGS_NAME = "blablabla";

    private static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        updateGPS();
    }

    private void updateGPS() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

/*
        LocationManager locationManager =
                (LocationManager) Application.get().getSystemService(Context.LOCATION_SERVICE);


        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                0, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        Toast.makeText(Application.this, "onLocationChanged " +
                                        location.toString(),
                                Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {
                        Toast.makeText(Application.this, "onStatusChanged " + s,
                                Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onProviderEnabled(String s) {
                        Toast.makeText(Application.this, "onProviderEnabled " + s,
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProviderDisabled(String s) {
                        Toast.makeText(Application.this, "onProviderDisabled " + s,
                                Toast.LENGTH_SHORT).show();

                    }
                });
   */
    }

    public static Application get() {
        return instance;
    }

    public static SharedPreferences getPrefs() {
        return get().getApplicationContext().getSharedPreferences(SETTINGS_NAME, MODE_PRIVATE);
    }
}
