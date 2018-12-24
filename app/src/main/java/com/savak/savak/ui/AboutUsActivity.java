package com.savak.savak.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.LinearGradient;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.savak.savak.R;
import com.savak.savak.adapters.RecyclerAdapter;
import com.savak.savak.models.SmartLibraryResponseModel;
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
import java.util.HashMap;

public class AboutUsActivity extends AppCompatActivity {

    private ImageView ivLibraryLogo;
    private TextView tvLibraryAboutUS, title, tvLibraryName, tvLibraryAddress1, tvLibraryAddress2, tvLibraryMCity, tvLibraryPin;
    private Button btnEnter;
    private SmartLibraryResponseModel libraryResponseModel;
    private String TAG = getClass().getSimpleName();
    private int srno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_back));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initui();
    }

    private void initui() {
        title = findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
        tvLibraryAboutUS = findViewById(R.id.tvLibraryAboutUs);
        tvLibraryAboutUS.setMovementMethod(new ScrollingMovementMethod());
        ivLibraryLogo = findViewById(R.id.ivLibraryLogo);
        tvLibraryName = findViewById(R.id.tvLibraryName);
        tvLibraryName.setSelected(true);
        tvLibraryAddress1 = findViewById(R.id.tvLibraryAddress1);
        tvLibraryAddress1.setSelected(true);
        tvLibraryAddress2 = findViewById(R.id.tvLibraryAddress2);
        tvLibraryAddress2.setSelected(true);
        tvLibraryMCity = findViewById(R.id.tvLibraryMCity);
        tvLibraryMCity.setSelected(true);
        tvLibraryPin = findViewById(R.id.tvLibraryPin);
        btnEnter = findViewById(R.id.btnEnter);
        if(getIntent().hasExtra("library")){
            libraryResponseModel = (SmartLibraryResponseModel) getIntent().getExtras().get("library");
            title.setText(libraryResponseModel.getLibraryName());
            try {
                Glide.with(this).load(libraryResponseModel.getLogoImage()).into(ivLibraryLogo);
                tvLibraryName.setText(libraryResponseModel .getLibraryName());
                tvLibraryAddress1.setText(libraryResponseModel .getAddress1());
                tvLibraryAddress2.setText(libraryResponseModel .getAddress2());
                tvLibraryMCity.setText(libraryResponseModel .getM_CityName());
                tvLibraryPin.setText(" - " + libraryResponseModel .getPIN());
                srno = libraryResponseModel.getSrNo();
                HashMap<String, Object> map = new HashMap<>();
                map.put("LibraryId",libraryResponseModel.getSrNo());
                map.put("DatabaseName","LIBRARY");
                new GetAboutUsTask(URLConstants.ABOUT_US_ACTION,map, this).execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class GetAboutUsTask extends AsyncTask<Void, Void, JSONArray>{

        private String ACTION;
        private HashMap<String, Object> map;
        private ProgressDialog progressDialog;
        private Context context;
        private String TAG = getClass().getSimpleName();
        private JSONArray resultJSONArray;

        public GetAboutUsTask(String ACTION, HashMap<String, Object> map, Context context){
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
                for (int i = 0; i < jsonArray.length(); i++) {
                    if (srno == jsonArray.getJSONObject(i).getInt("SrNo")) {
                        tvLibraryAboutUS.setText(jsonArray.getJSONObject(i).get("AboutUsNote").toString());
                    }
                }
            }catch (Exception e){

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
