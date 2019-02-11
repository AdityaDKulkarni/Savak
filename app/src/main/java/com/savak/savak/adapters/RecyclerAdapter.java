package com.savak.savak.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.savak.savak.R;
import com.savak.savak.models.BookModel;
import com.savak.savak.models.ComplementModel;
import com.savak.savak.models.ManagementBodyModel;
import com.savak.savak.models.MembershipModel;
import com.savak.savak.models.SmartLibraryResponseModel;
import com.savak.savak.models.SocialProjectModel;
import com.savak.savak.ui.AboutUsActivity;
import com.savak.savak.ui.ControlPanelActivity;
import com.savak.savak.utils.ActionTypes;
import com.savak.savak.utils.SOAPUtils;
import com.savak.savak.utils.URLConstants;
import com.savak.savak.worker.LibraryTasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * @author Aditya Kulkarni
 */

public class RecyclerAdapter extends RecyclerView.Adapter {

    private ArrayList<SmartLibraryResponseModel> libraryResponseModels;
    private ArrayList<ManagementBodyModel> managementBodyModels;
    private ArrayList<SocialProjectModel> socialProjectModels;
    private ArrayList<ComplementModel> complementModels;
    private ArrayList<BookModel> bookModels;
    private ArrayList<MembershipModel> membershipModels;
    private Context context;
    private String book_name, imageBaseUrl;

    public RecyclerAdapter(ArrayList<SmartLibraryResponseModel> libraryResponseModels, Context context, String book_name) {
        this.libraryResponseModels = libraryResponseModels;
        this.context = context;
        this.book_name = book_name;
    }

    public RecyclerAdapter(ArrayList<ManagementBodyModel> managementBodyModels, Context context) {
        this.managementBodyModels = managementBodyModels;
        this.context = context;
    }

    public RecyclerAdapter(Context context, ArrayList<SocialProjectModel> socialProjectModels) {
        this.socialProjectModels = socialProjectModels;
        this.context = context;
    }

    public RecyclerAdapter(Context context, ArrayList<ComplementModel> complementModels, String imageBaseUrl) {
        this.complementModels = complementModels;
        this.context = context;
        this.imageBaseUrl = imageBaseUrl;
    }

    public RecyclerAdapter(Context context, String imageBaseUrl, ArrayList<BookModel> bookModels) {
        this.bookModels = bookModels;
        this.context = context;
        this.imageBaseUrl = imageBaseUrl;
    }

    public RecyclerAdapter(ArrayList<MembershipModel> membershipModels, Context context, int dummy) {
        this.membershipModels = membershipModels;
        this.context = context;
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

            case ActionTypes.TYPE_MANAGEMENT_BODY:
                view = LayoutInflater.from(context).inflate(R.layout.management_body_row_layout, parent, false);
                return new ManagementBodyViewHolder(view);

            case ActionTypes.TYPE_SOCIAL_PROJECTS:
                view = LayoutInflater.from(context).inflate(R.layout.social_project_row_layout, parent, false);
                return new SocialProjectsViewHolder(view);

            case ActionTypes.TYPE_COMPLEMENTS:
                view = LayoutInflater.from(context).inflate(R.layout.complement_row_layout, parent, false);
                return new ComplementsViewHolder(view);

            case ActionTypes.TYPE_REGIONS:
                view = LayoutInflater.from(context).inflate(R.layout.regions_row_layout, parent, false);
                return new RegionsViewHolder(view);

            case ActionTypes.TYPE_CONTROL_PANEL:
                view = LayoutInflater.from(context).inflate(R.layout.control_panel_row_layout, parent, false);
                return new ControlPanelViewHolder(view);

            case ActionTypes.TYPE_NEW_BOOKS:
                view = LayoutInflater.from(context).inflate(R.layout.books_row_layout, parent, false);
                return new NewBooksViewHolder(view);
            case ActionTypes.TYPE_GRANTHSAMPADA:
                view = LayoutInflater.from(context).inflate(R.layout.book_statistics_row_layout, parent, false);
                return new BookStatisticsViewHolder(view);
            case ActionTypes.TYPE_READERS_CHOICE:
                view = LayoutInflater.from(context).inflate(R.layout.books_row_layout, parent, false);
                return new NewBooksViewHolder(view);
            case ActionTypes.TOP_10_READERS:
                Log.e("Adapter", "readers");
                view = LayoutInflater.from(context).inflate(R.layout.table_row_layout, parent, false);
                return new TopReadersViewHolder(view);
            case ActionTypes.MEMBERSHIP_PLANS:
                view = LayoutInflater.from(context).inflate(R.layout.membership_plans_row_layout, parent, false);
                return new MembershipPlansViewHolder(view);
            case ActionTypes.TYPE_WISE_COUNT:
                view = LayoutInflater.from(context).inflate(R.layout.table_row_layout, parent, false);
                return new TypeWiseCount(view);
            case ActionTypes.MEMBER_STATISTICS:
                view = LayoutInflater.from(context).inflate(R.layout.table_row_layout, parent, false);
                return new MemberStatisticsHolder(view);
            case ActionTypes.TYPE_WISE_PURCHASE:
                view = LayoutInflater.from(context).inflate(R.layout.purchase_row_layout, parent, false);
                return new TypePurchaseViewHolder(view);
            default:
                return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (libraryResponseModels != null) {
            return libraryResponseModels.get(position).getType();
        }
        if (socialProjectModels != null) {
            return socialProjectModels.get(position).getType();
        }
        if (complementModels != null) {
            return complementModels.get(position).getType();
        }
        if (bookModels != null) {
            return bookModels.get(position).getType();
        }
        if (membershipModels != null) {
            return membershipModels.get(position).getType();
        }
        return managementBodyModels.get(position).getType();
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

            case ActionTypes.TYPE_MANAGEMENT_BODY:
                ((ManagementBodyViewHolder) holder).bindView(position);
                break;

            case ActionTypes.TYPE_SOCIAL_PROJECTS:
                ((SocialProjectsViewHolder) holder).bindView(position);
                break;

            case ActionTypes.TYPE_REGIONS:
                ((RegionsViewHolder) holder).bindView(position);
                break;

            case ActionTypes.TYPE_COMPLEMENTS:
                ((ComplementsViewHolder) holder).bindView(position);
                break;
            case ActionTypes.TYPE_CONTROL_PANEL:
                ((ControlPanelViewHolder) holder).bindView(position);
                break;
            case ActionTypes.TYPE_NEW_BOOKS:
                ((NewBooksViewHolder) holder).bindView(position);
                break;
            case ActionTypes.TYPE_GRANTHSAMPADA:
                ((BookStatisticsViewHolder) holder).bindView(position);
                break;
            case ActionTypes.TYPE_READERS_CHOICE:
                ((NewBooksViewHolder) holder).bindView(position);
                break;
            case ActionTypes.TOP_10_READERS:
                Log.e("Adapter", "readers");
                ((TopReadersViewHolder) holder).bindView(position);
                break;
            case ActionTypes.MEMBERSHIP_PLANS:
                ((MembershipPlansViewHolder) holder).bindView(position);
                break;
            case ActionTypes.TYPE_WISE_COUNT:
                ((TypeWiseCount) holder).bindView(position);
                break;
            case ActionTypes.TYPE_WISE_PURCHASE:
                ((TypePurchaseViewHolder) holder).bindView(position);
                break;
            case ActionTypes.MEMBER_STATISTICS:
                ((MemberStatisticsHolder) holder).bindView(position);
        }
    }

    @Override
    public int getItemCount() {
        if (managementBodyModels != null) {
            return managementBodyModels.size();
        } else if (libraryResponseModels != null) {
            return libraryResponseModels.size();
        } else if (socialProjectModels != null) {
            return socialProjectModels.size();
        } else if (complementModels != null) {
            return complementModels.size();
        } else if (bookModels != null) {
            return bookModels.size();
        } else if (membershipModels != null) {
            return membershipModels.size();
        } else {
            return 0;
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
                if(libraryResponseModels.get(position).getMembershipTYpe() == 3){
                    tvLibraryName.setTextColor(Color.RED);
                }
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
        View itemView;

        public SearchViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ivLibraryLogo = itemView.findViewById(R.id.ivSearchLibraryLogo);
            tvLibraryName = itemView.findViewById(R.id.tvSearchLibraryName);
            tvLibraryName.setSelected(true);
            tvLibraryBookCount = itemView.findViewById(R.id.tvSearchLibraryBookCount);
            tvLibraryBookCount.setSelected(true);
            tvLibraryBookName = itemView.findViewById(R.id.tvSearchBookName);
        }

        void bindView(final int position) {
            try {
                final int libraryId = libraryResponseModels.get(position).getLibraryId();
                final String databaseName = libraryResponseModels.get(position).getDatabaseName();
                final SmartLibraryResponseModel model = libraryResponseModels.get(position);
                Glide.with(context).load(libraryResponseModels.get(position).getLogoImage()).into(ivLibraryLogo);
                tvLibraryName.setText(libraryResponseModels.get(position).getLibraryName());
                tvLibraryBookName.setText("'" + book_name + "'");
                SpannableString spannableString = new SpannableString(context.getString(R.string.total) + " " + libraryResponseModels.get(position).getResultCount() + " " + context.getString(R.string.books));
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View view) {

                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        ds.setColor(context.getResources().getColor(R.color.colorPrimary));
                    }
                };
                if (Locale.getDefault().getDisplayLanguage().equalsIgnoreCase("english")) {
                    spannableString.setSpan(clickableSpan, 5, 7, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                } else if (Locale.getDefault().getDisplayLanguage().equalsIgnoreCase("मराठी")) {
                    spannableString.setSpan(clickableSpan, 5, 5 + String.valueOf(libraryResponseModels.get(position).getResultCount()).length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                }
                tvLibraryBookCount.setText(spannableString);
                tvLibraryBookCount.setMovementMethod(new LinkMovementMethod());

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (SOAPUtils.isNetworkConnected(context)) {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("LibraryId", libraryId);
                            map.put("DatabaseName", databaseName);
                            map.put("SearchParam", book_name);
                            map.put("library", model);
                            new LibraryTasks(URLConstants.ACTION_SEARCH_BOOK, map, context).execute();
                        } else {
                            Toast.makeText(context, context.getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
                        }
                    }
                });
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

    class ControlPanelViewHolder extends RecyclerView.ViewHolder {
        TextView tvRegionName;

        public ControlPanelViewHolder(View itemView) {
            super(itemView);

            tvRegionName = itemView.findViewById(R.id.tvRegionName);
        }

        void bindView(int position) {
            tvRegionName.setText(libraryResponseModels.get(position).getRegionName());
        }
    }

    class RegionSpecificLibraryViewHolder extends RecyclerView.ViewHolder {

        ImageView ivLibraryLogo;
        TextView tvLibraryName, tvLibraryAddress1, tvLibraryAddress2, tvLibraryMCity, tvLibraryPin;

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
        }

        public void bindView(final int position) {
            try {
                Glide.with(context).load(libraryResponseModels.get(position).getLogoImage()).into(ivLibraryLogo);
                if(libraryResponseModels.get(position).getMembershipTYpe() == 3){
                    tvLibraryName.setTextColor(Color.RED);
                }
                tvLibraryName.setText(libraryResponseModels.get(position).getLibraryName());
                tvLibraryAddress1.setText(libraryResponseModels.get(position).getAddress1());
                tvLibraryAddress2.setText(libraryResponseModels.get(position).getAddress2());
                tvLibraryMCity.setText(libraryResponseModels.get(position).getM_CityName());
                tvLibraryPin.setText(" - " + libraryResponseModels.get(position).getPIN());

                tvLibraryName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(libraryResponseModels.get(position).getMembershipTYpe() != 3){
                            Intent intent = new Intent(context, AboutUsActivity.class);
                            intent.putExtra("library", libraryResponseModels.get(position));
                            Log.e("Intent", libraryResponseModels.get(position).getSrNo() + "");
                            context.startActivity(intent);
                        }
                    }
                });

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(libraryResponseModels.get(position).getMembershipTYpe() != 3) {
                            Intent intent = new Intent(context, ControlPanelActivity.class);
                            intent.putExtra("library", libraryResponseModels.get(position));
                            context.startActivity(intent);
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class ManagementBodyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivManagementBodyPerson;
        TextView tvManagementBodyName, tvManagementBodyProfile;

        public ManagementBodyViewHolder(View itemView) {
            super(itemView);
            Log.e("holder", "management");
            ivManagementBodyPerson = itemView.findViewById(R.id.ivManagementBodyPerson);
            tvManagementBodyName = itemView.findViewById(R.id.tvManagementBodyName);
            tvManagementBodyProfile = itemView.findViewById(R.id.tvManagementBodyProfile);
        }

        public void bindView(int position) {
            try {
                Glide.with(context).load("http://" + managementBodyModels.get(position).getWebsite() + "/CP/Uploads/ManagementBodyImages/" + managementBodyModels.get(position).getLogoImage()).into(ivManagementBodyPerson);
                tvManagementBodyName.setText(managementBodyModels.get(position).getManagementBodyName());
                tvManagementBodyProfile.setText(managementBodyModels.get(position).getProfileIdentity());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class SocialProjectsViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProject;
        TextView tvProjectTitle, tvProjectDate, tvProjectDesc;

        public SocialProjectsViewHolder(View itemView) {
            super(itemView);

            ivProject = itemView.findViewById(R.id.ivProjectPhoto);
            tvProjectTitle = itemView.findViewById(R.id.tvProjectTitle);
            tvProjectDate = itemView.findViewById(R.id.tvProjectDate);
            tvProjectDesc = itemView.findViewById(R.id.tvProjectDesc);
        }

        public void bindView(final int position) {
            try {
                Glide.with(context).load("http://" + socialProjectModels.get(position).getWebsite() + "/CP/Uploads/ProjectImages/" + socialProjectModels.get(position).getPhotoImage()).into(ivProject);
                tvProjectTitle.setText(socialProjectModels.get(position).getProjectTitle());
                tvProjectDate.setText(socialProjectModels.get(position).getProjectDate());
                tvProjectDesc.setText(socialProjectModels.get(position).getLongDesc());

                ivProject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(SOAPUtils.isNetworkConnected(context)){
                            View view1 = LayoutInflater.from(context).inflate(R.layout.image_zoom_layout, null, false);
                            Dialog dialog = new Dialog(context);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(view1);
                            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                            ImageView imageView = view1.findViewById(R.id.ivProjectPhoto);
                            Glide.with(context).load("http://" + socialProjectModels.get(position).getWebsite() + "/CP/Uploads/ProjectImages/" + socialProjectModels.get(position).getPhotoImage()).into(imageView);

                            dialog.show();
                        }else{
                            Toast.makeText(context, context.getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();

                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class ComplementsViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProject;
        TextView tvProjectTitle, tvProjectDate, tvProjectDesc;

        public ComplementsViewHolder(View itemView) {
            super(itemView);
            ivProject = itemView.findViewById(R.id.ivProjectPhoto);
            tvProjectTitle = itemView.findViewById(R.id.tvProjectTitle);
            tvProjectDate = itemView.findViewById(R.id.tvProjectDate);
            tvProjectDesc = itemView.findViewById(R.id.tvProjectDesc);
        }

        public void bindView(final int position) {
            try {
                Glide.with(context).load(imageBaseUrl + complementModels.get(position).getScannedCopy()).into(ivProject);
                tvProjectTitle.setText(complementModels.get(position).getComplementsBy());
                tvProjectDate.setText(complementModels.get(position).getComplementDate());
                tvProjectDesc.setText(complementModels.get(position).getDetails());

                ivProject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(SOAPUtils.isNetworkConnected(context)){
                            View view1 = LayoutInflater.from(context).inflate(R.layout.image_zoom_layout, null, false);
                            Dialog dialog = new Dialog(context);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(view1);
                            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                            ImageView imageView = view1.findViewById(R.id.ivProjectPhoto);
                            Glide.with(context).load(imageBaseUrl + complementModels.get(position).getScannedCopy()).into(imageView);

                            dialog.show();
                        }else{
                            Toast.makeText(context, context.getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();

                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class NewBooksViewHolder extends RecyclerView.ViewHolder {

        ImageView ivBookCover;
        TextView tvBookTitle, tvAuthor, tvBookType, tvPublisher, tvBookNo, tvCount;

        public NewBooksViewHolder(View itemView) {
            super(itemView);

            ivBookCover = itemView.findViewById(R.id.ivBookCover);
            tvBookNo = itemView.findViewById(R.id.tvBookNumber);
            tvBookTitle = itemView.findViewById(R.id.tvBookTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvBookType = itemView.findViewById(R.id.tvBookType);
            tvPublisher = itemView.findViewById(R.id.tvPublisher);
            tvPublisher.setSelected(true);
            tvCount = itemView.findViewById(R.id.tvCount);
        }

        public void bindView(int position) {
            try {
                Glide.with(context).load(imageBaseUrl + bookModels.get(position).getBookImage()).into(ivBookCover);
                tvBookNo.setText(bookModels.get(position).getBookInwardNo() + " / " + bookModels.get(position).getBookNo());
                tvBookTitle.setText(bookModels.get(position).getBookTitle());
                tvAuthor.setText(bookModels.get(position).getAuthor());
                tvBookType.setText(bookModels.get(position).getBookType());
                tvPublisher.setText(bookModels.get(position).getPublisherName());
                if (bookModels.get(position).getBookCount() != 0) {
                    tvPublisher.setText(bookModels.get(position).getBookCount() + " " + context.getString(R.string.members_has_read));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class BookStatisticsViewHolder extends RecyclerView.ViewHolder {
        TextView tvBookType, tvBookCount;

        public BookStatisticsViewHolder(View itemView) {
            super(itemView);

            tvBookType = itemView.findViewById(R.id.tvBookType);
            tvBookCount = itemView.findViewById(R.id.tvBookCount);
        }

        public void bindView(int position) {
            try {
                tvBookType.setText(bookModels.get(position).getBookType());
                tvBookCount.setText(String.valueOf(bookModels.get(position).getBookCount()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class TopReadersViewHolder extends RecyclerView.ViewHolder {
        TextView tv2, tv3, tv4;

        public TopReadersViewHolder(View itemView) {
            super(itemView);

            tv2 = itemView.findViewById(R.id.tv2);
            tv3 = itemView.findViewById(R.id.tv3);
            tv4 = itemView.findViewById(R.id.tv4);
        }

        public void bindView(int position) {
            try {
                tv2.setText(String.valueOf(position + 1));
                tv3.setText(bookModels.get(position).getMember());
                tv4.setText(String.valueOf(bookModels.get(position).getBookCount()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class TypeWiseCount extends RecyclerView.ViewHolder {
        TextView tv2, tv3, tv4;

        public TypeWiseCount(View itemView) {
            super(itemView);

            tv2 = itemView.findViewById(R.id.tv2);
            tv3 = itemView.findViewById(R.id.tv3);
            tv4 = itemView.findViewById(R.id.tv4);
        }

        public void bindView(int position) {
            try {
                tv2.setText(String.valueOf(position + 1));
                tv3.setText(bookModels.get(position).getBookType());
                tv4.setText(String.valueOf(bookModels.get(position).getBookCount()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class TypePurchaseViewHolder extends RecyclerView.ViewHolder {
        TextView tv2, tv3, tv4, tv5;

        public TypePurchaseViewHolder(View itemView) {
            super(itemView);

            tv2 = itemView.findViewById(R.id.tv2);
            tv3 = itemView.findViewById(R.id.tv3);
            tv4 = itemView.findViewById(R.id.tv4);
            tv5 = itemView.findViewById(R.id.tv5);
        }

        public void bindView(int position) {
            try {
                tv2.setText(String.valueOf(position + 1));
                tv3.setText(bookModels.get(position).getBookType());
                tv5.setText(String.valueOf(bookModels.get(position).getBookCount()));
                tv4.setText(String.valueOf(bookModels.get(position).getTotalAmount()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class MemberStatisticsHolder extends RecyclerView.ViewHolder {
        TextView tv2, tv3, tv4;

        public MemberStatisticsHolder(View itemView) {
            super(itemView);

            tv2 = itemView.findViewById(R.id.tv2);
            tv3 = itemView.findViewById(R.id.tv3);
            tv4 = itemView.findViewById(R.id.tv4);
        }

        public void bindView(int position) {
            try {
                tv2.setText(String.valueOf(position + 1));
                tv3.setText(membershipModels.get(position).getMembershipPlan());
                tv4.setText(String.valueOf(membershipModels.get(position).getMeberCount()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class MembershipPlansViewHolder extends RecyclerView.ViewHolder {

        TextView tvPlan, tvMaxBooks, tvDuration;

        public MembershipPlansViewHolder(View itemView) {
            super(itemView);

            tvPlan = itemView.findViewById(R.id.tvPlan);
            tvMaxBooks = itemView.findViewById(R.id.tvMaxBooks);
            tvDuration = itemView.findViewById(R.id.tvDuration);
        }

        public void bindView(int position) {
                tvPlan.setText(membershipModels.get(position).getMembershipPlan());
                tvMaxBooks.setText(context.getString(R.string.books) + " "
                        + membershipModels.get(position).getMaxBooks() + " / "
                        + context.getString(R.string.magazines) + " "
                        + membershipModels.get(position).getMaxMagazines());
                tvDuration.setText(context.getString(R.string.duration) + " - " + membershipModels.get(position).getDuration() + " " + context.getString(R.string.days));

        }
    }
}