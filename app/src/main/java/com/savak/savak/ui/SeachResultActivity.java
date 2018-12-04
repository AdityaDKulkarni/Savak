package com.savak.savak.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.savak.savak.R;
import com.savak.savak.adapters.BookSearchAdapter;
import com.savak.savak.models.SmartLibraryResponseModel;

import java.util.ArrayList;

public class SeachResultActivity extends AppCompatActivity {


    private RecyclerView rvBookSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seach_result);
        getSupportActionBar().setTitle(getString(R.string.search_books));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvBookSearch = findViewById(R.id.rvBookSearch);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvBookSearch.getContext(),
                new LinearLayoutManager(this).getOrientation());
        rvBookSearch.addItemDecoration(dividerItemDecoration);
        rvBookSearch.setLayoutManager(new LinearLayoutManager(this));

        if(getIntent().hasExtra("list") && getIntent().hasExtra("book_name")){
            ArrayList<SmartLibraryResponseModel> libraryResponseModels = (ArrayList<SmartLibraryResponseModel>) getIntent().getExtras().get("list");
            String book_name = getIntent().getStringExtra("book_name");
            rvBookSearch.setAdapter(new BookSearchAdapter(libraryResponseModels, SeachResultActivity.this, book_name));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                onBackPressed();
                return true;
        }

        return false;
    }
}
