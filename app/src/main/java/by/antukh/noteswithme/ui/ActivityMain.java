package by.antukh.noteswithme.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import org.osmdroid.config.Configuration;

import by.antukh.noteswithme.R;
import by.antukh.noteswithme.system.Utils;

public class ActivityMain
        extends AppCompatActivity {

    private FrameLayout mMainContainer;
    private FragmentMap fragmentMap;
    private FragmentNotesList fragmentNotesList;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.tab_notes_list:
                    Utils.setFragment(getSupportFragmentManager(), R.id.main_container, fragmentNotesList);
                    return true;
                case R.id.tab_map:
                    Utils.setFragment(getSupportFragmentManager(), R.id.main_container, fragmentMap);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init views
        Toolbar toolbar = findViewById(R.id.main_act_toolbar);
        setSupportActionBar(toolbar);
        mMainContainer = findViewById(R.id.main_container);
        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        findViewById(R.id.btnSettings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityMain.this, ActivitySettings.class));
            }
        });

        // init fragments
        if (fragmentMap == null) fragmentMap = new FragmentMap();
        if (fragmentNotesList == null) fragmentNotesList = new FragmentNotesList();

        //osmdroid map req
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        //TODO select first tab by index
        Utils.setFragment(getSupportFragmentManager(), R.id.main_container, fragmentNotesList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        final int PERM_GRANTED = PackageManager.PERMISSION_GRANTED;
        final String LOCATION_FINE = Manifest.permission.ACCESS_FINE_LOCATION;
        final String LOCATION_COARSE = Manifest.permission.ACCESS_COARSE_LOCATION;

        if (ActivityCompat.checkSelfPermission(this, LOCATION_FINE) != PERM_GRANTED
                && ActivityCompat.checkSelfPermission(this, LOCATION_COARSE) != PERM_GRANTED)
            ActivityCompat.requestPermissions(this,
                    new String[]{LOCATION_FINE, LOCATION_COARSE}, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
}
