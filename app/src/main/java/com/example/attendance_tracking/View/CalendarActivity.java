package com.example.attendance_tracking.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import com.example.attendance_tracking.OnBackKeyPressedListener;
import com.example.attendance_tracking.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class CalendarActivity extends AppCompatActivity
        implements  NavigationView.OnNavigationItemSelectedListener,
                    DayDetailFragment.OnDayDetailFragmentInteractionListener {

    static String TAG = "CalendarActivity";
    public static final String EXTRA_DATA = "com.example.attendance_tracking.calendaractivity.DATA";
    private Fragment calendarFragment;
    private Fragment daydetailFragment;

    private int moveMainActivity = 1;

    private Fragment currentFragment;
    int mMode;

    private CalendarActivity getThisActivity(){return this;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        {
            Context context = getApplicationContext();
            CharSequence text = "Start CalendarActivity...";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            Log.v(TAG, "-------- on Create --------");
        }

        currentFragment = null;

        calendarFragment = null;
        daydetailFragment = null;
        mMode = 0;

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

        FloatingActionButton addWorkHoursButton = findViewById(R.id.button_add_working_hours);
        addWorkHoursButton.setOnClickListener(new CalendarView.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getThisActivity(), AddWorkingHoursActivity.class);
                startActivity(intent);
                Log.v(TAG,"== fab pushed ==");
            }
        });
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

//        Log.d(TAG,"------------id: "+id+"------------");

        switch(id){
            case R.id.menu_calendar:
                Log.d(TAG, "CalendarMenu Selected...");
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.Calendar_drawerLayout);
                drawer.closeDrawer(GravityCompat.START);
                return false;
            case R.id.menu_item2:
                Log.d(TAG, "Item 2 Selected!");
                intent = null;
//                intent = new android.content.Intent(this, CalendarActivity.class);
                break;
            case R.id.menu_editEmployee:
                Log.d(TAG, "EditEmployeeMenu Selected...");
                intent = new android.content.Intent(this, EditEmployeeActivity.class);
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
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.Calendar_drawerLayout);
            drawer.closeDrawer(GravityCompat.START);
            return false;
        }

        startActivity(intent);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.Calendar_drawerLayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri){


    }

    @Override
    public void onBackPressed(){
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("daydetailBackPressed");
        if(fragment != null) {
            if (fragment instanceof OnBackKeyPressedListener) {
                ((OnBackKeyPressedListener) fragment).onBackPressed();
            }
            return;
        }

        Log.v(TAG, "--- on Back Pressed ---");
        MainActivity activity = new MainActivity();
        Intent intent = new android.content.Intent(this, MainActivity.class);
//        intent.putExtra(EXTRA_DATA, moveMainActivity);
        int RequestCode = 2;
        setResult(RESULT_OK, intent);
        startActivityForResult(intent, RequestCode);

        super.onBackPressed();
    }

    @Override
    public void onResume(){
        super.onResume();

        if(mMode == 1) { // DayDetailFragment への切り替え
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.detach(calendarFragment);
            if(daydetailFragment == null) {
                daydetailFragment = DayDetailFragment.newInstance();
                transaction.add(R.id.fragment_calendar_root, daydetailFragment, "daydetailBackPressed")
                    .addToBackStack(null);
                // addToBackStack(null) がないと，BackPressedでActivityがPauseするので，必須．
            }else {
                transaction.attach(daydetailFragment);
            }

            transaction.commit();
            Log.v(TAG, "==== Changing DayDetailFragment... ====");

        } else if(mMode == 0){ // CalendarFragment への切り替え
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            if(calendarFragment == null){
                calendarFragment = CalendarFragment.newInstance();
            }

            if(daydetailFragment == null){
                transaction.replace(R.id.fragment_calendar_root, calendarFragment).commit();
                Log.v(TAG, "==== Fragment Replaced ====");
            }else{
                transaction.detach(daydetailFragment);
                transaction.attach(calendarFragment);
                transaction.commit();
                daydetailFragment = null;
                Log.v(TAG, "==== Fragment Detached ====");
            }
            Log.v(TAG, "==== Changing CalendarFragment... ====");
        }
        Log.v(TAG, "==== on Resume ====");
    }

    @Override
    public void onPause(){
        super.onPause();
//        if(currentFragment != null){
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .remove(currentFragment)
//                    .commit();
//            currentFragment = null;
//        }
        Log.v(TAG, "====on Pause ====");
    }

    public void setupBackButton(boolean enableBackButton){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(enableBackButton);
    }
}