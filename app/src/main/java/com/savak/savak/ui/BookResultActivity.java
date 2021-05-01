package com.savak.savak.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

public class BookResultActivity extends BaseActivity {

    private Toolbar toolbar;
    private SmartLibraryResponseModel libraryResponseModel;
    private ArrayList<SmartLibraryResponseModel> libraryResponseModels;
    private ImageView ivLibraryLogo;
    private TextView title, tvtitle2, tvLibraryFinancialYear, tvLibraryAddress1, tvLibraryAddress2, tvLibraryMCity, tvLibraryPin, tvLibraryContact;
    private EditText etSearchBook;
    private Button btnSearch;
    private RecyclerView rvNewBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_result);
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
        etSearchBook = findViewById(R.id.etSeachBook);
        btnSearch = findViewById(R.id.btnSearch);
        ivLibraryLogo = findViewById(R.id.ivLibraryLogo);
        tvLibraryAddress1 = findViewById(R.id.tvLibraryAddress1);
        tvLibraryAddress2 = findViewById(R.id.tvLibraryAddress2);
        tvLibraryMCity = findViewById(R.id.tvLibraryMCity);
        tvLibraryPin = findViewById(R.id.tvLibraryPin);
        tvLibraryContact = findViewById(R.id.tvLibraryContact);
        tvLibraryFinancialYear = findViewById(R.id.tvLibraryFinancialYear);

        rvNewBooks = findViewById(R.id.rvNewBooks);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        rvNewBooks.setLayoutManager(gridLayoutManager);

        if (getIntent().hasExtra("library")) {
            libraryResponseModel = (SmartLibraryResponseModel) getIntent().getExtras().get("library");
            title.setText(libraryResponseModel.getLibraryName());
            if(getIntent().hasExtra("book_name")){
                String book_name = (String) getIntent().getExtras().get("book_name");
                tvtitle2.setText(getString(R.string.search_books) + " - " + book_name);
            }
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
                tvLibraryFinancialYear.setText(libraryResponseModel.getFinancialYear());

                btnSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (SOAPUtils.isNetworkConnected(BookResultActivity.this)) {
                            if (!etSearchBook.getText().toString().isEmpty()) {
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("SearchParam", etSearchBook.getText().toString());
                                map.put("LibraryId", libraryId);
                                map.put("RegionId", regionId);
                                map.put("DatabaseName", databaseName);
                                map.put("library", libraryResponseModel);
                                new SearchTask(URLConstants.ACTION_SEARCH_BOOK, map).execute();
                            } else {
                                etSearchBook.setError(getString(R.string.cannot_be_empty));
                            }
                        } else {
                            Toast.makeText(BookResultActivity.this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
                        }
                    }
                });

                if (getIntent().hasExtra("books")) {
                    ArrayList<BookModel> bookModels = (ArrayList<BookModel>) getIntent().getExtras().get("books");
                    rvNewBooks.setAdapter(new RecyclerAdapter(BookResultActivity.this, "http://" + libraryResponseModel.getWebsite() + "/CP/Uploads/BookImages/", bookModels));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    class SearchTask extends AsyncTask<Void, Void, JSONArray> {

        String ACTION, TAG = getClass().getSimpleName();
        JSONArray resultJSONArray;
        HashMap<String, Object> map;
        ProgressDialog progressDialog;
        ArrayList<BookModel> bookModels;
        int code;

        public SearchTask(String ACTION, HashMap<String, Object> map) {
            this.ACTION = ACTION;
            this.map = map;
            this.progressDialog = ProgressDialogUtil.config(BookResultActivity.this);
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
            } catch (final Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(BookResultActivity.this);
                        builder.setTitle(getString(R.string.error));
                        builder.setMessage(e.getMessage());
                        builder.create().show();
                    }
                });
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
                    bookModels = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        BookModel bookModel = new BookModel();
                        object = jsonArray.getJSONObject(i);
                        if (i > 1) {
                            bookModel.setLibraryId(object.getInt("LibraryId"));
                            bookModel.setAuthor(object.getString("Author"));
                            bookModel.setBookImage(object.getString("BookImage"));
                            bookModel.setBookType(object.getString("BookType"));
                            bookModel.setBookInwardNo(object.getString("BookInwardNo"));
                            bookModel.setBookNo(object.getString("BookNo"));
                            bookModel.setBookTitle(object.getString("BookTitle"));
                            bookModel.setPublisherName(object.getString("PublisherName"));
                            bookModel.setAvailable(object.getBoolean("IsAvailable"));
                            bookModel.setType(ActionTypes.TYPE_NEW_BOOKS);
                            bookModels.add(bookModel);
                        }
                    }
                    rvNewBooks.setAdapter(new RecyclerAdapter(BookResultActivity.this, "http://savak.in/CP/Uploads/BookImages/", bookModels));
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(BookResultActivity.this);
                            builder.setTitle(getString(R.string.error));
                            builder.setMessage(e.getMessage());
                            builder.create().show();
                        }
                    });
                    e.printStackTrace();
                }
            } else if (code == 200) {
                Toast.makeText(BookResultActivity.this, getString(R.string.nothing_to_show), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(BookResultActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
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
