package com.example.carassistant.ui.view;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.carassistant.data.models.Expense;
import com.example.carassistant.ui.adapters.ExpenseAdapter;
import com.example.carassistant.data.room.root.ExpenseDB;
import com.example.carassistant.data.room.dao.ExpenseDao;
import com.example.carassistant.R;
import com.example.carassistant.databinding.FragmentExpensesBinding;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ExpensesFragment extends Fragment {

    private FragmentExpensesBinding binding;
    Disposable expenseListDisposable;

    public static final String key = "carIdKey";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentExpensesBinding.inflate(inflater, container, false);




    ExpenseDB expenseDB = ExpenseDB.getInstance(requireContext());
    ExpenseDao expenseDao = expenseDB.expenseDao();

    expenseListDisposable = expenseDao
            .getALLThisId(requireActivity().getSharedPreferences("id", Context.MODE_PRIVATE).getInt(DiagramFragment.key, -1))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onExpensesLoaded, throwable -> Log.wtf("error", throwable.toString()));


        binding.iconAddExpense.setOnClickListener(v -> Navigation.findNavController(binding.getRoot())
                .navigate(R.id.action_expensesFragment_to_addExpenseFragment));

        binding.iconBack.setOnClickListener(v -> Navigation.findNavController(binding.getRoot()).popBackStack());

        return binding.getRoot();
    }

    private void onExpensesLoaded(List<Expense> expenses) {
        ExpenseAdapter expenseAdapter =new ExpenseAdapter((ArrayList<Expense>) expenses);
        binding.recyclerviewListExpenses.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerviewListExpenses.setAdapter(expenseAdapter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(expenseListDisposable != null)
            expenseListDisposable.dispose();
    }
}