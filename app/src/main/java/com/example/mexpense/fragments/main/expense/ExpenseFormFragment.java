
package com.example.mexpense.fragments.main.expense;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.mexpense.R;
import com.example.mexpense.databinding.FragmentExpenseFormBinding;
import com.example.mexpense.entity.Expense;
import com.example.mexpense.services.ExpenseService;
import com.example.mexpense.services.LocationService;
import com.example.mexpense.ultilities.Constants;
import com.example.mexpense.ultilities.Utilities;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class ExpenseFormFragment extends Fragment implements View.OnClickListener {

    private ExpenseFormViewModel mViewModel;
    final Calendar myCalendar = Calendar.getInstance();
    private FragmentExpenseFormBinding binding;
    private ExpenseService service;

    DatePickerDialog.OnDateSetListener date;

    private int expenseId;
    private int tripId;

    private TextInputLayout categoryLayout;
    private TextInputLayout costLayout;
    private TextInputLayout amountLayout;
    private TextInputLayout dateLayout;

    private AutoCompleteTextView editCategory;
    private TextInputEditText editCost;
    private TextInputEditText editDate;
    private TextInputEditText editAmount;

    private Button buttonAddImage;

    private LocationService locationService;

    private static final String CATEGORY_KEY = "category";
    private static final String COST_KEY = "cost";
    private static final String AMOUNT_KEY = "amount";
    private static final String DATE_KEY = "date";
    private static final String COMMENT_KEY = "comment";

    public static ExpenseFormFragment newInstance() {
        return new ExpenseFormFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(this).get(ExpenseFormViewModel.class);
        binding = FragmentExpenseFormBinding.inflate(inflater, container, false);
        service = new ExpenseService(getContext());
        locationService = new LocationService(getContext());
        locationService.getLocation();

        try {
            expenseId = getArguments().getInt("expenseId");
        } catch (Exception e) {
            expenseId = -1;
        }

        tripId = getArguments().getInt("tripId");

        categoryLayout = binding.textInputLayoutCategory;
        costLayout = binding.textInputLayoutCost;
        dateLayout = binding.textInputLayoutDate;
        amountLayout = binding.textInputLayoutAmount;

        editCategory = binding.inputTextCategories;
        editCost = binding.inputCost;
        editDate = binding.inputDate;
        editAmount = binding.inputAmount;

        buttonAddImage = binding.btnImage;
        binding.previewIcon.setOnClickListener(this);

        Button saveButton = binding.btnSaveExpense;
        saveButton.setOnClickListener(this);
        editDate.setOnClickListener(this);
        buttonAddImage.setOnClickListener(this);

        date = (datePicker, year, month, day) -> {
            setCalendar(year, month, day);
            updateDate();
        };


        mViewModel.expense.observe(
                getViewLifecycleOwner(),
                expense -> {
                    if (savedInstanceState != null) {
                        binding.inputTextCategories.setText(savedInstanceState.getString(CATEGORY_KEY));
                        getCategories();
                        binding.inputDate.setText(expense.getDate());
                        binding.inputCost.setText(String.valueOf(savedInstanceState.getDouble(COST_KEY)));
                        binding.inputAmount.setText(String.valueOf(savedInstanceState.getInt(AMOUNT_KEY)));
                        binding.inputTextComment.setText(savedInstanceState.getString(COMMENT_KEY));
                    } else {
                        if (expenseId == -1) {
                            editCategory.setText(Constants.categories[0]);
                        } else {
                            editCategory.setText(expense.getCategory());
                        }
                        getCategories();
                        binding.inputDate.setText(expense.getDate());
                        binding.inputCost.setText(String.valueOf(expense.getCost()));
                        binding.inputAmount.setText(String.valueOf(expense.getAmount()));
                        binding.inputTextComment.setText(expense.getComment());

                        if(!expense.getImage().equals("")){
                            currentPhotoPath = expense.getImage();
                            buttonAddImage.setText("Change Image");
                            binding.previewIcon.setImageBitmap(Utilities.getImageFromURL(expense.getImage(), 52, 52));
                        } else {
                            binding.previewIcon.setImageResource(R.drawable.ic_camera);
                        }
                    }
                }
        );


        service.getExpenseById(mViewModel.expense, expenseId);

        AppCompatActivity app = (AppCompatActivity) getActivity();
        ActionBar ab = app.getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_back);

        if(expenseId == Constants.NEW_EXPENSE){
            ab.setTitle("Adding New Expense");
        } else {
            ab.setTitle("Editing Expense");
        }

        setHasOptionsMenu(true);

        requireActivity().invalidateOptionsMenu();

        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.edit_menu, menu);
        if (expenseId != Constants.NEW_EXPENSE) {
            menu.findItem(R.id.action_delete).setVisible(true);
            menu.findItem(R.id.action_edit).setVisible(true);
        } else {
            menu.findItem(R.id.action_delete).setVisible(false);
            menu.findItem(R.id.action_edit).setVisible(false);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Utilities.hideInput(getActivity(), getView());
                // Close service
                locationService.removeService();
                Utilities.hideInput(getActivity(), getView());
                Navigation.findNavController(getView()).navigateUp();
                return true;
            case R.id.action_delete:
                handleDelete();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        Expense e = mViewModel.expense.getValue();
        if (e != null
                && e.getId() == Constants.NEW_EXPENSE) {
            menu.findItem(R.id.action_delete).setVisible(false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(CATEGORY_KEY, editCategory.getText().toString());
        savedInstanceState.putDouble(COST_KEY, Double.parseDouble(editCost.getText().toString()));
        savedInstanceState.putInt(AMOUNT_KEY, Integer.parseInt(editAmount.getText().toString()));
        savedInstanceState.putString(DATE_KEY, editDate.getText().toString());
        savedInstanceState.putString(COMMENT_KEY, binding.inputTextComment.getText().toString());
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSaveExpense:
                locationService.getLocation();
                handleSave();
                break;
            case R.id.btnImage:
                setImage();
                break;
            case R.id.previewIcon:
                viewPreview();
                break;
            case R.id.inputDate:
                setDate();
                break;
            case R.id.inputTextCategories:
                getCategories();
            default:
                break;
        }
    }

    private void viewPreview(){
        if(currentPhotoPath.equals("")) return;

        Dialog dialog = new Dialog(getContext());
        LayoutInflater inflater = getLayoutInflater();

        View preview = (View) inflater.inflate(R.layout.image_preview, null);

        dialog.setContentView(preview);

        ImageView imageView = preview.findViewById(R.id.previewImageView);
        imageView.setImageBitmap(Utilities.getImageFromURL(currentPhotoPath, 300, 400));

        Button removeImage = preview.findViewById(R.id.btnRemoveCurrentImage);
        removeImage.setOnClickListener(view -> {
            currentPhotoPath = "";
            binding.previewIcon.setImageResource(R.drawable.ic_camera);
            dialog.dismiss();
            Toast.makeText(getContext(), "Image removed", Toast.LENGTH_SHORT).show();
        });

        dialog.show();
    }

    private void setImage() {
        int MY_PERMISSIONS_REQUEST_CAMERA = 0;
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else{
                Toast.makeText(getContext(), "Camera access must be allowed to capture image", Toast.LENGTH_SHORT).show();
            }
        } else {
            try {
                openCamera();
            } catch (Exception e) {
                Toast.makeText(getContext(), "Camera access must be allowed to capture image", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
            }
        }
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;

        try {
            Log.i("FILE CREATION", "CREATING FILES");
            photoFile = createImageFile();
        } catch (IOException ex) {
            Log.i("FILE ERROR", ex.toString());
        }

        Uri photoURI = FileProvider.getUriForFile(getContext(), "com.example.android.fileprovider", photoFile);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
    }

    String currentPhotoPath = "";
    Uri contentUri;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private File createImageFile() throws IOException {
        String imageFileName = "JPEG_" +  expenseId;
        File storage = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File expenseImage = File.createTempFile(imageFileName, ".jpg", storage);
        currentPhotoPath = expenseImage.getAbsolutePath();
        return expenseImage;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getContext().sendBroadcast(mediaScanIntent);
        if (!currentPhotoPath.equals("")) {
            buttonAddImage.setText("Change Image");
            binding.previewIcon.setImageBitmap(Utilities.getImageFromURL(currentPhotoPath, 52 , 52));
        } else {
            binding.previewIcon.setImageResource(R.drawable.ic_camera);
        }
    }

    private void getCategories() {
        ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.dropdown_item, Constants.categories);
        editCategory.setAdapter(adapter);
        editCategory.setOnClickListener(this);
    }

    private void handleDelete() {
        new AlertDialog.Builder(getContext()).setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Confirmation").setMessage("Are you sure?")
                .setPositiveButton("Yes", (arg0, arg1) -> {
                    service.deleteExpense(expenseId);
                    Bundle bundle = new Bundle();
                    bundle.putInt("tripId", tripId);
                    Utilities.hideInput(getActivity(), getView());
                    locationService.removeService();
                    Utilities.hideInput(getActivity(), getView());
                    Navigation.findNavController(getView()).navigate(R.id.expenseMainFragment, bundle);
                    Toast.makeText(getContext(), "Expense deleted", Toast.LENGTH_SHORT).show();
                }).setNegativeButton("No", null).show();
    }

    private void handleSave() {
        if (validation()) {
            String comment = binding.inputTextComment.getText().toString().equals("")
                    ? "No comments"
                    : binding.inputTextComment.getText().toString();
            new AlertDialog.Builder(getContext()).setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Confirmation").setMessage("This expense will be added:\nCategory: "
                            + editCategory.getText().toString() + "\nCost: $"
                            + editCost.getText().toString() + "\nAmount: "
                            + editAmount.getText().toString() + "\nDate: "
                            + editDate.getText().toString() + "\nComment: "
                            + comment + "\n")
                    .setPositiveButton("Yes", (arg0, arg1) -> {
                        if (expenseId == -1) {
                            service.addExpense(getFormInput());
                            Toast.makeText(getContext(), "New Expense added", Toast.LENGTH_SHORT).show();
                        } else {
                            service.updateExpense(expenseId, getFormInput());
                            Toast.makeText(getContext(), "Expense updated", Toast.LENGTH_SHORT).show();
                        }
                        // Close service;
                        locationService.removeService();
                        Utilities.hideInput(getActivity(), getView());
                        Navigation.findNavController(getView()).navigateUp();
                    }).setNegativeButton("No", null).show();
        }
    }

    private boolean validation() {
        boolean result = true;
        String startDate = getArguments().getString("startDate");
        String endDate = getArguments().getString("endDate");

        if (editAmount.getText().toString().equals("")) {
            binding.textInputLayoutAmount.setError(Constants.EMPTY_FIELD_MESSAGE);
            result = false;
        } else {
            categoryLayout.setError(null);
        }

        if (editDate.getText().toString().equals("")) {
            dateLayout.setError(Constants.EMPTY_FIELD_MESSAGE);
            result = false;
        } else {
            if (!dateValidation(startDate, endDate, binding.inputDate.getText().toString())) {
                dateLayout.setError("Expense date must be within " + startDate + " and " + endDate);
                result = false;
            }
        }

        if(!binding.inputTextComment.getText().toString().equals("")){
            if(!Utilities.onlyCharsAndSpace(binding.inputTextComment.getText().toString())){
                binding.layoutComment.setError(Constants.CHARACTERS_ONLY_MESSAGE);
            }
        } else {
            binding.layoutComment.setError(null);
        }

        if (Integer.parseInt(editAmount.getText().toString()) == 0) {
            amountLayout.setError(Constants.EMPTY_FIELD_MESSAGE);
            result = false;
        } else {
            amountLayout.setError(null);
        }

        if (Float.parseFloat(editCost.getText().toString()) == 0.0) {
            costLayout.setError(Constants.EMPTY_FIELD_MESSAGE);
            result = false;
        } else {
            costLayout.setError(null);
        }

        return result;
    }

    private boolean dateValidation(String startStr, String endStr, String expenseStr) {
        DateTimeFormatter sql = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_DATABASE);
        LocalDate startDate = LocalDate.parse(startStr, sql);
        LocalDate endDate = LocalDate.parse(endStr, sql);
        LocalDate expenseDate = LocalDate.parse(expenseStr, sql);
        return expenseDate.isAfter(startDate)
                && expenseDate.isBefore(endDate)
                || expenseDate.isEqual(startDate)
                || expenseDate.isEqual(endDate);
    }

    private Expense getFormInput() {
        String category = editCategory.getText().toString();
        String date = editDate.getText().toString();
        String comment = binding.inputTextComment.getText().toString();
        double cost = Double.parseDouble(editCost.getText().toString());
        int amount = Integer.parseInt(editAmount.getText().toString());
        locationService.getLocation();

        return new Expense(-1, category, cost, amount, date, comment, tripId, locationService.getLatitude(), locationService.getLongitude(), currentPhotoPath);
    }

    private void updateDate() {
        String format = Constants.DATE_FORMAT_DATABASE;
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
        editDate.setText(dateFormat.format(myCalendar.getTime()));
    }

    public void setDate() {
        DateTimeFormatter source = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_DATABASE);
        if (expenseId == -1) {
            LocalDate startDate = LocalDate.parse(getArguments().getString("startDate"), source);
            setCalendar(startDate.getYear(), startDate.getMonthValue() - 1, startDate.getDayOfMonth());
        } else {
            LocalDate currentDate = LocalDate.parse(mViewModel.expense.getValue().getDate(), source);
            setCalendar(currentDate.getYear(), currentDate.getMonthValue() - 1, currentDate.getDayOfMonth());
        }
        new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void setCalendar(int year, int month, int day) {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, month);
        myCalendar.set(Calendar.DAY_OF_MONTH, day);
    }
}