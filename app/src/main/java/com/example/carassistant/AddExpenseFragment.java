package com.example.carassistant;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.carassistant.databinding.FragmentAddExpenseBinding;
import com.example.carassistant.databinding.FragmentExpensesBinding;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddExpenseFragment extends Fragment {

    private FragmentAddExpenseBinding binding;
    Disposable disposable;

    DatePickerDialog.OnDateSetListener dateSetListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddExpenseBinding.inflate(inflater, container, false);


        ExpenseDB expenseDB = ExpenseDB.getInstance(requireContext());
        ExpenseDao expenseDao = expenseDB.expenseDao();


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
                            .addExpense(
                                    new Expense(
                                            getActivity().getSharedPreferences("id", Context.MODE_PRIVATE).getInt(DiagramFragment.key, -1),
                                            binding.etExpense.getText().toString(),
                                            binding.spinner.getSelectedItem().toString(),
                                            binding.etData.getText().toString(),
                                            binding.etComment.getText().toString(),
                                            Integer.parseInt(binding.etMileage.getText().toString())


                                    )
                            )
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(this::onExpenseAdded, throwable -> {
                                Log.wtf("Error", throwable.toString());
                            });
//                ExpenseDB.databaseWriteExecutor.execute(() -> {
//                    expenseDao
//                            .addExpense(
//                                    new Expense(
//                                            //carBundle.getInt(ExpensesFragment.key),
//                                            binding.etExpense.getText().toString(),
//                                            binding.spinner.getSelectedItem().toString(),
//                                            binding.etData.getText().toString(),
//                                            binding.etComment.getText().toString(),
//                                            Integer.parseInt(binding.etMileage.getText().toString())
//
//
//                                    )
//                            );
//                });
//                onExpenseAdded();
                }
            }

            private void onExpenseAdded(){
                //Toast.makeText(getContext(), String.valueOf(carBundle.getInt(ExpensesFragment.key)), Toast.LENGTH_SHORT).show();
                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_addExpenseFragment_to_diagramFragment);
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