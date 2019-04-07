package applicationname.companydomain.finalproject1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Adapters.FoodAdapter;
import Controller.SearchFoodController;
import Models.SimpleFood;

public class SearchFoodActivity extends AppCompatActivity {
    String TAG = "hey";
    private ListView foodListView;
    private EditText searchFoodEdit;
    private SearchFoodController mSearchFoodController;
    private String foodName;
    private FoodAdapter adapter;
    private  SimpleFood sf;
    private List<SimpleFood> foodList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        foodList = new ArrayList<>();
        //sf = new SimpleFood("","",0, null, null);
        //foodList.add(sf);
        mSearchFoodController = new SearchFoodController(this);
        searchFoodEdit = (EditText) findViewById(R.id.input_search);
        foodListView = (ListView) findViewById(R.id.foodList);
        foodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                nextScreen(foodList.get(position).foodURI, foodList.get(position).measurements, foodList.get(position).measurementsURI,foodList.get(position).name);
            }
        });
        adapter = new FoodAdapter(this, foodList);
        foodListView.setAdapter(adapter);
        searchFoodEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    mSearchFoodController.searchFoods(searchFoodEdit.getText().toString());
                }
                return false;
            }
        });
    }

    private void nextScreen(String uri, ArrayList<String> measurements, ArrayList<String> measurementsURI, String name) {
        Intent loadNutrientScreen = new Intent(this, NutrientsActivity.class);
        loadNutrientScreen.putExtra("URI", uri);
        loadNutrientScreen.putExtra("measurements", measurements);
        loadNutrientScreen.putExtra("measurementsURI", measurementsURI);
        loadNutrientScreen.putExtra("name", name);
        startActivity(loadNutrientScreen);
    }


    public void display(final List<SimpleFood> foodList, final SearchFoodActivity sfa)
    {
        this.foodList = foodList;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.getData().clear();
                adapter.getData().addAll(foodList);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
