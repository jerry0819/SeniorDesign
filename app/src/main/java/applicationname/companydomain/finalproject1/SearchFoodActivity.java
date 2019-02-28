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

import Controller.SearchFoodController;

public class SearchFoodActivity extends AppCompatActivity {
    String TAG = "hey";
    private ListView foodListView;
    private EditText searchFoodEdit;
    private SearchFoodController mSearchFoodController;
    private String foodName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mSearchFoodController = new SearchFoodController(this);
        searchFoodEdit = (EditText) findViewById(R.id.input_search);
        foodListView = (ListView) findViewById(R.id.foodList);
        foodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                foodName = foodListView.getItemAtPosition(position).toString();
                nextScreen(mSearchFoodController.getURI(position), mSearchFoodController.measurementsMatrix.get(position), mSearchFoodController.measurementsURIMatrix.get(position));
            }
        });
        searchFoodEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    mSearchFoodController.searchFoods(searchFoodEdit.getText().toString());
                }
                return false;
            }
        });
    }

    private void nextScreen(String uri, ArrayList<String> measurements, ArrayList<String> measurementsURI) {
        Intent loadNutrientScreen = new Intent(this, NutrientsActivity.class);
        loadNutrientScreen.putExtra("URI", uri);
        loadNutrientScreen.putExtra("measurements", measurements);
        loadNutrientScreen.putExtra("measurementsURI", measurementsURI);
        loadNutrientScreen.putExtra("name", foodName);
        startActivity(loadNutrientScreen);
    }


    public void display(final List<String> s, final SearchFoodActivity sfa)
    {
        Log.e("IS IT WOKRING?", "display: " + s.size() );
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Stuff that updates the UI
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(sfa, android.R.layout.simple_expandable_list_item_1, s);
                foodListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
