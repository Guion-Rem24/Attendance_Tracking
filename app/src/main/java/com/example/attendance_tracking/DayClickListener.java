package com.example.attendance_tracking;

// 解決するまで使用禁止


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DayClickListener implements View.OnClickListener {
    final String TAG = "DayClickListener";
    @Override
    public void onClick(View view){
        TextView textView = (TextView) view.getRootView();
//        int id = textView.getId();

//        final String str = id+":: generated id";
//        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
        String day = textView.getText().toString();
        CharSequence text = day+" is touched";
//        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
        Log.v(TAG, "---------------");
        // とりあえずMainActivityに戻るように設定
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
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