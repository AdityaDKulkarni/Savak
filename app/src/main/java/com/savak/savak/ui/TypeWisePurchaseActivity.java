package com.savak.savak.ui;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.savak.savak.R;
import com.savak.savak.adapters.RecyclerAdapter;
import com.savak.savak.models.BookModel;
import com.savak.savak.models.SmartLibraryResponseModel;
import com.savak.savak.utils.ActionTypes;
import com.savak.savak.utils.SOAPUtils;
import com.savak.savak.utils.TableRowTextViewUtil;
import com.savak.savak.utils.URLConstants;
import com.savak.savak.worker.LibraryTasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class TypeWisePurchaseActivity extends BaseActivity {

    private Toolbar toolbar;
    private SmartLibraryResponseModel libraryResponseModel;
    private ImageView ivLibraryLogo;
    private TextView tvtitle2, tvSrNo, tvBookType, tvBookCount, tvTotalAmount, title, tvFinancialYear, tvLibraryAddress1, tvLibraryAddress2, tvLibraryMCity, tvLibraryPin, tvLibraryContact;
    private EditText etSearchBook;
    private Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_wise_purchase);
        initToolbar();
        initui();
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toobar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
    }

    private void initui() {
        title = toolbar.findViewById(R.id.tv_title);
        tvtitle2 = toolbar.findViewById(R.id.tv_title_2);
        title.setSelected(true);
        etSearchBook = findViewById(R.id.etSeachBook);
        btnSearch = findViewById(R.id.btnSearch);
        ivLibraryLogo = findViewById(R.id.ivLibraryLogo);
        tvLibraryAddress1 = findViewById(R.id.tvLibraryAddress1);
        tvLibraryAddress2 = findViewById(R.id.tvLibraryAddress2);
        tvLibraryMCity = findViewById(R.id.tvLibraryMCity);
        tvLibraryPin = findViewById(R.id.tvLibraryPin);
        tvLibraryContact = findViewById(R.id.tvLibraryContact);
        tvFinancialYear = findViewById(R.id.tvLibraryFinancialYear);

        if (getIntent().hasExtra("library")) {
            libraryResponseModel = (SmartLibraryResponseModel) getIntent().getExtras().get("library");
            title.setText(libraryResponseModel.getLibraryName());
            tvtitle2.setText(getString(R.string.current_year_book_purchase));
            final int libraryId = libraryResponseModel.getLibraryId();
            final int yearId = libraryResponseModel.getFinancialYearId();
            final String databaseName = libraryResponseModel.getDatabaseName();
            final int regionId = libraryResponseModel.getRegionId();
            try {
                Glide.with(this).load(libraryResponseModel.getLogoImage()).into(ivLibraryLogo);
                tvLibraryAddress1.setText(libraryResponseModel.getAddress1());
                tvLibraryAddress2.setText(libraryResponseModel.getAddress2());
                tvLibraryMCity.setText(libraryResponseModel.getM_CityName());
                tvLibraryPin.setText(" - " + libraryResponseModel.getPIN());
                String contact = String.format(getResources().getString(R.string.contact), libraryResponseModel.getPhoneNo1());
                tvLibraryContact.setText(contact);
                tvFinancialYear.setText(libraryResponseModel.getFinancialYear());

                btnSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (SOAPUtils.isNetworkConnected(TypeWisePurchaseActivity.this)) {
                            if (!etSearchBook.getText().toString().isEmpty()) {
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("SearchParam", etSearchBook.getText().toString());
                                map.put("LibraryId", libraryId);
                                map.put("RegionId", regionId);
                                map.put("DatabaseName", databaseName);
                                map.put("library", libraryResponseModel);
                                new LibraryTasks(URLConstants.ACTION_SEARCH_BOOK, map, TypeWisePurchaseActivity.this).execute();
                            } else {
                                etSearchBook.setError(getString(R.string.cannot_be_empty));
                            }
                        } else {
                            Toast.makeText(TypeWisePurchaseActivity.this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
                        }
                    }
                });
                 int bookSum = 0;
                 double priceSum = 0;
                if (getIntent().hasExtra("purchase")) {
                    ArrayList<BookModel> bookModels = (ArrayList<BookModel>) getIntent().getExtras().get("purchase");

                    for(int i = 0; i < bookModels.size(); i++){
                        priceSum += bookModels.get(i).getTotalAmount();
                        bookSum += bookModels.get(i).getBookCount();
                    }
                    BookModel bookModel = new BookModel();
                    bookModel.setBookType(getString(R.string.total));
                    bookModel.setBookCount(bookSum);
                    bookModel.setTotalAmount(priceSum);
                    bookModels.add(bookModel);
                    bookModel.setType(ActionTypes.TYPE_WISE_PURCHASE);

                    for(int i = 0; i < bookModels.size(); i++){
                        TableLayout table = findViewById(R.id.table);
                        TableRow row = new TableRow(this);

                        if(i == bookModels.size() - 1){
                            tvBookType = new TableRowTextViewUtil().config(this, R.color.black, bookModels.get(i).getBookType());
                            tvSrNo = new TableRowTextViewUtil().config(this, R.color.black, "");
                        }else{
                            tvBookType = new TableRowTextViewUtil().config(this, R.color.colorPrimary, bookModels.get(i).getBookType());
                            tvSrNo = new TableRowTextViewUtil().config(this, R.color.black, String.valueOf(i + 1));
                        }
                        tvSrNo.setGravity(Gravity.END);
                        tvBookType.setGravity(Gravity.START);
                        tvBookType.setPadding(16, 8,8,8);
                        tvBookCount = new TableRowTextViewUtil().config(this, R.color.black, String.valueOf(bookModels.get(i).getBookCount()));
                        tvBookCount.setPadding(0,0,16,0);
                        tvBookCount.setGravity(Gravity.END);
                        tvTotalAmount = new TableRowTextViewUtil().config(this, R.color.black, String.format(Locale.US, "%.2f", bookModels.get(i).getTotalAmount()));
                        tvTotalAmount.setPadding(0,0,16, 0);
                        tvTotalAmount.setGravity(Gravity.END);

                        row.addView(tvSrNo);
                        row.addView(tvBookType);
                        row.addView(tvBookCount);
                        row.addView(tvTotalAmount);
                        table.addView(row,new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }
}
