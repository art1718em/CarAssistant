package com.example.carassistant.ui.view;


import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.carassistant.core.Error;
import com.example.carassistant.core.ExpenseStatus;
import com.example.carassistant.core.Success;
import com.example.carassistant.data.models.Expense;
import com.example.carassistant.R;
import com.example.carassistant.databinding.FragmentExpenseDescriptionBinding;
import com.example.carassistant.ui.viewModels.ExpenseDescriptionViewModel;



public class ExpenseDescriptionFragment extends Fragment {
    private FragmentExpenseDescriptionBinding binding;
    public static final String expenseIndexKey = "expenseIndexKey";

    public static final String expenseIdKey = "expenseIdKey";

    private ExpenseDescriptionViewModel expenseDescriptionViewModel;

    private String expenseId;

    private ExpenseStatus expenseStatus;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentExpenseDescriptionBinding.inflate(inflater, container, false);

        binding.constantLayout.setVisibility(View.INVISIBLE);
        binding.progressBar.setVisibility(View.VISIBLE);

        expenseDescriptionViewModel = new ViewModelProvider(this).get(ExpenseDescriptionViewModel.class);

        Bundle expenseBundle = requireArguments();
        int indexExpense = expenseBundle.getInt(expenseIndexKey);
        String idCar = expenseBundle.getString(AddCarFragment.carIdKey);

        Bundle idCarBundle = new Bundle();
        idCarBundle.putString(AddCarFragment.carIdKey, expenseBundle.getString(AddCarFragment.carIdKey));

        SharedPreferences sharedPreferences = getActivity()
                .getSharedPreferences(LoginFragment.sharedPreferencesName, Context.MODE_PRIVATE);

        String idGuestUser = sharedPreferences.getString(LoginFragment.sharedPreferencesKey, "null");

        if (!idGuestUser.equals("null")){
            binding.btnReject.setVisibility(View.GONE);
            binding.btnApprove.setVisibility(View.GONE);
            expenseDescriptionViewModel.loadExpenseDescriptionGuest(
                    sharedPreferences.getString(LoginFragment.sharedPreferencesKey, "null"), indexExpense);
        }
        else{
            expenseDescriptionViewModel.loadExpenseDescription(idCar, indexExpense);
        }

        expenseDescriptionViewModel.resultOfLoadExpenseDescription.observe(getViewLifecycleOwner(), result -> {
            binding.progressBar.setVisibility(View.GONE);
            if (result instanceof Success){
                Pair<String, Expense> pair = ((Success<Pair<String, Expense>>) result).getData();
                Expense expense = pair.second;
                expenseId = pair.first;
                expenseStatus = expense.getExpenseStatus();
                if (expenseStatus != ExpenseStatus.UNDER_CONSIDERATION){
                    binding.btnReject.setVisibility(View.GONE);
                    binding.btnApprove.setVisibility(View.GONE);
                }
                binding.tvCategoryData.setText(expense.getCategory());
                binding.tvCostData.setText(String.valueOf(expense.getExpense()));
                binding.tvCommentData.setText(expense.getComment());
                binding.tvDateData.setText(expense.getData());
                binding.tvMileageData.setText(String.valueOf(expense.getMileage()));
                if (!idGuestUser.equals("null")){
                    if (expenseStatus == ExpenseStatus.APPROVED)
                        binding.tvStatusData.setText("Одобрено");
                    else if(expenseStatus == ExpenseStatus.REJECTED)
                        binding.tvStatusData.setText("Отклонено");
                    else
                        binding.tvStatusData.setText("На рассмотрении");
                }else{
                    if (expenseStatus == null)
                        binding.tvStatusData.setText("Добавлено вами");
                    else if(expenseStatus == ExpenseStatus.APPROVED)
                        binding.tvStatusData.setText("Добавлено гостем: Одобрено");
                    else
                        binding.tvStatusData.setText("Добавлено гостем: На рассмотрении");
                }
                if (!idGuestUser.equals("null") && expenseStatus != ExpenseStatus.UNDER_CONSIDERATION)
                    binding.btnRedaction.setVisibility(View.GONE);
                binding.constantLayout.setVisibility(View.VISIBLE);
            }else
                Toast.makeText(container.getContext(), ((Error)result).getMessage(), Toast.LENGTH_SHORT).show();
        });

        expenseDescriptionViewModel.resultOfDeleteExpenseInGuestUser.observe(getViewLifecycleOwner(), result -> {
            binding.progressBar.setVisibility(View.GONE);
            binding.darkOverlay.setVisibility(View.GONE);
            if (result instanceof Success){
                Toast.makeText(container.getContext(), "Трата была удалена", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(binding.getRoot())
                        .navigate(R.id.action_expenseDescriptionFragment_to_expensesFragment, idCarBundle);
            }else
                Toast.makeText(container.getContext(), ((Error)result).getMessage(), Toast.LENGTH_SHORT).show();
        });


        expenseDescriptionViewModel.resultOfDeleteExpense.observe(getViewLifecycleOwner(), result -> {
            binding.progressBar.setVisibility(View.GONE);
            binding.darkOverlay.setVisibility(View.GONE);
            if (result instanceof Success) {
                Toast.makeText(container.getContext(), "Трата была удалена", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(binding.getRoot())
                        .navigate(R.id.action_expenseDescriptionFragment_to_expensesFragment, idCarBundle);
            }else
                Toast.makeText(container.getContext(), ((Error)result).getMessage(), Toast.LENGTH_SHORT).show();
        });


        expenseDescriptionViewModel.resultOfChangeExpenseStatus.observe(getViewLifecycleOwner(), result -> {
            binding.darkOverlay.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.GONE);
            if (result instanceof Success){
                Toast.makeText(container.getContext(), "Трата была успешно одобрена", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(binding.getRoot())
                        .navigate(R.id.action_expenseDescriptionFragment_to_expensesFragment, idCarBundle);
            }else
                Toast.makeText(container.getContext(), ((Error)result).getMessage(), Toast.LENGTH_SHORT).show();

        });

        binding.iconBack.setOnClickListener(v -> Navigation.findNavController(binding.getRoot()).popBackStack());

        binding.iconDelete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(container.getContext());
            builder.setIcon(R.drawable.exclamation);

            builder.setNegativeButton("Отмена", (dialogInterface, i) -> dialogInterface.dismiss());

            if (!idGuestUser.equals("null")){
                builder.setTitle("Подтверждение");

                if (expenseStatus == ExpenseStatus.UNDER_CONSIDERATION){
                    builder.setMessage("Данная трата находится на рассмотрении, " +
                            "поэтому вы можете удалить ее только на этом аккаунте или на основном тоже.");

                    builder.setNeutralButton("Отмена", (dialogInterface, i) -> dialogInterface.dismiss());

                    builder.setNegativeButton("Удалить везде", (dialogInterface, i) -> {
                        expenseDescriptionViewModel.deleteExpenseInGuestUser(idGuestUser, indexExpense,
                                true, idCar);
                        dialogInterface.dismiss();
                        binding.darkOverlay.setVisibility(View.VISIBLE);
                        binding.progressBar.setVisibility(View.VISIBLE);
                    });

                    builder.setPositiveButton("Удалить у себя", (dialogInterface, i) -> {
                        expenseDescriptionViewModel.deleteExpenseInGuestUser(idGuestUser, indexExpense,
                                false, idCar);
                        dialogInterface.dismiss();
                        binding.darkOverlay.setVisibility(View.VISIBLE);
                        binding.progressBar.setVisibility(View.VISIBLE);
                    });

                }else{
                    if (expenseStatus == ExpenseStatus.APPROVED)
                        builder.setMessage("Данная трата была одобрена основным аккаунтом, " +
                                "вы можете удалить ее только у себя.");
                    else
                        builder.setMessage("Вы точно хотите удалить данную трату на этом аккаунте?");
                    builder.setPositiveButton("Удалить", (dialogInterface, i) -> {
                        expenseDescriptionViewModel.deleteExpenseInGuestUser(idGuestUser, indexExpense, true, idCar);
                        dialogInterface.dismiss();
                        binding.darkOverlay.setVisibility(View.VISIBLE);
                        binding.progressBar.setVisibility(View.VISIBLE);
                    });
                }
            }else{
                builder.setMessage("Вы точно хотите удалить данную трату?");

                builder.setPositiveButton("Да", (dialogInterface, i) -> {
                    expenseDescriptionViewModel.deleteExpense(idCar, indexExpense);
                    dialogInterface.dismiss();
                    binding.darkOverlay.setVisibility(View.VISIBLE);
                    binding.progressBar.setVisibility(View.VISIBLE);
                });
            }
            AlertDialog dialog = builder.create();
            dialog.show();

        });

        binding.btnRedaction.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(AddCarFragment.carIdKey, expenseBundle.getString(AddCarFragment.carIdKey));
            bundle.putString(expenseIdKey, expenseId);
            bundle.putInt(expenseIndexKey, indexExpense);
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_expenseDescriptionFragment_to_redactionExpenseFragment, bundle);

        });

        binding.btnApprove.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(container.getContext());
            builder.setIcon(R.drawable.exclamation);
            builder.setTitle("Подтверждение");
            builder.setMessage("Вы точно хотите одобрить данную трату?");
            builder.setPositiveButton("Да", (dialogInterface, i) -> {
                dialogInterface.dismiss();
                binding.darkOverlay.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.VISIBLE);
                expenseDescriptionViewModel.changeExpenseStatus(idCar, indexExpense, ExpenseStatus.APPROVED);
            });
            builder.setNegativeButton("Отмена", (dialogInterface, i) -> dialogInterface.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();

        });

        binding.btnReject.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(container.getContext());
            builder.setIcon(R.drawable.exclamation);
            builder.setTitle("Подтверждение");
            builder.setMessage("Вы точно хотите отклонить данную трату? Это приведет к тому, что она будет удалена");
            builder.setPositiveButton("Да", (dialogInterface, i) -> {
                dialogInterface.dismiss();
                binding.darkOverlay.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.VISIBLE);
                expenseDescriptionViewModel.changeExpenseStatus(idCar, indexExpense, ExpenseStatus.REJECTED);
            });
            builder.setNegativeButton("Отмена", (dialogInterface, i) -> dialogInterface.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        return binding.getRoot();
    }
}