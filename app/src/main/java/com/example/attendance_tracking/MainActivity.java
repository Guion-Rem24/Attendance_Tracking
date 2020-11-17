package com.example.attendance_tracking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

//import android.widget.Toolbar;

//import android.R.menu;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        if(pref.getBoolean("fix_Calendar_Panel",true)) {
            Intent intent = new android.content.Intent(this, CalendarActivity.class);
            startActivity(intent);
            return;
        }

        // レイアウトリソースの読み込み
        setContentView(R.layout.activity_main);

        // ToolBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Main");

        // ManifestのTheme設定がおかしかった
        // 修正済み
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // DrawerToggle
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // NavigationView Listener
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        /*
        new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    return false;
                }
            }
            と
         item -> false

         が同値
         */

//        leanBackMode();
        {
            Context context = getApplicationContext();
            CharSequence text = "Start MainActivity...";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mItem){
        switch(mItem.getItemId()){
            case R.id.optionsMenu_Pref:
                Toast.makeText(getApplicationContext(),"'設定' is selected...", Toast.LENGTH_LONG).show();
                Log.d(TAG, "...onOptionsItemSelected is active");
                Intent intent = new android.content.Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.optionsMenu_help:
                Toast.makeText(getApplicationContext(),"'ヘルプ' is selected...", Toast.LENGTH_LONG).show();
                return true;
            case R.id.optionsMenu_info:
                Toast.makeText(getApplicationContext(), "'お知らせ' is selected...", Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(), "'action_settings' is selected...", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(mItem);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item){
        int id = item.getItemId();
        Intent intent;

        Log.d(TAG,"------------id: "+id+"------------");

        switch(id){
            case R.id.menu_item1:
                Log.d(TAG, "Item 1 Selected!");
                intent = new android.content.Intent(this, CalendarActivity.class);
                break;
            case R.id.menu_item2:
                Log.d(TAG, "Item 2 Selected!");
                intent = null;
//                intent = new android.content.Intent(this, CalendarActivity.class);
                break;
            case R.id.menu_item3:
                intent = null;
                Log.d(TAG, "Item 3 Selected!");
//                intent = new android.content.Intent(this, CalendarActivity.class);
                break;
            case R.id.menu_item4:
                intent = null;
                Log.d(TAG, "Item 4 Selected!");
//                intent = new android.content.Intent(this, CalendarActivity.class);
                break;
            default:
                intent = null;
                break;
        }
        if(intent == null){
            Toast.makeText(getApplicationContext(), "intent in Drawer is null pointer", Toast.LENGTH_LONG).show();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
            drawer.closeDrawer(GravityCompat.START);
            return false;
        }

        startActivity(intent);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void leanBackMode(){
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );

        decorView.setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        // Note that system bars will only be "visible" if none of the
                        // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            Log.d("debug","The system bars are visible");
                        } else {
                            Log.d("debug","The system bars are NOT visible");
                        }
                    }
                });
    }

}