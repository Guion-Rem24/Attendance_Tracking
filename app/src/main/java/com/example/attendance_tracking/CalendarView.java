package com.example.attendance_tracking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.util.Calendar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attendance_tracking.DayClickListener;
import com.example.attendance_tracking.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CalendarView extends LinearLayout {

    public int beginningWeek = Calendar.SUNDAY;
    public int todayColor = Color.RED;
    public int todayBackgroundColor = Color.LTGRAY;
    public int defaultColor;
    public int defaultBackgroundColor = Color.TRANSPARENT;
    public float titlefontPx;
    /** 日付のフォントサイズ */
    public float dayFontPx;
    private LinearLayout mWeekLayout;
    private ArrayList<LinearLayout> mWeeks = new ArrayList<LinearLayout>();
    private Calendar mToday;
    private TextView title_text;
    private static final int WEEK_7 = 7;
    private static final int MAX_WEEK = 6;

    private static Context context;

    public static Context getCalendarContext(){
        return context;
    }

    @SuppressLint("ResourceType")
    public CalendarView(Context context, AttributeSet attrs) {

        super(context, attrs);
        this.setOrientation(VERTICAL);

        //        Context t_context = getApplicationContext();
        CharSequence text = "Start CalendarView...";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        final float scale = context.getResources().getDisplayMetrics().density;
        final int padding = (int) (scale * 5);
        final int paddingR = (int) (scale * 15);
        final int paddingT = (int) (scale * 20);
        titlefontPx = scale * 20;

        // 今日の日付保持
        mToday = Calendar.getInstance();
        // タイトル部 年月表示
        title_text = new TextView(context);
        title_text.setGravity(Gravity.CENTER_HORIZONTAL); // 中央に表示
        title_text.setTypeface(null, Typeface.BOLD);
        title_text.setText("DEBUG");
        title_text.setPadding(0,0,0,paddingT);
        addView(title_text, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        dayFontPx = title_text.getTextSize();
        defaultColor = title_text.getTextColors().getDefaultColor();

        // 週表示部　日月火水木金土
        Log.d("CalendarView", "週表示部　日月火水木金土");
        mWeekLayout = new LinearLayout(context); // 週表示レイアウト
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, beginningWeek); // 週の頭をセット
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("E"); // 曜日を取得するフォーマッタ

        for (int i = 0; i < WEEK_7; i++) {
            TextView textView = new TextView(context);
            textView.setGravity(Gravity.CENTER_HORIZONTAL); // 中央に表示
            LayoutParams llp = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
            llp.weight = 1;
            mWeekLayout.addView(textView, llp);
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        addView(mWeekLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        //クリックリスナーインスタンスを作成
        DayClickListener dayClickListener = new DayClickListener();

        // カレンダー部 最大6行必要
        Log.v("CalendarView", "カレンダー部 最大6行必要");
        for (int i = 0; i < MAX_WEEK; i++) {
            LinearLayout weekLine = new LinearLayout(context);
            mWeeks.add(weekLine);
            // 1週間分の日付ビュー作成
            for (int j = 0; j < WEEK_7; j++) {
                TextView dayView = new TextView(context);
                //dayView.setText(String.valueOf((i * WEEK_7) + (j + 1))); // TODO:DEBUG
                dayView.setGravity(Gravity.RIGHT); // 右寄せで表示
                dayView.setClickable(true);//Textviewをクリック可能にする
                // textをクリックできないと落ちる
                dayView.setId(View.generateViewId());
                //個々の日付の余白設定、左、右、トップ、ボトムの順
                dayView.setPadding(0, padding, paddingR, padding*10);
                LayoutParams llp = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
                llp.weight = 1;
                weekLine.addView(dayView, llp);
                //リスナーをセット
                dayView.setOnClickListener(dayClickListener);
            }

            this.addView(weekLine, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        }
        init(mToday.get(Calendar.YEAR), mToday.get(Calendar.MONTH));

         //test
//        android.widget.CalendarView calendarView = new android.widget.CalendarView(context);
//        calendarView.layout(0, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,0);
//
//        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.Main);
//        linearLayout.addView(calendarView);
    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
//        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
//    }

    // 表示設定
    public void init(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.clear(); // まずクリアしてからセットしないといけない
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int todayYear = mToday.get(Calendar.YEAR);
        int todayMonth = mToday.get(Calendar.MONTH);
        int todayDay = mToday.get(Calendar.DAY_OF_MONTH);

        // カレンダーの最初の空白の個数を求める。
        int skipCount; // 空白の個数
        int firstDayMonthWeek = cal.get(Calendar.DAY_OF_WEEK); // 1日の曜日

        if (beginningWeek > firstDayMonthWeek) {
            skipCount = firstDayMonthWeek - beginningWeek + WEEK_7;
        } else {
            skipCount = firstDayMonthWeek - beginningWeek;
        }

        int lastDay = cal.getActualMaximum(Calendar.DATE); // 月の最終日
        // 日付を生成する。
        int dayCounter = 1;

        for (int i = 0; i < MAX_WEEK; i++) {
            LinearLayout weekLayout = mWeeks.get(i);
            weekLayout.setBackgroundColor(defaultBackgroundColor);

            for (int j = 0; j < WEEK_7; j++) {
                TextView dayView = (TextView) weekLayout.getChildAt(j);
                if (i == 0 && skipCount > 0) {
                    dayView.setText(" ");
                    skipCount--;
                } else if (dayCounter <= lastDay) {
                    dayView.setText(String.valueOf(dayCounter));
                    // 今日の日付を赤文字にする
                    if (todayYear == year && todayMonth == month && todayDay == dayCounter) {
                        dayView.setTextColor(todayColor); // 赤文字
                        dayView.setTypeface(null, Typeface.BOLD); // 太字
                        weekLayout.setBackgroundColor(todayBackgroundColor); // 週の背景グレー
                    } else {
                        dayView.setTextColor(defaultColor);
                        dayView.setTypeface(null, Typeface.NORMAL);
                    }
                    dayCounter++;
                } else {
                    dayView.setText(" ");
                }
            }
        }
    }
}
