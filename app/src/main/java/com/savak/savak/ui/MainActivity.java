package com.savak.savak.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

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

public class MainActivity extends BaseActivity implements ActionTypes{

    ArrayList<SmartLibraryResponseModel> libraryResponseModels;
    private RecyclerView recyclerView;
    private FloatingActionButton fabSeach;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        libraryResponseModels = new ArrayList<>();
        fabSeach = findViewById(R.id.fabSearch);
        fabSeach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
        recyclerView = findViewById(R.id.rvSmartLibraries);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                new LinearLayoutManager(this).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        new SOAPTask(MainActivity.this, URLConstants.LIBRARY_ACTION, new HashMap<String, Object>()).execute();
    }

    @Override
    public int getType() {
        return ActionTypes.TYPE_SMART_LIBRARY;
    }

    private class SOAPTask extends AsyncTask<Void, Void, JSONArray> {

        private String ACTION;
        private HashMap<String, Object> map;
        private ProgressDialog progressDialog;
        private Context context;
        private String TAG = getClass().getSimpleName();

        public SOAPTask(Context context, String ACTION, HashMap<String, Object> map) {
            this.context = context;
            this.ACTION = ACTION;
            this.map = map;
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

                jsonArray = new JSONArray(response);

                for(int i = 0; i < jsonArray.length(); i++){
                    SmartLibraryResponseModel model = new SmartLibraryResponseModel();
                    JSONObject object = jsonArray.getJSONObject(i);
                    model.setLogoImage("http://www.tantraved.in/CP/Uploads/LibraryInfo/" + object.getString("LogoImage"));
                    Log.e("Logo", model.getLogoImage());
                    model.setLibraryName(object.getString("LibraryName"));
                    model.setAddress1(object.getString("Address1"));
                    model.setAddress2(object.getString("Address2"));
                    model.setM_CityName(object.getString("M_CityName"));
                    model.setPIN(object.getString("PIN"));
                    model.setType(getType());
                    libraryResponseModels.add(model);
                }

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
        protected void onPostExecute(JSONArray jsonObject) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            recyclerView.setAdapter(new RecyclerAdapter(libraryResponseModels, context, ""));
        }
    }
}
