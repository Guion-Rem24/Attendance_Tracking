package com.example.attendance_tracking;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class CalendarActivity extends AppCompatActivity
        implements  NavigationView.OnNavigationItemSelectedListener,
                    DayDetailFragment.OnFragmentInteractionListener {

    static String TAG = "CalendarActivity";
    private Fragment currentFragment;
    int mMode = 0;

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
        }

        currentFragment = null;

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

        /*
        calendarView.setOnDateChangeListener((CalendarView view, int year, int month, int dayOfMonth) -> {

            DayDetailFragment fragment = new DayDetailFragment();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_daydetail, fragment);

            transaction.commit();
            Toast.makeText(
                    getApplicationContext(),
                    "Selected Date... " + "Year:" + year + "Month:" + month + "Day:" + dayOfMonth,
                    Toast.LENGTH_LONG).show();
//            dayView.setText("Date: "+year+"/"+month+"/"+dayOfMonth);
        });

         */

        CalendarFragment fragment = CalendarFragment.newInstance();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_calendar_root, fragment)
                .commit();



        /*
        Button button = findViewById(R.id.main_button);
        //ボタンが押下されたら、Fragmentを表示する
        main_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fragmentを呼びだす
                // Fragmentを作成します
                MainFragment fragment = new MainFragment();
                // Fragmentの追加や削除といった変更を行う際は、Transactionを利用します
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                // 新しく追加を行うのでaddを使用します
                transaction.add(R.id.fragment_layout, fragment);
                // 最後にcommitを使用することで変更を反映します
                transaction.commit();
            }
        });

         */
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

    @Override
    public void onFragmentInteraction(Uri uri){


    }

    @Override
    public void onResume(){
        super.onResume();
//        if(mMode == 1){
//            currentFragment = DayDetailFragment.newInstance();
//        }else if(mMode == 0){
//            currentFragment = CalendarFragment.newInstance();
//        }
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.fragment_calendar_root, currentFragment)
//                .commit();
    }

    @Override
    public void onPause(){
        super.onPause();
        if(currentFragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(currentFragment)
                    .commit();
            currentFragment = null;
        }
    }

    public void setupBackButton(boolean enableBackButton){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(enableBackButton);
    }


}