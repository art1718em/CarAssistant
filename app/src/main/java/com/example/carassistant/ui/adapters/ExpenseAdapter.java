package com.example.carassistant.ui.adapters;


import android.annotation.SuppressLint;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carassistant.R;
import com.example.carassistant.data.models.ExpenseDto;
import com.example.carassistant.databinding.ExpenseItemBinding;
import com.example.carassistant.ui.view.AddCarFragment;
import com.example.carassistant.ui.view.ExpenseDescriptionFragment;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private final List<ExpenseDto> data;
    private ExpenseItemBinding binding;

    private final String idCar;

    public ExpenseAdapter(List<ExpenseDto> data, String idCar) {
        this.data = data;
        this.idCar = idCar;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         binding = ExpenseItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new ExpenseViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ExpenseAdapter.ExpenseViewHolder holder, int position) {

        ExpenseDto item  = data.get(position);
        switch (item.getCategory()){
            case "Топливо":
                holder.binding.iconCategory.setImageResource(R.drawable.gasoline_pump);
                break;
            case "Запчасти":
                holder.binding.iconCategory.setImageResource(R.drawable.spare_parts);
                break;
            case "Шины":
                holder.binding.iconCategory.setImageResource(R.drawable.tire);
                break;
            case "Диски":
                holder.binding.iconCategory.setImageResource(R.drawable.rim);
                break;
            case "Работа сервиса":
                holder.binding.iconCategory.setImageResource(R.drawable.mechanical);
                break;
            case "Автомойка":
                holder.binding.iconCategory.setImageResource(R.drawable.car_wash);
                break;
            case "Другое":
                holder.binding.iconCategory.setImageResource(R.drawable.question);
                break;
        }
        holder.binding.tvCategory.setText(item.getCategory());
        holder.binding.tvCost.setText(item.getExpense()+ " ₽");

        holder.binding.constantLayoutExpenseItem.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt(ExpenseDescriptionFragment.expenseIndexKey, position);
            bundle.putString(AddCarFragment.carIdKey, idCar);
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_expensesFragment_to_expenseDescriptionFragment, bundle);
        });

    }

    @Override
    public int getItemCount() {return data.size();}

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        ExpenseItemBinding binding;
        public ExpenseViewHolder(@NonNull ExpenseItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
