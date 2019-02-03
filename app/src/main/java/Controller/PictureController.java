package Controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import applicationname.companydomain.finalproject1.PictureActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PictureController {
    //private final SharedPreferenceHelper mSharedPreferenceHelper;

    private static final String CLOUD_VISION_API_KEY = "AIzaSyB6E94oGWkqs4xouI2Jfr_BtJ2sngZthwg";
    private ArrayList<String> measurements;
    public ArrayList<ArrayList<String>> measurementsMatrix;
    private Context mContext;
    private static final String TAG = "HttpURLGET";
    private List<String> list = new ArrayList<>();
    private List<String> foods =  new ArrayList<>();
    private List<String> data;
    private List<String> URIlist = new ArrayList<>();
    public ArrayList<ArrayList<String>> measurementsURIMatrix;
    private ArrayList<String> measurementsURI;
    PictureActivity pa;
    public PictureController(Context context, Bitmap bitmap, Feature feature,PictureActivity pa) {
        this.pa = pa;
        measurementsMatrix=new ArrayList<>();
        measurementsURIMatrix=new ArrayList<>();
        callCloudVision(bitmap, feature);

    }



    public void callCloudVision(final Bitmap bitmap, final Feature feature) {

        final List<Feature> featureList = new ArrayList<>();
        featureList.add(feature);

        final List<AnnotateImageRequest> annotateImageRequests = new ArrayList<>();

        AnnotateImageRequest annotateImageReq = new AnnotateImageRequest();
        annotateImageReq.setFeatures(featureList);
        annotateImageReq.setImage(getImageEncodeImage(bitmap));
        annotateImageRequests.add(annotateImageReq);


        new AsyncTask<Object, Void, String>() {
            @Override
            protected String doInBackground(Object... params) {
                try {

                    HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
                    JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

                    VisionRequestInitializer requestInitializer = new VisionRequestInitializer(CLOUD_VISION_API_KEY);

                    Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
                    builder.setVisionRequestInitializer(requestInitializer);

                    Vision vision = builder.build();

                    BatchAnnotateImagesRequest batchAnnotateImagesRequest = new BatchAnnotateImagesRequest();
                    batchAnnotateImagesRequest.setRequests(annotateImageRequests);

                    Vision.Images.Annotate annotateRequest = vision.images().annotate(batchAnnotateImagesRequest);
                    annotateRequest.setDisableGZipContent(true);
                    BatchAnnotateImagesResponse response = annotateRequest.execute();
                    return convertResponseToString(response);
                } catch (GoogleJsonResponseException e) {
                } catch (IOException e) {
                }
                return "Cloud Vision API request failed. Check logs for details.";
            }

            protected void onPostExecute(String result) {

            }
        }.execute();
    }

    @NonNull
    private Image getImageEncodeImage(Bitmap bitmap) {
        Image base64EncodedImage = new Image();
        // Convert the bitmap to a JPEG
        // Just in case it's a format that Android understands but Cloud Vision
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        // Base64 encode the JPEG
        base64EncodedImage.encodeContent(imageBytes);
        return base64EncodedImage;
    }

    private String convertResponseToString(BatchAnnotateImagesResponse response) {

        AnnotateImageResponse imageResponses = response.getResponses().get(0);
        data =  new ArrayList<>();

        String message="";

        com.google.api.services.vision.v1.model.WebDetection wb = imageResponses.getWebDetection();

        List<com.google.api.services.vision.v1.model.WebEntity> wbl = wb.getWebEntities();
        if(wbl!=null) {
            for (int i = 0; i < wbl.size(); i++) {
                if (wbl.get(i).getScore() >= .5) {
                    data.add(wbl.get(i).getDescription());
                    Log.e("YOOOOOOOOOOOOOO", "convertResponseToString: " + wbl.get(i).getDescription() + " " + wbl.get(i).getScore());
                }
            }

            listFoods(data);
            //new MyAsyncTask().execute();
            //message = formatAnnotation(entityAnnotations);
        }
        return message;
    }


    public List<String> listFoods(List<String> s){
        findRecipes(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e(TAG, "LET US COUNT" );
                try {
                    measurements = new ArrayList<>();
                    measurementsURI = new ArrayList<>();
                    String jsonData = response.body().string();
                    JSONObject returnJSON = new JSONObject(jsonData);
                    JSONArray hints = (JSONArray) returnJSON.get("hints");
                    String foodName = returnJSON.getString("text");
                    JSONObject food1 = hints.getJSONObject(0);
                    JSONObject morefood = food1.getJSONObject("food");
                    String food = morefood.getString("label");
                    String foodURI = morefood.getString("uri");
                    foods.add(returnJSON.getString("text"));
                    JSONArray measures = food1.getJSONArray("measures");
                    for (int i = 0; i < measures.length(); i++) {
                        JSONObject jsonMeasures = measures.getJSONObject(i);
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
                    URIlist.add(foodURI);
                    list.add(food);
                    Log.e(TAG, "FOOD NAME: " + foodName );
                    pa.display(foods, pa);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, s);
        //pa.display(list, pa);
        return list;
    }


    public void findRecipes(Callback callback, List<String> s) {

        String APP_KEY = "54d1e8339e20e21b6a8e69cd0ecded66";
        String APP_ID = "20f18e21";
        for (String food:s) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .build();
            String ingredients = ("" + "," + "").replaceAll("\\s", "");
            HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.edamam.com/api/food-database/parser").newBuilder();
            urlBuilder.addQueryParameter("ingr", food);
            urlBuilder.addQueryParameter("app_id", APP_ID);
            urlBuilder.addQueryParameter("app_key", APP_KEY);
            String url = urlBuilder.build().toString();
            Log.e(TAG, url);

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(callback);
        }
    }


    public String getURI(int position){
        return URIlist.get(position);
    }
}

