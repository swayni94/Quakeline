package com.example.quakeline.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.quakeline.Helpers.CurrentLocationHelper;
import com.example.quakeline.R;
import com.example.quakeline.RestApi.Model.Result;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class QuakelineRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity context;
    private List<Result> datas;
    private CurrentLocationHelper helper;
    private double userlat, userlon;
    private int midKM=50, highKM=25;

    public QuakelineRecyclerViewAdapter(List<Result> _datas, Activity _context, double _userlat, double _userlon){
        context = _context;
        datas = _datas;
        userlat = _userlat;
        userlon = _userlon;
        helper = new CurrentLocationHelper();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.quake_item, parent,false);
        return new QuakelineRecyclerViewViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Result quake = datas.get(position);
        QuakelineRecyclerViewViewHolder viewHolder=(QuakelineRecyclerViewViewHolder) holder;

        double term = helper.currentLocationKm(userlat, userlon, quake.getLat(), quake.getLng());

        /*if (helper.currentLocation(userlat, userlon, quake.getLat(), quake.getLng(), 25)){
            viewHolder.background.setBackgroundTintList(ColorStateList.valueOf(R.color.backgroundColor_highPriority));
        }
        else if (helper.currentLocation(userlat, userlon, quake.getLat(), quake.getLng(), 50)){
            viewHolder.background.setBackgroundTintList(ColorStateList.valueOf(R.color.backgroundColor_middlePriority));
        }
        else {
            viewHolder.background.setBackgroundTintList(ColorStateList.valueOf(R.color.backgroundColor_lowPriority));
        }
        */
         if (term<=highKM){
             viewHolder.background.setBackgroundTintList(ColorStateList.valueOf(R.color.backgroundColor_highPriority));
         }else if (term<=midKM){
             viewHolder.background.setBackgroundTintList(ColorStateList.valueOf(R.color.backgroundColor_middlePriority));
         }else {
             viewHolder.background.setBackgroundTintList(ColorStateList.valueOf(R.color.backgroundColor_lowPriority));
         }
        viewHolder.title.setText(quake.getTitle());
        viewHolder.currentLocation.setText(" "+term);
        viewHolder.mag.setText(quake.getMag().toString());
        viewHolder.date.setText(quake.getDate());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class QuakelineRecyclerViewViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView mag;
        TextView date;
        TextView currentLocation;
        LinearLayout background;
        private QuakelineRecyclerViewViewHolder(@NonNull View itemView) {
            super(itemView);
            title= itemView.findViewById(R.id.quakeItem_Title);
            mag = itemView.findViewById(R.id.quakeItem_Mag);
            date = itemView.findViewById(R.id.quakeItem_Date);
            currentLocation = itemView.findViewById(R.id.quakeItem_currentLocation);
            background = itemView.findViewById(R.id.quakeItem_background);
        }
    }
}
