package com.savak.savak.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.savak.savak.R;
import com.savak.savak.adapters.RecyclerAdapter;
import com.savak.savak.listeners.RecyclerViewItemListener;
import com.savak.savak.models.SmartLibraryResponseModel;
import com.savak.savak.utils.ActionTypes;
import com.savak.savak.utils.SOAPUtils;
import com.savak.savak.utils.URLConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class RegionsActivity extends AppCompatActivity implements ActionTypes {

    private RecyclerView rvRegions;
    private ArrayList<SmartLibraryResponseModel> libraryResponseModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regions);
        getSupportActionBar().setTitle(getString(R.string.regions));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        libraryResponseModels = new ArrayList<>();
        rvRegions = findViewById(R.id.rvRegions);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        rvRegions.setLayoutManager(gridLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvRegions.getContext(),
                new LinearLayoutManager(this).getOrientation());
        rvRegions.addItemDecoration(dividerItemDecoration);

        if (SOAPUtils.isNetworkConnected(this)) {
            new RegionsAction(URLConstants.REGION_ACTION, new HashMap<String, String>(), this).execute();
        } else {
            Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
        }


        rvRegions.addOnItemTouchListener(new RecyclerViewItemListener(this, rvRegions, new RecyclerViewItemListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                HashMap<String, Integer> map = new HashMap();
                map.put("RegionId", libraryResponseModels.get(position).getSrNo());
                if (SOAPUtils.isNetworkConnected(RegionsActivity.this)) {
                    Intent intent = new Intent(RegionsActivity.this, RegionSpecificLibrariesActivity.class);
                    intent.putExtra("map", map);
                    startActivity(intent);
                } else {
                    Toast.makeText(RegionsActivity.this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }

    @Override
    public int getType() {
        return ActionTypes.TYPE_REGIONS;
    }

    class RegionsAction extends AsyncTask<Void, Void, JSONArray> {

        private String ACTION;
        private HashMap<String, String> map;
        private ProgressDialog progressDialog;
        private Context context;
        private String TAG = getClass().getSimpleName();
        private JSONArray resultJSONArray;

        public RegionsAction(String ACTION, HashMap<String, String> map, Context context) {
            this.ACTION = ACTION;
            this.map = map;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(context.getString(R.string.please_wait));
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected JSONArray doInBackground(Void... voids) {
            HttpURLConnection urlConnection;
            OutputStream outputStream;
            InputStream inputStream;
            int code = -1;

            try {
                URL url = new URL(URLConstants.BASE_URL);
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "text/xml");
                urlConnection.setRequestProperty("SOAPAction", ACTION);
                Log.e(TAG, ACTION);
                urlConnection.setRequestProperty("Content-length", SOAPUtils.getData(ACTION, map).length + "");
                HttpURLConnection.setFollowRedirects(false);
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setUseCaches(true);
                urlConnection.setConnectTimeout(60000);
                urlConnection.setReadTimeout(60000);
                urlConnection.connect();
                outputStream = urlConnection.getOutputStream();
                if (outputStream != null) {
                    outputStream.write(SOAPUtils.getData(ACTION, map));
                    outputStream.flush();
                }
                code = urlConnection.getResponseCode();
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

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return resultJSONArray;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            try {
                libraryResponseModels.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    SmartLibraryResponseModel model = new SmartLibraryResponseModel();
                    JSONObject object = jsonArray.getJSONObject(i);
                    model.setSrNo(object.getInt("SrNo"));
                    model.setRegionName(object.getString("RegionName"));
                    model.setDescription(object.getString("Description"));
                    model.setCreatedBy(object.getString("CreatedBy"));
                    model.setCreatedDate(object.getString("CreatedDate"));
                    model.setLastModifiedBy(object.getString("LastModifiedBy"));
                    model.setLastModifiedDate(object.getString("LastModifiedDate"));
                    model.setType(getType());
                    libraryResponseModels.add(model);
                }

                rvRegions.setAdapter(new RecyclerAdapter(libraryResponseModels, RegionsActivity.this, ""));

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
