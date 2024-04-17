package com.example.carassistant.ui.view;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.carassistant.databinding.ChangePasswordDialogBinding;
import com.example.carassistant.databinding.FragmentAccountBinding;
import com.example.carassistant.ui.adapters.CarAdapter;
import com.example.carassistant.ui.viewModels.AccountViewModel;


import java.util.List;
import java.util.regex.Pattern;


public class AccountFragment extends Fragment {


    public static final String indexCar = "indexCar";


    private CarAdapter carAdapter;

    private FragmentAccountBinding binding;

    private AccountViewModel accountViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);

        ChangePasswordDialogBinding changePasswordDialogBinding = ChangePasswordDialogBinding.inflate(getLayoutInflater());

        binding.constantLayout.setVisibility(View.INVISIBLE);
        binding.progressBar.setVisibility(View.VISIBLE);

        Bundle bundle = new Bundle();


        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);


        SharedPreferences sharedPreferences = getContext()
                .getSharedPreferences(LoginFragment.sharedPreferencesName, Context.MODE_PRIVATE);

        String idGuestUser = sharedPreferences.getString(LoginFragment.sharedPreferencesKey, "null");

        if (!idGuestUser.equals("null")){
            binding.tvLogin.setText("Гостевой аккаунт");
            accountViewModel.loadCarGuest(idGuestUser);
            binding.btnAddCar.setVisibility(View.GONE);
            binding.recyclerviewListCars.setVisibility(View.GONE);
            binding.btnChangePassword.setVisibility(View.GONE);
            binding.btnDeleteAccount.setVisibility(View.GONE);
            binding.constantLayoutOneItem.setVisibility(View.VISIBLE);
        }else{
            accountViewModel.loadUserEmail();
            accountViewModel.loadCarList();
        }


        accountViewModel.resultOfLoadEmail.observe(getViewLifecycleOwner(), s -> binding.tvLogin.setText(s));

        accountViewModel.resultOfLoadCarGuest.observe(getViewLifecycleOwner(), result -> {
            binding.progressBar.setVisibility(View.GONE);
            binding.constantLayout.setVisibility(View.VISIBLE);
            if (result instanceof Success){
                CarDto carDto = ((Success<CarDto>) result).getData();
                bundle.putString(AddCarFragment.carIdKey, carDto.getId());
                binding.tvMark.setText(carDto.getMark());
                binding.tvModel.setText(carDto.getModel());
                binding.tvColor.setText(carDto.getColor());
            }else
                Toast.makeText(container.getContext(), ((Error)result).getMessage(), Toast.LENGTH_SHORT).show();
        });

        accountViewModel.resultOfChangeActiveCar.observe(getViewLifecycleOwner(), result -> {
            if (result instanceof Success){

                carAdapter.notifyItemChanged(carAdapter.getIndexActiveCar());

                carAdapter.setIndexActiveCar(((Success<Integer>) result).getData());

                carAdapter.notifyItemChanged(carAdapter.getIndexActiveCar());

                binding.progressBar.setVisibility(View.GONE);
                binding.darkOverlay.setVisibility(View.GONE);

            }else
                Toast.makeText(container.getContext(), ((Error)result).getMessage(), Toast.LENGTH_SHORT).show();
        });


        accountViewModel.resultOfLoadListCars.observe(getViewLifecycleOwner(), result -> {
            binding.progressBar.setVisibility(View.GONE);
            if (result instanceof Success){
                Pair<List<CarDto>, Integer> pair = ((Success<Pair<List<CarDto>, Integer>>) result).getData();
                carAdapter = new CarAdapter(pair.first, pair.second, binding, accountViewModel);
                binding.recyclerviewListCars.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.recyclerviewListCars.setAdapter(carAdapter);

                binding.constantLayout.setVisibility(View.VISIBLE);
            }else
                Toast.makeText(container.getContext(), ((Error)result).getMessage(), Toast.LENGTH_SHORT).show();
        });

        accountViewModel.resultOfCheckOldPassword.observe(getViewLifecycleOwner(), result -> {
            if (result instanceof Success)
                accountViewModel.changePassword(changePasswordDialogBinding.etInputNewPassword.getText().toString());
            else {
                binding.progressBar.setVisibility(View.GONE);
                binding.darkOverlay.setVisibility(View.GONE);
                Toast.makeText(container.getContext(), ((Error) result).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        accountViewModel.resultOfChangePassword.observe(getViewLifecycleOwner(), result -> {
            binding.progressBar.setVisibility(View.GONE);
            binding.darkOverlay.setVisibility(View.GONE);
            if (result instanceof Success)
                Toast.makeText(container.getContext(), "Вы успешно сменили пароль!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(container.getContext(), ((Error)result).getMessage(), Toast.LENGTH_SHORT).show();
        });

        accountViewModel.resultOfDeleteAccount.observe(getViewLifecycleOwner(), result -> {
            binding.progressBar.setVisibility(View.GONE);
            binding.darkOverlay.setVisibility(View.GONE);
            if (result instanceof Success)
                ((MainActivity)getActivity()).navRestart();
            else
                Toast.makeText(container.getContext(), ((Error)result).getMessage(), Toast.LENGTH_SHORT).show();
        });


        binding.btnAddCar.setOnClickListener(view -> Navigation.findNavController(binding.getRoot())
                .navigate(R.id.action_accountFragment_to_addCarFragment));


        binding.iconInformation.setOnClickListener(view -> Navigation.findNavController(binding.getRoot())
                .navigate(R.id.action_accountFragment_to_carDescriptionFragment, bundle));


        binding.btnLogOut.setOnClickListener(view -> {
            AlertDialog.Builder builder = getBuilder(container, idGuestUser);
            builder.setPositiveButton("Да", (dialogInterface, i) -> {
                if (!idGuestUser.equals("null")) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove(LoginFragment.sharedPreferencesKey);
                    editor.apply();
                }
                else
                    accountViewModel.logOut();
                dialogInterface.dismiss();
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.darkOverlay.setVisibility(View.VISIBLE);
                ((MainActivity)getActivity()).navRestart();
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        binding.btnDeleteAccount.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(container.getContext());
            builder.setMessage("Вы точно хотите удалить аккаунт? Это приведет к потере всех данных");
            builder.setTitle("Подтверждение");
            builder.setIcon(R.drawable.exclamation);
            builder.setNegativeButton("Отмена", (dialogInterface, i) -> dialogInterface.dismiss());
            builder.setPositiveButton("Да", (dialogInterface, i) -> {
                accountViewModel.deleteAccount();
                dialogInterface.dismiss();
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.darkOverlay.setVisibility(View.VISIBLE);
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        });

        binding.btnChangePassword.setOnClickListener(view -> {
            Dialog dialog = new Dialog(container.getContext());
            dialog.setContentView(changePasswordDialogBinding.getRoot());
            changePasswordDialogBinding.btnChangePassword.setOnClickListener(view1 -> {
                if (validateAuthData(changePasswordDialogBinding, container)){
                    accountViewModel.checkOldPassword(changePasswordDialogBinding.etInputOldPassword.getText().toString());
                    dialog.dismiss();
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.darkOverlay.setVisibility(View.VISIBLE);
                }
            });
            dialog.show();
        });

        return binding.getRoot();
    }

    @NonNull
    private static AlertDialog.Builder getBuilder(ViewGroup container, String idGuestUser) {
        AlertDialog.Builder builder = new AlertDialog.Builder(container.getContext());
        if (!idGuestUser.equals("null"))
            builder.setMessage("Вы точно хотите выйти из аккаунта? " +
                    "Так как ваш аккаунт гостевой, вся информация не сохранится.");
        else
            builder.setMessage("Вы точно хотите выйти из аккаунта?");
        builder.setTitle("Подтверждение");
        builder.setIcon(R.drawable.exclamation);
        builder.setNegativeButton("Отмена", (dialogInterface, i) -> dialogInterface.dismiss());
        return builder;
    }

    private Boolean validateAuthData(ChangePasswordDialogBinding binding, ViewGroup container) {
        Pattern patternPassword = Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$");
        if(binding.etInputOldPassword.getText().toString().isEmpty() || binding.etInputNewPassword.getText().toString().isEmpty() ||
                binding.etInputNewPasswordAgain.getText().toString().isEmpty()){
            Toast.makeText(container.getContext(), "Вы ввели не все данные", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!binding.etInputNewPasswordAgain.getText().toString().equals(binding.etInputNewPassword.getText().toString())) {
            Toast.makeText(container.getContext(), "Пароли не совпадают", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!patternPassword.matcher(binding.etInputNewPassword.getText().toString()).matches()){
            Toast.makeText(container.getContext(), "Ваш пароль ненадежный", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    @Override
    public void onPause() {
        super.onPause();
        accountViewModel.clearResultOfChangeActiveCar();
    }
}