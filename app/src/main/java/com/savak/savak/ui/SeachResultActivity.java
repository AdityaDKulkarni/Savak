package com.savak.savak.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.savak.savak.R;
import com.savak.savak.adapters.RecyclerAdapter;
import com.savak.savak.models.SmartLibraryResponseModel;
import com.savak.savak.utils.ActionTypes;
import com.savak.savak.utils.ProgressDialogUtil;
import com.savak.savak.utils.SOAPUtils;
import com.savak.savak.utils.URLConnectionUtil;
import com.savak.savak.utils.URLConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;

public class SeachResultActivity extends BaseActivity {

    private RecyclerView rvBookSearch;
    private Toolbar toolbar;
    private ArrayList<SmartLibraryResponseModel> libraryResponseModels;
    private EditText etSearchBook;
    TextView tvtitle2;
    private Button btnSearchBook;

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
        etSearchBook = findViewById(R.id.etRegionSearchBook);
        btnSearchBook = findViewById(R.id.btnRegionSearchBook);

        if (getIntent().hasExtra("list") && getIntent().hasExtra("book_name")) {
            libraryResponseModels = (ArrayList<SmartLibraryResponseModel>) getIntent().getExtras().get("list");
            String book_name = getIntent().getStringExtra("book_name");
            tvtitle2.setText(getString(R.string.search_books) + " - " + book_name);
            rvBookSearch.setAdapter(new RecyclerAdapter(libraryResponseModels, this, book_name));
        }

        btnSearchBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SOAPUtils.isNetworkConnected(SeachResultActivity.this)) {
                    if (!etSearchBook.getText().toString().isEmpty()) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("SearchParam", etSearchBook.getText().toString());
                        new SearchTask(URLConstants.SEARCH_ACTION, map, SeachResultActivity.this)
                                .execute();
                    } else {
                        etSearchBook.setError(getString(R.string.cannot_be_empty));
                    }
                } else {
                    Toast.makeText(SeachResultActivity.this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toobar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        tvtitle2 = findViewById(R.id.tv_title_2);
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

    /**
     * Updates existing recycler view for new book search.
     */
    class SearchTask extends AsyncTask<Void, Void, JSONArray> {

        private String ACTION;
        private HashMap<String, Object> map;
        private ProgressDialog progressDialog;
        private Context context;
        private String TAG = getClass().getSimpleName();
        private JSONArray resultJSONArray;
        private ArrayList<SmartLibraryResponseModel> libraryResponseModels;
        private int code;

        public SearchTask(String ACTION, HashMap<String, Object> map, Context context) {
            this.ACTION = ACTION;
            this.map = map;
            this.context = context;
            this.progressDialog = ProgressDialogUtil.config(context);
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected JSONArray doInBackground(Void... voids) {
            HttpURLConnection urlConnection;
            OutputStream outputStream;
            InputStream inputStream;
            code = -1;

            try {
                urlConnection = URLConnectionUtil.config(ACTION, map);
                urlConnection.connect();
                outputStream = urlConnection.getOutputStream();
                if (outputStream != null) {
                    outputStream.write(SOAPUtils.getData(ACTION, map));
                    outputStream.flush();
                }
                code = urlConnection.getResponseCode();
                if (code == 200) {
                    Log.e(TAG + " response code", String.valueOf(code));
                    inputStream = urlConnection.getInputStream();
                    StringBuilder builder = new StringBuilder();
                    String line = null;
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    while ((line = reader.readLine()) != null) {
                        builder.append(line + "\n");
                    }
                    String response = builder.toString();
                    Log.e(TAG, response);

                    resultJSONArray = new JSONArray(response);
                    return resultJSONArray;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (jsonArray != null) {
                SmartLibraryResponseModel model = null;
                JSONObject object = null;
                try {
                    libraryResponseModels = new ArrayList();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        model = new SmartLibraryResponseModel();
                        object = jsonArray.getJSONObject(i);
                        model.setLibraryId(object.getInt("LibraryId"));
                        model.setDatabaseName(object.getString("DatabaseName"));
                        model.setWebsite(object.getString("Website"));
                        model.setLogoImage("http://www.tantraved.in/CP/Uploads/LibraryInfo/" + object.getString("LogoImage"));
                        model.setLibraryName(object.getString("LibraryName"));
                        model.setResultCount(object.getInt("ResultCount"));
                        model.setType(ActionTypes.TYPE_SMART_SEARCH);
                        libraryResponseModels.add(model);
                    }

                    rvBookSearch.setAdapter(new RecyclerAdapter(libraryResponseModels, SeachResultActivity.this, map.get("SearchParam").toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (code == 200) {
                Toast.makeText(context, context.getString(R.string.nothing_to_show), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, context.getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
            }
        }
    }
}
