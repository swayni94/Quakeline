package com.example.quakeline.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quakeline.Adapters.QuakelineRecyclerViewAdapter;
import com.example.quakeline.R;
import com.example.quakeline.ViewPage.MainViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class QuakeListFragment extends Fragment {

    MainViewModel quakeListViewModel;
    private RecyclerView recyclerView;
    QuakelineRecyclerViewAdapter recyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container ,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_quake_list , container , false);
    }

    @Override
    public void onViewCreated(@NonNull View view , @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view , savedInstanceState);
        if (savedInstanceState != null) {
            savedInstanceState.getDouble("loccation");
        }
        recyclerView = view.findViewById(R.id.quakeRecyclerview);
        quakeListViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        quakeListViewModel.getNewQuakes().observe(requireActivity() , results -> quakeListViewModel.getLocation().observe(requireActivity() , location -> {
            Log.e("QuakeFragment" , "Location is running");
            recyclerViewAdapter = new QuakelineRecyclerViewAdapter(results , getActivity() , location.getLatitude() , location.getLongitude());
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(recyclerViewAdapter);
        }));
    }
}