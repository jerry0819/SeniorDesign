package Controller;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.json.Json;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.lang.NonNull;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateResult;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Models.Food;
import Util.SharedPreferenceHelper;
import applicationname.companydomain.finalproject1.NutrientsActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NutrientsController {
    private Context mContext;
    private List<String> nuts;
    private String TAG = "hey";
    private NutrientsActivity mNutrientsActivity;
    private SharedPreferenceHelper mSharedPreferenceHelper;
    private StitchAppClient client;
    private final RemoteMongoClient mongoClient;
    private final RemoteMongoCollection<Document> coll;
    private List<String> measurements;
    private List<String> measurementsURI;
    private Food f;
    public NutrientsController(Context context,NutrientsActivity sna, List<String> measurements, List<String> measurementsURI) {
        this.measurements = measurements;
        this.measurementsURI = measurementsURI;
        mContext = context;
        nuts = new ArrayList<>();
        mNutrientsActivity = sna;
        mSharedPreferenceHelper = new SharedPreferenceHelper(context);
        client = null;
        try {
            client = Stitch.initializeDefaultAppClient("clustertest-sfown");
        }
        catch(Exception e){

        }
        try {
            client = Stitch.getDefaultAppClient();
        }
        catch(Exception e){

        }
        mSharedPreferenceHelper = new SharedPreferenceHelper(context);
        mongoClient = client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
        coll = mongoClient.getDatabase("photoFoodDB").getCollection("photoFoodColl");
    }

    public void getNutrients(String URI, int position) throws JSONException {
        findNutrients(URI,position, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                f = new Food();
                Calendar cal = Calendar. getInstance();
                f.date = cal.getTime();
                JSONObject nutrients = null;
                JSONObject j = null;
                Log.e(TAG, "onNotResponse: " + jsonData);

                //TODO change to if statements.
                try {
                    JSONObject returnJSON = new JSONObject(jsonData);
                    nutrients = returnJSON.getJSONObject("totalNutrients");
                    j = nutrients.getJSONObject("ENERC_KCAL");
                    f.calories = j.getDouble("quantity");
                }catch(Exception e) {
                }
                try {
                    j = nutrients.getJSONObject("FAT");
                    f.fat= j.getDouble("quantity");
                }catch(Exception e) {
                }
                try {
                    j = nutrients.getJSONObject("FASAT");
                    f.saturated_fat = j.getDouble("quantity");
                }catch(Exception e) {
                }
                try {
                    j = nutrients.getJSONObject("FATRN");
                    f.trans_fat = j.getDouble("quantity");
                }catch(Exception e) {
                }
                try {
                    j = nutrients.getJSONObject("FAMS");
                    f.monounsaturated_fat= j.getDouble("quantity");
                }catch(Exception e) {
                }
                try {
                    j = nutrients.getJSONObject("FAPU");
                    f.polyunsaturated_fat= j.getDouble("quantity");
                }catch(Exception e) {
                }
                try {
                    j = nutrients.getJSONObject("CHOCDF");
                    f.carbs= j.getDouble("quantity");
                }catch(Exception e) {
                }
                try {
                    j = nutrients.getJSONObject("FIBTG");
                    f.fiber= j.getDouble("quantity");
                }catch(Exception e) {
                }
                try {
                    j = nutrients.getJSONObject("SUGAR");
                    f.sugars = j.getDouble("quantity");
                }catch(Exception e) {
                }
                try {
                    j = nutrients.getJSONObject("SUGAR.added");
                    f.added_sugars= j.getDouble("quantity");
                }catch(Exception e) {
                }
                try {
                    j = nutrients.getJSONObject("PROCNT");
                    f.protein= j.getDouble("quantity");
                }catch(Exception e) {
                }
                try {
                    j = nutrients.getJSONObject("CHOLE");
                    f.cholesterol= j.getDouble("quantity");
                }catch(Exception e) {
                }
                try {
                    j = nutrients.getJSONObject("NA");
                    f.sodium= j.getDouble("quantity");
                }catch(Exception e) {
                }
                try {
                    j = nutrients.getJSONObject("CA");
                    f.calcium= j.getDouble("quantity");
                }catch(Exception e) {
                }
                try {
                    j = nutrients.getJSONObject("MG");
                    f.magnesium= j.getDouble("quantity");
                }catch(Exception e) {
                }
                try {
                    j = nutrients.getJSONObject("K");
                    f.potassium= j.getDouble("quantity");
                }catch(Exception e) {
                }
                try {
                    j = nutrients.getJSONObject("FE");
                    f.iron= j.getDouble("quantity");
                }catch(Exception e) {
                }
                try {
                    j = nutrients.getJSONObject("ZN");
                    f.zinc= j.getDouble("quantity");
                }catch(Exception e) {
                }
                try {
                    j = nutrients.getJSONObject("P");
                    f.phosphorus= j.getDouble("quantity");
                }catch(Exception e) {
                }
                try {
                    j = nutrients.getJSONObject("VITA_RAE");
                    f.vitamin_A= j.getDouble("quantity");
                }catch(Exception e) {
                }
                try {
                    j = nutrients.getJSONObject("VITC");
                    f.vitamin_C= j.getDouble("quantity");
                }catch(Exception e) {
                }
                try {
                    j = nutrients.getJSONObject("THIA");
                    f.thiamin= j.getDouble("quantity");
                }catch(Exception e) {
                }
                try {
                    j = nutrients.getJSONObject("RIBF");
                    f.riboflavin= j.getDouble("quantity");
                }catch(Exception e) {
                }
                try {
                    j = nutrients.getJSONObject("NIA");
                    f.niacin= j.getDouble("quantity");
                }catch(Exception e) {
                }
                try {
                    j = nutrients.getJSONObject("VITB6A");
                    f.vitamin_B6= j.getDouble("quantity");
                }catch(Exception e) {
                }
                try {
                    j = nutrients.getJSONObject("FOLDFE");
                    f.folate_equivalent= j.getDouble("quantity");
                }catch(Exception e) {
                }
                try {
                    j = nutrients.getJSONObject("FOLFD");
                    f.folate= j.getDouble("quantity");
                }catch(Exception e) {
                }
                try {
                    j = nutrients.getJSONObject("FOLAC");
                    f.folic_acid= j.getDouble("quantity");
                }catch(Exception e) {
                }
                try {
                    j = nutrients.getJSONObject("VITB12");
                    f.vitamin_B12= j.getDouble("quantity");
                }catch(Exception e) {
                }
                try {
                    j = nutrients.getJSONObject("VITD");
                    f.vitamin_D= j.getDouble("quantity");
                }catch(Exception e) {
                }
                try {
                    j = nutrients.getJSONObject("TOCPHA");
                    f.vitamin_E= j.getDouble("quantity");
                }catch(Exception e) {
                }
                try {
                    j = nutrients.getJSONObject("VITK1");
                    f.vitamin_K= j.getDouble("quantity");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                nuts.add("calories per: " + f.calories);
                nuts.add("carbs per: " + f.carbs);
                nuts.add("sugars per: " + f.sugars);
                mNutrientsActivity.showNutrients(nuts, mNutrientsActivity, f);
                Log.e(TAG, "onResponse: " + f.calories );
            }
        });
    }


    public void findNutrients(String foodURI,int position, Callback callback) throws JSONException {
        OkHttpClient client = new OkHttpClient.Builder()
                .build();
        JSONObject obj = new JSONObject()
                .put("quantity", 1)
                .put("measureURI",measurementsURI.get(position))
                .put("foodURI", foodURI);
        JSONArray ja = new JSONArray()
                .put(obj);
        String json = new JSONObject()
                .put("ingredients", ja).toString();
        Log.e(TAG, "findNutrients: " + json );
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.edamam.com/api/food-database/nutrients").newBuilder();
        urlBuilder.addQueryParameter("app_id","7162d658");
        urlBuilder.addQueryParameter("app_key","283c09f15ef61bca03b9d9d1c444f0b0");
        String url = urlBuilder.build().toString();
        Log.e(TAG,url);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public void addNutrients(Food f)
    {
        Document filterDoc = new Document().append("owner_id", mSharedPreferenceHelper.getCode());
        Document updateDoc = new Document().append("$push",
                new Document().append("nutrientsInfoList", new Document()
                        .append("name", f.name)
                        .append("date", f.date)
                        .append("calories", f.calories)
                        .append("added_sugars", f.added_sugars)
                        .append("calcium", f.calcium)
                        .append("carbs", f.carbs)
                        .append("cholesterol", f.cholesterol)
                        .append("fat", f.fat)
                        .append("fiber", f.fiber)
                        .append("folate", f.folate)
                        .append("folate_equivalent", f.folate_equivalent)
                        .append("folic_acid", f.folic_acid)
                        .append("iron", f.iron)
                        .append("magnesium", f.magnesium)
                        .append("measure", f.measure)
                        .append("monounsaturated_fat", f.monounsaturated_fat)
                        .append("niacin", f.niacin)
                        .append("phosphorus", f.phosphorus)
                        .append("polyunsaturated_fat", f.polyunsaturated_fat)
                        .append("potassium", f.potassium)
                        .append("protein", f.protein)
                        .append("riboflavin", f.riboflavin)
                        .append("saturated_fat", f.saturated_fat)
                        .append("sodium", f.sodium)
                        .append("sugars", f.sugars)
                        .append("thiamin", f.thiamin)
                        .append("trans_fat", f.trans_fat)
                        .append("URI", f.URI)
                        .append("vitamin_A", f.vitamin_A)
                        .append("vitamin_B6", f.vitamin_B6)
                        .append("vitamin_C", f.vitamin_C)
                        .append("vitamin_B12", f.vitamin_B12)
                        .append("vitamin_D", f.vitamin_D)
                        .append("vitamin_E", f.vitamin_E)
                        .append("vitamin_K", f.vitamin_K)
                        .append("zinc", f.zinc)
                )
        );


        final Task <RemoteUpdateResult> updateTask =
                coll.updateOne(filterDoc, updateDoc);
        updateTask.addOnCompleteListener(new OnCompleteListener <RemoteUpdateResult> () {
            @Override
            public void onComplete(@NonNull Task <RemoteUpdateResult> task) {
                if (task.isSuccessful()) {
                    long numMatched = task.getResult().getMatchedCount();
                    long numModified = task.getResult().getModifiedCount();
                    Log.d("app", String.format("successfully matched %d and modified %d documents",
                            numMatched, numModified));
                } else {
                    Log.e("app", "failed to update document with: ", task.getException());
                }
            }
        });
    }

}

