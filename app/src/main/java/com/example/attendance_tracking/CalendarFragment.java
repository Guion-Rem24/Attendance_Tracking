package com.example.attendance_tracking;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import java.util.Calendar;


public class CalendarFragment extends Fragment implements CalendarView.OnDateChangeListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

//    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private DayDetailFragment.OnFragmentInteractionListener mListener;
    private CalendarView calendarView;
    private TextView dayView;

    public CalendarFragment() {
        // Required empty public constructor
        mListener = new DayDetailFragment.OnFragmentInteractionListener() {
            @Override
            public void onFragmentInteraction(Uri uri) {
                // NOP.
            }
        };
    }

    public static CalendarFragment newInstance(){
        return new CalendarFragment();
    }

    public static CalendarFragment newInstance(String mParam1, String mParam2){
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1,mParam1);
        args.putString(ARG_PARAM2,mParam2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_calendar, container, false);
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        CalendarActivity calActivity = (CalendarActivity) getActivity();
        calActivity.setTitle("カレンダー Fragment");

        // CalendarView Listener
        calendarView = (CalendarView) view.findViewById(R.id.calendar_view);
        dayView = (TextView) view.findViewById(R.id.day_view);

        calendarView.setOnDateChangeListener(this);


        return view;
    }

    @Override
    public void onDetach(){
        super.onDetach();
        mListener = new DayDetailFragment.OnFragmentInteractionListener(){
            @Override
            public void onFragmentInteraction(Uri uri){
                // NOP
            }
        };
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


    // 日付をクリックされた時の処理
    public void onSelectedDayChange(CalendarView view, int year, int month,
                                    int dayOfMonth) {

//        Intent varIntent = new Intent(getApplication(), MainActivity.class);
//
////            dayView.setText("Date: "+year+"/"+month+"/"+dayOfMonth);
//        startActivity(varIntent);

//        DayDetailFragment fragment = DayDetailFragment.newInstance();

//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.add(R.id.fragment_daydetail, fragment);
//        transaction.commit();
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.fragment_calendar_root,fragment)
//                .commit();

//        mMode = 1;
        CalendarActivity activity = (CalendarActivity) getActivity();
        activity.mMode = 1;

    }
}