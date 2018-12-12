package com.savak.savak.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.savak.savak.R;
import com.savak.savak.models.SmartLibraryResponseModel;
import com.savak.savak.ui.AboutUsActivity;
import com.savak.savak.utils.ActionTypes;

import java.util.ArrayList;

/**
 * @author Aditya Kulkarni
 */

public class RecyclerAdapter extends RecyclerView.Adapter {

    private ArrayList<SmartLibraryResponseModel> libraryResponseModels;
    private Context context;
    private String book_name;

    public RecyclerAdapter(ArrayList<SmartLibraryResponseModel> libraryResponseModels, Context context, String book_name) {
        this.libraryResponseModels = libraryResponseModels;
        this.context = context;
        this.book_name = book_name;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case ActionTypes.TYPE_SMART_LIBRARY:
                view = LayoutInflater.from(context).inflate(R.layout.smart_libraries_rowlayout, parent, false);
                return new SmartLibrariesViewHolder(view);

            case ActionTypes.TYPE_SMART_SEARCH:
                view = LayoutInflater.from(context).inflate(R.layout.book_search_row_layout, parent, false);
                return new SearchViewHolder(view);

            case ActionTypes.TYPE_REGION_LIBRARIES:
                view = LayoutInflater.from(context).inflate(R.layout.region_library_layout, parent, false);
                return new RegionSpecificLibraryViewHolder(view);

            default:
                view = LayoutInflater.from(context).inflate(R.layout.regions_row_layout, parent, false);
                return new RegionsViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return libraryResponseModels.get(position).getType();
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case ActionTypes.TYPE_SMART_LIBRARY:
                ((SmartLibrariesViewHolder) holder).bindView(position);
                break;
            case ActionTypes.TYPE_SMART_SEARCH:
                ((SearchViewHolder) holder).bindView(position);
                break;

            case ActionTypes.TYPE_REGION_LIBRARIES:
                ((RegionSpecificLibraryViewHolder) holder).bindView(position);
                break;

            default:
                ((RegionsViewHolder) holder).bindView(position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (libraryResponseModels == null) {
            return 0;
        } else {
            return libraryResponseModels.size();
        }
    }

    class SmartLibrariesViewHolder extends RecyclerView.ViewHolder {

        ImageView ivLibraryLogo;
        TextView tvLibraryName, tvLibraryAddress1, tvLibraryAddress2, tvLibraryMCity, tvLibraryPin;

        public SmartLibrariesViewHolder(View itemView) {
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

        public void bindView(int position) {
            try {
                Glide.with(context).load(libraryResponseModels.get(position).getLogoImage()).into(ivLibraryLogo);
                tvLibraryName.setText(libraryResponseModels.get(position).getLibraryName());
                tvLibraryAddress1.setText(libraryResponseModels.get(position).getAddress1());
                tvLibraryAddress2.setText(libraryResponseModels.get(position).getAddress2());
                tvLibraryMCity.setText(libraryResponseModels.get(position).getM_CityName());
                tvLibraryPin.setText(libraryResponseModels.get(position).getPIN());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    class SearchViewHolder extends RecyclerView.ViewHolder {

        ImageView ivLibraryLogo;
        TextView tvLibraryName, tvLibraryBookCount, tvLibraryBookName;

        public SearchViewHolder(View itemView) {
            super(itemView);

            ivLibraryLogo = itemView.findViewById(R.id.ivSearchLibraryLogo);
            tvLibraryName = itemView.findViewById(R.id.tvSearchLibraryName);
            tvLibraryName.setSelected(true);
            tvLibraryBookCount = itemView.findViewById(R.id.tvSearchLibraryBookCount);
            tvLibraryBookCount.setSelected(true);
            tvLibraryBookName = itemView.findViewById(R.id.tvSearchBookName);
        }

        void bindView(int position) {
            try {
                Glide.with(context).load(libraryResponseModels.get(position).getLogoImage()).into(ivLibraryLogo);
                tvLibraryName.setText(libraryResponseModels.get(position).getLibraryName());
                tvLibraryBookName.setText("'" + book_name + "'");
                tvLibraryBookCount.setText("Total " + libraryResponseModels.get(position).getResultCount() + " books");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class RegionsViewHolder extends RecyclerView.ViewHolder {
        TextView tvRegionName;

        public RegionsViewHolder(View itemView) {
            super(itemView);

            tvRegionName = itemView.findViewById(R.id.tvRegionName);
        }

        void bindView(int position) {
            tvRegionName.setText(libraryResponseModels.get(position).getRegionName());
        }
    }

    class RegionSpecificLibraryViewHolder extends RecyclerView.ViewHolder{

        ImageView ivLibraryLogo;
        TextView tvLibraryName, tvLibraryAddress1, tvLibraryAddress2, tvLibraryMCity, tvLibraryPin;
        Button btnEnter;
        public RegionSpecificLibraryViewHolder(View itemView) {
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
            btnEnter = itemView.findViewById(R.id.btnEnter);
        }

        public void bindView(final int position) {
            try {
                Glide.with(context).load(libraryResponseModels.get(position).getLogoImage()).into(ivLibraryLogo);
                tvLibraryName.setText(libraryResponseModels.get(position).getLibraryName());
                tvLibraryAddress1.setText(libraryResponseModels.get(position).getAddress1());
                tvLibraryAddress2.setText(libraryResponseModels.get(position).getAddress2());
                tvLibraryMCity.setText(libraryResponseModels.get(position).getM_CityName());
                tvLibraryPin.setText(" - " + libraryResponseModels.get(position).getPIN());

                tvLibraryName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, AboutUsActivity.class);
                        intent.putExtra("library", libraryResponseModels.get(position));
                        Log.e("Intent", libraryResponseModels.get(position).getSrNo() + "");
                        context.startActivity(intent);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
