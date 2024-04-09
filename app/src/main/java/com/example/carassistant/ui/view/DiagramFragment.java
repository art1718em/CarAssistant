package com.example.carassistant.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;



import com.example.carassistant.R;
import com.example.carassistant.core.Error;

import com.example.carassistant.core.Success;

import com.example.carassistant.data.models.ExpenseDto;
import com.example.carassistant.databinding.FragmentDiagramBinding;

import com.example.carassistant.ui.adapters.DiagramAdapter;
import com.example.carassistant.ui.viewModels.DiagramViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class DiagramFragment extends Fragment {

    private FragmentDiagramBinding binding;

    private PieChart pieChart;
    private Bundle bundle;

    public static final String key = "carId";



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDiagramBinding.inflate(inflater, container, false);

        DiagramViewModel diagramViewModel = new ViewModelProvider(this).get(DiagramViewModel.class);

        getParentFragmentManager().setFragmentResultListener(
                key, getViewLifecycleOwner(), (requestKey, carBundle) -> bundle = carBundle);

        String s = requireActivity().getSharedPreferences("id", Context.MODE_PRIVATE).getString(key, "");

        pieChart = binding.pieChart;

        diagramViewModel.getListExpenses(s);

        diagramViewModel.resultOfExpenses.observe(getViewLifecycleOwner(), result -> {
            if (result instanceof Success){
                onDiagramLoaded(((Success<List<ExpenseDto>>) result).getData());
            }else
                Toast.makeText(container.getContext(), ((Error)result).getMessage(), Toast.LENGTH_SHORT).show();
        });




        binding.iconAddExpense.setOnClickListener(v -> Navigation.findNavController(binding.getRoot())
                .navigate(R.id.action_diagramFragment_to_addExpenseFragment, bundle));

        binding.iconGoToListExpenses.setOnClickListener(v -> Navigation.findNavController(binding.getRoot())
                .navigate(R.id.action_diagramFragment_to_expensesFragment, bundle));


        return binding.getRoot();
    }


    private void onDiagramLoaded(List<ExpenseDto> expenses) {
        HashMap<String, Double> map = new HashMap<>();
        int sum = 0;
        for (ExpenseDto i: expenses
             ) {
            if (map.containsKey(i.getCategory()))
                map.put(i.getCategory(), map.get(i.getCategory()) + i.getExpense());
            else
                map.put(i.getCategory(), i.getExpense());
        }

        ArrayList<PieEntry> entries = new ArrayList<>();

        for (String str: map.keySet()) {
            sum += map.get(str);
            entries.add(new PieEntry(map.get(str).floatValue(), str));
        }


        PieDataSet pieDataSet = new PieDataSet(entries, "");
        int[] colors = {Color.rgb(255,154,0), Color.rgb(255,76,0)
                , Color.rgb(255,219,0), Color.rgb(220,249,0)
                , Color.rgb(0,184,74) , Color.rgb(3,138,157)
                , Color.rgb(40,24,178), Color.rgb(119,8,171)};
        pieDataSet.setColors(ColorTemplate.createColors(colors));
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(0f);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.setCenterTextSize(24f);
        pieChart.setEntryLabelTextSize(24f);
        pieChart.setCenterText(sum + " ₽");
        pieChart.animate();
        pieChart.invalidate();

        DiagramAdapter diagramAdapter =new DiagramAdapter((ArrayList<ExpenseDto>) expenses);
        binding.recyclerviewListCategoryExpenses.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerviewListCategoryExpenses.setAdapter(diagramAdapter);

        binding.pieChart.setVisibility(View.VISIBLE);
        binding.recyclerviewListCategoryExpenses.setVisibility(View.VISIBLE);
        binding.iconAddExpense.setVisibility(View.VISIBLE);
        binding.iconGoToListExpenses.setVisibility(View.VISIBLE);
        binding.progressBar.setVisibility(View.GONE);
        binding.iconGoToListExpenses.setClickable(true);
    }
}