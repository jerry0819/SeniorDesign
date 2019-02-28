package Controller;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.json.Json;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mongodb.DBObject;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;

import org.bson.BsonType;
import org.bson.Document;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.mongodb.client.model.Projections;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Models.Food;
import applicationname.companydomain.finalproject1.UserFoodInfoActivity;

public class UserFoodInfoController {
    private  List<Food> userFoodList = new ArrayList<>();
    private StitchAppClient client;
    private final RemoteMongoClient mongoClient;
    private final RemoteMongoCollection<Document> coll;
    private UserFoodInfoActivity ufia;
    public UserFoodInfoController(UserFoodInfoActivity ufia, String key) {
        this.ufia = ufia;
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
        mongoClient = client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
        coll = mongoClient.getDatabase("photoFoodDB").getCollection("photoFoodColl");
        getFoodInfo(key);
    }

    private void getFoodInfo(String key) {
        Document filterDoc = new Document()
                .append("owner_id", new Document().append("$eq", key));

        final Task<Document> findTask = coll.find(filterDoc).projection(fields(include("nutrientsInfoList"), excludeId())).first();
        findTask.addOnCompleteListener(new OnCompleteListener<Document>() {
            @Override
            public void onComplete(@NonNull Task <Document> task) {
                if (task.isSuccessful()) {
                    if (task.getResult() == null) {
                        Log.d("app", "Could not find any matching documents");
                    } else {
                        Log.d("app", String.format("successfully found document: %s",
                                task.getResult().toJson()));
                        try {
                            JSONObject jo = new JSONObject(task.getResult().toJson());
                            JSONArray ja = jo.getJSONArray("nutrientsInfoList");
                            Gson gson = new Gson();
                            Log.d("app", String.format("successfully found document: %s", ja));
                            List<Food> lf = gson.fromJson(ja.toString(), new TypeToken<List<Food>>(){}.getType());
                            int monTotal=0;
                            int tuesTotal=0;
                            int wedTotal=0;
                            int thursTotal=0;
                            int friTotal=0;
                            int satTotal=0;
                            int sunTotal=0;

                            long prevDay = System.currentTimeMillis() - 1000*60*60*24*7;
                            Date prev = new Date(prevDay);
                            Log.e("TAG", "onComplete: " + lf.size());
                            for (Food f:lf) {
                                Date date = new SimpleDateFormat( "EEE MMM dd HH:mm:ss z yyyy").parse(f.date);
                                if(date.before(prev)){
                                } else {
                                    if(date.toString().contains("Mon")){
                                        System.out.println("The date is older than current day");
                                        monTotal += f.calories;
                                        Log.e("TAG", "onComplete: " + f.name);
                                    } else if(date.toString().contains("Tue")){
                                        System.out.println("The date is older than current day");
                                        tuesTotal += f.calories;
                                        Log.e("TAG", "onComplete: " + f.name);
                                    }else if(date.toString().contains("Wed")){
                                        System.out.println("The date is older than current day");
                                        wedTotal += f.calories;
                                        Log.e("TAG", "onComplete: " + f.name);
                                    }else if(date.toString().contains("Thu")){
                                        System.out.println("The date is older than current day");
                                        thursTotal += f.calories;
                                        Log.e("TAG", "onComplete: " + f.name);
                                    }else if(date.toString().contains("Fri")){
                                        System.out.println("The date is older than current day");
                                        friTotal += f.calories;
                                        Log.e("TAG", "onComplete: " + f.name);
                                    }else if(date.toString().contains("Sat")){
                                        System.out.println("The date is older than current day");
                                        satTotal += f.calories;
                                        Log.e("TAG", "onComplete: " + f.name);
                                    }else if(date.toString().contains("Sun")){
                                        System.out.println("The date is older than current day");
                                        sunTotal += f.calories;
                                        Log.e("TAG", "onComplete: " + f.name);
                                    }
                                }

                            }
                            ufia.showCalories(monTotal, tuesTotal, wedTotal, thursTotal, friTotal, satTotal, sunTotal);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("TAG", "onComplete: " + e.toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                            Log.e("TAG", "onComplete: " + e.toString());
                        }
                    }

                } else {
                    Log.e("app", "failed to find document with: ", task.getException());
                }
            }
        });
    }
}
