package com.example.carassistant;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.example.carassistant.databinding.FragmentRedactionExpenseBinding;

import java.util.Calendar;
import java.util.Date;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class RedactionExpenseFragment extends Fragment {

    private FragmentRedactionExpenseBinding binding;
    Disposable disposable;
    Disposable expenseListDisposable;

    DatePickerDialog.OnDateSetListener dateSetListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRedactionExpenseBinding.inflate(inflater, container, false);

        ExpenseDB expenseDB = ExpenseDB.getInstance(requireContext());
        ExpenseDao expenseDao = expenseDB.expenseDao();
        Bundle expenseBundle = requireArguments();
        if (expenseBundle.getInt("expenseIdKey", -1) != -1){
            expenseListDisposable = expenseDao
                    .getExpenseById(expenseBundle.getInt("expenseIdKey", -1))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onExpensesLoaded, throwable -> {
                        Log.wtf("error", throwable.toString());
                    });
        }


        binding.iconCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), dateSetListener,
                        year,        month,
                        day);
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();

            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int mYear = year;
                int mMonth = month;
                int mDay = dayOfMonth;
                String selectedDate = new StringBuilder().append(mDay)
                        .append("-").append(mMonth + 1).append("-").append(mYear)
                        .append(" ").toString();
                binding.etData.setText(selectedDate);
            }
        };




        binding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = true;
                ColorStateList colorStateList = ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.red));
                ColorStateList primalColor = ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.background_edittext));
                if (binding.etExpense.getText().toString().equals("")) {
                    flag = false;
                    binding.etExpense.setBackgroundTintList(colorStateList);
                }
                else
                    binding.etExpense.setBackgroundTintList(primalColor);
                if(binding.etData.getText().toString().equals("")) {
                    flag = false;
                    binding.etData.setBackgroundTintList(colorStateList);
                }
                else
                    binding.etData.setBackgroundTintList(primalColor);
                if(binding.etComment.getText().toString().equals("")) {
                    flag = false;
                    binding.etComment.setBackgroundTintList(colorStateList);
                }
                else
                    binding.etComment.setBackgroundTintList(primalColor);
                if(binding.etMileage.getText().toString().equals("")) {
                    flag = false;
                    binding.etMileage.setBackgroundTintList(colorStateList);
                }
                else
                    binding.etMileage.setBackgroundTintList(primalColor);
                if(flag) {
                    disposable = expenseDao
                            .updateExpense(
                                    expenseBundle.getInt("expenseIdKey", -1),
                                    binding.etExpense.getText().toString(),
                                    binding.spinner.getSelectedItem().toString(),
                                    binding.etData.getText().toString(),
                                    binding.etComment.getText().toString(),
                                    Integer.parseInt(binding.etMileage.getText().toString())
                            )
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(this::onExpenseAdded, throwable -> {
                                Log.wtf("Error", throwable.toString());
                            });
//
                }
            }

            private void onExpenseAdded(){
                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_redactionExpenseFragment_to_expenseDescriptionFragment,
                        expenseBundle);
            }

        });


        return binding.getRoot();
    }

    private void onExpensesLoaded(Expense expense) {
        switch (expense.getCategory()){
            case "Топливо":
                binding.spinner.setSelection(0);
                break;
            case "Запчасти":
                binding.spinner.setSelection(1);
                break;
            case "Шины":
                binding.spinner.setSelection(2);
                break;
            case "Диски":
                binding.spinner.setSelection(3);
                break;
            case "Работа сервиса":
                binding.spinner.setSelection(4);
                break;
            case "Автомойка":
                binding.spinner.setSelection(5);
                break;
            case "Другое":
                binding.spinner.setSelection(6);
                break;
        }
        binding.etExpense.setText(expense.getExpense());
        binding.etComment.setText(expense.getComment());
        binding.etData.setText(expense.getData());
        binding.etMileage.setText(String.valueOf(expense.getMileage()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null)
            disposable.dispose();
    }
}