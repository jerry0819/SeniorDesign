package Controller;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Models.SimpleFood;
import applicationname.companydomain.finalproject1.SearchFoodActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchFoodController {
    private List<String> fl;
    private List<String> uriList;
    private SearchFoodActivity sfa;
    private List<String> list = new ArrayList<>();
    private List<String> foods =  new ArrayList<>();
    private ArrayList<String> measurements;
    public ArrayList<ArrayList<String>> measurementsMatrix;
    public ArrayList<ArrayList<String>> measurementsURIMatrix;
    private ArrayList<String> measurementsURI;
    public String foodName;
    public SearchFoodController(Context context){
        fl = new ArrayList<>();
        measurementsMatrix=new ArrayList<>();
        measurementsURIMatrix=new ArrayList<>();
        uriList = new ArrayList<>();
        sfa = (SearchFoodActivity) context;
    }

    public void searchFoods(String s){

        List<SimpleFood> foodList = new ArrayList<>();
        findFoods(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String jsonData = response.body().string();
                    JSONObject returnJSON = new JSONObject(jsonData);
                    JSONArray hints = (JSONArray) returnJSON.get("hints");
                    for(int x=0; x<hints.length(); x++) {
                        measurements = new ArrayList<>();
                        measurementsURI = new ArrayList<>();
                        JSONObject food1 = hints.getJSONObject(x);
                        JSONObject morefood = food1.getJSONObject("food");
                        String food = morefood.getString("label");
                        String foodURI = morefood.getString("foodId");
                        JSONObject nutrients = morefood.getJSONObject("nutrients");
                        int calories = nutrients.getInt("ENERC_KCAL");
                        JSONArray measures = food1.getJSONArray("measures");
                        for (int i = 0; i < measures.length(); i++) {
                            JSONObject jsonMeasures = measures.getJSONObject(i);
                            measurements.add(jsonMeasures.getString("label"));
                            measurementsURI.add(jsonMeasures.getString("uri"));
                        }
                        SimpleFood sf = new SimpleFood(food, foodURI, calories, measurements, measurementsURI);
                        foodList.add(sf);
                    }
                    sfa.display(foodList, sfa);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("TAG", "onResponse: " + e.getMessage() );
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("TAG", "onResponse: " + e.getMessage() );
                }
            }

        }, s);
    }




    public void findFoods(Callback callback, String s) {

        String APP_KEY = "54d1e8339e20e21b6a8e69cd0ecded66";
        String APP_ID = "20f18e21";
        OkHttpClient client = new OkHttpClient.Builder().build();
        String ingredients = ("" + "," + "").replaceAll("\\s", "");
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.edamam.com/api/food-database/parser").newBuilder();
        urlBuilder.addQueryParameter("ingr", s);
        urlBuilder.addQueryParameter("app_id", APP_ID);
        urlBuilder.addQueryParameter("app_key", APP_KEY);
        String url = urlBuilder.build().toString();
        Request request = new Request.Builder().url(url).build();
        Log.e("URL:", url);
        Call call = client.newCall(request);
        call.enqueue(callback);
    }
    public String getURI(int position){
        return uriList.get(position);
    }
}
