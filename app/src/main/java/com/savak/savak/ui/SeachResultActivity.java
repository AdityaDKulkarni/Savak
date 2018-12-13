package com.savak.savak.ui;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import com.savak.savak.R;
import com.savak.savak.adapters.RecyclerAdapter;
import com.savak.savak.models.SmartLibraryResponseModel;

import java.util.ArrayList;

public class SeachResultActivity extends AppCompatActivity {


    private RecyclerView rvBookSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seach_result);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_back));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView title = findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
        title.setText(getString(R.string.search_books));

        rvBookSearch = findViewById(R.id.rvBookSearch);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvBookSearch.getContext(),
                new LinearLayoutManager(this).getOrientation());
        rvBookSearch.addItemDecoration(dividerItemDecoration);
        rvBookSearch.setLayoutManager(new LinearLayoutManager(this));

        if(getIntent().hasExtra("list") && getIntent().hasExtra("book_name")){
            ArrayList<SmartLibraryResponseModel> libraryResponseModels = (ArrayList<SmartLibraryResponseModel>) getIntent().getExtras().get("list");
            String book_name = getIntent().getStringExtra("book_name");
            rvBookSearch.setAdapter(new RecyclerAdapter(libraryResponseModels, this, book_name));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return false;
    }
}
