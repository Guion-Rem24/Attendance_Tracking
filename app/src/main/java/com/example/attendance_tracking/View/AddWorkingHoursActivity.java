package com.example.attendance_tracking.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.attendance_tracking.R;
import com.example.attendance_tracking.utils.PushedButton;

import java.util.Objects;

public class AddWorkingHoursActivity extends AppCompatActivity {

    private final String TAG = "AddWorkingHoursActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_working_hours);

        // ToolBar //////
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_add_working_hours);
        toolbar.setTitle("シフトの追加");

        setSupportActionBar(toolbar);
        // Backボタンを有効にする
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        /////////////////

        EditText nameString = (EditText) findViewById(R.id.editText_add_work_hours);
        PushedButton search_button = findViewById(R.id.button_search);
        search_button.setOnClickListener(v -> {
            String name = nameString.getText().toString();
            Toast.makeText(getApplicationContext(), name + " searching..", Toast.LENGTH_LONG).show();
        });
    }



    static public
    AddWorkingHoursActivity newInstance(){
        return new AddWorkingHoursActivity();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            Log.v(TAG, "---- move HOME ----");
            finish();
            return true;
        }

        switch(id){
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
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}