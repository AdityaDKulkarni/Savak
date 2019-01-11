package com.savak.savak.worker;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.savak.savak.R;
import com.savak.savak.models.BookModel;
import com.savak.savak.models.ComplementModel;
import com.savak.savak.models.ManagementBodyModel;
import com.savak.savak.models.MembershipModel;
import com.savak.savak.models.SmartLibraryResponseModel;
import com.savak.savak.models.SocialProjectModel;
import com.savak.savak.ui.BookResultActivity;
import com.savak.savak.ui.ComplementsActivity;
import com.savak.savak.ui.GranthSampadaActivity;
import com.savak.savak.ui.HistoryActivity;
import com.savak.savak.ui.ManagementBodyActivity;
import com.savak.savak.ui.MemberActivity;
import com.savak.savak.ui.MembershipPlansActivity;
import com.savak.savak.ui.MyMembershipActivity;
import com.savak.savak.ui.NewBooksActivity;
import com.savak.savak.ui.ReadersChoiceActivity;
import com.savak.savak.ui.RegionSpecificLibrariesActivity;
import com.savak.savak.ui.RegionsActivity;
import com.savak.savak.ui.SeachResultActivity;
import com.savak.savak.ui.SocialProjectActivity;
import com.savak.savak.ui.TopReadersActivity;
import com.savak.savak.ui.TypeWiseCountActivity;
import com.savak.savak.ui.TypeWisePurchaseActivity;
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

/**
 * @author Aditya Kulkarni
 */

public class LibraryTasks extends AsyncTask<Void, Void, JSONArray> {

    private String ACTION;
    private HashMap<String, Object> map;
    private ProgressDialog progressDialog;
    private Context context;
    private String TAG = getClass().getSimpleName();
    private JSONArray resultJSONArray;
    private ArrayList<SmartLibraryResponseModel> libraryResponseModels;
    private ArrayList<ManagementBodyModel> managementBodyModels;
    private ArrayList<SocialProjectModel> socialProjectModels;
    private ArrayList<ComplementModel> complementModels;
    private ArrayList<BookModel> bookModels;
    private ArrayList<MembershipModel> membershipModels;
    private int code;

    public LibraryTasks(String ACTION, HashMap<String, Object> map, Context context) {
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
            }
            outputStream.flush();
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
            Intent intent = null;
            SmartLibraryResponseModel model = null;
            JSONObject object = null;
            try {
                switch (ACTION) {
                    case "http://tantraved.in/SmartSearch":
                        libraryResponseModels = new ArrayList();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            model = new SmartLibraryResponseModel();
                            object = jsonArray.getJSONObject(i);
                            model.setLogoImage("http://www.tantraved.in/CP/Uploads/LibraryInfo/" + object.getString("LogoImage"));
                            model.setLibraryId(object.getInt("LibraryId"));
                            model.setDatabaseName(object.getString("DatabaseName"));
                            model.setLibraryName(object.getString("LibraryName"));
                            model.setResultCount(object.getInt("ResultCount"));
                            model.setType(ActionTypes.TYPE_SMART_SEARCH);
                            libraryResponseModels.add(model);
                        }
                        intent = new Intent(context, SeachResultActivity.class);
                        intent.putExtra("book_name", map.get("SearchParam").toString());
                        break;
                    case "http://tantraved.in/GetRegions":
                        libraryResponseModels = new ArrayList();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            model = new SmartLibraryResponseModel();
                            object = jsonArray.getJSONObject(i);
                            model.setSrNo(object.getInt("SrNo"));
                            model.setRegionName(object.getString("RegionName"));
                            model.setDescription(object.getString("Description"));
                            model.setCreatedBy(object.getString("CreatedBy"));
                            model.setCreatedDate(object.getString("CreatedDate"));
                            //model.setDatabaseName(object.getString("DatabaseName"));
                            model.setLastModifiedBy(object.getString("LastModifiedBy"));
                            model.setLastModifiedDate(object.getString("LastModifiedDate"));
                            model.setType(ActionTypes.TYPE_REGIONS);
                            libraryResponseModels.add(model);
                        }
                        intent = new Intent(context, RegionsActivity.class);
                        intent.putExtra("book_name", "");
                        break;
                    case "http://tantraved.in/GetSmartLibraries":
                        libraryResponseModels = new ArrayList();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            model = new SmartLibraryResponseModel();
                            object = jsonArray.getJSONObject(i);
                            model.setSrNo(object.getInt("SrNo"));
                            model.setLibraryId(object.getInt("LibraryId"));
                            model.setLogoImage("http://www.tantraved.in/CP/Uploads/LibraryInfo/" + object.getString("LogoImage"));
                            model.setLibraryName(object.getString("LibraryName"));
                            model.setAddress1(object.getString("Address1"));
                            model.setAddress2(object.getString("Address2"));
                            model.setRegionId(object.getInt("RegionId"));
                            model.setWebsite(object.getString("Website"));
                            model.setDatabaseName(object.getString("DatabaseName"));
                            model.setM_CityName(object.getString("M_CityName"));
                            model.setPIN(object.getString("PIN"));
                            model.setPhoneNo1(object.getString("PhoneNo1"));
                            model.setType(ActionTypes.TYPE_REGION_LIBRARIES);
                            libraryResponseModels.add(model);
                        }
                        intent = new Intent(context, RegionSpecificLibrariesActivity.class);
                        intent.putExtra("book_name", "");
                        break;

                    case "http://tantraved.in/GetHistory":
                        libraryResponseModels = new ArrayList();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            model = new SmartLibraryResponseModel();
                            object = jsonArray.getJSONObject(i);
                            if(i == 1){
                                model = (SmartLibraryResponseModel) map.get("library");
                                object = jsonArray.getJSONObject(i);
                                model.setFinancialYear(object.getString("Financial Year"));
                            }
                            if(i > 1) {
                                model.setSrNo(object.getInt("SrNo"));
                                model.setLibraryId(object.getInt("LibraryId"));
                                model.setHistory(object.getString("History"));
                                libraryResponseModels.add(model);
                            }
                        }
                        intent = new Intent(context, HistoryActivity.class);
                        intent.putExtra("library", (SmartLibraryResponseModel) map.get("library"));
                        break;
                    case "http://tantraved.in/GetManagementBody":
                        managementBodyModels = new ArrayList<>();
                        for(int i = 0; i < jsonArray.length(); i++){
                            ManagementBodyModel bodyModel = new ManagementBodyModel();
                            object = jsonArray.getJSONObject(i);
                            if(i == 1){
                                model = (SmartLibraryResponseModel) map.get("library");
                                object = jsonArray.getJSONObject(i);
                                model.setFinancialYear(object.getString("Financial Year"));
                            }
                            if(i > 1) {
                                bodyModel.setSrNo(object.getInt("SrNo"));
                                bodyModel.setLibraryId(object.getInt("LibraryId"));
                                bodyModel.setTitle(object.getString("Title"));
                                bodyModel.setFirstName(object.getString("FirstName"));
                                bodyModel.setMiddleName(object.getString("MiddleName"));
                                bodyModel.setLastName(object.getString("LastName"));
                                bodyModel.setManagementBodyName(object.getString("ManagementBodyName"));
                                bodyModel.setGender(object.getInt("Gender"));
                                bodyModel.setProfileIdentity(object.getString("ProfileIdentity"));
                                bodyModel.setDateOfBirth(object.getString("DateOfBirth"));
                                bodyModel.setMobileNo1(object.getString("MobileNo1"));
                                bodyModel.setMobileNo2(object.getString("MobileNo2"));
                                bodyModel.setFromDate(object.getString("FromDate"));
                                bodyModel.setToDate(object.getString("ToDate"));
                                bodyModel.setLogoImage(object.getString("LogoImage"));
                                bodyModel.setIsContactPerson(object.getBoolean("IsContactPerson"));
                                bodyModel.setType(ActionTypes.TYPE_MANAGEMENT_BODY);
                                managementBodyModels.add(bodyModel);
                            }
                        }
                        intent = new Intent(context, ManagementBodyActivity.class);
                        intent.putExtra("management", managementBodyModels);
                        intent.putExtra("library", (SmartLibraryResponseModel)map.get("library"));
                        break;
                    case "http://tantraved.in/GetSocialProjects":
                        socialProjectModels = new ArrayList<>();
                        for(int i = 0; i < jsonArray.length(); i++){
                            SocialProjectModel socialProjectModel = new SocialProjectModel();
                            object = jsonArray.getJSONObject(i);
                            if(i == 1){
                                model = (SmartLibraryResponseModel) map.get("library");
                                object = jsonArray.getJSONObject(i);
                                model.setFinancialYear(object.getString("Financial Year"));
                            }
                            if(i > 1) {
                                socialProjectModel.setSrNo(object.getInt("SrNo"));
                                socialProjectModel.setLibraryId(object.getInt("LibraryId"));
                                socialProjectModel.setProjectTitle(object.getString("ProjectTitle"));
                                socialProjectModel.setProjectDate(object.getString("ProjectDate"));
                                socialProjectModel.setShortDesc(object.getString("ShortDesc"));
                                socialProjectModel.setLongDesc(object.getString("LongDesc"));
                                socialProjectModel.setPhotoImage(object.getString("PhotoImage"));
                                socialProjectModel.setShowPromo(object.getBoolean("ShowPromo"));
                                socialProjectModel.setCreatedBy(object.getString("CreatedBy"));
                                socialProjectModel.setCreatedDate(object.getString("CreatedDate"));
                                socialProjectModel.setLastModifiedBy(object.getString("LastModifiedBy"));
                                socialProjectModel.setLastModifiedDate(object.getString("LastModifiedDate"));
                                socialProjectModel.setIsActive(object.getBoolean("IsActive"));
                                socialProjectModel.setType(ActionTypes.TYPE_SOCIAL_PROJECTS);
                                socialProjectModels.add(socialProjectModel);
                            }
                        }
                        intent = new Intent(context, SocialProjectActivity.class);
                        intent.putExtra("social", socialProjectModels);
                        intent.putExtra("library", (SmartLibraryResponseModel)map.get("library"));
                        break;
                    case "http://tantraved.in/GetComplements":
                        complementModels = new ArrayList<>();
                        for(int i = 0; i < jsonArray.length(); i++){
                            ComplementModel complementModel = new ComplementModel();
                            object = jsonArray.getJSONObject(i);
                            if(i == 1){
                                model = (SmartLibraryResponseModel) map.get("library");
                                object = jsonArray.getJSONObject(i);
                                model.setFinancialYear(object.getString("Financial Year"));
                            }
                            if(i > 1) {
                                complementModel.setSrNo(object.getInt("SrNo"));
                                complementModel.setLibraryId(object.getInt("LibraryId"));
                                complementModel.setComplementDate(object.getString("ComplementDate"));
                                complementModel.setComplementsBy(object.getString("ComplementsBy"));
                                complementModel.setAboutThePerson(object.getString("AboutThePerson"));
                                complementModel.setDetails(object.getString("Details"));
                                complementModel.setScannedCopy(object.getString("ScannedCopy"));
                                complementModel.setDescription(object.getString("Description"));
                                complementModel.setCreatedBy(object.getString("CreatedBy"));
                                complementModel.setCreatedDate(object.getString("CreatedDate"));
                                complementModel.setLastModifiedBy(object.getString("LastModifiedBy"));
                                complementModel.setLastModifiedDate(object.getString("LastModifiedDate"));
                                complementModel.setType(ActionTypes.TYPE_COMPLEMENTS);
                                complementModels.add(complementModel);
                            }
                        }
                        intent = new Intent(context, ComplementsActivity.class);
                        intent.putExtra("complements", complementModels);
                        intent.putExtra("library", (SmartLibraryResponseModel) map.get("library"));
                        break;
                    case "http://tantraved.in/GetNewBooks":
                        bookModels = new ArrayList<>();
                        for(int i = 0; i < jsonArray.length(); i++){
                            BookModel bookModel = new BookModel();
                            object = jsonArray.getJSONObject(i);
                            if(i == 1){
                                model = (SmartLibraryResponseModel) map.get("library");
                                object = jsonArray.getJSONObject(i);
                                model.setFinancialYear(object.getString("Financial Year"));
                            }
                            if(i > 1) {
                                bookModel.setLibraryId(object.getInt("LibraryId"));
                                bookModel.setAuthor(object.getString("Author"));
                                bookModel.setBookImage(object.getString("BookImage"));
                                bookModel.setBookType(object.getString("BookType"));
                                bookModel.setBookInwardNo(object.getString("BookInwardNo"));
                                bookModel.setBookNo(object.getString("BookNo"));
                                bookModel.setBookTitle(object.getString("BookTitle"));
                                bookModel.setPublisherName(object.getString("PublisherName"));
                                bookModel.setType(ActionTypes.TYPE_NEW_BOOKS);
                                bookModels.add(bookModel);
                            }
                        }
                        intent = new Intent(context, NewBooksActivity.class);
                        intent.putExtra("library", (SmartLibraryResponseModel) map.get("library"));
                        intent.putExtra("books", bookModels);
                        break;
                    case "http://tantraved.in/GetRareBooks":
                        bookModels = new ArrayList<>();
                        for(int i = 0; i < jsonArray.length(); i++){
                            BookModel bookModel = new BookModel();
                            object = jsonArray.getJSONObject(i);
                            if(i == 1){
                                model = (SmartLibraryResponseModel) map.get("library");
                                object = jsonArray.getJSONObject(i);
                                model.setFinancialYear(object.getString("Financial Year"));
                            }
                            if(i > 1) {
                                bookModel.setLibraryId(object.getInt("LibraryId"));
                                bookModel.setAuthor(object.getString("Author"));
                                bookModel.setBookImage(object.getString("BookImage"));
                                bookModel.setBookType(object.getString("BookType"));
                                bookModel.setBookInwardNo(object.getString("BookInwardNo"));
                                bookModel.setBookNo(object.getString("BookNo"));
                                bookModel.setBookTitle(object.getString("BookTitle"));
                                bookModel.setPublisherName(object.getString("PublisherName"));
                                bookModel.setType(ActionTypes.TYPE_NEW_BOOKS);
                                bookModels.add(bookModel);
                            }
                        }
                        intent = new Intent(context, NewBooksActivity.class);
                        intent.putExtra("library", (SmartLibraryResponseModel) map.get("library"));
                        intent.putExtra("books", bookModels);
                        break;
                    case "http://tantraved.in/GetFinancialYear":
                        for(int i = 0; i < jsonArray.length(); i++){
                            model = (SmartLibraryResponseModel) map.get("library");
                            object = jsonArray.getJSONObject(i);
                            model.setFinancialYearId(object.getInt("SrNo"));
                            model.setFomYear(object.getString("FromYear"));
                            model.setFinancialYear(object.getString("Financial Year"));
                        }
                        if(map.get("requiredAction").toString().equalsIgnoreCase(context.getString(R.string.type_wise_reading))){
                            intent = new Intent(context, TypeWiseCountActivity.class);
                            intent.putExtra("library", model);
                        }else if(map.get("requiredAction").toString().equalsIgnoreCase(context.getString(R.string.top_10_readers))){
                            intent = new Intent(context, TopReadersActivity.class);
                            intent.putExtra("library", model);
                        }else if(map.get("requiredAction").toString().equalsIgnoreCase(context.getString(R.string.popular_10_books))){
                            intent = new Intent(context, ReadersChoiceActivity.class);
                            intent.putExtra("library", model);
                        }else {
                            intent = new Intent(context, MyMembershipActivity.class);
                            intent.putExtra("library", model);
                        }
                        break;
                    case "http://tantraved.in/GetBookStatistics":
                        bookModels = new ArrayList<>();
                        for(int i = 0; i < jsonArray.length(); i++){
                            BookModel bookModel = new BookModel();
                            object = jsonArray.getJSONObject(i);
                            if(i == 1){
                                model = (SmartLibraryResponseModel) map.get("library");
                                object = jsonArray.getJSONObject(i);
                                model.setFinancialYear(object.getString("Financial Year"));
                            }
                            if(i > 1) {
                                bookModel.setBookType(object.getString("BookType"));
                                bookModel.setBookCount(object.getInt("BookCount"));
                                bookModel.setType(ActionTypes.TYPE_GRANTHSAMPADA);
                                bookModels.add(bookModel);
                            }
                        }
                        intent = new Intent(context, GranthSampadaActivity.class);
                        intent.putExtra("library", (SmartLibraryResponseModel) map.get("library"));
                        intent.putExtra("statistics", bookModels);
                        break;
                    case "http://tantraved.in/GetReadersChoice":
                        bookModels = new ArrayList<>();
                        for(int i = 0; i < jsonArray.length(); i++){
                            BookModel bookModel = new BookModel();
                            object = jsonArray.getJSONObject(i);
                            if(i == 1){
                                model = (SmartLibraryResponseModel) map.get("library");
                                object = jsonArray.getJSONObject(i);
                                model.setFinancialYear(object.getString("Financial Year"));
                            }
                            if(i > 1){

                                bookModel.setBookTitle(object.getString("BookTitle"));
                                bookModel.setBookInwardNo(object.getString("BookInwardNo"));
                                bookModel.setBookNo(object.getString("BookNo"));
                                bookModel.setBookImage(object.getString("BookImage"));
                                bookModel.setBookType(object.getString("BookType"));
                                bookModel.setAuthor(object.getString("Author"));
                                bookModel.setBookCount(object.getInt("BookCount"));
                                bookModel.setType(ActionTypes.TYPE_READERS_CHOICE);
                                bookModels.add(bookModel);
                            }
                        }
                        intent = new Intent(context, ReadersChoiceActivity.class);
                        intent.putExtra("library", (SmartLibraryResponseModel) map.get("library"));
                        intent.putExtra("books", bookModels);
                        break;
                    case "http://tantraved.in/SearchBooksWithLibraryDetails":
                        bookModels = new ArrayList<>();
                        SmartLibraryResponseModel libraryResponseModel = new SmartLibraryResponseModel();
                        libraryResponseModel.setSrNo(jsonArray.getJSONObject(0).getInt("SrNo"));
                        libraryResponseModel.setLibraryId(jsonArray.getJSONObject(0).getInt("LibraryId"));
                        libraryResponseModel.setLogoImage("http://www.tantraved.in/CP/Uploads/LibraryInfo/" + jsonArray.getJSONObject(0).getString("LogoImage"));
                        libraryResponseModel.setLibraryName(jsonArray.getJSONObject(0).getString("LibraryName"));
                        libraryResponseModel.setAddress1(jsonArray.getJSONObject(0).getString("Address1"));
                        libraryResponseModel.setAddress2(jsonArray.getJSONObject(0).getString("Address2"));
                        libraryResponseModel.setDatabaseName(jsonArray.getJSONObject(0).getString("DatabaseName"));
                        libraryResponseModel.setM_CityName(jsonArray.getJSONObject(0).getString("M_CityName"));
                        libraryResponseModel.setPIN(jsonArray.getJSONObject(0).getString("PIN"));
                        libraryResponseModel.setPhoneNo1(jsonArray.getJSONObject(0).getString("PhoneNo1"));
                        for(int i = 0; i < jsonArray.length(); i++){
                            BookModel bookModel = new BookModel();
                            object = jsonArray.getJSONObject(i);
                            if(i == 1){
                                object = jsonArray.getJSONObject(i);
                                libraryResponseModel.setFinancialYear(object.getString("Financial Year"));
                            }
                            if(i > 1) {
                                bookModel.setLibraryId(object.getInt("LibraryId"));
                                bookModel.setAuthor(object.getString("Author"));
                                bookModel.setBookImage(object.getString("BookImage"));
                                bookModel.setBookType(object.getString("BookType"));
                                bookModel.setBookInwardNo(object.getString("BookInwardNo"));
                                bookModel.setBookNo(object.getString("BookNo"));
                                bookModel.setBookTitle(object.getString("BookTitle"));
                                bookModel.setPublisherName(object.getString("PublisherName"));
                                bookModel.setType(ActionTypes.TYPE_NEW_BOOKS);
                                bookModels.add(bookModel);
                            }
                        }
                        intent = new Intent(context, BookResultActivity.class);
                        intent.putExtra("library", libraryResponseModel);
                        intent.putExtra("books", bookModels);
                        break;
                    case "http://tantraved.in/BookTypewiseReadingCount":
                        bookModels = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            BookModel bookModel = new BookModel();
                            object = jsonArray.getJSONObject(i);

                            if( i > 1){
                                bookModel.setType(ActionTypes.TYPE_WISE_COUNT);
                                bookModel.setBookType(object.getString("BookType"));
                                bookModel.setBookCount(object.getInt("BookCount"));
                                bookModels.add(bookModel);
                            }
                        }
                        intent = new Intent(context, TypeWiseCountActivity.class);
                        intent.putExtra("library", (SmartLibraryResponseModel) map.get("library"));
                        intent.putExtra("types", bookModels);
                        break;
                    case "http://tantraved.in/GetTop10Readers":
                        bookModels = new ArrayList<>();
                        for(int i = 0; i < jsonArray.length(); i++){
                            BookModel bookModel = new BookModel();
                            object = jsonArray.getJSONObject(i);
                            if(i == 1){
                                model = (SmartLibraryResponseModel) map.get("library");
                                object = jsonArray.getJSONObject(i);
                                model.setFinancialYear(object.getString("Financial Year"));
                            }
                            if(i > 1){
                                bookModel.setMemberId(object.getInt("MemberId"));
                                bookModel.setMember(object.getString("Member"));
                                bookModel.setMemberImage(object.getString("MemberImage"));
                                bookModel.setUserName(object.getString("UserName"));
                                bookModel.setBookCount(object.getInt("BookCount"));
                                bookModel.setType(ActionTypes.TOP_10_READERS);
                                bookModels.add(bookModel);
                            }
                        }
                        intent = new Intent(context, TopReadersActivity.class);
                        intent.putExtra("library", (SmartLibraryResponseModel) map.get("library"));
                        intent.putExtra("readers", bookModels);
                        break;
                    case "http://tantraved.in/GetMembershipPlans":
                        membershipModels = new ArrayList<>();
                        for(int i = 0; i < jsonArray.length(); i++){
                            if( i == 1){
                                model = (SmartLibraryResponseModel) map.get("library");
                                object = jsonArray.getJSONObject(i);
                                model.setFinancialYear(object.getString("Financial Year"));
                            }
                            if(i > 1){
                                MembershipModel membershipModel = new MembershipModel();
                                object = jsonArray.getJSONObject(i);
                                membershipModel.setMembershipPlan(object.getString("MembershipPlan"));
                                membershipModel.setMaxBooks(object.getInt("MaxBooks"));
                                membershipModel.setMaxMagazines(object.getInt("MaxMagazines"));
                                membershipModel.setDuration(object.getInt("Duration"));
                                membershipModel.setType(ActionTypes.MEMBERSHIP_PLANS);
                                membershipModels.add(membershipModel);
                            }
                        }
                        intent = new Intent(context, MembershipPlansActivity.class);
                        intent.putExtra("plans", membershipModels);
                        intent.putExtra("library", (SmartLibraryResponseModel) map.get("library"));
                        break;

                    case "http://tantraved.in/GetMemberStatistics":
                        membershipModels = new ArrayList<>();
                        for(int i = 0; i < jsonArray.length(); i++){
                            if( i == 1){
                                model = (SmartLibraryResponseModel) map.get("library");
                                object = jsonArray.getJSONObject(i);
                                model.setFinancialYear(object.getString("Financial Year"));
                            }
                            if(i > 1){
                                MembershipModel membershipModel = new MembershipModel();
                                object = jsonArray.getJSONObject(i);
                                membershipModel.setMembershipPlan(object.getString("MembershipPlan"));
                                membershipModel.setMeberCount(object.getInt("MemberCount"));
                                membershipModel.setType(ActionTypes.MEMBER_STATISTICS);
                                membershipModels.add(membershipModel);
                            }
                        }
                        intent = new Intent(context, MemberActivity.class);
                        intent.putExtra("memberStatistics", membershipModels);
                        intent.putExtra("library", (SmartLibraryResponseModel) map.get("library"));
                        break;

                    case "http://tantraved.in/GetBookTypewisePurchase":
                        bookModels = new ArrayList<>();
                        for(int i = 0; i < jsonArray.length(); i++){
                            BookModel bookModel = new BookModel();
                            object = jsonArray.getJSONObject(i);
                            if(i == 1){
                                model = (SmartLibraryResponseModel) map.get("library");
                                object = jsonArray.getJSONObject(i);
                                model.setFinancialYear(object.getString("Financial Year"));
                            }
                            if(i > 1){
                                bookModel.setTotalAmount(object.getInt("TotalAmount"));
                                bookModel.setBookType(object.getString("BookType"));
                                bookModel.setBookCount(object.getInt("TotalBooks"));
                                bookModel.setType(ActionTypes.TYPE_WISE_PURCHASE);
                                bookModels.add(bookModel);
                            }
                        }
                        intent = new Intent(context, TypeWisePurchaseActivity.class);
                        intent.putExtra("library", (SmartLibraryResponseModel) map.get("library"));
                        intent.putExtra("purchase", bookModels);
                        break;
                }
                if (intent != null) {
                    intent.putExtra("list", libraryResponseModels);
                    context.startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if(code == 200) {
            Toast.makeText(context, context.getString(R.string.nothing_to_show), Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(context, context.getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
        }
    }
}