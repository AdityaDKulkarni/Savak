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

public class BookSearchAdapter extends RecyclerView.Adapter<BookSearchAdapter.ViewHolder> {

    ArrayList<SmartLibraryResponseModel> libraryResponseModels;
    Context context;

    public BookSearchAdapter(ArrayList<SmartLibraryResponseModel> libraryResponseModels, Context context) {
        this.libraryResponseModels = libraryResponseModels;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.book_search_row_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            Glide.with(context).load(libraryResponseModels.get(position).getLogoImage()).into(holder.ivLibraryLogo);
            holder.tvLibraryName.setText(libraryResponseModels.get(position).getLibraryName());
            holder.tvLibraryBookCount.setText("Total " + libraryResponseModels.get(position).getResultCount() + " books");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return libraryResponseModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivLibraryLogo;
        TextView tvLibraryName, tvLibraryBookCount;

        public ViewHolder(View itemView) {
            super(itemView);

            ivLibraryLogo = itemView.findViewById(R.id.ivSearchLibraryLogo);
            tvLibraryName = itemView.findViewById(R.id.tvSearchLibraryName);
            tvLibraryName.setSelected(true);
            tvLibraryBookCount = itemView.findViewById(R.id.tvSearchLibraryBookCount);
            tvLibraryBookCount.setSelected(true);
        }
    }
}
