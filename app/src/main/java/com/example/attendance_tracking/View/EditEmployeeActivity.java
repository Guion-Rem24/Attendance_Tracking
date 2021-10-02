package com.example.attendance_tracking.View;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.attendance_tracking.Model.Employee.Employee;
import com.example.attendance_tracking.R;

import com.example.attendance_tracking.View.NewEmployeeFragment.OnNewEmployeeFragmentInteractionListener;
import com.example.attendance_tracking.View.EditEmployeeFragment.OnEditEmployeeFragmentInteractionListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;


public class EditEmployeeActivity
        extends AppCompatActivity
        implements OnNewEmployeeFragmentInteractionListener,
                   OnEditEmployeeFragmentInteractionListener,
                   View.OnTouchListener
{

    private static final String TAG = "EditEmployeeActivity";

    private Toolbar toolbar;
    private View root;


    private ViewPager2 viewPager;
    private ViewPagerAdapter pagerAdapter;
    private InputMethodManager inputMethodManager;
    private CoordinatorLayout coordinatorLayout;
    private AppBarLayout appBarLayout;

    public Employee employee = null;

    private FloatingActionButton addButton;

    private static EmployeeHomeFragment homeEmployeeFragment;
    private static EditEmployeeFragment editEmployeeFragment;
    private static NewEmployeeFragment newEmployeeFragment;

    private List<MenuItem> options;

    public int employeeAssigned = -1;

    int mMode;

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    public enum FragNum {
//        public static final int NewEmployee=0;
//        public static final int EditEmployee=1;
//        public static final int HomeEmployee=2;
        NewEmployee(0),
        EditEmployee(1),
        HomeEmployee(2),
        ;
        private final int id;
        FragNum(int i) {
            this.id = i;
        }
        public int get(){return this.id;}
    };
    public static FragNum getAs(final int id) {
        FragNum[] types = FragNum.values();
        for (FragNum type : types) {
            if (type.get() == id) {
                return type;
            }
        }
        return null;
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
        viewPager.setCurrentItem(FragNum.HomeEmployee.get(), false);
//        viewPager.setOnTouchListener(this/*newEmployeeFragment*/);

        // ToolBar
        toolbar.setTitle("従業員の編集");
//        CollapsingToolbarLayout toolbarLayout = findViewById(R.id.toolbar_layout);
//        toolbarLayout.setTitleEnabled(false);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Log.d(TAG, "[onBackPressed] on Toolbar");

            switch(getAs(viewPager.getCurrentItem())){
                case HomeEmployee:
                    Log.d(TAG, " -- from HomeEmployee");
                    finish();
                    break;
                case NewEmployee:
                    Log.d(TAG, " -- from NewEmployee");
                    viewPager.setCurrentItem(FragNum.HomeEmployee.get(), false);
                    break;
                case EditEmployee:
                    Log.d(TAG, " -- from EditEmployee");
                    viewPager.setCurrentItem(FragNum.HomeEmployee.get(), false);
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
//                Fragment frag = null;
//                switch(viewPager.getCurrentItem()){
//                    case FragNum.EditEmployee:
//                        frag = editEmployeeFragment;
//                        break;
//                    case FragNum.HomeEmployee:
//                        frag = homeEmployeeFragment;
//                        break;
//                    case FragNum.NewEmployee:
//                        frag = newEmployeeFragment;
//                        break;
//                }
//                if(frag != null) {
//                    getSupportFragmentManager()
//                            .beginTransaction()
//                            .attach(frag);
//                }

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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
//        inflater.inflate(R.menu.menu_search_employee,menu);
//        MenuItem menuItem = menu.findItem(R.id.item_search_employee);
//
//        EmployeeSearchView employeeSearchView = (EmployeeSearchView) menuItem.getActionView();
//        employeeSearchView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                employeeSearchView.requestFocus();
//                setOptionsVisibility(false);
//            }
//        });
//        employeeSearchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(!hasFocus){
//                    setOptionsVisibility(true);
//                }
//            }
//        });
//        SearchView searchView = employeeSearchView.searchView;

//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                Log.d(TAG, "[onQueryTextSubmit] query: "+query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                Log.d(TAG, "[onQueryTextChange]: "+newText);
//                return false;
//            }
//        });
//        searchView.setFocusable(true);
//        searchView.setOnSearchClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "[onSearchClick] searchView");
//
//            }
//        });
//
//        searchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                Log.d(TAG, "[onFocusChanged] searchView");
//            }
//        });




        return true;
    }

    private void setOptionsVisibility(boolean visibility){
        for(MenuItem item: options){
            item.setVisible(visibility);
        }
    }

    private void findViews(){
        root = findViewById(R.id.layout_editemployee_root);
        pagerAdapter = new ViewPagerAdapter(this);
        viewPager = findViewById(R.id.viewPager_editEmployee);
        toolbar = findViewById(R.id.toolbar_editEmployee);
        inputMethodManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        appBarLayout = findViewById(R.id.appbar_editEmployee);
    }
    private void setListeners(){
        // action when Fragment changed by viewPager
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback(){
            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "[onPageSelected]:"+position);

                switch(getAs(position)){
                    case EditEmployee:
                        if(editEmployeeFragment!=null) editEmployeeFragment.resetCoordinatorView();
                        break;
                    case NewEmployee:
                        if(newEmployeeFragment!=null) newEmployeeFragment.resetCoordinatorView();
                        break;
                    case HomeEmployee:
                        if(homeEmployeeFragment!=null) homeEmployeeFragment.resetCoordinatorView();
                        break;
                }

                appBarLayout.setExpanded(true,false);
                super.onPageSelected(position);
            }
        });

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
            Log.d(TAG, "position:" + position);
            switch(getAs(position)){
                case NewEmployee:
                    newEmployeeFragment = NewEmployeeFragment.newInstance();
                    fragment = newEmployeeFragment;
                    break;
                case EditEmployee:
                    editEmployeeFragment = EditEmployeeFragment.newInstance();
                    fragment = editEmployeeFragment;
                    break;
                case HomeEmployee:
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
    public
    AppBarLayout getAppBarLayout() { return appBarLayout; }
    public
    CoordinatorLayout getCoordinatorLayout() { return (CoordinatorLayout) root;}
    public
    InputMethodManager getInputMethodManager() { return inputMethodManager; }
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
        switch (getAs(current)){
            case EditEmployee:
                viewPager.setCurrentItem(FragNum.HomeEmployee.get(), false);
                break;
            case NewEmployee:
                appBarLayout.removeOnOffsetChangedListener(newEmployeeFragment.getOffsetChangedListener());
                newEmployeeFragment.getNestedScrollView().fullScroll(NestedScrollView.FOCUS_UP);
                getAppBarLayout().setExpanded(true,false);
                viewPager.setCurrentItem(FragNum.HomeEmployee.get(), false);
                break;
            case HomeEmployee:
                Intent intent = new android.content.Intent(this, CalendarActivity.class);
                startActivity(intent);
        }
    }

    public EmployeeHomeFragment getHomeFragment(){ return homeEmployeeFragment; }

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

    @Override
    protected void onResume() {
        Log.d(TAG, "[onResume]");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "[onPause]");
        super.onPause();
    }

    @Override
    protected void onResumeFragments() {
        Log.d(TAG,"[onResumeFragments]");
        super.onResumeFragments();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        Log.d(TAG, "touched");
//        root.performClick();
        // FIXME: not Focused to base Window
        // hide keyboard when touching background
//        View currentView = getCurrentFocus();
//        if(currentView != null){
//            inputMethodManager.hideSoftInputFromWindow(currentView.getWindowToken(),
//                    InputMethodManager.HIDE_NOT_ALWAYS);
//            currentView.requestFocus();
//        }
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        v.requestFocus();
        return true;
    }


}