package com.savak.savak.ui;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.savak.savak.R;
import com.savak.savak.adapters.RecyclerAdapter;
import com.savak.savak.models.BookModel;
import com.savak.savak.models.SmartLibraryResponseModel;
import com.savak.savak.utils.SOAPUtils;
import com.savak.savak.utils.URLConstants;
import com.savak.savak.worker.LibraryTasks;

import java.util.ArrayList;
import java.util.HashMap;

public class NewBooksActivity extends BaseActivity {

    private Toolbar toolbar;
    private SmartLibraryResponseModel libraryResponseModel;
    private ImageView ivLibraryLogo;
    private TextView title, tvFinancialYear, tvLibraryAddress1, tvLibraryAddress2, tvLibraryMCity, tvLibraryPin, tvLibraryContact;
    private EditText etSearchBook;
    private Button btnSearch;
    private RecyclerView rvNewBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_books);
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
        etSearchBook = findViewById(R.id.etSeachBook);
        btnSearch = findViewById(R.id.btnSearch);
        ivLibraryLogo = findViewById(R.id.ivLibraryLogo);
        tvLibraryAddress1 = findViewById(R.id.tvLibraryAddress1);
        tvLibraryAddress2 = findViewById(R.id.tvLibraryAddress2);
        tvLibraryMCity = findViewById(R.id.tvLibraryMCity);
        tvLibraryPin = findViewById(R.id.tvLibraryPin);
        tvLibraryContact = findViewById(R.id.tvLibraryContact);
        tvFinancialYear = findViewById(R.id.tvLibraryFinancialYear);

        rvNewBooks = findViewById(R.id.rvNewBooks);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        rvNewBooks.setLayoutManager(gridLayoutManager);

        if (getIntent().hasExtra("library")) {
            libraryResponseModel = (SmartLibraryResponseModel) getIntent().getExtras().get("library");
            title.setText(libraryResponseModel.getLibraryName());
            final int libraryId = libraryResponseModel.getLibraryId();
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
                        if (SOAPUtils.isNetworkConnected(NewBooksActivity.this)) {
                            if (!etSearchBook.getText().toString().isEmpty()) {
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("SearchParam", etSearchBook.getText().toString());
                                map.put("LibraryId", libraryId);
                                map.put("DatabaseName", databaseName);
                                map.put("RegionId", regionId);
                                map.put("library", libraryResponseModel);
                                new LibraryTasks(URLConstants.ACTION_SEARCH_BOOK, map, NewBooksActivity.this).execute();
                            } else {
                                etSearchBook.setError(getString(R.string.cannot_be_empty));
                            }
                        } else {
                            Toast.makeText(NewBooksActivity.this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
                        }
                    }
                });

                if (getIntent().hasExtra("books")) {
                    ArrayList<BookModel> bookModels = (ArrayList<BookModel>) getIntent().getExtras().get("books");
                    rvNewBooks.setAdapter(new RecyclerAdapter(NewBooksActivity.this, "http://savak.in/CP/Uploads/BookImages/", bookModels));
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
