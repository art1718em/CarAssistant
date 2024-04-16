package com.example.carassistant.ui.view;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.carassistant.R;
import com.example.carassistant.core.Error;

import com.example.carassistant.core.Success;
import com.example.carassistant.databinding.FragmentRegistationBinding;
import com.example.carassistant.ui.viewModels.RegistrationViewModel;

import java.util.regex.Pattern;

public class RegistrationFragment extends Fragment {

    private FragmentRegistationBinding binding;


    private RegistrationViewModel registrationViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegistationBinding.inflate(inflater, container, false);
        registrationViewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);

        registrationViewModel.isLogIn();

        registrationViewModel.resultLogIn.observe(getViewLifecycleOwner(), resultLogIn -> {
            if (resultLogIn)
                Navigation.findNavController(binding.getRoot())
                        .navigate(R.id.action_registrationFragment_to_panelFragment);
        });

        registrationViewModel.resultOfRegistration.observe(getViewLifecycleOwner(), result -> {
            if (result instanceof Success){
                registrationViewModel.addUserToFirestore();
            }else {
                binding.progressBar.setVisibility(View.GONE);
                binding.darkOverlay.setVisibility(View.GONE);
                Toast.makeText(container.getContext(), ((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        registrationViewModel.resultAddUser.observe(getViewLifecycleOwner(), result -> {
            binding.progressBar.setVisibility(View.GONE);
            binding.darkOverlay.setVisibility(View.GONE);
            if (result instanceof Success){
                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_registrationFragment_to_panelFragment);
            }else
                Toast.makeText(container.getContext(), ((Error)result).getMessage(), Toast.LENGTH_SHORT).show();
        });


        binding.btnGoBackToLogin.setOnClickListener(v -> Navigation.findNavController(binding.getRoot())
                .navigate(R.id.action_registrationFragment_to_loginFragment));


        binding.btnRegistration.setOnClickListener(v -> {

            if (validateAuthData(container)){

                binding.progressBar.setVisibility(View.VISIBLE);
                binding.darkOverlay.setVisibility(View.VISIBLE);

                registrationViewModel.registration(
                        binding.etInputLoginRegistration.getText().toString(),
                        binding.etInputPasswordRegistration.getText().toString()
                );
            }
        });




        return binding.getRoot();
    }

    private Boolean validateAuthData(ViewGroup container) {
        Pattern patternPassword = Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$");
        Pattern patternEmail = Pattern.compile("^\\S+@\\S+\\.\\S+$");
        if(binding.etInputLoginRegistration.getText().toString().isEmpty() || binding.etInputPasswordRegistration.getText().toString().isEmpty() ||
            binding.etInputPasswordAgainRegistration.getText().toString().isEmpty()){
            Toast.makeText(container.getContext(), "Вы ввели не все данные", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!binding.etInputPasswordRegistration.getText().toString().equals(binding.etInputPasswordAgainRegistration.getText().toString())) {
            Toast.makeText(container.getContext(), "Пароли не совпадают", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!patternPassword.matcher(binding.etInputPasswordRegistration.getText().toString()).matches()){
            Toast.makeText(container.getContext(), "Ваш пароль ненадежный", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!patternEmail.matcher(binding.etInputLoginRegistration.getText().toString()).matches()){
            Toast.makeText(container.getContext(), "Вы ввели некорректный логин", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


}