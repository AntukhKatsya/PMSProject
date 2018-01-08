package by.antukh.noteswithme.system;


import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import by.antukh.noteswithme.R;
import by.antukh.noteswithme.synk.ExchangePacket;

/**
 * Created by acdc on 06.01.18.
 */

public class Utils {

    public static void setFragment(FragmentManager supportFragmentManager, int idContainer, Fragment fragment) {

        supportFragmentManager
                .beginTransaction()
                .replace(idContainer, fragment)
                //.addToBackStack(null)
                .commit();
    }

    public static String getExchangePacketJsonString() {

        String username = Settings.getInstance().getUsername();
        String password = Settings.getInstance().getPassword();


        ExchangePacket packet = (new ExchangePacket.ExchangePacketBuilder())
                .setUsername(username)
                .setPassword(password)
                .setUnsynkNotes(DB.getInstance().getUnsynkedNotes())
                .createExchangePacket();

        return new GsonBuilder()
                .setPrettyPrinting()
                .create().toJson(packet);
    }

    public static String getLocationName() {
        Context context = Application.get();
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), true);

        @SuppressLint("MissingPermission")
        Location locations = locationManager.getLastKnownLocation(provider);
        List<String> providerList = locationManager.getAllProviders();
        if (null != locations && null != providerList && providerList.size() > 0) {
            double longitude = locations.getLongitude();
            double latitude = locations.getLatitude();
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            try {
                List<Address> listAddresses = geocoder.getFromLocation(latitude, longitude, 1);
                if (null != listAddresses && listAddresses.size() > 0) {
                    return listAddresses.get(0).getAddressLine(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return "";
    }
}
