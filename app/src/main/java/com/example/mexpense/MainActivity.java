package com.example.mexpense;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mexpense.base.BaseActivity;
import com.example.mexpense.databinding.ActivityMainBinding;
import com.example.mexpense.fragments.main.balance.BalanceFragment;
import com.example.mexpense.fragments.main.budget.BudgetFragment;
import com.example.mexpense.fragments.main.expense.ExpenseFormFragment;
import com.example.mexpense.fragments.main.expense.ExpenseMainFragment;
import com.example.mexpense.fragments.main.home.HomeFragment;
import com.example.mexpense.fragments.main.setting.SettingFragment;
import com.example.mexpense.fragments.main.trip.TripMainFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class MainActivity extends BaseActivity {
    TextView textView;
    ImageView imgShow;
    ImageView imgAction;
    boolean isShow = false;
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());
        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.balance:
                    replaceFragment(new TripMainFragment());
                    break;
                case R.id.budgets:
                    replaceFragment(new BudgetFragment());
                    break;
                case R.id.setting:
                    replaceFragment(new SettingFragment());
                    break;
                default:
                    replaceFragment(new ExpenseMainFragment());
                    break;
            }

            return true;
        });

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new ExpenseFormFragment());
            }
        });
        initiateActionBar();
    }

    private  void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void initiateActionBar(){
//        // assigning ID of the toolbar to a variable
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//
//        // using toolbar as ActionBar
//        setSupportActionBar(toolbar);
//        // Display application icon in the toolbar
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setLogo(R.drawable.ic_credit);
//        getSupportActionBar().setDisplayUseLogoEnabled(true);
//
//        imgShow = findViewById(R.id.imgViewBalance);
//        imgAction = findViewById(R.id.imgAction);
//        textView = findViewById(R.id.tvBalance);
//        imgShow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (isShow) {
//                    textView.setText("1.000.000");
//                } else textView.setText("*********");
//                isShow = !isShow;
//            }
//        });
    }

}