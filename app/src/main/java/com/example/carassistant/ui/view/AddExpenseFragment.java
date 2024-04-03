package com.example.carassistant.ui.view;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.carassistant.data.models.Expense;
import com.example.carassistant.data.room.root.ExpenseDB;
import com.example.carassistant.data.room.dao.ExpenseDao;
import com.example.carassistant.R;
import com.example.carassistant.databinding.FragmentAddExpenseBinding;


import java.util.Calendar;
import java.util.Date;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddExpenseFragment extends Fragment {

    private FragmentAddExpenseBinding binding;
    Disposable disposable;
    private NavController navController;

    DatePickerDialog.OnDateSetListener dateSetListener;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(binding.getRoot());
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddExpenseBinding.inflate(inflater, container, false);

        ExpenseDB expenseDB = ExpenseDB.getInstance(requireContext());
        ExpenseDao expenseDao = expenseDB.expenseDao();


        binding.iconCalendar.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), dateSetListener,
                    year, month, day);
            datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
            datePickerDialog.show();

        });



        dateSetListener = (view, year, month, dayOfMonth) -> {
            String selectedDate = dayOfMonth + "-" + (month + 1) + "-" + year + " ";
            binding.etDate.setText(selectedDate);
        };




        binding.btnAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = true;
                ColorStateList colorStateList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.red));
                ColorStateList primalColor = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.background_edittext));
                EditText[] arrayEditText = new EditText[]{binding.etSumExpense, binding.etDate, binding.etComment,
                        binding.etMileage};
                for (EditText editText: arrayEditText){
                    if (editText.getText().toString().isEmpty()) {
                        flag = false;
                        editText.setBackgroundTintList(colorStateList);
                    }
                    else
                        binding.etSumExpense.setBackgroundTintList(primalColor);
                }
                if(flag) {
                    disposable = expenseDao
                            .addExpense(
                                    new Expense(
                                            requireActivity().getSharedPreferences("id", Context.MODE_PRIVATE).getInt(DiagramFragment.key, -1),
                                            binding.etSumExpense.getText().toString(),
                                            binding.spinnerChoiceCategory.getSelectedItem().toString(),
                                            binding.etDate.getText().toString(),
                                            binding.etComment.getText().toString(),
                                            Integer.parseInt(binding.etMileage.getText().toString())


                                    )
                            )
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(this::onExpenseAdded, throwable -> Log.wtf("Error", throwable.toString()));
                }
            }

            @SuppressLint("RestrictedApi")
            private void onExpenseAdded(){
                if(navController.getBackQueue().get(navController.getBackQueue().getSize()-2).getDestination().getId() == R.id.diagramFragment)
                    Navigation.findNavController(binding.getRoot()).navigate(R.id.action_addExpenseFragment_to_diagramFragment);
                else
                    Navigation.findNavController(binding.getRoot()).navigate(R.id.action_addExpenseFragment_to_expensesFragment);

            }

        });
        return binding.getRoot();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null)
            disposable.dispose();
    }
}