package com.example.attendance_tracking.View;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.attendance_tracking.Model.Employee;
import com.example.attendance_tracking.R;

import com.example.attendance_tracking.View.NewEmployeeFragment.OnNewEmployeeFragmentInteractionListener;
import com.example.attendance_tracking.View.EditEmployeeFragment.OnEditEmployeeFragmentInteractionListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class EditEmployeeActivity
        extends AppCompatActivity
        implements OnNewEmployeeFragmentInteractionListener,
                   OnEditEmployeeFragmentInteractionListener
{

    private final String TAG = "EditEmployeeActivity";

    private Toolbar toolbar;
    private ConstraintLayout root;


    private ViewPager2 viewPager;
    private ViewPagerAdapter pagerAdapter;
    private InputMethodManager inputMethodManager;

    public Employee employee = null;

    private FloatingActionButton addButton;

    private static EmployeeHomeFragment homeEmployeeFragment;
    private static EditEmployeeFragment editEmployeeFragment;
    private static NewEmployeeFragment newEmployeeFragment;

    public int employeeAssigned = -1;

    int mMode;

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    public static class FragNum{
        public static final int NewEmployee=0;
        public static final int EditEmployee=1;
        public static final int HomeEmployee=2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_employee);

        findViews();
        setListeners();

        // ViewPager2: Switching Fragments
        viewPager.setAdapter(pagerAdapter);
        viewPager.setUserInputEnabled(false); // forbid to swipe
        viewPager.setCurrentItem(FragNum.HomeEmployee, false);
        viewPager.setOnTouchListener(newEmployeeFragment);

        // ToolBar
        toolbar.setTitle("従業員の編集");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Log.d(TAG, "[onBackPressed] on Toolbar");

            switch(viewPager.getCurrentItem()){
                case FragNum.HomeEmployee:
                    Log.d(TAG, " -- from HomeEmployee");
                    finish();
                    break;
                case FragNum.NewEmployee:
                    Log.d(TAG, " -- from NewEmployee");
                    viewPager.setCurrentItem(FragNum.HomeEmployee, false);
                    break;
                case FragNum.EditEmployee:
                    Log.d(TAG, " -- from EditEmployee");
                    viewPager.setCurrentItem(FragNum.HomeEmployee, false);
                    break;
                default:
                    finish();
            }
            return super.onOptionsItemSelected(item);
        }

        switch(id){
            case R.id.optionsMenu_Pref:
                Toast.makeText(getApplicationContext(),"'設定' is selected...", Toast.LENGTH_LONG).show();
                Log.d(TAG, "...onOptionsItemSelected is active");
                Intent intent = new android.content.Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.optionsMenu_help:
                Toast.makeText(getApplicationContext(),"'ヘルプ' is selected...", Toast.LENGTH_LONG).show();
                return true;
            case R.id.optionsMenu_info:
                Toast.makeText(getApplicationContext(), "'お知らせ' is selected...", Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(), "'action_settings' is selected...", Toast.LENGTH_LONG).show();
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    private void findViews(){
        root = findViewById(R.id.layout_editemployee_root);
        pagerAdapter = new ViewPagerAdapter(this);
        viewPager = findViewById(R.id.viewPager_editEmployee);
        toolbar = findViewById(R.id.toolbar_editEmployee);
    }
    private void setListeners(){

    }

    public void setToolbarTitle(String title){
        toolbar.setTitle(title);
    }
    public void setToolbarTitle(int id){
        toolbar.setTitle(id);
    }

    private static class ViewPagerAdapter extends FragmentStateAdapter {
        // extends FragmentStateAdapter
        public ViewPagerAdapter(FragmentActivity fa){
            super(fa);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Fragment fragment = null;
            switch(position){
                case FragNum.NewEmployee:
                    newEmployeeFragment = NewEmployeeFragment.newInstance();
                    fragment = newEmployeeFragment;
                    break;
                case FragNum.EditEmployee:
                    editEmployeeFragment = EditEmployeeFragment.newInstance();
                    fragment = editEmployeeFragment;
                    break;
                case FragNum.HomeEmployee:
                    homeEmployeeFragment = EmployeeHomeFragment.newInstance();
                    fragment = homeEmployeeFragment;
                    break;
            }
            // position given as an argument do not match any FragNums,
            // returns HomeFragment
            if(fragment == null){
                throw new NullPointerException();
            }
            return fragment;
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }

    public
    ViewPager2 getViewPager(){ return viewPager; }

//    @Override
//    public boolean onTouchEvent(MotionEvent event){
//        // hide keyboard when touching background
//        inputMethodManager.hideSoftInputFromWindow(root.getWindowToken(),
//                InputMethodManager.HIDE_NOT_ALWAYS);
//        root.requestFocus();
//        return true;
//    }

    @Override
    public void onBackPressed(){
        Log.d(TAG, "[onBackPressed]");
        int current = viewPager.getCurrentItem();
        switch (current){
            case FragNum.EditEmployee:
            case FragNum.NewEmployee:
                viewPager.setCurrentItem(FragNum.HomeEmployee, false);
                break;
            case FragNum.HomeEmployee:
                Intent intent = new android.content.Intent(this, CalendarActivity.class);
                startActivity(intent);
        }
    }


    // For EditEmployeeFragment
    // TODO: How to implement on EditEmployeeFragment?
    public void onRadioButtonClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()){
            case R.id.radio_fulltime:
            case R.id.radio_parttime:
                employeeAssigned = view.getId();
                break;
        }
    }
}