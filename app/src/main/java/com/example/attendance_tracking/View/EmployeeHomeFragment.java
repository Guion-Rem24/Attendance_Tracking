package com.example.attendance_tracking.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.attendance_tracking.Model.Employee.Employee;
import com.example.attendance_tracking.R;
import com.example.attendance_tracking.ViewModel.EmployeeHomeFragmentViewModel;
import com.example.attendance_tracking.utils.SwipeController;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.example.attendance_tracking.View.EditEmployeeActivity.FragNum;

import java.util.List;
import java.util.Objects;

/**
 * TODO: change fab to Extended-fab
 * https://medium.com/nerd-for-tech/how-to-add-extended-floating-action-button-in-android-android-studio-java-481cc9b3cdcb
  */

public class EmployeeHomeFragment extends Fragment {

    private static final String TAG = "HomeEmployeeFragment";

    private EditEmployeeActivity parent;
    private View base;
    private ViewPager2 viewPager;
    private InputMethodManager inputMethodManager;
    private EmployeeHomeFragmentViewModel viewModel;
    private EmployeeListAdapter adapter;
    private RecyclerView recyclerView;
    private List<Employee> employees;
    private SwipeController swipeController;
    private ItemTouchHelper touchHelper;
    private EmployeeSearchView employeeSearchView;
    private ScrollView scrollView;
    private NestedScrollView nestedScrollView;
    private TextView subtitle;
    private AppBarLayout _appBarLayout;
    private CoordinatorLayout.LayoutParams appBarLayoutParams;
    private AppBarLayout.Behavior behavior;
    AppBarLayout.OnOffsetChangedListener appBarListener;

    private FloatingActionButton addButton;

    public static EmployeeHomeFragment newInstance() { return new EmployeeHomeFragment(); }

    public EmployeeHomeFragment() {
        // Required empty public constructor
    }

    public static EmployeeHomeFragment newInstance(String param1, String param2) {
        EmployeeHomeFragment fragment = new EmployeeHomeFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "[onCreate]");
        parent = (EditEmployeeActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "[onCreateView]");

        base = inflater.inflate(R.layout.fragment_employee_home, container, false);
        findViews();
        setListeners();
        viewModel.getAllEmployees().observe( getViewLifecycleOwner(), new Observer<List<Employee>>() {
            @Override
            public void onChanged(List<Employee> employee) {
                adapter.setEmployees(employee);
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(parent));
        recyclerView.setAdapter(adapter);
        touchHelper.attachToRecyclerView(recyclerView);
        // TODO: display employees in database
        viewModel.getAllEmployees().observe(getViewLifecycleOwner(), new Observer<List<Employee>>() {
            @Override
            public void onChanged(List<Employee> employee) {
                Log.d(TAG, "[onChanged] employees");
                employees = employee;
                ((EmployeeListAdapter) Objects.requireNonNull(recyclerView.getAdapter())).setEmployees(employee);
                employeeSearchView.setEmployees(employee);
            }
        });

        final TypedArray styledAttributes = parent.getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize });
        int mActionBarSize = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) addButton.getLayoutParams();
        params.setMargins(params.leftMargin,params.topMargin,params.rightMargin,(mActionBarSize+ params.bottomMargin) );
        final int preBottomMargin = params.bottomMargin;
        addButton.setLayoutParams(params);
        _appBarLayout = parent.getAppBarLayout();
        appBarLayoutParams = (CoordinatorLayout.LayoutParams) _appBarLayout.getLayoutParams();
        behavior = (AppBarLayout.Behavior) appBarLayoutParams.getBehavior();
        appBarListener =  new  AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                ViewGroup.MarginLayoutParams mParams = (ViewGroup.MarginLayoutParams) addButton.getLayoutParams();
                mParams.setMargins(mParams.leftMargin,mParams.topMargin,mParams.rightMargin,(verticalOffset + preBottomMargin));
                addButton.setLayoutParams(mParams);
            }
        };



        return base;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPause() {
        Log.d(TAG, "[onPause]");
        super.onPause();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }).start();
        if(recyclerView.getAdapter() == null) return;// throw new NullPointerException();
        int num = recyclerView.getAdapter().getItemCount();
        for(int i=0;i<num;++i){
            recyclerView.getAdapter().notifyItemChanged(i);
        }
//        int[] consumed = new int[2];
//        behavior.onNestedPreScroll(parent.getCoordinatorLayout(), _appBarLayout,null,0,-1000,consumed, ViewCompat.TYPE_TOUCH);
//        _appBarLayout.removeOnOffsetChangedListener(appBarListener);
//        nestedScrollView.fullScroll(NestedScrollView.FOCUS_UP);
//        parent.getAppBarLayout().setExpanded(true);

    }

    @Override
    public void onResume() {
        super.onResume();
        _appBarLayout.addOnOffsetChangedListener(appBarListener);
    }

    public void requestRemoveEmployee(Employee employee){
        viewModel.deleteEmployee(employee);
    }


    public void findViews(){
        addButton = base.findViewById(R.id.button_addEmployee);
        scrollView = base.findViewById(R.id.scrollview_home_employee);
        subtitle = base.findViewById(R.id.textview_subtitle);
        recyclerView = (RecyclerView) base.findViewById(R.id.recyclerView_editEmployee);
        viewModel = new ViewModelProvider(this).get(EmployeeHomeFragmentViewModel.class);
        inputMethodManager = (InputMethodManager) parent.getSystemService(Context.INPUT_METHOD_SERVICE);
        adapter = new EmployeeListAdapter(parent);
        viewPager = parent.getViewPager();
        recyclerView.setAdapter(adapter);

//        swipeController = new SwipeController(new SwipeController.ButtonsClickListener(null, new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Log.d("deleteButton", "[onClicked]");
//            }
//        }));
        swipeController = new SwipeController(getContext(), recyclerView) {
            @SuppressLint("ResourceType")

            @Override
            public void instantiateBehindButton(EmployeeListAdapter.EmployeeViewHolder viewHolder, List<BehindButton> behindButtons) {
                behindButtons.add(new BehindButton(recyclerView, viewHolder, new BehindButtonClickListener() {
                    @Override
                    public void onClick(EmployeeListAdapter.EmployeeViewHolder viewHolder, int position) {
                        Log.d("BehindButton", "[onClick]");
                    }
                }));
                behindButtons.add(new BehindButton(recyclerView, viewHolder, new BehindButtonClickListener() {
                    @Override
                    public void onClick(EmployeeListAdapter.EmployeeViewHolder viewHolder, int position) {
                        Log.d("BehindButton", "[onClick]");
                    }
                }));
            }
        };
        touchHelper = new ItemTouchHelper(swipeController);
        employeeSearchView = base.findViewById(R.id.search_view_employee);
        employeeSearchView.setRecyclerView(recyclerView);
        TextView searchTextEdit = employeeSearchView.searchView.findViewById(androidx.appcompat.R.id.search_src_text);


    }

    public void setListeners(){
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "addButton: [onClick]");
//                _appBarLayout.removeOnOffsetChangedListener(appBarListener);
//                nestedScrollView.fullScroll(NestedScrollView.FOCUS_UP);
//                parent.getAppBarLayout().setExpanded(true, false);
                viewPager.setCurrentItem(FragNum.NewEmployee.get(), false);
            }
        });

        base.findViewById(R.id.scrollview_home_employee).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG,v.getWindowToken().toString());
                parent.getInputMethodManager().hideSoftInputFromWindow(v.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                v.requestFocus();
                return false;
            }
        });

        nestedScrollView = base.findViewById(R.id.nested_scroll_home_employee);
        subtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"[onClick]");
//                scrollView.fullScroll(ScrollView.FOCUS_UP);
                nestedScrollView.fullScroll(NestedScrollView.FOCUS_UP);
                parent.getInputMethodManager().hideSoftInputFromWindow(nestedScrollView.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                nestedScrollView.requestFocus();
            }
        });
    }

    public void resetCoordinatorView(){
        nestedScrollView.fullScroll(NestedScrollView.FOCUS_UP);
        parent.getAppBarLayout().removeOnOffsetChangedListener(appBarListener);
    }

}