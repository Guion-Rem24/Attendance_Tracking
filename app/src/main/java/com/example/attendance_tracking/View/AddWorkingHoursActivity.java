package com.example.attendance_tracking.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.attendance_tracking.R;
import com.example.attendance_tracking.utils.PushedButton;

public class AddWorkingHoursActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_working_hours);

        PushedButton search_button = findViewById(R.id.button_search);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "searching..", Toast.LENGTH_LONG).show();
            }
        });
    }

    static public
    AddWorkingHoursActivity newInstance(){
        return new AddWorkingHoursActivity();
    }
}