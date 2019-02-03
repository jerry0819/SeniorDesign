package Controller;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    public SearchFoodController(Context context){
        fl = new ArrayList<>();
        measurementsMatrix=new ArrayList<>();
        measurementsURIMatrix=new ArrayList<>();
        uriList = new ArrayList<>();
        sfa = (SearchFoodActivity) context;
    }

    public List<String> searchFoods(String s){
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
                    for(int i =0; i<10; i++) {
                        measurements = new ArrayList<>();
                        measurementsURI = new ArrayList<>();
                        JSONArray hints = (JSONArray) returnJSON.get("hints");
                        String foodName = returnJSON.getString("text");
                        JSONObject food1 = hints.getJSONObject(i);
                        JSONObject morefood = food1.getJSONObject("food");
                        String food = morefood.getString("label");
                        String foodURI = morefood.getString("uri");
                        foods.add(returnJSON.getString("text"));
                        JSONArray measures = food1.getJSONArray("measures");
                        for (int x = 0; x < measures.length(); x++) {
                            JSONObject jsonMeasures = measures.getJSONObject(x);
                            measurements.add(jsonMeasures.getString("label"));
                            measurementsURI.add(jsonMeasures.getString("uri"));
                        }
                        measurementsMatrix.add(measurements);
                        measurementsURIMatrix.add(measurementsURI);
                        //TODO: get brand names working, currently breaks out of try catch if no brand
                        //if(morefood.getString("brand")!=null)
                        //{
                        //if(foodName.toLowerCase().equals(morefood.getString("brand").toLowerCase()))
                        //{
                        //   Log.e(TAG, "FOUND ONE MY DUDE" + foodName );
                        //}
                        //}
                        uriList.add(foodURI);
                        list.add(food);
                    }
                    sfa.display(list, sfa);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, s);
        return null;
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
