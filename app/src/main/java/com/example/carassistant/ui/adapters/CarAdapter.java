package com.example.carassistant.ui.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;



import com.example.carassistant.R;
import com.example.carassistant.data.models.CarDto;
import com.example.carassistant.data.models.User;
import com.example.carassistant.databinding.CarItemBinding;
import com.example.carassistant.databinding.FragmentCarsBinding;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.FirebaseFirestore;


import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {


    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();



    private final List<CarDto> data;
    private final FragmentCarsBinding binding;
    private final Fragment fragment;
    public CarAdapter(List<CarDto> data, FragmentCarsBinding binding, Fragment fragment) {
        this.data = data;
        this.binding=binding;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public CarAdapter.CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CarItemBinding binding = CarItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new CarViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CarAdapter.CarViewHolder holder, int position) {
        CarDto item  = data.get(position);
        holder.binding.tvMark.setText(item.getMark());
        holder.binding.tvModel.setText(item.getModel());
        holder.binding.tvColor.setText(item.getColor());

        holder.binding.constraintLayoutCarItem.setOnClickListener(v -> {
            Bundle carBundle = new Bundle();
            db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    User user = task.getResult().toObject(User.class);
                    carBundle.putString("carId", user.getListCars().get(position).getId());
                    fragment.requireActivity().getSharedPreferences("id", Context.MODE_PRIVATE).edit()
                            .putString("carId", user.getListCars().get(position).getId()).apply();
                    Navigation.findNavController(binding.getRoot()).navigate(R.id.action_carsFragment_to_panelFragment,
                            carBundle);
                }
            });

        });


    }

    @Override
    public int getItemCount() {return data.size();}

    public static class CarViewHolder extends RecyclerView.ViewHolder {
        CarItemBinding binding;
        public CarViewHolder(@NonNull CarItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
