package com.example.carassistant;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
                disposable = expenseDao
                        .addExpense(
                                new Expense(
                                        binding.etExpense.getText().toString(),
                                        binding.spinner.getSelectedItem().toString(),
                                        binding.etData.getText().toString(),
                                        binding.etComment.getText().toString(),
                                        Integer.parseInt(binding.etMileage.getText().toString())


                                        )
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onExpenseAdded);
            }

            private void onExpenseAdded(){
                Navigation.findNavController(binding.getRoot()).popBackStack();
            }
        });



        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}