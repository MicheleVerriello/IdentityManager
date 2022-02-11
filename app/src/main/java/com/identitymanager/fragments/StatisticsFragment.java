package com.identitymanager.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.identitymanager.R;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.charts.Pie;

import com.identitymanager.activities.InstagramWeakPasswordsActivity;
import com.identitymanager.activities.InstagramToChangePasswordsActivity;
import com.identitymanager.activities.FacebookWeakPasswordsActivity;
import com.identitymanager.activities.FacebookToChangePasswordsActivity;
import com.identitymanager.activities.YoutubeWeakPasswordsActivity;
import com.identitymanager.activities.YoutubeToChangePasswordsActivity;

import java.util.ArrayList;
import java.util.List;

public class StatisticsFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View settView = inflater.inflate(R.layout.fragment_statistics, container, false);

        setupPieChart(settView);
        setupWeakList(settView);
        setupToChangeList(settView);

        return settView;
    }

    private void setupPieChart(View settView) {
        AnyChartView anyChartView;
        anyChartView = settView.findViewById(R.id.any_chart_view);

        String[] levels = {"Strong", "Medium", "Weak"};
        int[] totals = {30, 25, 10};

        Pie pie = AnyChart.pie();
        List<DataEntry> dataEntries = new ArrayList<>();

        for (int i = 0; i < levels.length; i++){
            dataEntries.add(new ValueDataEntry(levels[i], totals[i]));
        }

        pie.data(dataEntries);
        anyChartView.setChart(pie);
    }

    private void setupWeakList(View settView) {
        ListView listView;
        listView = settView.findViewById(R.id.category_weak_passwords);

        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add("Instagram");
        arrayList.add("Facebook");
        arrayList.add("Youtube");

        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if(position == 0) {
                    startActivity(new Intent(getActivity(), InstagramWeakPasswordsActivity.class));
                } else if (position == 1) {
                    startActivity(new Intent(getActivity(), FacebookWeakPasswordsActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), YoutubeWeakPasswordsActivity.class));
                }
            }
        });
    }

    private void setupToChangeList(View settView) {
        ListView listView;
        listView = settView.findViewById(R.id.category_to_change_passwords);

        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add("Instagram");
        arrayList.add("Facebook");
        arrayList.add("Youtube");

        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if(position == 0) {
                    startActivity(new Intent(getActivity(), InstagramToChangePasswordsActivity.class));
                } else if (position == 1) {
                    startActivity(new Intent(getActivity(), FacebookToChangePasswordsActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), YoutubeToChangePasswordsActivity.class));
                }
            }
        });
    }
}