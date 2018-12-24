package com.savak.savak.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.savak.savak.R;
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
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends BaseActivity implements ActionTypes {

    private String TAG = getClass().getSimpleName();
    ArrayList<SmartLibraryResponseModel> libraryResponseModels;
    private TextView tvTagLine, tvTantraved;
    private EditText etSearch;
    private Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_back));
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
        TextView title = findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
        title.setTypeface(Typeface.DEFAULT_BOLD);
        title.setText(getString(R.string.smart_library));

        initui();

    }

    private void initui() {
        libraryResponseModels = new ArrayList<>();

        SpannableString spannableString = new SpannableString(getString(R.string.tag_line));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this, RegionsActivity.class);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
            }
        };

        if (Locale.getDefault().getDisplayLanguage().equalsIgnoreCase("english")) {
            Log.e("Locale", "english");
            spannableString.setSpan(clickableSpan, 0, 9, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        } else if (Locale.getDefault().getDisplayLanguage().equalsIgnoreCase("मराठी")) {
            Log.e("Locale", "मराठी");
            spannableString.setSpan(clickableSpan, 22, 30, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }
        tvTagLine = findViewById(R.id.tvTagLine);
        tvTagLine.setText(spannableString);
        tvTagLine.setMovementMethod(LinkMovementMethod.getInstance());
        tvTantraved = findViewById(R.id.tvTantraved);
        tvTantraved.setMovementMethod(LinkMovementMethod.getInstance());
        etSearch = findViewById(R.id.etSeachBook);
        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (SOAPUtils.isNetworkConnected(SearchActivity.this)) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("SearchParam", etSearch.getText().toString());
                    new SearchTask(URLConstants.SEARCH_ACTION, map, SearchActivity.this)
                            .execute(etSearch.getText().toString());

                } else {
                    Toast.makeText(SearchActivity.this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getType() {
        return ActionTypes.TYPE_SMART_SEARCH;
    }

    private class SearchTask extends AsyncTask<String, Void, JSONArray> {

        private String ACTION;
        private HashMap<String, Object> map;
        private ProgressDialog progressDialog;
        private Context context;
        private String TAG = getClass().getSimpleName();
        private JSONArray resultJSONArray;

        public SearchTask(String ACTION, HashMap<String, Object> map, Context context) {
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
        protected JSONArray doInBackground(String... strings) {
            HttpURLConnection urlConnection;
            OutputStream outputStream;
            InputStream inputStream;
            int code = -1;

            try {
                URL url = new URL(URLConstants.BASE_URL);
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
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
        protected void onPostExecute(JSONArray jsonObject) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            try {


                libraryResponseModels.clear();
                for (int i = 0; i < resultJSONArray.length(); i++) {
                    SmartLibraryResponseModel model = new SmartLibraryResponseModel();
                    JSONObject object = null;
                    try {
                        object = resultJSONArray.getJSONObject(i);
                        model.setLogoImage("http://www.tantraved.in/CP/Uploads/LibraryInfo/" + object.getString("LogoImage"));
                        model.setLibraryName(object.getString("LibraryName"));
                        model.setResultCount(object.getInt("ResultCount"));
                        model.setType(getType());
                        libraryResponseModels.add(model);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Intent intent = new Intent(SearchActivity.this, SeachResultActivity.class);
                intent.putExtra("list", libraryResponseModels);
                intent.putExtra("book_name", map.get("SearchParam").toString());
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
