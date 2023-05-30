package com.example.carassistant;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carassistant.databinding.ExpenseItemBinding;

import java.util.ArrayList;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {



    private final ArrayList<Expense> data;
    private ExpenseItemBinding binding;
    public ExpenseAdapter(ArrayList<Expense> data) {this.data = data;};

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         binding = ExpenseItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new ExpenseViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseAdapter.ExpenseViewHolder holder, int position) {
        Expense item  = data.get(position);
        switch (item.getCategory()){
            case "Топливо":
                holder.binding.imageView.setImageResource(R.drawable.gasoline_pump);
                break;
            case "Запчасти":
                holder.binding.imageView.setImageResource(R.drawable.spare_parts);
                break;
            case "Шины":
                holder.binding.imageView.setImageResource(R.drawable.tire);
                break;
            case "Диски":
                holder.binding.imageView.setImageResource(R.drawable.rim);
                break;
            case "Работа сервиса":
                holder.binding.imageView.setImageResource(R.drawable.mechanical);
                break;
            case "Автомойка":
                holder.binding.imageView.setImageResource(R.drawable.car_wash);
                break;
            case "Другое":
                holder.binding.imageView.setImageResource(R.drawable.question);
                break;
        }
        holder.binding.tvCategory.setText(item.getCategory());
        holder.binding.tvCost.setText(item.getExpense()+ " ₽");

        holder.binding.expenseItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Bundle carBundle = new Bundle();
                //carBundle.putInt(ExpensesFragment.key, position+1);
                //fragment.getActivity().getSharedPreferences("id", Context.MODE_PRIVATE).edit().putInt(ExpensesFragment.key, position+1).apply();
                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_expensesFragment_to_expenseDescriptionFragment);
            }
        });


    }

    @Override
    public int getItemCount() {return data.size();}

    public class ExpenseViewHolder extends RecyclerView.ViewHolder {
        ExpenseItemBinding binding;
        public ExpenseViewHolder(@NonNull ExpenseItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
