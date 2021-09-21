package com.example.attendance_tracking.View;

import static java.util.Calendar.FRIDAY;
import static java.util.Calendar.MONDAY;
import static java.util.Calendar.SATURDAY;
import static java.util.Calendar.SUNDAY;
import static java.util.Calendar.THURSDAY;
import static java.util.Calendar.TUESDAY;
import static java.util.Calendar.WEDNESDAY;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.attendance_tracking.Model.Employee;
import com.example.attendance_tracking.OnBackKeyPressedListener;
import com.example.attendance_tracking.R;
import com.example.attendance_tracking.ViewModel.NewEmployeeFragmentViewModel;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/*
 TODO: 保存時fixedTimeの入力
 TODO: 出勤時間が入力された時，退勤時間がそれよりも後になるようにListenerを設定

*/

public class NewEmployeeFragment extends Fragment implements OnBackKeyPressedListener
//        , View.OnTouchListener
{

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
    private RadioButton fulltime, parttime;
    private LinearLayout week_container;
    private LinearLayout mondayLayout, tuesdayLayout, wednesdayLayout, thursdayLayout, fridayLayout, saturdayLayout, sundayLayout;
    private Map<Integer, LinearLayout> weekLayouts;
    private Map<LinearLayout, Button> activateButtons;
    private List<Boolean> activated;
    private Map<Integer, Pair<NumberPicker,NumberPicker>> hoursOf, minutesOf;

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
        settings();
        setListeners();


        // ToolBar
//        Toolbar toolbar = base.findViewById(R.id.toolbar_editEmployee);
//        toolbar.setTitle("従業員の編集");
//        CollapsingToolbarLayout toolbarLayout = base.findViewById(R.id.toolbar_layout);
//        toolbarLayout.setTitleEnabled(false);
//        parent.setSupportActionBar(toolbar);
//
//        Objects.requireNonNull(parent.getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
//        setHasOptionsMenu(true);

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
        clear();
    }
    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "[onResume]");
        Log.d(TAG, getView().getClass().getName());
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
        fulltime = base.findViewById(R.id.radio_fulltime);
        parttime = base.findViewById(R.id.radio_parttime);
        week_container = base.findViewById(R.id.layout_setting_week);
        mondayLayout = week_container.findViewById(R.id.layout_monday);
        tuesdayLayout = week_container.findViewById(R.id.layout_tuesday);
        wednesdayLayout = week_container.findViewById(R.id.layout_wednesday);
        thursdayLayout = week_container.findViewById(R.id.layout_thursday);
        fridayLayout  = week_container.findViewById(R.id.layout_friday);
        saturdayLayout = week_container.findViewById(R.id.layout_saturday);
        sundayLayout = week_container.findViewById(R.id.layout_sunday);
        weekLayouts = new HashMap<>();
        weekLayouts.put(MONDAY, mondayLayout);
        weekLayouts.put(TUESDAY, tuesdayLayout);
        weekLayouts.put(WEDNESDAY, wednesdayLayout);
        weekLayouts.put(THURSDAY, thursdayLayout);
        weekLayouts.put(FRIDAY, fridayLayout);
        weekLayouts.put(SATURDAY, saturdayLayout);
        weekLayouts.put(SUNDAY, sundayLayout);
        activateButtons = new HashMap<>();
        activateButtons.put(mondayLayout, mondayLayout.findViewById(R.id.button_activate));
        activateButtons.put(tuesdayLayout, tuesdayLayout.findViewById(R.id.button_activate));
        activateButtons.put(wednesdayLayout, wednesdayLayout.findViewById(R.id.button_activate));
        activateButtons.put(thursdayLayout, thursdayLayout.findViewById(R.id.button_activate));
        activateButtons.put(fridayLayout, fridayLayout.findViewById(R.id.button_activate));
        activateButtons.put(saturdayLayout, saturdayLayout.findViewById(R.id.button_activate));
        activateButtons.put(sundayLayout, sundayLayout.findViewById(R.id.button_activate));
        activated = new ArrayList<>();
        for(int i=0;i<7;i++) activated.add(false);
        hoursOf = new HashMap<>();
        minutesOf = new HashMap<>();
        String[] minutes = new String[4];
        for(int i=0;i<4;i++){
            minutes[i] = String.format("%02d",i*15);
        }
        String[] hours = new String[17];
        for(int i=0;i<17;i++){
            hours[i] = String.valueOf(i+8);
        }
         for(int i=1;i<8;i++){
            NumberPicker startHour = weekLayouts.get(i).findViewById(R.id.picker_start_hour);
            NumberPicker startMin = weekLayouts.get(i).findViewById(R.id.picker_start_min);
            NumberPicker endHour = weekLayouts.get(i).findViewById(R.id.picker_end_hour);
            NumberPicker endMin = weekLayouts.get(i).findViewById(R.id.picker_end_min);
//            startHour.setDisplayedValues(hours);
            startHour.setMinValue(8);
            startHour.setMaxValue(24);
            startHour.setEnabled(false);
//            endHour.setDisplayedValues(hours);
            endHour.setMinValue(8);
            endHour.setMaxValue(24);
            endHour.setEnabled(false);
            startMin.setDisplayedValues(minutes);
            startMin.setMinValue(0);
            startMin.setMaxValue(3);
            startMin.setEnabled(false);
            setInput(startMin);
            endMin.setDisplayedValues(minutes);
            endMin.setMinValue(0);
            endMin.setMaxValue(3);
            endMin.setEnabled(false);
            setInput(endMin);
            hoursOf.put(i,new Pair<>(startHour,endHour));
            minutesOf.put(i,new Pair<>(startMin,endMin));
        }
    }

    private void setInput(ViewGroup vg) {
        for(int i = 0; i < vg.getChildCount(); i++) {
            View child = vg.getChildAt(i);
            if(child instanceof ViewGroup) {
                // Recurse
                setInput((ViewGroup) child);
            } else if(child instanceof EditText) {
                // Force InputType
                ((EditText) child).setInputType(InputType.TYPE_CLASS_NUMBER);
            }
        }
    }

    private void settings(){
        if(activateButtons.isEmpty()) throw new RuntimeException();
        if(activateButtons.size() > 7) throw new RuntimeException();
        activateButtons.get(mondayLayout).setText("月");
        activateButtons.get(tuesdayLayout).setText("火");
        activateButtons.get(wednesdayLayout).setText("水");
        activateButtons.get(thursdayLayout).setText("木");
        activateButtons.get(fridayLayout).setText("金");
        activateButtons.get(saturdayLayout).setText("土");
        activateButtons.get(sundayLayout).setText("日");
    }



    private void setListeners(){

//        Log.d(TAG,(((CoordinatorLayout)base).isNestedScrollingEnabled()?"True":"False"));
        base.findViewById(R.id.constraint_scrollable).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "[onTouch] constraint under scrollview");
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                v.requestFocus();
                return false;
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "clicked save button");
                String[] reasons = new String[1];
                if(existsBlank(reasons)){
                    new AlertDialog.Builder(parent)
                            .setTitle("未入力箇所があります")
                            .setMessage(reasons[0])
                            .setPositiveButton("閉じる", null)
                            .show();

                    return;
                }
                if(parent.employeeAssigned < 0){
                    new AlertDialog.Builder(parent)
                            .setIcon(R.drawable.announce)
                            .setMessage("正社員，アルバイトどちらか選択してください")
                            .setTitle("未入力箇所があります")
                            .setPositiveButton("閉じる", null)
                            .show();
                    return;
                }
                new AlertDialog.Builder(parent)
                        .setIcon(R.drawable.question)
                        .setTitle("確認")
                        .setMessage("保存しますか？")
                        .setPositiveButton("はい", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                saveInformation();
                                viewPager.setCurrentItem(EditEmployeeActivity.FragNum.HomeEmployee, false);
                            }
                        })
                        .setNegativeButton("いいえ", null)
                        .show();
            }
        });
        familyNameEdit.addTextChangedListener(new TextWatcher() {
            String str = null;
            int before_length = 0;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG,"[beforeTextChanged]: "+s.toString());
                before_length = s.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(isHiragana(s) && before_length<=s.length()){
                    str = convert(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(str!=null){
                    familyKanaEdit.setText(str);
                    str = null;
                }
            }
        });
        firstNameEdit.addTextChangedListener(new TextWatcher() {
            String str = null;
            int before_length = 0;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG,"[beforeTextChanged]: "+s.toString());
                before_length = s.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(isHiragana(s) && before_length<=s.length()){
                    str = convert(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(str!=null){
                    firstKanaEdit.setText(str);
                    str = null;
                }
            }
        });
        for(int i=1;i<8;++i){
            int I = i;
            activateButtons.get(weekLayouts.get(i)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean active = activated.get(I-1);
                    weekLayouts.get(I).findViewById(R.id.frame_covering).setVisibility((active?View.VISIBLE:View.INVISIBLE));
                    v.setBackground((!active?
                            getResources().getDrawable(R.drawable.ripple_background_primary_reverse,null):
                            getResources().getDrawable(R.drawable.ripple_background_primary,null)));
                    switchActivation(I,!active);
                    activated.set(I-1,!active);
                }
            });
        }
    }

    private void switchActivation(int position, boolean activate){
        hoursOf.get(position).first.setEnabled(activate);
        hoursOf.get(position).second.setEnabled(activate);
        minutesOf.get(position).first.setEnabled(activate);
        minutesOf.get(position).second.setEnabled(activate);
    }

    private boolean existsBlank(String[] reason_){
        boolean exists = false;

        String reason = "以下の箇所を入力してください．\n";
        if(Objects.requireNonNull(familyNameEdit.getText()).toString().matches("")){
            reason += "* 姓（漢字）\n";
        }
        if(Objects.requireNonNull(familyKanaEdit.getText()).toString().matches("")){
            reason += "* 姓（カナ）\n";
        }
        if(Objects.requireNonNull(firstNameEdit.getText()).toString().matches("")){
            reason += "* 名（漢字) \n";
        }
        if(Objects.requireNonNull(firstKanaEdit.getText()).toString().matches("")){
            reason += "* 名（カナ）\n";
        }
        if(Objects.requireNonNull(employeeIdEdit.getText()).toString().matches("")){
            reason += "* 従業員番号 \n";
        }
        reason_[0] = reason;
        if(!reason.matches("以下の箇所を入力してください．\n")){
            exists = true;
        }

        return exists;

    }


    private void saveInformation(){
        Employee employee = new Employee();
        employee.familyName = Objects.requireNonNull(familyNameEdit.getText()).toString();
        employee.familyKana = Objects.requireNonNull(familyKanaEdit.getText()).toString();
        employee.firstName = Objects.requireNonNull(firstNameEdit.getText()).toString();
        employee.firstKana = Objects.requireNonNull(firstKanaEdit.getText()).toString();
        int id = Integer.parseInt(Objects.requireNonNull(employeeIdEdit.getText()).toString());
        employee.setId(id);
        int view_id = parent.employeeAssigned;
        switch(view_id){
            case R.id.radio_fulltime:
                employee.fulltime = true;
                break;
            case R.id.radio_parttime:
                employee.fulltime = false;
                break;
        }
        parent.employeeAssigned = -1;
        viewModel.insert(employee);
    }


    public interface OnNewEmployeeFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void clear(){
        familyNameEdit.setText("");
        firstNameEdit.setText("");
        familyKanaEdit.setText("");
        firstKanaEdit.setText("");
        employeeIdEdit.setText("");
        fulltime.setChecked(false);
        parttime.setChecked(false);
    }

    // convert Hiragana to Katakana
    static private String convert(String old){
        StringBuilder buffer = new StringBuilder();
        for(int i=0;i<old.length();++i){
            char c = old.charAt(i);
            if ((c >= 0x3041) && (c <= 0x3093)) {
                Log.d(TAG, "カナ "+c);
                buffer.append((char) (c + 0x60));
            }
            else {
                buffer.append(c);
            }
        }
        return buffer.toString();
    }

    static private boolean isHiragana(CharSequence c){

        return c.toString().matches("^[\\u3040-\\u309F]+$");

    }
}
