package com.example.quakeline.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.common.RestApi.Model.Result;
import com.example.common.helper.CurrentLocationHelper;
import com.example.quakeline.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class QuakelineRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Activity context;
    private final List<Result> datas;
    private final double userlat;
    private final double userlon;

    public QuakelineRecyclerViewAdapter(List<Result> _datas, Activity _context, double _userlat, double _userlon){
        context = _context;
        datas = _datas;
        userlat = _userlat;
        userlon = _userlon;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.quake_item, parent,false);
        return new QuakelineRecyclerViewViewHolder(view);
    }

    @SuppressLint({"SetTextI18n" , "ResourceAsColor" , "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Result quake = datas.get(position);
        QuakelineRecyclerViewViewHolder viewHolder=(QuakelineRecyclerViewViewHolder) holder;

        double term = CurrentLocationHelper.currentLocation(userlat, userlon, quake.getLat(), quake.getLng());

        double midKM = 50;
        double highKM = 25;
        if (term <= highKM){
            viewHolder.background.setBackground(ContextCompat.getDrawable(context, R.color.backgroundColor_highPriority));
         }else if (term <= midKM){
            viewHolder.background.setBackground(ContextCompat.getDrawable(context, R.color.backgroundColor_middlePriority));
         }else {
            viewHolder.background.setBackground(ContextCompat.getDrawable(context, R.color.backgroundColor_lowPriority));
         }
        viewHolder.title.setText(quake.getTitle());
        viewHolder.currentLocation.setText(String.format("%.2f",term)+" km");
        viewHolder.mag.setText(quake.getMag().toString());
        viewHolder.date.setText(quake.getDate());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    static class QuakelineRecyclerViewViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView mag;
        TextView date;
        TextView currentLocation;
        ConstraintLayout background;
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
