package com.example.attendance_tracking.View;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.attendance_tracking.Model.Employee;
import com.example.attendance_tracking.Model.EmployeeRepository;
import com.example.attendance_tracking.OnBackKeyPressedListener;
import com.example.attendance_tracking.R;

public class EditEmployeeFragment extends Fragment implements OnBackKeyPressedListener {


    private static final String TAG = "NewEmployeeFragment";
//    private NewEmployeeFragment.OnEmployeeFragmentInteractionListener interactionListener;
    private EditEmployeeActivity parent;
    private EmployeeRepository employeeRepository;
    private EmployeeListAdapter employeeListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "[onCreate]");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        Log.d(TAG, "[onCreateView]");

        final View view = inflater.inflate(R.layout.fragment_edit_employee, container, false);

        parent = (EditEmployeeActivity) getActivity();

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        onAttachContext(context);
    }

    protected void onAttachContext(Context context){
//        if(context instanceof NewEmployeeFragment.OnEmployeeFragmentInteractionListener){
//            interactionListener = (NewEmployeeFragment.OnEmployeeFragmentInteractionListener)context;
//        }else{
//            throw new RuntimeException(context.toString() +
//                    " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach(){
//        interactionListener = new NewEmployeeFragment.OnEmployeeFragmentInteractionListener() {
//            @Override
//            public void onFragmentInteraction(Uri uri) {
//                //NOP
//            }
//        };
        super.onDetach();
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG,"[onPause]");
    }
    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "[onResume]");
    }

    @Override
    public void onBackPressed() {
//        parent.onResume();

    }
}
