package com.example.mexpense.fragments.main.expense;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.mexpense.R;
import com.example.mexpense.databinding.FragmentExpenseDetailsBinding;
import com.example.mexpense.entity.Expense;
import com.example.mexpense.services.ExpenseService;
import com.example.mexpense.ultilities.Utilities;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ExpenseDetailsFragment extends Fragment implements OnMapReadyCallback {

    private ExpenseDetailsViewModel mViewModel;

    private FragmentExpenseDetailsBinding binding;

    private MapView mapView;
    private GoogleMap map;
    private ExpenseService service;
    private int expenseId;

    public static ExpenseDetailsFragment newInstance() {
        return new ExpenseDetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(ExpenseDetailsViewModel.class);
        binding = FragmentExpenseDetailsBinding.inflate(inflater, container, false);
        service = new ExpenseService(getContext());

        try{
            expenseId = getArguments().getInt("expenseId");
        } catch (Exception e){
            Log.i("ID EXCEPTION", "onCreateView: " + e);
        }

        mapView = binding.mapView;
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        mViewModel.expense.observe(
                getViewLifecycleOwner(),
                expense -> {
                    binding.textExpenseAmount.setText(String.valueOf(expense.getAmount()));
                    binding.textExpenseComment.setText(expense.getComment().length() > 0 ? expense.getComment() : "No comments available");
                    binding.textExpenseCost.setText("$" + expense.getCost());
                    binding.textExpenseDate.setText(Utilities.convertDate(expense.getDate(), false));
                    binding.textExpenseCategory.setText(expense.getCategory());
                    binding.iconExpense.setImageResource(getIcon(expense.getCategory()));
                    setImageView(expense.getImage());
                }
        );
        service.getExpenseById(mViewModel.expense, expenseId);

        AppCompatActivity app = (AppCompatActivity) getActivity();
        ActionBar ab = app.getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_back);
        ab.setTitle("Details");
        setHasOptionsMenu(true);

        return binding.getRoot();
    }

    private void setImageView(String imagePath) {
        if(imagePath.equals("")) {
            binding.expenseImageView.setImageResource(R.drawable.ic_no_image_foreground);
            return;
        }
        binding.expenseImageView.setImageBitmap(Utilities.getImageFromURL(imagePath, 400, 320));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.edit_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Navigation.findNavController(getView()).navigateUp();
                return true;
            case R.id.action_delete:
                handleDelete();
                return true;
            case R.id.action_edit:
                handleSave();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private int getIcon(String category) {
        return binding.getRoot()
                .getResources()
                .getIdentifier("ic_" + category.toLowerCase(), "drawable", binding.getRoot().getContext().getPackageName());
    }

    private void handleSave(){
        Bundle bundle = new Bundle();
        bundle.putInt("expenseId", expenseId);
        bundle.putInt("tripId", mViewModel.expense.getValue().getTripId());
        bundle.putString("startDate", getArguments().getString("startDate"));
        bundle.putString("endDate", getArguments().getString("endDate"));;
        Navigation.findNavController(getView()).navigate(R.id.expenseFormFragment, bundle);
    }

    private void handleDelete() {
        new AlertDialog.Builder(getContext()).setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Confirmation").setMessage("Are you sure?")
                .setPositiveButton("Yes", (arg0, arg1) -> {
                    Bundle bundle = new Bundle();

                    try{
                        bundle.putInt("tripId", mViewModel.expense.getValue().getTripId());
                        service.deleteExpense(expenseId);
                    } catch (Exception e){
                        Log.i("ERROR", "handleDelete: " + e);
                    }

                    Utilities.hideInput(getActivity(), getView());
                    Navigation.findNavController(getView()).navigate(R.id.expenseMainFragment, bundle);

                }).setNegativeButton("No", null).show();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setMyLocationEnabled(true);

        Expense e = mViewModel.expense.getValue();

        try {
            LatLng location = new LatLng(e.getLatitude(), e.getLongitude());
            map.addMarker(new MarkerOptions().position(location).title(e.getCategory()));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f));
        } catch (Exception ex){
            Log.i("LOCATION", ex.toString());
        }
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        if(mapView!=null) {
            mapView.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mapView!=null){
            mapView.onDestroy();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}