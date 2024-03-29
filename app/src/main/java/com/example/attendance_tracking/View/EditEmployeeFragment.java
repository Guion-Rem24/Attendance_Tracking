package com.example.attendance_tracking.View;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendance_tracking.Model.Employee.Employee;
import com.example.attendance_tracking.Model.Employee.EmployeeRepository;
import com.example.attendance_tracking.OnBackKeyPressedListener;
import com.example.attendance_tracking.R;
import com.example.attendance_tracking.ViewModel.EditEmployeeFragmentViewModel;
import com.example.attendance_tracking.View.EditEmployeeActivity.FragNum;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

// TODO: create editing window
public class EditEmployeeFragment extends Fragment
        implements OnBackKeyPressedListener
{


    private static final String TAG = "EditEmployeeFragment";
    private EditEmployeeFragmentViewModel viewModel;
//    private NewEmployeeFragment.OnEmployeeFragmentInteractionListener interactionListener;
    private EditEmployeeActivity parent;
    private EmployeeRepository employeeRepository;
    private EmployeeListAdapter employeeListAdapter;
    private OnEditEmployeeFragmentInteractionListener interactionListener;

    private FloatingActionButton addButton;
    private RecyclerView recyclerView;
    private EmployeeListAdapter adapter;
    private Employee employee;

    private OnBackPressedCallback backPressedCallback;

    public static EditEmployeeFragment newInstance(){ return new EditEmployeeFragment(); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "[onCreate]");
        parent = (EditEmployeeActivity) getActivity();
        viewModel = new ViewModelProvider(this).get(EditEmployeeFragmentViewModel.class);
        adapter = new EmployeeListAdapter(parent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        Log.d(TAG, "[onCreateView]");

        final View view = inflater.inflate(R.layout.fragment_edit_employee, container, false);

//        addButton = view.findViewById(R.id.button_addEmployee);

        viewModel.getAllEmployees().observe( getViewLifecycleOwner(), new Observer<List<Employee>>() {
            @Override
            public void onChanged(List<Employee> employee) {
                adapter.setEmployees(employee);
            }
        });

        // TODO: listen to click employee and move to edit window

        setListeners();
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context){
        Log.d(TAG, "[onAttach]");
        super.onAttach(context);
        onAttachContext(context);
//        backPressedCallback = new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                Log.d(TAG, "[handleOnBackPressed]");
//                showDialog();
//                parent.getViewPager().setCurrentItem(EditEmployeeActivity.FragNum.HomeEmployee,false);
//            }
//        };
//        requireActivity().getOnBackPressedDispatcher().addCallback(this, backPressedCallback);

    }

    protected void onAttachContext(Context context){
        if(context instanceof OnEditEmployeeFragmentInteractionListener){
            interactionListener = (OnEditEmployeeFragmentInteractionListener)context;
        }else{
            throw new RuntimeException(context.toString() +
                    " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach(){
        interactionListener = new OnEditEmployeeFragmentInteractionListener() {
            @Override
            public void onFragmentInteraction(Uri uri) {
                //NOP
            }
        };
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
        this.employee = parent.employee;
        parent.employee = null;
    }

    @Override
    public void onBackPressed() {
//        parent.onResume();
        Log.d(TAG, "[onBackPressed]");
        parent.getViewPager().setCurrentItem(FragNum.HomeEmployee.get());

    }
    private void setListeners(){

    }

    private void showDialog(){

    }


    public interface OnEditEmployeeFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


    public void resetCoordinatorView(){

    }
}
