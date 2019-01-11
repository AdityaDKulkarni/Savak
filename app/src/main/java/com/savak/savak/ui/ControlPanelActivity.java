package com.savak.savak.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.savak.savak.listeners.RecyclerViewItemListener;
import com.savak.savak.models.SmartLibraryResponseModel;
import com.savak.savak.utils.ActionTypes;
import com.savak.savak.utils.ProgressDialogUtil;
import com.savak.savak.utils.SOAPUtils;
import com.savak.savak.utils.URLConnectionUtil;
import com.savak.savak.utils.URLConstants;
import com.savak.savak.worker.LibraryTasks;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;

public class ControlPanelActivity extends BaseActivity {

    private Toolbar toolbar;
    private SmartLibraryResponseModel libraryResponseModel;
    private ImageView ivLibraryLogo;
    private TextView title, tvFinancialYear, tvLibraryAddress1, tvLibraryAddress2, tvLibraryMCity, tvLibraryPin, tvLibraryContact;
    private EditText etSearchBook;
    private Button btnSearch;
    private RecyclerView rvContaolPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_panel);
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

        final String[] strings = {
                getString(R.string.history),
                getString(R.string.management),
                getString(R.string.venture),
                getString(R.string.opinion_of_dignitaries),
                getString(R.string.new_books),
                getString(R.string.rare_books),
                getString(R.string.numerology),
                getString(R.string.my_membership)
        };

        final ArrayList<SmartLibraryResponseModel> libraryResponseModels = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            SmartLibraryResponseModel model = new SmartLibraryResponseModel();
            model.setRegionName(strings[i]);
            model.setType(ActionTypes.TYPE_CONTROL_PANEL);
            libraryResponseModels.add(model);
        }

        rvContaolPanel = findViewById(R.id.rvControlPanel);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        rvContaolPanel.setLayoutManager(gridLayoutManager);
        rvContaolPanel.setAdapter(new RecyclerAdapter(libraryResponseModels, ControlPanelActivity.this, ""));

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

                if (SOAPUtils.isNetworkConnected(this)) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("LibraryId", libraryId);
                    map.put("DatabaseName", databaseName);
                    new FinancialYearTask(URLConstants.FINANCIAL_YEAR_ACTION, map, ControlPanelActivity.this).execute();
                }

                btnSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (SOAPUtils.isNetworkConnected(ControlPanelActivity.this)) {
                            if (!etSearchBook.getText().toString().isEmpty()) {
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("SearchParam", etSearchBook.getText().toString());
                                map.put("LibraryId", libraryId);
                                map.put("RegionId", regionId);
                                map.put("DatabaseName", databaseName);
                                map.put("library", libraryResponseModel);
                                new LibraryTasks(URLConstants.ACTION_SEARCH_BOOK, map, ControlPanelActivity.this).execute();
                            } else {
                                etSearchBook.setError(getString(R.string.cannot_be_empty));
                            }
                        } else {
                            Toast.makeText(ControlPanelActivity.this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
                        }
                    }
                });

                rvContaolPanel.addOnItemTouchListener(new RecyclerViewItemListener(ControlPanelActivity.this,
                        rvContaolPanel, new RecyclerViewItemListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (SOAPUtils.isNetworkConnected(ControlPanelActivity.this)) {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("LibraryId", libraryResponseModel.getSrNo());
                            map.put("DatabaseName", libraryResponseModel.getDatabaseName());
                            if (strings[position].equalsIgnoreCase(getString(R.string.history))) {
                                map.put("library", libraryResponseModel);
                                new LibraryTasks(URLConstants.HISTORY_ACTION, map, ControlPanelActivity.this).execute();
                            }
                            if (strings[position].equalsIgnoreCase(getString(R.string.management))) {
                                map.put("library", libraryResponseModel);
                                new LibraryTasks(URLConstants.MANAGEMENT_ACTION, map, ControlPanelActivity.this).execute();
                            }
                            if (strings[position].equalsIgnoreCase(getString(R.string.venture))) {
                                map.put("library", libraryResponseModel);
                                new LibraryTasks(URLConstants.SOCIAL_PROJECT_ACTION, map, ControlPanelActivity.this).execute();
                            }
                            if (strings[position].equalsIgnoreCase(getString(R.string.opinion_of_dignitaries))) {
                                map.put("library", libraryResponseModel);
                                new LibraryTasks(URLConstants.COMPLEMENTS_ACTION, map, ControlPanelActivity.this).execute();
                            }
                            if (strings[position].equalsIgnoreCase(getString(R.string.new_books))) {
                                map.put("library", libraryResponseModel);
                                new LibraryTasks(URLConstants.NEW_BOOKS_ACTION, map, ControlPanelActivity.this).execute();
                            }
                            if (strings[position].equalsIgnoreCase(getString(R.string.rare_books))) {
                                map.put("library", libraryResponseModel);
                                new LibraryTasks(URLConstants.RARE_BOOKS_ACTION, map, ControlPanelActivity.this).execute();
                            }
                            if (strings[position].equalsIgnoreCase(getString(R.string.numerology))) {
                                Intent intent = new Intent(ControlPanelActivity.this, StatisticsActivity.class);
                                intent.putExtra("library", libraryResponseModel);
                                startActivity(intent);
                            }
                            if (strings[position].equalsIgnoreCase(getString(R.string.my_membership))) {
                                Intent intent = new Intent(ControlPanelActivity.this, MyMembershipActivity.class);
                                intent.putExtra("library", libraryResponseModel);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(ControlPanelActivity.this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class FinancialYearTask extends AsyncTask<Void, Void, JSONArray> {
        private String ACTION;
        private HashMap<String, Object> map;
        private Context context;
        private String TAG = getClass().getSimpleName();
        private JSONArray resultJSONArray;
        private int code;
        private ProgressDialog progressDialog;

        public FinancialYearTask(String ACTION, HashMap<String, Object> map, Context context) {
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
                JSONObject object = null;
                try {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        object = jsonArray.getJSONObject(i);
                        libraryResponseModel.setFinancialYearId(object.getInt("SrNo"));
                        libraryResponseModel.setFinancialYear(object.getString("Financial Year"));
                        libraryResponseModel.setFomYear(object.getString("FromYear"));
                        libraryResponseModel.setToYear(object.getString("ToYear"));
                    }
                    tvFinancialYear.setText(libraryResponseModel.getFinancialYear());
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
