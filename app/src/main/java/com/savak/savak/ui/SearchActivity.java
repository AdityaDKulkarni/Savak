package com.savak.savak.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.savak.savak.R;
import com.savak.savak.adapters.BookSearchAdapter;
import com.savak.savak.models.SmartLibraryResponseModel;
import com.savak.savak.utils.SOAPUtils;
import com.savak.savak.utils.URLConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

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

public class SearchActivity extends AppCompatActivity {

    ArrayList<SmartLibraryResponseModel> libraryResponseModels;
    private EditText etSearch;
    private RecyclerView rvBookSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        libraryResponseModels = new ArrayList<>();
        etSearch = findViewById(R.id.etSeachBook);
        rvBookSearch = findViewById(R.id.rvBookSearch);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvBookSearch.getContext(),
                new LinearLayoutManager(this).getOrientation());
        rvBookSearch.addItemDecoration(dividerItemDecoration);
        rvBookSearch.setLayoutManager(new LinearLayoutManager(this));

        etSearch.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("SearchParam", etSearch.getText().toString());
                    new SearchTask(URLConstants.SEARCH_ACTION, map, SearchActivity.this)
                            .execute(etSearch.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    private class SearchTask extends AsyncTask<String, Void, JSONObject> {

        private String ACTION;
        private HashMap<String, String> map;
        private ProgressDialog progressDialog;
        private Context context;
        private String TAG = getClass().getSimpleName();

        public SearchTask(String ACTION, HashMap<String, String> map, Context context) {
            this.ACTION = ACTION;
            this.map = map;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(context.getString(R.string.getting_books));
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            JSONObject jsonObject = null;
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
                jsonObject = XML.toJSONObject(new String(response));
                JSONObject soapEnv = jsonObject.optJSONObject("soap:Envelope");
                JSONObject soapBody = soapEnv.optJSONObject("soap:Body");
                JSONObject lib = soapBody.optJSONObject("JSmartSearchResponse");

                String result = lib.optString("JSmartSearchResult");
                JSONArray jsonArray = new JSONArray(result);

                libraryResponseModels.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    SmartLibraryResponseModel model = new SmartLibraryResponseModel();
                    JSONObject object = jsonArray.getJSONObject(i);
                    model.setLogoImage("http://www.tantraved.in/CP/Uploads/LibraryInfo/" + object.getString("LogoImage"));
                    model.setLibraryName(object.getString("LibraryName"));
                    model.setResultCount(object.getInt("ResultCount"));
                    libraryResponseModels.add(model);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e(TAG, jsonObject.toString());
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            rvBookSearch.setAdapter(new BookSearchAdapter(libraryResponseModels, context));
        }
    }
}
