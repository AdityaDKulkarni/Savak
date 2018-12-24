package com.savak.savak.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.savak.savak.R;
import com.savak.savak.adapters.RecyclerAdapter;
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

public class RegionSpecificLibrariesActivity extends BaseActivity implements ActionTypes {



    private RecyclerView rvRegionLibraries;
    private ArrayList<SmartLibraryResponseModel> libraryResponseModels;
    private HashMap<String, Integer> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region_specific_libraries);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_back));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView title = findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
        title.setText(getString(R.string.updated_libraries));

        libraryResponseModels = new ArrayList<>();
        rvRegionLibraries = findViewById(R.id.rvRegionLibraries);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvRegionLibraries.getContext(),
                new LinearLayoutManager(this).getOrientation());
        rvRegionLibraries.addItemDecoration(dividerItemDecoration);
        rvRegionLibraries.setLayoutManager(new LinearLayoutManager(this));
        if(getIntent().hasExtra("map")){
            map = (HashMap<String, Integer>) getIntent().getExtras().get("map");
            if(SOAPUtils.isNetworkConnected(this)) {
                new GetRegionLibraries(this, URLConstants.LIBRARY_ACTION, map).execute();
            }else{
                Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public int getType() {
        return ActionTypes.TYPE_REGION_LIBRARIES;
    }

    private class GetRegionLibraries extends AsyncTask<Void, Void, JSONArray> {

        private String ACTION;
        private HashMap<String, Integer> map;
        private ProgressDialog progressDialog;
        private Context context;
        private String TAG = getClass().getSimpleName();

        public GetRegionLibraries(Context context, String ACTION, HashMap<String, Integer> map) {
            this.context = context;
            this.ACTION = ACTION;
            this.map = map;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(context.getString(R.string.getting_Smart_libraries));
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected JSONArray doInBackground(Void... voids) {
            JSONArray jsonArray = null;
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
                urlConnection.setRequestProperty("Content-length", SOAPUtils.getIntData(ACTION, map).length + "");
                HttpURLConnection.setFollowRedirects(false);
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setUseCaches(true);
                urlConnection.setConnectTimeout(60000);
                urlConnection.setReadTimeout(60000);
                urlConnection.connect();
                outputStream = urlConnection.getOutputStream();
                if (outputStream != null) {
                    outputStream.write(SOAPUtils.getIntData(ACTION, map));
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
                jsonArray = new JSONArray(response);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonArray;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            try{
                for(int i = 0; i < jsonArray.length(); i++){
                    SmartLibraryResponseModel model = new SmartLibraryResponseModel();
                    JSONObject object = jsonArray.getJSONObject(i);
                    model.setSrNo(object.getInt("SrNo"));
                    model.setLogoImage("http://www.tantraved.in/CP/Uploads/LibraryInfo/" + object.getString("LogoImage"));
                    model.setLibraryName(object.getString("LibraryName"));
                    model.setAddress1(object.getString("Address1"));
                    model.setAddress2(object.getString("Address2"));
                    model.setM_CityName(object.getString("M_CityName"));
                    model.setPIN(object.getString("PIN"));
                    model.setType(getType());
                    libraryResponseModels.add(model);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            rvRegionLibraries.setAdapter(new RecyclerAdapter(libraryResponseModels, context, ""));
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
