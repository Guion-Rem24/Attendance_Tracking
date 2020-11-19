package com.example.attendance_tracking;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import org.apache.commons.lang3.StringUtils;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DayDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DayDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // コールバックの通知先
    private OnFragmentInteractionListener mListener;

    public DayDetailFragment() {
        // Required empty public constructor
        mListener = new OnFragmentInteractionListener() {
            @Override
            public void onFragmentInteraction(Uri uri) {
                // NOP.
            }
        };
    }
    public static DayDetailFragment newInstance() {
        return new DayDetailFragment();
    }
    public static DayDetailFragment newInstance(String mParam1, String mParam2){
        DayDetailFragment fragment = new DayDetailFragment();
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
        } else {
            mParam1 = StringUtils.EMPTY;
            mParam2 = StringUtils.EMPTY;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_day_detail, container, false);
        TextView titleText = view.findViewById(R.id.dayfragment_text);
        Button button = view.findViewById(R.id.daydetail_button);
        CalendarActivity calActivity = (CalendarActivity) getActivity();
        calActivity.setTitle("DayDetail Fragment");

        calActivity.setupBackButton(true);
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachContext(context);
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1){
            return;
        }
        onAttachContext(activity);
    }

    protected void onAttachContext(Context context){
        if(context instanceof OnFragmentInteractionListener){
            mListener = (OnFragmentInteractionListener) context;
        }else{
            throw new RuntimeException(context.toString() +
                    " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach(){
        super.onDetach();
        mListener = new OnFragmentInteractionListener(){
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

    @Override
    public boolean onOptionsItemSelected(MenuItem Item){
        if(Item.getItemId() == R.id.home){
            getChildFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(Item);
    }

}