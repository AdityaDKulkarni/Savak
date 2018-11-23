package com.savak.savak.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.savak.savak.R;
import com.savak.savak.models.SmartLibraryResponseModel;

import java.util.ArrayList;

/**
 * @author Aditya Kulkarni
 */

public class SmartLibrariesAdapter extends RecyclerView.Adapter<SmartLibrariesAdapter.ViewHolder>{

    ArrayList<SmartLibraryResponseModel> libraryResponseModels;
    Context context;

    public SmartLibrariesAdapter(ArrayList<SmartLibraryResponseModel> libraryResponseModels, Context context) {
        this.libraryResponseModels = libraryResponseModels;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.smart_libraries_rowlayout, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            Glide.with(context).load(libraryResponseModels.get(position).getLogoImage()).into(holder.ivLibraryLogo);
            holder.tvLibraryName.setText(libraryResponseModels.get(position).getLibraryName());
            holder.tvLibraryAddress1.setText(libraryResponseModels.get(position).getAddress1());
            holder.tvLibraryAddress2.setText(libraryResponseModels.get(position).getAddress2());
            holder.tvLibraryMCity.setText(libraryResponseModels.get(position).getM_CityName());
            holder.tvLibraryPin.setText(libraryResponseModels.get(position).getPIN());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return libraryResponseModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivLibraryLogo;
        TextView tvLibraryName, tvLibraryAddress1, tvLibraryAddress2, tvLibraryMCity, tvLibraryPin;
        public ViewHolder(View itemView) {
            super(itemView);

            ivLibraryLogo = itemView.findViewById(R.id.ivLibraryLogo);
            tvLibraryName = itemView.findViewById(R.id.tvLibraryName);
            tvLibraryName.setSelected(true);
            tvLibraryAddress1 = itemView.findViewById(R.id.tvLibraryAddress1);
            tvLibraryAddress1.setSelected(true);
            tvLibraryAddress2 = itemView.findViewById(R.id.tvLibraryAddress2);
            tvLibraryAddress2.setSelected(true);
            tvLibraryMCity = itemView.findViewById(R.id.tvLibraryMCity);
            tvLibraryMCity.setSelected(true);
            tvLibraryPin = itemView.findViewById(R.id.tvLibraryPin);

        }
    }
}
