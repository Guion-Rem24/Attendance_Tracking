package com.example.attendance_tracking;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity implements PreferenceFragmentCompat.OnPreferenceStartScreenCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.settings_activity);
//        if (savedInstanceState == null) {
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.settings, new SettingsFragment())
//                    .commit();
//        }
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
        setContentView(R.layout.activity_settings);

        // Toolbarの設定
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            // トップ画面のFragmentを表示
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, Preference_Fragment.newInstance("preference_root"))
                    .commit();
        }

//        getFragmentManager().beginTransaction().replace(android.R.id.content, new Preference_Fragment()).commit();
    }

    @Override
    public boolean onPreferenceStartScreen(PreferenceFragmentCompat caller, PreferenceScreen pref){
        // Fragmentの切り替えと、addToBackStackで戻るボタンを押した時に前のFragmentに戻るようにする
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, Preference_Fragment.newInstance(pref.getKey()))
                .addToBackStack(null)
                .commit();
        return true;
    }
//
//    public static class SettingsFragment extends PreferenceFragmentCompat {
//        @Override
//        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
//            setPreferencesFromResource(R.xml.preference, rootKey);
//        }
//    }
}