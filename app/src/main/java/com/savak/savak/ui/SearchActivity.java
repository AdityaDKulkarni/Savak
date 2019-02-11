package com.savak.savak.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.savak.savak.R;
import com.savak.savak.models.SmartLibraryResponseModel;
import com.savak.savak.utils.SOAPUtils;
import com.savak.savak.utils.URLConstants;
import com.savak.savak.worker.LibraryTasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class SearchActivity extends BaseActivity {

    private String TAG = getClass().getSimpleName();
    ArrayList<SmartLibraryResponseModel> libraryResponseModels;
    private TextView tvTagLine, tvTantraved;
    private EditText etSearch;
    private Button btnSearch;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initToolbar();

        initui();

    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toobar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
    }

    private void initui() {
        libraryResponseModels = new ArrayList<>();
        SpannableString spannableString = new SpannableString(getString(R.string.tag_line));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                if (SOAPUtils.isNetworkConnected(SearchActivity.this)) {
                    new LibraryTasks(URLConstants.REGION_ACTION, new HashMap<String, Object>(), SearchActivity.this)
                            .execute();
                } else {
                    Toast.makeText(SearchActivity.this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(getResources().getColor(R.color.colorPrimary));
            }
        };

        if (Locale.getDefault().getDisplayLanguage().equalsIgnoreCase("english")) {
            spannableString.setSpan(clickableSpan, 0, 9, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        } else if (Locale.getDefault().getDisplayLanguage().equalsIgnoreCase("मराठी")) {
            spannableString.setSpan(clickableSpan, 22, 30, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }
        tvTagLine = findViewById(R.id.tvTagLine);
        tvTagLine.setText(spannableString);
        tvTagLine.setMovementMethod(LinkMovementMethod.getInstance());
        tvTantraved = findViewById(R.id.tvTantraved);
        tvTantraved.setLinkTextColor(getResources().getColor(R.color.colorPrimary));
        tvTantraved.setMovementMethod(LinkMovementMethod.getInstance());
        etSearch = findViewById(R.id.etSeachBook);
        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (SOAPUtils.isNetworkConnected(SearchActivity.this)) {
                    if (!etSearch.getText().toString().isEmpty()) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("SearchParam", etSearch.getText().toString());
                        new LibraryTasks(URLConstants.SEARCH_ACTION, map, SearchActivity.this)
                                .execute();
                    } else {
                        etSearch.setError(getString(R.string.cannot_be_empty));
                    }
                } else {
                    Toast.makeText(SearchActivity.this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
