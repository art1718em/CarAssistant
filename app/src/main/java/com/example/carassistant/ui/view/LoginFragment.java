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
import com.example.carassistant.databinding.FragmentLoginBinding;
import com.example.carassistant.ui.viewModels.LoginViewModel;



public class LoginFragment extends Fragment {
    FragmentLoginBinding binding;

    LoginViewModel loginViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        loginViewModel.isLogIn();

        loginViewModel.resultOfLogin.observe(getViewLifecycleOwner(), result -> {
            binding.progressBar.setVisibility(View.INVISIBLE);
            if (result instanceof Success){
                Navigation.findNavController(binding.getRoot())
                        .navigate(R.id.action_loginFragment_to_panelFragment);
            }else
                Toast.makeText(container.getContext(), ((Error)result).getMessage(), Toast.LENGTH_SHORT).show();
        });

        loginViewModel.resultLogIn.observe(getViewLifecycleOwner(), isLogin -> {
            if(isLogin)
                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_loginFragment_to_panelFragment);
        });


        binding.btnGoToRegistration.setOnClickListener(v -> Navigation.findNavController(binding.getRoot())
                .navigate(R.id.action_loginFragment_to_registrationFragment));


        binding.btnLogIn.setOnClickListener(v -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            if(binding.etInputLogin.getText().toString().isEmpty() || binding.etInputPassword.getText().toString().isEmpty()){
                Toast.makeText(container.getContext(), "Вы ввели не все данные", Toast.LENGTH_SHORT).show();
                binding.progressBar.setVisibility(View.INVISIBLE);
            }else{
                loginViewModel.login(
                        binding.etInputLogin.getText().toString(),
                        binding.etInputPassword.getText().toString()
                );
            }
        });

        return binding.getRoot();
    }
}