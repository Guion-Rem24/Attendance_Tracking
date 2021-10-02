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
import android.content.res.TypedArray;
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
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.attendance_tracking.Model.Employee.Employee;
import com.example.attendance_tracking.OnBackKeyPressedListener;
import com.example.attendance_tracking.R;
import com.example.attendance_tracking.ViewModel.NewEmployeeFragmentViewModel;
import com.example.attendance_tracking.View.EditEmployeeActivity.FragNum;
import com.example.attendance_tracking.utils.Utils;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
/*
    Tips: NestedScrollViewを使った場合には, ViewPagerのTouchEventが聞かない
    Tips: CoordinatorLayoutでAppBarを相互作用的にアニメーションした時，ViewPager内のFABなども動く->appBar.addOnOffsetChangedListener()でMarginを変更する必要あり
 */
/*
 TODO: 出勤時間が入力された時，退勤時間がそれよりも後になるようにListenerを設定
 TODO: Check AppBar reset correctly when Fragment changed

*/


public class NewEmployeeFragment extends Fragment implements OnBackKeyPressedListener
//        , View.OnTouchListener
{

    private static final String TAG = "NewEmployeeFragment";
    private OnNewEmployeeFragmentInteractionListener interactionListener;
    private EditEmployeeActivity parent;
    private View base;
    private ViewPager2 viewPager;
    private NestedScrollView nestedScrollView;
    private AppBarLayout.OnOffsetChangedListener appBarListener;

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

    private final int initStartHour = 12, initEndHour = 18;
    private final String initMinute = "00";

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

        final TypedArray styledAttributes = parent.getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize });
        int mActionBarSize = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) saveButton.getLayoutParams();
        params.setMargins(params.leftMargin,params.topMargin,params.rightMargin,(mActionBarSize+ params.bottomMargin) );
        final int preBottomMargin = params.bottomMargin;
        AppBarLayout _appBarLayout = parent.getAppBarLayout();
        CoordinatorLayout.LayoutParams appBarLayoutParams = (CoordinatorLayout.LayoutParams) _appBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) appBarLayoutParams.getBehavior();
        appBarListener = new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                ViewGroup.MarginLayoutParams mParams = (ViewGroup.MarginLayoutParams) saveButton.getLayoutParams();
                mParams.setMargins(mParams.leftMargin,mParams.topMargin,mParams.rightMargin,(verticalOffset + preBottomMargin));
                saveButton.setLayoutParams(mParams);
            }
        };

//        nestedScrollView.fullScroll(NestedScrollView.FOCUS_UP);
//        behavior.onNestedPreScroll(parent.getCoordinatorLayout(), parent.getAppBarLayout(), null,0,-1000,new int[2], ViewCompat.TYPE_NON_TOUCH);
//        saveButton.setLayoutParams(params);

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
        Log.d(TAG,"[onAttach]");
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
        parent.getAppBarLayout().addOnOffsetChangedListener(appBarListener);
    }

    @Override
    public void onBackPressed() {
//        parent.onResume();
    }

    private void findViews(){
        saveButton = base.findViewById(R.id.button_saveEdit);
        viewPager = parent.getViewPager();
        viewModel = new ViewModelProvider(this).get(NewEmployeeFragmentViewModel.class);
        nestedScrollView = base.findViewById(R.id.nested_scroll_new_employee);
        inputMethodManager = (InputMethodManager) parent.getSystemService(Context.INPUT_METHOD_SERVICE);
        familyNameEdit = base.findViewById(R.id.input_familyname_text);
        familyKanaEdit = base.findViewById(R.id.input_familykana_text);
        firstNameEdit = base.findViewById(R.id.input_firstname_text);
        firstKanaEdit = base.findViewById(R.id.input_firstkana_text);
        employeeIdEdit = base.findViewById(R.id.input_employeeid_text);
        fulltime = base.findViewById(R.id.radio_fulltime);
        parttime = base.findViewById(R.id.radio_parttime);
        // TODO: 週初めが日曜か月曜かでLayoutを変更
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
//        String[] hours = new String[17];
//        for(int i=0;i<17;i++){
//            hours[i] = String.valueOf(i+8);
//        }
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
            initValueAt(i);
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


    public AppBarLayout.OnOffsetChangedListener
    getOffsetChangedListener() { return appBarListener; }

    public NestedScrollView
    getNestedScrollView() { return nestedScrollView; }

    private void setListeners(){

//        Log.d(TAG,(((CoordinatorLayout)base).isNestedScrollingEnabled()?"True":"False"));
        base.findViewById(R.id.constraint_scrollable).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG,v.getWindowToken().toString());
                parent.getInputMethodManager().hideSoftInputFromWindow(v.getWindowToken(),
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
                                viewPager.setCurrentItem(FragNum.HomeEmployee.get(), false);
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
                    switchActivation(I, (Button) v,!active);
                    activated.set(I-1,!active);
                }
            });
        }
    }

    private void switchActivation(int position, Button button, boolean activate){
        weekLayouts.get(position).findViewById(R.id.frame_covering).setVisibility((activate?View.INVISIBLE:View.VISIBLE));
//        v.setBackground((!active?
//                getResources().getDrawable(R.drawable.ripple_background_primary_reverse,null):
//                getResources().getDrawable(R.drawable.ripple_background_primary,null)));
        button.setBackground((activate?
                getResources().getDrawable(R.drawable.ripple_background_primary_reverse,null):
                getResources().getDrawable(R.drawable.ripple_background_primary,null)));
        hoursOf.get(position).first.setEnabled(activate);
        hoursOf.get(position).second.setEnabled(activate);
        minutesOf.get(position).first.setEnabled(activate);
        minutesOf.get(position).second.setEnabled(activate);

    }
    private void initValueAt(int day){
        hoursOf.get(day).first.setValue(initStartHour);
        hoursOf.get(day).second.setValue(initEndHour);
        minutesOf.get(day).first.setValue(0);
        minutesOf.get(day).second.setValue(0);
    }

    private boolean existsBlank(String[] reason_){
        boolean exists = false;

        String pre, reason;
        pre = reason = "以下の箇所を入力してください．\n";
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
        if(!reason.matches(pre)){
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
        String[] workTimes = new String[7];
        for(int i=1;i<8;++i){
            String time = "";
            if(activated.get(i-1)){
                int startHour = ((NumberPicker) weekLayouts.get(i).findViewById(R.id.picker_start_hour)).getValue();
                int endHour = ((NumberPicker) weekLayouts.get(i).findViewById(R.id.picker_end_hour)).getValue();
                int startMin = ((NumberPicker) weekLayouts.get(i).findViewById(R.id.picker_start_min)).getValue();
                int endMin = ((NumberPicker) weekLayouts.get(i).findViewById(R.id.picker_end_min)).getValue();
                time = Utils.toWorkDayString(startHour,startMin*15,endHour,endMin*15);
            }
            workTimes[i-1] = time;
        }
        for(int i=0;i<7;++i){
            Log.d(TAG, (i+1)+": "+workTimes[i]);
        }
        employee.fixedTime = Utils.toWorkDayString(workTimes);
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
        for(int i=1;i<8;++i){
            activated.set(i-1,false);
            switchActivation(i,activateButtons.get(weekLayouts.get(i)),false);
            initValueAt(i);
        }
//        parent.getAppBarLayout().removeOnOffsetChangedListener(appBarListener);
//        nestedScrollView.fullScroll(NestedScrollView.FOCUS_UP);
//        parent.getAppBarLayout().setExpanded(true,false);
    }

    // convert Hiragana to Katakana
    static private String convert(String old){
        StringBuilder buffer = new StringBuilder();
        for(int i=0;i<old.length();++i){
            char c = old.charAt(i);
            if ((c >= 0x3041) && (c <= 0x3093)) {
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

    public void resetCoordinatorView(){
        clear();
        nestedScrollView.fullScroll(NestedScrollView.FOCUS_UP);
        parent.getAppBarLayout().removeOnOffsetChangedListener(appBarListener);
    }

}
