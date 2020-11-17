package com.example.attendance_tracking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class DayClickListener extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onClick(View view){
        TextView textView = (TextView) view.getRootView();
        String day = textView.getText().toString();

        // とりあえずMainActivityに戻るように設定
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

/*
public class ButtonListener extends AppCompatActivity implements View.OnClickListener {
    @Override
    public void onClick(View view){
        View v = view.getRootView();
        EditText editName = (EditText)v.findViewById(R.id.editTextName);
        String strName = editName.getText().toString();
        EditText editEmail = (EditText)v.findViewById(R.id.editTextAddress);
        String strEmail = editEmail.getText().toString();
        Context context = MainActivity.getAppContext();
        Intent intent = new Intent(context, NextActivity.class);
        intent.putExtra("name",strName);
        intent.putExtra("email",strEmail);
        startActivity(intent);
    }
}
 */