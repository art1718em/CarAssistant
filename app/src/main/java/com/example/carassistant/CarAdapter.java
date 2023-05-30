package com.example.carassistant;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;


import com.example.carassistant.databinding.CarItemBinding;
import com.example.carassistant.databinding.FragmentCarsBinding;

import java.util.ArrayList;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {



    private final ArrayList<Car> data;
    private FragmentCarsBinding binding;
    private Fragment fragment;
    public CarAdapter(ArrayList<Car> data, FragmentCarsBinding binding, Fragment fragment) {this.data = data;
    this.binding=binding; this.fragment = fragment;};

    @NonNull
    @Override
    public CarAdapter.CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CarItemBinding binding = CarItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new CarAdapter.CarViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CarAdapter.CarViewHolder holder, int position) {
        Car item  = data.get(position);
        holder.binding.tvMark.setText(item.getMark());
        holder.binding.tvModel.setText(item.getModel());
        holder.binding.tvColor.setText(item.getColor());

        holder.binding.clCarItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle carBundle = new Bundle();
                carBundle.putInt(ExpensesFragment.key, position+1);
                fragment.getActivity().getSharedPreferences("id", Context.MODE_PRIVATE).edit().putInt(ExpensesFragment.key, position+1).apply();
                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_carsFragment_to_panelFragment,
                        carBundle);
            }
        });


    }

    @Override
    public int getItemCount() {return data.size();}

    public class CarViewHolder extends RecyclerView.ViewHolder {
        CarItemBinding binding;
        public CarViewHolder(@NonNull CarItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
