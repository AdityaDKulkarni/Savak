package com.savak.savak.ui;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.savak.savak.R;
import com.savak.savak.adapters.RecyclerAdapter;
import com.savak.savak.models.SmartLibraryResponseModel;

import java.util.ArrayList;

public class SeachResultActivity extends BaseActivity {

    private RecyclerView rvBookSearch;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_seach_result);
        initToolbar();

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

    private void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toobar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
    }
}
