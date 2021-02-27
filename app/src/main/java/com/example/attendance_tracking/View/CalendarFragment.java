package com.example.attendance_tracking.View;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attendance_tracking.R;

import com.example.attendance_tracking.View.DayDetailFragment.OnDayDetailFragmentInteractionListener;


public class CalendarFragment extends Fragment implements CalendarView.OnDateChangeListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

//    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnDayDetailFragmentInteractionListener mListener;
    private CalendarView calendarView;
    private TextView dayView;

    private final String TAG = "CalendarFragment";


    public CalendarFragment getFragment(){ return this; }

    public CalendarFragment() {
        // Required empty public constructor
        mListener = new OnDayDetailFragmentInteractionListener() {
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
        Log.v(TAG, "====on Create ====");


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

        Log.v(TAG, "====on CreateView ====");


        return view;
    }

    @Override
    public void onDetach(){
        super.onDetach();
        mListener = new DayDetailFragment.OnDayDetailFragmentInteractionListener(){
            @Override
            public void onFragmentInteraction(Uri uri) {

            }
        };
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.v(TAG, "====on Resume ====");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.v(TAG, "====on Pause ====");
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


    // 日付をクリックされた時の処理
    public void onSelectedDayChange(CalendarView view, int year, int month,
                                    int dayOfMonth) {

        Log.v(TAG, "====on SelectedDayChange ====");
        CalendarActivity activity = (CalendarActivity) getActivity();
        activity.mMode = 1;
        Toast.makeText(activity.getApplicationContext(), "touched day button...", Toast.LENGTH_LONG).show();
        activity.onResume();

    }
}