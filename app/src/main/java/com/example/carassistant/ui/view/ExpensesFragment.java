package com.example.carassistant.ui.view;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.carassistant.core.Error;

import com.example.carassistant.core.Success;

import com.example.carassistant.data.models.ExpenseDto;
import com.example.carassistant.ui.adapters.ExpenseAdapter;
import com.example.carassistant.R;
import com.example.carassistant.databinding.FragmentExpensesBinding;

import com.example.carassistant.ui.viewModels.ExpensesViewModel;


import java.util.List;


public class ExpensesFragment extends Fragment {

    private FragmentExpensesBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentExpensesBinding.inflate(inflater, container, false);

        ExpensesViewModel expensesViewModel = new ViewModelProvider(this).get(ExpensesViewModel.class);


        String idCar = requireActivity().getSharedPreferences("id", Context.MODE_PRIVATE).getString("carId", "");

        expensesViewModel.loadListExpenses(idCar);

        expensesViewModel.resultOfListExpenses.observe(getViewLifecycleOwner(), result -> {
            if (result instanceof Success){
                ExpenseAdapter expenseAdapter =new ExpenseAdapter(((Success<List<ExpenseDto>>) result).getData());
                binding.recyclerviewListExpenses.setLayoutManager(new LinearLayoutManager(requireContext()));
                binding.recyclerviewListExpenses.setAdapter(expenseAdapter);
            }else
                Toast.makeText(container.getContext(), ((Error)result).getMessage(), Toast.LENGTH_SHORT).show();
        });






        binding.iconAddExpense.setOnClickListener(v -> Navigation.findNavController(binding.getRoot())
                .navigate(R.id.action_expensesFragment_to_addExpenseFragment));

        binding.iconBack.setOnClickListener(v -> Navigation.findNavController(binding.getRoot()).popBackStack());

        return binding.getRoot();
    }



}