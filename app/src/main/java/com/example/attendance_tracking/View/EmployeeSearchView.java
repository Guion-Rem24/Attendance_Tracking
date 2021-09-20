package com.example.attendance_tracking.View;

import static android.os.VibrationEffect.DEFAULT_AMPLITUDE;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendance_tracking.Model.Employee;
import com.example.attendance_tracking.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 参考: https://qiita.com/kikuchy/items/0fa6bd232930bb12e187
 * How to Use
 * 1.
 */
public class EmployeeSearchView extends LinearLayout
{
    public SearchView searchView;
    public AppCompatSpinner spinner;
    private RecyclerView recyclerView;
    private List<Employee> employees;
    private Context context;

    private static class SearchOpt{
        final static public int Name = 0;
        final static public int Id = 1;
    }

    private static final String TAG = "EmployeeSearchView";
    public EmployeeSearchView(@NonNull Context context) {
        super(context);
        Log.d(TAG,"[constructor] 1");
        // 第３引数にtrueを渡すと、このComplexViewの直下にレイアウトのmergeの子要素を展開してくれる
        LayoutInflater.from(context).inflate(R.layout.view_employee_search,this,true);
        searchView = findViewById(R.id.search_employee);
        spinner = findViewById(R.id.spinner_search_employee);
        setSettings(context);
        setListeners(context);
    }

    public EmployeeSearchView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Log.d(TAG,"[constructor] 2");
        Log.d(TAG, "context:"+context.getClass().getName());
        // 第３引数にtrueを渡すと、このComplexViewの直下にレイアウトのmergeの子要素を展開してくれる
        LayoutInflater.from(context).inflate(R.layout.view_employee_search,this,true);
        searchView = findViewById(R.id.search_employee);
        spinner = findViewById(R.id.spinner_search_employee);
        setSettings(context);
        setListeners(context);
    }

    public EmployeeSearchView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.d(TAG,"[constructor] 3");
        // 第３引数にtrueを渡すと、このComplexViewの直下にレイアウトのmergeの子要素を展開してくれる
        LayoutInflater.from(context).inflate(R.layout.view_employee_search,this,true);
        searchView = findViewById(R.id.search_employee);
        spinner = findViewById(R.id.spinner_search_employee);
        setSettings(context);
        setListeners(context);
    }

    public EmployeeSearchView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Log.d(TAG,"[constructor] 4");
        // 第３引数にtrueを渡すと、このComplexViewの直下にレイアウトのmergeの子要素を展開してくれる
        LayoutInflater.from(context).inflate(R.layout.view_employee_search,this,true);
        searchView = findViewById(R.id.search_employee);
        spinner = findViewById(R.id.spinner_search_employee);
        setSettings(context);
        setListeners(context);
    }

    private void setListeners(Context context){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
        R.array.search_employee_type, android.R.layout.simple_spinner_item);
//         Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//         Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Log.d(TAG, parent.getItemAtPosition(position).getClass().getName());
//                Log.d(TAG, view.getClass().getName());
//                Log.d(TAG, "position: "+position);
//                Log.d(TAG, "id: "+id);
                switch(position){
                    case SearchOpt.Name:
                    {
                        searchView.setQueryHint("名前で検索");
                        searchView.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                        break;
                    }
                    case SearchOpt.Id:
                    {
                        searchView.setQueryHint("社員番号で検索");
                        searchView.setInputType(InputType.TYPE_CLASS_NUMBER);
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, parent.getClass().getName());
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "[onQueryTextSubmit]");
                if(SearchOpt.Id == spinner.getSelectedItemPosition()){
                    int query_num = Integer.parseInt(query);
                    if(query_num == query_num%1000){
                        Toast.makeText(context, "社員番号は4桁です．", Toast.LENGTH_SHORT).show();
                        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                        if(Build.VERSION_CODES.O <= Build.VERSION.SDK_INT) {
                            VibrationEffect effect = VibrationEffect.createOneShot(300, DEFAULT_AMPLITUDE);
                            vibrator.vibrate(effect);
                        }
                        else{
                            vibrator.vibrate(300);
                        }
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "[onQueryTextChange]");
                List<Employee> emps = new ArrayList<>();
                if(newText.equals("")) {
                    setupView(employees);
                    return false;
                }
                int pos = spinner.getSelectedItemPosition();
                newText = newText.toLowerCase(Locale.ROOT);
                switch(pos){
                    case SearchOpt.Name:
                    {
                        newText = convert(newText);
                        for(Employee e : employees){
                            if(e.familyName.contains(newText) || e.firstName.contains(newText) ||
                                e.familyKana.contains(newText) || e.firstKana.contains(newText))
                            {
                                emps.add(e);
                            }
                        }
                        break;
                    }
                    case SearchOpt.Id:
                    {
                        for(Employee e : employees){
                            if(String.valueOf(e.getId()).contains(newText)){
                                emps.add(e);
                            }
                        }
                        break;
                    }
                }
                setupView(emps);
                return false;
            }
        });


    }
    private void setSettings(Context context){
        this.context = context;
        // 虫眼鏡アイコンを最初表示するかの設定
        searchView.setIconifiedByDefault(false);
        // Submitボタンを表示するかどうか
        searchView.setSubmitButtonEnabled(false);

    }

    public void setRecyclerView(RecyclerView view){ this.recyclerView = view; }

    public void setEmployees(List<Employee> employee){
        employees = employee;
    }

    private void setupView(List<Employee> employee){
        EmployeeListAdapter adapter = new EmployeeListAdapter(context);
        adapter.setEmployees(employee);
        adapter.setOnItemClickListener(((EmployeeListAdapter)recyclerView.getAdapter()).getItemClickListener());
        adapter.setOnDeleteButtonsClickListener(((EmployeeListAdapter)recyclerView.getAdapter()).getDeleteButtonCLickListener());
        recyclerView.setAdapter(adapter);
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

}
