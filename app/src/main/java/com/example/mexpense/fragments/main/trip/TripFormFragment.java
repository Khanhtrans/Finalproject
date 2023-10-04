package com.example.mexpense.fragments.main.trip;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.mexpense.R;
import com.example.mexpense.databinding.FragmentTripFormBinding;
import com.example.mexpense.entity.Trip;
import com.example.mexpense.services.TripService;
import com.example.mexpense.ultilities.Constants;
import com.example.mexpense.ultilities.Utilities;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class TripFormFragment extends Fragment implements View.OnClickListener {

    private TripFormViewModel mViewModel;
    private FragmentTripFormBinding binding;

    final Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener startDate;
    private DatePickerDialog.OnDateSetListener endDate;

    private TextInputEditText editStartDate;
    private TextInputEditText editEndDate;
    private AutoCompleteTextView editTripType;
    private TextInputEditText editDestination;
    private TextInputEditText editDescription;
    private TextInputEditText editParticipant;
    private Switch assessment;

    private TripService service;
    int tripId;

    private static final String NAME_KEY = "category";
    private static final String DESTINATION_KEY = "destination";
    private static final String START_DATE_KEY = "start";
    private static final String END_DATE_KEY = "end";
    private static final String PARTICIPANT_KEY = "participant";
    private static final String DESCRIPTION_KEY = "notes";
    private static final String ASSESSMENT_KEY = "assessment";

    public static TripFormFragment newInstance() {
        return new TripFormFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(TripFormViewModel.class);
        binding = FragmentTripFormBinding.inflate(inflater, container, false);
        service = new TripService(getContext());

        try {
            tripId = getArguments().getInt("tripId");
        } catch (Exception e) {
            tripId = -1;
        }

        editTripType = binding.inputTextTripType;
        editDestination = binding.inputTextDestination;
        editDescription = binding.inputTextDescription;
        editParticipant = binding.inputTextParticipant;
        assessment = binding.switchRequiredAssessment;

        editStartDate = binding.inputStartDate;
        editStartDate.setOnClickListener(this);
        startDate = (datePicker, year, month, day) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, day);
            updateDate(binding.inputStartDate);
        };

        editEndDate = binding.inputEndDate;
        editEndDate.setOnClickListener(this);
        endDate = (datePicker, year, month, day) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, day);
            updateDate(binding.inputEndDate);
        };

        Button btnSaveTrip = binding.btnSaveTrip;
        btnSaveTrip.setOnClickListener(this);

        mViewModel.trip.observe(
                getViewLifecycleOwner(),
                trip -> {
                    if (savedInstanceState != null) {
                        binding.inputTextTripType.setText(savedInstanceState.getString(NAME_KEY));
                        getTrips();
                        binding.inputStartDate.setText(savedInstanceState.getString(START_DATE_KEY));
                        binding.inputEndDate.setText(savedInstanceState.getString(END_DATE_KEY));
                        binding.inputTextDestination.setText(savedInstanceState.getString(DESTINATION_KEY));
                        binding.inputTextDescription.setText(savedInstanceState.getString(DESCRIPTION_KEY));
                        binding.inputTextParticipant.setText(savedInstanceState.getString(PARTICIPANT_KEY));
                        binding.switchRequiredAssessment.setChecked(savedInstanceState.getBoolean(ASSESSMENT_KEY));
                    } else {
                        if(tripId == -1) {
                            binding.inputTextTripType.setText(Constants.trips[0]);
                        } else {
                            binding.inputTextTripType.setText(trip.getName());
                        }
                        getTrips();
                        binding.inputStartDate.setText(trip.getStartDate());
                        binding.inputEndDate.setText(trip.getEndDate());
                        binding.inputTextDestination.setText(trip.getDestination());
                        binding.inputTextDescription.setText(trip.getDescription());
                        binding.inputTextParticipant.setText(String.valueOf(trip.getParticipants()));
                        binding.switchRequiredAssessment.setChecked(trip.getRequiredAssessment());
                    }

                }
        );
        service.getTrip(mViewModel.trip, tripId);

        AppCompatActivity app = (AppCompatActivity) getActivity();
        ActionBar ab = app.getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_back);
        setHasOptionsMenu(true);

        if (tripId == -1) {
            ab.setTitle("Adding New Trip");
        } else {
            ab.setTitle("Editing Trip");
        }

        requireActivity().invalidateOptionsMenu();

        return binding.getRoot();
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
                Utilities.hideInput(getActivity(), getView());
                Navigation.findNavController(getView()).navigateUp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (tripId != Constants.NEW_EXPENSE) {
            menu.findItem(R.id.action_delete).setVisible(true);
            menu.findItem(R.id.action_edit).setVisible(true);
        } else {
            menu.findItem(R.id.action_delete).setVisible(false);
            menu.findItem(R.id.action_edit).setVisible(false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(NAME_KEY, editTripType.getText().toString());
        savedInstanceState.putString(DESTINATION_KEY, editDestination.getText().toString());
        savedInstanceState.putString(START_DATE_KEY, editStartDate.getText().toString());
        savedInstanceState.putString(END_DATE_KEY, editEndDate.getText().toString());
        savedInstanceState.putString(PARTICIPANT_KEY, editParticipant.getText().toString());
        savedInstanceState.putString(DESCRIPTION_KEY, editDescription.getText().toString());
        savedInstanceState.putBoolean(ASSESSMENT_KEY, binding.switchRequiredAssessment.isChecked());
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.inputStartDate:
                new DatePickerDialog(getContext(), startDate, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.inputEndDate:
                new DatePickerDialog(getContext(), endDate, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.inputTextTripType:
                Utilities.hideInput(getActivity(), getView());
                getTrips();
                return;
            case R.id.btnSaveTrip:
                handleSave();
            default:
                break;
        }
    }

    private void handleSave() {
        if (validation()) {
            String assessmentRequired = assessment.isChecked() ? "Assessment Required" : "Assessment Not Required";
            new AlertDialog.Builder(getContext()).setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Confirmation").setMessage("Are you sure?" + "\nTrip Type: " + editTripType.getText().toString()
                            + "\nDestination: " + editDestination.getText().toString() + "\nFrom "
                            + editStartDate.getText().toString() + " to " + editEndDate.getText().toString()
                            + "\nParticipants: " + Integer.parseInt(editParticipant.getText().toString())
                            + "\n" + assessmentRequired + "\nOther notes: " + editDescription.getText().toString())
                    .setPositiveButton("Yes", (arg0, arg1) -> {
                        if (tripId == -1) {
                            service.addTrip(getFormInput());
                            Toast.makeText(getContext(), "New Trip added", Toast.LENGTH_SHORT).show();
                            Utilities.hideInput(getActivity(), getView());
                            Navigation.findNavController(getView()).navigate(R.id.tripMainFragment);
                        } else {
                            service.updateTrip(tripId, getFormInput());
                            Toast.makeText(getContext(), "Trip updated", Toast.LENGTH_SHORT).show();
                            Utilities.hideInput(getActivity(), getView());
                            Bundle b = new Bundle();
                            b.putInt("tripId", tripId);
                            Navigation.findNavController(getView()).navigate(R.id.expenseMainFragment, b);
                        }
                    }).setNegativeButton("No", null).show();
        }
    }

    private boolean validation() {
        boolean result = true;
        if (editDestination.getText().toString().equals("")) {
            binding.layoutDestination.setError(Constants.EMPTY_FIELD_MESSAGE);
            result = false;
        } else if(!Utilities.onlyCharsAndSpace(editDestination.getText().toString())) {
            binding.layoutDestination.setError(Constants.CHARACTERS_ONLY_MESSAGE);
            result = false;
        } else {
            binding.layoutDestination.setError(null);
        }

        if (editStartDate.getText().toString().equals("")) {
            binding.layoutStartDate.setError(Constants.EMPTY_FIELD_MESSAGE);
            result = false;
        } else {
            binding.layoutStartDate.setError(null);
        }

        if (editEndDate.getText().toString().equals("")) {
            binding.layoutEndDate.setError(Constants.EMPTY_FIELD_MESSAGE);
            result = false;
        } else {
            binding.layoutEndDate.setError(null);
        }

        if (!editEndDate.getText().toString().equals("") && !editStartDate.getText().toString().equals("")) {
            if (!dateValidation(binding.inputStartDate.getText().toString(), binding.inputEndDate.getText().toString())) {
                binding.layoutStartDate.setError("Start date must be");
                binding.layoutEndDate.setError("before end date");
                result = false;
            } else {
                binding.layoutStartDate.setError(null);
            }
        }

        if (Integer.parseInt(editParticipant.getText().toString()) == 0) {
            binding.layoutParticipants.setError(Constants.EMPTY_FIELD_MESSAGE);
            result = false;
        } else {
            binding.layoutParticipants.setError(null);
        }

        if(!binding.inputTextDescription.getText().toString().equals("")){
            if (!Utilities.onlyCharsAndSpace(binding.inputTextDescription.getText().toString())) {
                binding.layoutDescription.setError(Constants.CHARACTERS_ONLY_MESSAGE);
                result = false;
            } else {
                binding.layoutDescription.setError(null);
            }
        }

        return result;
    }

    private boolean dateValidation(String startStr, String endStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_DATABASE);
        LocalDate startDate = LocalDate.parse(startStr, formatter);
        LocalDate endDate = LocalDate.parse(endStr, formatter);
        return endDate.isAfter(startDate) || endDate.isEqual(startDate);
    }

    private Trip getFormInput() {
        String name = editTripType.getText().toString();
        String destination = editDestination.getText().toString();
        String startDate = editStartDate.getText().toString();
        String endDate = editEndDate.getText().toString();
        boolean a = assessment.isChecked();
        String description = editDescription.getText().toString();
        int participants = Integer.parseInt(editParticipant.getText().toString());
        return new Trip(-1, name, destination, startDate, endDate, a, participants, description);
    }

    private void updateDate(TextView dateView) {
        String format = Constants.DATE_FORMAT_DATABASE;
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
        dateView.setText(dateFormat.format(myCalendar.getTime()));
    }

    // Reference: https://developer.android.com/reference/android/widget/AutoCompleteTextView
    private void getTrips() {
        ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.dropdown_item, Constants.trips);
        editTripType.setAdapter(adapter);
        editTripType.setOnClickListener(this);
    }
}