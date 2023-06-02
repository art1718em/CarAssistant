package com.example.carassistant;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.carassistant.databinding.FragmentDiagramBinding;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DiagramFragment extends Fragment {

    private FragmentDiagramBinding binding;
    Disposable expenseListDisposable;
    private PieChart pieChart;
    private Bundle bundle;

    public static final String key = "carIdKey";


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getParentFragmentManager().setFragmentResultListener(key, getViewLifecycleOwner(), (requestKey, carBundle) -> {
            bundle = carBundle;
            Log.d("carasswork", carBundle.toString());
            ExpenseDB expenseDB = ExpenseDB.getInstance(requireContext());
            ExpenseDao expenseDao = expenseDB.expenseDao();
            if (bundle != null) {
                expenseListDisposable = expenseDao
                        .getALLThisId(bundle.getInt(key))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onExpensesLoaded, throwable -> {
                            Log.wtf("error", throwable.toString());
                        });
            }

        });

        Log.d("carasswork", String.valueOf(getActivity().getSharedPreferences("id", Context.MODE_PRIVATE).getInt(key, -1)));
        ExpenseDB expenseDB = ExpenseDB.getInstance(requireContext());
        ExpenseDao expenseDao = expenseDB.expenseDao();
            expenseListDisposable = expenseDao
                    .getALLThisId(getActivity().getSharedPreferences("id", Context.MODE_PRIVATE).getInt(key, -1))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onExpensesLoaded, throwable -> {
                        Log.wtf("error", throwable.toString());
                    });




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDiagramBinding.inflate(inflater, container, false);

        pieChart = binding.pieChart;
        binding.iconExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_diagramFragment_to_expensesFragment,
                        bundle);
            }
        });

        binding.actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_diagramFragment_to_addExpenseFragment,
                        bundle);
            }
        });


        return binding.getRoot();
    }


    private void onExpensesLoaded(List<Expense> expenses) {
        HashMap<String, Integer> map = new HashMap<>();
        int sum = 0;
        for (Expense i: expenses
             ) {
            if (map.containsKey(i.getCategory()))
                map.put(i.getCategory(), map.get(i.getCategory()) + Integer.parseInt(i.getExpense()));
            else
                map.put(i.getCategory(), Integer.parseInt(i.getExpense()));
        }

        ArrayList<PieEntry> entries = new ArrayList<>();

        for (String str: map.keySet()
             ) {
            sum += map.get(str);
            entries.add(new PieEntry(map.get(str), str));
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
        pieChart.setCenterText(String.valueOf(sum) + " ₽");
        pieChart.animate();
        pieChart.invalidate();

        DiagramAdapter diagramAdapter =new DiagramAdapter((ArrayList<Expense>) expenses);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerview.setAdapter(diagramAdapter);

        binding.pieChart.setVisibility(View.VISIBLE);
        binding.recyclerview.setVisibility(View.VISIBLE);
        binding.progressBar.setVisibility(View.GONE);
    }
}