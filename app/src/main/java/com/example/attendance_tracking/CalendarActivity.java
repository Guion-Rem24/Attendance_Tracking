package com.example.attendance_tracking;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class CalendarActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static String TAG = "CalendarActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        // ToolBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.calendar_toolbar);
        toolbar.setTitle("カレンダー");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.Calendar_drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // NavigationView Listener
        NavigationView navigationView = (NavigationView) findViewById(R.id.Calendar_navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        {
            Context context = getApplicationContext();
            CharSequence text = "Start CalendarActivity...";
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
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.Calendar_drawerLayout);
                drawer.closeDrawer(GravityCompat.START);
                return false;
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


}