package com.example.attendance_tracking.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import com.example.attendance_tracking.R;

public class SettingsActivity extends AppCompatActivity implements PreferenceFragmentCompat.OnPreferenceStartScreenCallback {

    private static final String TAG = "SettingsActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // initilize ToolBar
        this.initializeToolBar(savedInstanceState);
        Intent intent = getIntent();


//        getFragmentManager().beginTransaction().replace(android.R.id.content, new Preference_Fragment()).commit();
    }

    @Override
    public boolean onPreferenceStartScreen(PreferenceFragmentCompat caller, PreferenceScreen pref){
        // Fragmentの切り替えと、addToBackStackで戻るボタンを押した時に前のFragmentに戻るようにする
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, Preference_Fragment.newInstance("preference_root"))
                .addToBackStack(null)
                .commit();
        return true;
    }

    private void initializeToolBar(Bundle savedInstanceState){
        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        ActionBar actionbar = this.getSupportActionBar();
        if(actionbar == null){
            return;
        }
        if (savedInstanceState == null) {
            // トップ画面のFragmentを表示
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, Preference_Fragment.newInstance("preference_root"))
                    .commit();
        }
        actionbar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "[onBackPressed]");
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "[onOptionsItemSelected]");
        return super.onOptionsItemSelected(item);
    }
}