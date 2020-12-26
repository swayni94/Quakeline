package com.example.quakeline.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.quakeline.R;
import com.example.quakeline.ViewPage.MainViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class MainFragment extends Fragment {

    private boolean term = false;
    private final String textview_string = "Location is failed. => \n \n Please wait!";
    TextView textView;
    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container ,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main , container , false);
    }

    @Override
    public void onViewCreated(@NonNull View view , @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view , savedInstanceState);

        MainViewModel mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        textView = view.findViewById(R.id.main_fragment_textview);
        listenerLocation(mainViewModel);
    }

    private void listenerLocation(MainViewModel mainViewModel){
        Log.e("MainFragment", "listener listen running");
        CountDownTimer timer = new CountDownTimer(7000 , 1000) {
            @Override
            public void onTick(long l) {

            }
            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {
                mainViewModel.getLocation().observe(requireActivity(), location -> {
                    if (location !=null){
                        term =true;
                    }
                });
                if (term) {
                    NavController navController = Navigation.findNavController(requireActivity() , R.id.main_fragment);
                    navController.navigate(R.id.quake_list_fragment);
                }
                else {
                    textView.setText(textview_string);
                    listenerLocation(mainViewModel);
                }
            }
        };
        timer.start();
    }
}