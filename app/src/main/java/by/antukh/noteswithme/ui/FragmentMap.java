package by.antukh.noteswithme.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;

import by.antukh.noteswithme.R;
import by.antukh.noteswithme.system.DB;
import by.antukh.noteswithme.system.GPSTracker;
import by.antukh.noteswithme.system.Note;
import by.antukh.noteswithme.system.Settings;
import by.antukh.noteswithme.ui.anim.PushPullAnimation;

public class FragmentMap extends Fragment {

    private static final String LOG_TAG = "FragmentMap";


    public FragmentMap() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private MapView mapView;
    private IMapController mapController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FrameLayout rl = (FrameLayout) inflater.inflate(R.layout.fragment_map, container, false);
        mapView = rl.findViewById(R.id.map);
        setupMapView();
        return rl;
        //return inflater.inflate(R.layout.fragment_map, container, false);
    }

    private void setupMapView() {
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setFlingEnabled(true);
        mapView.setMultiTouchControls(true);

        mapController = mapView.getController();
        mapController.setZoom(Settings.getInstance().getLastZoom());
        mapController.setCenter(Settings.getInstance().getLastPosition());
    }

    private void showMyLocation() {
        MyLocationNewOverlay myLocationOverlay =
                new MyLocationNewOverlay(new GpsMyLocationProvider(getContext()), mapView);
        mapView.getOverlays().add(myLocationOverlay);

    }

    private void showCompass() {
        CompassOverlay mCompassOverlay = new CompassOverlay(this.getContext(),
                new InternalCompassOrientationProvider(this.getContext()), mapView);
        mCompassOverlay.enableCompass();
        mapView.getOverlays().add(mCompassOverlay);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateOverlay();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {
                        double homeLat = 0.0;
                        double homeLong = 0.0;
                        final GPSTracker tracker = new GPSTracker(FragmentMap.this.getContext());
                        homeLat = tracker.getLatitude();
                        homeLong = tracker.getLongitude();
                        if (homeLat != 0.0 && homeLong != 0)
                            mapController.animateTo(new GeoPoint(homeLat, homeLong));
                    }
                });

            }
        }).start();
    }

    private void setHomePoint() {
        //GeoPoint centerPt = new GeoPoint(50.89489, 4.34140); // Point of Grand Place Brussels

        final GPSTracker tracker = new GPSTracker(this.getContext());
        double homeLat = tracker.getLatitude();
        double homeLong = tracker.getLongitude();


        GeoPoint centerPt = new GeoPoint(homeLat, homeLong);

        mapController.setCenter(centerPt);

        Drawable marker = getResources().getDrawable(R.drawable.person);

        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
        items.add(new OverlayItem("Here I am!", "Hello", centerPt));

        ItemizedOverlay<OverlayItem> itemOverlay =
                new ItemizedIconOverlay<OverlayItem>(items, marker, null,
                        getContext());
        mapView.getOverlays().add(itemOverlay);
        mapView.invalidate();

    }

    private void updateOverlay() {

        //your items
        ArrayList<OverlayItem> items = new ArrayList<>();
        ArrayList<Note> notes = DB.getInstance().getStandardNotes();

        for (Note note : notes)
            items.add(new OverlayItem(getString(R.string.note) + note.getId(), note.getText(),
                    new GeoPoint(note.getLatitude(), note.getLongitude())));


        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<>(items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(int index, OverlayItem item) {
                        return false;
                    }

                    @Override
                    public boolean onItemLongPress(int index, OverlayItem item) {
                        return false;
                    }
                }, this.getContext());
        mOverlay.setFocusItemsOnTap(true);

        mapView.getOverlays().clear();
        mapView.getOverlays().add(mOverlay);
        setHomePoint();
        showCompass();

        // TODO test

        ScaleBarOverlay mScaleBarOverlay = new ScaleBarOverlay(mapView);
        //mScaleBarOverlay.setCentred(true);
        mapView.getOverlays().add(mScaleBarOverlay);
    }


    @Override
    public void onResume() {
        super.onResume();
        // osmdroid req
        Configuration.getInstance().load(this.getContext(), PreferenceManager.getDefaultSharedPreferences(this.getContext()));
        mapController.animateTo(Settings.getInstance().getLastPosition());
    }

    @Override
    public void onPause() {
        super.onPause();
        Settings.getInstance().saveLastPositionAndZoom(
                mapView.getMapCenter(), mapView.getZoomLevel());
    }


    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return PushPullAnimation.create(PushPullAnimation.DOWN, enter, 300);
    }


}
