package com.example.carassistant.ui.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.carassistant.data.models.Car;
import com.example.carassistant.databinding.FragmentCarDescriptionBinding;
import com.example.carassistant.databinding.GuestCodeDialogBinding;
import com.example.carassistant.ui.viewModels.CarDescriptionViewModel;

public class CarDescriptionFragment extends Fragment {

    private FragmentCarDescriptionBinding binding;

    private CarDescriptionViewModel carDescriptionViewModel;

    private Bundle bundle;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCarDescriptionBinding.inflate(inflater, container, false);


        binding.constantLayout.setVisibility(View.INVISIBLE);
        binding.progressBar.setVisibility(View.VISIBLE);

        carDescriptionViewModel = new ViewModelProvider(this).get(CarDescriptionViewModel.class);

        SharedPreferences sharedPreferences = getContext()
                .getSharedPreferences(LoginFragment.sharedPreferencesName, Context.MODE_PRIVATE);

        String idGuestUser = sharedPreferences.getString(LoginFragment.sharedPreferencesKey, "null");

        bundle = requireArguments();

        if (!idGuestUser.equals("null")){
            binding.btnGenCode.setVisibility(View.GONE);
            binding.tvCodeDescription.setVisibility(View.GONE);
            binding.btnRedaction.setVisibility(View.GONE);
            binding.iconDelete.setVisibility(View.GONE);
            carDescriptionViewModel.getIdCarGuest(idGuestUser);
        }else{
            carDescriptionViewModel.loadCarDescription(bundle.getString(AddCarFragment.carIdKey));
        }

        carDescriptionViewModel.resultOfGetIdCarGuest.observe(getViewLifecycleOwner(), result -> {
            if (result instanceof Success)
                carDescriptionViewModel.loadCarDescription(((Success<String>) result).getData());
            else
                Toast.makeText(container.getContext(), ((Error)result).getMessage(), Toast.LENGTH_SHORT).show();
        });


        carDescriptionViewModel.resultOfLoadCarDescription.observe(getViewLifecycleOwner(), result -> {
            binding.progressBar.setVisibility(View.GONE);
            if (result instanceof Success){
                Car car = ((Success<Car>) result).getData();
                binding.tvColorData.setText(car.getColor());
                binding.tvCostData.setText(String.valueOf(car.getCost()));
                binding.tvMileageData.setText(String.valueOf(car.getMileage()));
                binding.tvMarkData.setText(car.getMark());
                binding.tvModelData.setText(car.getModel());
                binding.tvYearData.setText(String.valueOf(car.getYear()));
                binding.constantLayout.setVisibility(View.VISIBLE);
            } else
                Toast.makeText(container.getContext(), ((Error)result).getMessage(), Toast.LENGTH_SHORT).show();
        });

        carDescriptionViewModel.resultOfDeleteCar.observe(getViewLifecycleOwner(), result -> {
            binding.darkOverlay.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.GONE);
            if (result instanceof Success)
                Navigation.findNavController(binding.getRoot()).popBackStack();
            else
                Toast.makeText(container.getContext(), ((Error)result).getMessage(), Toast.LENGTH_SHORT).show();
        });


        binding.iconDelete.setOnClickListener(view -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(container.getContext());
            builder.setTitle("Подтверждение");
            builder.setMessage("Вы точно хотите удалить данный автомобиль?");
            builder.setIcon(R.drawable.exclamation);

            builder.setPositiveButton("Да", (dialogInterface, i) -> {
                carDescriptionViewModel
                        .getCar(bundle.getString(AddCarFragment.carIdKey), bundle.getInt(AccountFragment.indexCar));
                dialogInterface.dismiss();
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.darkOverlay.setVisibility(View.VISIBLE);
            });

            builder.setNegativeButton("Отмена", (dialogInterface, i) -> dialogInterface.dismiss());

            AlertDialog dialog = builder.create();
            dialog.show();
        });

        binding.btnGenCode.setOnClickListener(view -> {

            Dialog codeDialog = new Dialog(container.getContext());
            GuestCodeDialogBinding guestCodeDialogBinding = GuestCodeDialogBinding.inflate(getLayoutInflater());
            codeDialog.setContentView(guestCodeDialogBinding.getRoot());
            guestCodeDialogBinding.tvCode.setText(bundle.getString(AddCarFragment.carIdKey));
            guestCodeDialogBinding.iconCopy.setOnClickListener(view1 -> {
                ClipboardManager clipboard = (ClipboardManager) container.getContext()
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("text", guestCodeDialogBinding.tvCode.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(container.getContext(),
                        "Код успешно скопирован в буфер обмена!", Toast.LENGTH_SHORT).show();
                codeDialog.dismiss();
            });
            codeDialog.show();
        });



        binding.iconBack.setOnClickListener(v -> Navigation.findNavController(binding.getRoot())
                .navigate(R.id.action_carDescriptionFragment_to_accountFragment));

        binding.btnRedaction.setOnClickListener(view -> Navigation.findNavController(binding.getRoot())
                .navigate(R.id.action_carDescriptionFragment_to_redactionCarFragment, bundle));

        return binding.getRoot();
    }
}