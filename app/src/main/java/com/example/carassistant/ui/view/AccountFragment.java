package com.example.carassistant.ui.view;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.carassistant.R;
import com.example.carassistant.core.Error;
import com.example.carassistant.core.Success;
import com.example.carassistant.data.models.CarDto;
import com.example.carassistant.databinding.FragmentAccountBinding;
import com.example.carassistant.ui.adapters.CarAccountAdapter;
import com.example.carassistant.ui.viewModels.AccountViewModel;

import java.util.List;


public class AccountFragment extends Fragment {



    private FragmentAccountBinding binding;

    private AccountViewModel accountViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);

        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);

        accountViewModel.loadUserEmail();

        accountViewModel.resultOfLoadEmail.observe(getViewLifecycleOwner(), s -> {
            binding.tvLogin.setText(s);
        });

        accountViewModel.loadCarList();

        accountViewModel.resultOfLoadListCars.observe(getViewLifecycleOwner(), result -> {
            if (result instanceof Success){
                Pair<List<CarDto>, Integer> pair = ((Success<Pair<List<CarDto>, Integer>>) result).getData();
                CarAccountAdapter carAdapter = new CarAccountAdapter(pair.first, pair.second, binding, accountViewModel, this);
                binding.recyclerviewListCars.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.recyclerviewListCars.setAdapter(carAdapter);
            }else
                Toast.makeText(container.getContext(), ((Error)result).getMessage(), Toast.LENGTH_SHORT).show();
        });


        binding.btnAddCar.setOnClickListener(view -> Navigation.findNavController(binding.getRoot())
                .navigate(R.id.action_accountFragment_to_addCarFragment));





//        binding.btnCopyCode.setOnClickListener(view -> {
//            ClipboardManager clipboard = (ClipboardManager) container.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
//            ClipData clip = ClipData.newPlainText("text", binding.tvDisposableLogin.getText().toString());
//            clipboard.setPrimaryClip(clip);
//        });


        binding.btnLogOut.setOnClickListener(view -> accountViewModel.logOut());






        return binding.getRoot();
    }
}