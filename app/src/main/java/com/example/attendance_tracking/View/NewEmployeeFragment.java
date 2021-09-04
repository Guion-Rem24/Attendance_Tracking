package com.example.attendance_tracking.View;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.attendance_tracking.Model.Employee;
import com.example.attendance_tracking.OnBackKeyPressedListener;
import com.example.attendance_tracking.R;
import com.example.attendance_tracking.ViewModel.NewEmployeeFragmentViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class NewEmployeeFragment extends Fragment implements OnBackKeyPressedListener, View.OnTouchListener {

    private static final String TAG = "NewEmployeeFragment";
    private OnNewEmployeeFragmentInteractionListener interactionListener;
    private EditEmployeeActivity parent;
    private View base;
    private ViewPager2 viewPager;

    private InputMethodManager inputMethodManager;
    private TextInputEditText familyNameEdit;
    private TextInputEditText firstNameEdit;
    private TextInputEditText familyKanaEdit;
    private TextInputEditText firstKanaEdit;
    private TextInputEditText employeeIdEdit;
    private FloatingActionButton saveButton;

    private NewEmployeeFragmentViewModel viewModel;

    public static NewEmployeeFragment newInstance(){
        return new NewEmployeeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "[onCreate]");
        parent = (EditEmployeeActivity) getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        Log.d(TAG, "[onCreateView]");

        base = inflater.inflate(R.layout.fragment_new_employee, container, false);

        findViews();
        setListeners();

        return base;
    }

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        onAttachContext(context);
    }

    protected void onAttachContext(Context context){
        if(context instanceof OnNewEmployeeFragmentInteractionListener){
            interactionListener = (OnNewEmployeeFragmentInteractionListener)context;
        }else{
            throw new RuntimeException(context.toString() +
                    " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach(){
        interactionListener = new OnNewEmployeeFragmentInteractionListener() {
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
    }

    @Override
    public void onBackPressed() {
//        parent.onResume();
    }

    private void findViews(){
        saveButton = base.findViewById(R.id.button_saveEdit);
        viewPager = parent.getViewPager();
        viewModel = new ViewModelProvider(this).get(NewEmployeeFragmentViewModel.class);
        inputMethodManager = (InputMethodManager) parent.getSystemService(Context.INPUT_METHOD_SERVICE);
        familyNameEdit = base.findViewById(R.id.input_familyname_text);
        familyKanaEdit = base.findViewById(R.id.input_familykana_text);
        firstNameEdit = base.findViewById(R.id.input_firstname_text);
        firstKanaEdit = base.findViewById(R.id.input_firstkana_text);
        employeeIdEdit = base.findViewById(R.id.input_employeeid_text);
    }

    private void setListeners(){
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "clicked save button");
                new AlertDialog.Builder(parent)
                        .setIcon(R.drawable.question)
                        .setTitle("確認")
                        .setMessage("保存しますか？")
                        .setPositiveButton("はい", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                saveInformation();
                                viewPager.setCurrentItem(2, false);
                            }
                        })
                        .setNegativeButton("いいえ", null)
                        .show();
            }
        });
    }

    private void saveInformation(){
        Employee employee = new Employee();
        employee.familyName = Objects.requireNonNull(familyNameEdit.getText()).toString();
        employee.familyKana = Objects.requireNonNull(familyKanaEdit.getText()).toString();
        employee.firstName = Objects.requireNonNull(firstNameEdit.getText()).toString();
        employee.firstKana = Objects.requireNonNull(firstKanaEdit.getText()).toString();
        int id = Integer.parseInt(Objects.requireNonNull(employeeIdEdit.getText()).toString());
        employee.setId(id);
        viewModel.insert(employee);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d(TAG, "touched");
        // FIXME: not Focused to base Window
        // hide keyboard when touching background
//        inputMethodManager.hideSoftInputFromWindow(base.getWindowToken(),
//                InputMethodManager.HIDE_NOT_ALWAYS);
//        base.requestFocus();
        return true;
    }


    public interface OnNewEmployeeFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
