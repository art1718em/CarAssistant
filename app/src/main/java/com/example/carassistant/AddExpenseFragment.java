package com.example.carassistant;

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
import android.widget.Toast;

import com.example.carassistant.databinding.FragmentAddExpenseBinding;
import com.example.carassistant.databinding.FragmentExpensesBinding;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddExpenseFragment extends Fragment {

    private FragmentAddExpenseBinding binding;
    Disposable disposable;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddExpenseBinding.inflate(inflater, container, false);


        ExpenseDB expenseDB = ExpenseDB.getInstance(requireContext());
        ExpenseDao expenseDao = expenseDB.expenseDao();

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