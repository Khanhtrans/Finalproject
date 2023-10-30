package com.example.mexpense.fragments.main.expense;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mexpense.R;
import com.example.mexpense.adapters.ExpenseAdapter;
import com.example.mexpense.base.BaseActivity;
import com.example.mexpense.databinding.FragmentExpenseMainBinding;
import com.example.mexpense.entity.Expense;
import com.example.mexpense.services.ExpenseService;
import com.example.mexpense.services.TripService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ExpenseMainFragment extends Fragment implements ExpenseAdapter.ItemListener {

    private ExpenseMainViewModel mViewModel;
    private FragmentExpenseMainBinding binding;
    private ExpenseAdapter adapter;
    private ImageButton btnLogout;
    private TripService tripService;
    private int tripId;

    public static ExpenseMainFragment newInstance() {
        return new ExpenseMainFragment();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(ExpenseMainViewModel.class);
        binding = FragmentExpenseMainBinding.inflate(inflater, container, false);
        final BaseActivity act = (BaseActivity) getActivity();
//        if (act.getSupportActionBar() != null) {
//            ConstraintLayout toolbar = (ConstraintLayout) act.getSupportActionBar().getCustomView();
//            act.getSupportActionBar().show();
//           btnLogout = toolbar.findViewById(R.id.btnLogout);
//           btnLogout.setOnClickListener(new View.OnClickListener() {
//               @Override
//               public void onClick(View view) {
//                   Navigation.findNavController(view).navigate(ExpenseMainFragmentDirections.actionExpenseMainFragmentToLoginFragment());
//               }
//           });
//        }
        tripService = new TripService(getContext());
        ExpenseService expenseService = new ExpenseService(getContext());

        tripId = getArguments() != null ? getArguments().getInt("tripId") : -1;

        RecyclerView rv = binding.expenseRecyclerView;
        rv.setHasFixedSize(true);

        mViewModel.expenseList.observe(
                getViewLifecycleOwner(),
                expenses -> {
                    adapter = new ExpenseAdapter(expenses, this);
                    binding.expenseRecyclerView.setAdapter(adapter);
                    binding.expenseRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    if(expenses.size() == 0){
                        binding.emptyExpenseLayout.setVisibility(View.VISIBLE);
                    } else {
                        binding.emptyExpenseLayout.setVisibility(View.INVISIBLE);
                    }
                }
        );
        tripService.getTrip(mViewModel.trip, tripId);
        tripService.updateTotal(tripId, getTotal());

        mViewModel.trip.observe(
                getViewLifecycleOwner(),
                trip -> {
                    binding.textParticipants.setText("Number of Participant: " + trip.getParticipants());
                    binding.textExpenseTripName.setText(trip.getName());
                    binding.textTripStartDate.setText(trip.getStartDate());
                    binding.textTripEndDate.setText(trip.getEndDate());
                    binding.textExpenseTripDestination.setText(trip.getDestination());
                    binding.textRequiredAssessment.setText(trip.getRequiredAssessment() ? "Assessment Required " : "Assessment Not Required");
                }
        );
        expenseService.getExpenses(mViewModel.expenseList, tripId);

       // FloatingActionButton btnAdd = binding.btnAddExpense;
//        btnAdd.setOnClickListener(view -> {
//            Bundle bundle = getBundle(-1);
//            Navigation.findNavController(getView()).navigate(R.id.expenseFormFragment, bundle);
//        });

//        AppCompatActivity app = (AppCompatActivity)getActivity();
//        ActionBar ab = app.getSupportActionBar();
//        ab.setHomeButtonEnabled(true);
//        ab.setDisplayShowHomeEnabled(true);
//        ab.setDisplayHomeAsUpEnabled(true);
//        ab.setHomeAsUpIndicator(R.drawable.ic_back);
//        ab.setTitle("Trips");
        setHasOptionsMenu(true);

        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater){
        inflater.inflate(R.menu.edit_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Navigation.findNavController(getView()).navigate(R.id.tripMainFragment);
                return true;
            case R.id.action_delete:
                handleDelete();
                return true;
            case R.id.action_edit:
                handleEdit();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(int expenseId) {
        Bundle bundle = getBundle(expenseId);
        Navigation.findNavController(getView()).navigate(R.id.expenseDetailsFragment, bundle);
    }

    public double getTotal() {
        double total = 0.0;
        List<Expense> expenses = mViewModel.expenseList.getValue();
        for (int i = 0; i < (expenses != null ? expenses.size() : 0); i++) {
            total += (expenses.get(i).getCost() * expenses.get(i).getAmount());
            binding.textTotal.setText(Double.toString(total));
        }
        return total;
    }

    private Bundle getBundle(int expenseId){
        Bundle bundle = new Bundle();
        bundle.putInt("expenseId", expenseId);
        bundle.putInt("tripId", tripId);
        bundle.putString("startDate", binding.textTripStartDate.getText().toString());
        bundle.putString("endDate", binding.textTripEndDate.getText().toString());
        return bundle;
    }

    private void handleDelete(){
        new AlertDialog.Builder(getContext()).setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Confirmation").setMessage("Are you sure?")
                .setPositiveButton("Yes", (arg0, arg1) -> {
                    tripService.deleteTrip(tripId);
                    Navigation.findNavController(getView()).navigate(R.id.tripMainFragment);
                    Toast.makeText(getContext(), "Trip Deleted", Toast.LENGTH_SHORT).show();
                }).setNegativeButton("No", null).show();
    }

    private void handleEdit(){
        Bundle bundle = getBundle(tripId);
        Navigation.findNavController(getView()).navigate(R.id.tripFormFragment, bundle);
    }
}