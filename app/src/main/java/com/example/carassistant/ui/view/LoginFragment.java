package com.example.carassistant.ui.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    private FragmentLoginBinding binding;

    private LoginViewModel loginViewModel;

    public static final String sharedPreferencesName = "myPreferences";

    public static final String sharedPreferencesKey = "guestUserId";
    private SharedPreferences sharedPreferences;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE);

        if (!sharedPreferences.getString(sharedPreferencesKey, "null").equals("null")){
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_loginFragment_to_panelFragment);
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        loginViewModel.isLogIn();

        loginViewModel.resultOfLogin.observe(getViewLifecycleOwner(), result -> {
            binding.progressBar.setVisibility(View.GONE);
            binding.darkOverlay.setVisibility(View.GONE);
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

        loginViewModel.resultOfCheckCode.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean)
                loginViewModel.loginWithCode(binding.etInputCode.getText().toString());
            else{
                binding.progressBar.setVisibility(View.GONE);
                binding.darkOverlay.setVisibility(View.GONE);
                Toast.makeText(container.getContext(), "Неверный код!", Toast.LENGTH_SHORT).show();
            }
        });

        loginViewModel.resultOfLoginWithCode.observe(getViewLifecycleOwner(), result -> {
            binding.progressBar.setVisibility(View.GONE);
            binding.darkOverlay.setVisibility(View.GONE);
            if (result instanceof Success){
                sharedPreferences = getActivity().getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(sharedPreferencesKey, ((Success<String>) result).getData());
                editor.apply();
                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_loginFragment_to_panelFragment);
            }else {
                Toast.makeText(container.getContext(), ((Error)result).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnGoToRegistration.setOnClickListener(v -> Navigation.findNavController(binding.getRoot())
                .navigate(R.id.action_loginFragment_to_registrationFragment));

        binding.btnLogIn.setOnClickListener(v -> {
            if(binding.etInputLogin.getText().toString().isEmpty() || binding.etInputPassword.getText().toString().isEmpty()){
                Toast.makeText(container.getContext(), "Вы ввели не все данные", Toast.LENGTH_SHORT).show();
            }else{
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.darkOverlay.setVisibility(View.VISIBLE);
                loginViewModel.login(
                        binding.etInputLogin.getText().toString(),
                        binding.etInputPassword.getText().toString()
                );
            }
        });

        binding.btnLogInWithCode.setOnClickListener(view -> {
            if (!binding.etInputCode.getText().toString().isEmpty()){
                loginViewModel.checkCode(binding.etInputCode.getText().toString());
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.darkOverlay.setVisibility(View.VISIBLE);
            }else{
                Toast.makeText(container.getContext(), "Вы не ввели код", Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();
    }
}