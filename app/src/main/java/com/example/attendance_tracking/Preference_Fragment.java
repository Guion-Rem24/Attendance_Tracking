package com.example.attendance_tracking;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceFragmentCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PreferenceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Preference_Fragment extends PreferenceFragmentCompat {
//参考ページ
//    https://mnoqlo.hatenablog.com/entry/2018/10/12/214802
//    https://android.keicode.com/basics/ui-navigation-drawer-overview.php

    public static Preference_Fragment newInstance(String root_Key) {
        Preference_Fragment fragment = new Preference_Fragment();
        Bundle bundle = new Bundle();
        // 第1引数をPreferenceFragmentCompat.ARG_PREFERENCE_ROOTとすることでonCreatePreferencesの第2引数がここでputしたrootKeyになります
        bundle.putString(PreferenceFragmentCompat.ARG_PREFERENCE_ROOT, root_Key);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String root_Key){
//        super.onCreate(savedInstanceState, root_Key);
//        addPreferencesFromResource(R.xml.preference);
        setPreferencesFromResource(R.xml.preference, root_Key);
        switch (root_Key){
            case "preference_appearance":
                onCreateAppearancePreferences();
                break;
            case "preference_other":
                break;
            default:
                break;
        }
    }

    private void onCreateAppearancePreferences() {
        // テーマ設定の現在の値をSummaryに表示
        ListPreference themePreference = (ListPreference) findPreference("preference_theme");
        themePreference.setSummary(themePreference.getEntry());
        themePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                int indexOfValue = themePreference.findIndexOfValue(String.valueOf(newValue));
                themePreference.setSummary(indexOfValue >= 0 ? themePreference.getEntries()[indexOfValue] : null);
                return true;
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        // ActionBarのタイトルに現在表示中のPreferenceScreenのタイトルをセット
        String rootKey = requireArguments().getString(PreferenceFragmentCompat.ARG_PREFERENCE_ROOT);
        requireActivity().setTitle(findPreference(rootKey).getTitle());
    }

    /*
    public static class SettingsFragment extends PreferenceFragmentCompat {

        @Override
        public static SettingsFragment newInstance(String root_Key){
            // Require empty public constructor
            SettingsFragment fragment = new SettingsFragment();
            Bundle bundle = new Bundle();
            // 第1引数をPreferenceFragmentCompat.ARG_PREFERENCE_ROOTとすることでonCreatePreferencesの第2引数がここでputしたrootKeyになります
            bundle.putString(PreferenceFragmentCompat.ARG_PREFERENCE_ROOT, root_Key);
            fragment.setArguments(bundle);
            return fragment;
        }

//        @Override
//        public void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//
//            // Load the preferences from an XML resource
//            addPreferencesFromResource(R.xml.preference);
//        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String root_Key){
    //        super.onCreate(savedInstanceState, root_Key);
    //        addPreferencesFromResource(R.xml.preference);
            setPreferencesFromResource(R.xml.preference, root_Key);
            switch (root_Key){
                case "preference_appearance":
                    onCreateAppearancePreferences();
                    break;
                case "preference_other":
                    break;
                default:
                    break;
            }
        }

        private void onCreateAppearancePreferences() {
            // テーマ設定の現在の値をSummaryに表示
            ListPreference themePreference = (ListPreference) findPreference("preference_theme");
            themePreference.setSummary(themePreference.getEntry());
            themePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    int indexOfValue = themePreference.findIndexOfValue(String.valueOf(newValue));
                    themePreference.setSummary(indexOfValue >= 0 ? themePreference.getEntries()[indexOfValue] : null);
                    return true;
                }
            });
        }

        @Override
        public void onResume() {
            super.onResume();
            // ActionBarのタイトルに現在表示中のPreferenceScreenのタイトルをセット
            String rootKey = requireArguments().getString(PreferenceFragmentCompat.ARG_PREFERENCE_ROOT);
            requireActivity().setTitle(findPreference(rootKey).getTitle());
        }

    }
     */


}