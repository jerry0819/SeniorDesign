package applicationname.companydomain.finalproject1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import Controller.NutrientsController;
import Models.Food;

public class NutrientsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String TAG = "hey";
    private Food f;
    private ArrayAdapter<String> adapter;
    private ListView nutrientsListView;
    private NutrientsController mNutrientsController;
    private List<String> nutrientsList;
    private Spinner spinnerMeasurements;
    private List<String> measurements;
    private List<String> measurementsURI;
    private String URI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_nutrients);
        nutrientsList = new ArrayList<>();
        Bundle intentExtras = getIntent().getExtras();
        nutrientsListView = (ListView) findViewById(R.id.nutrientsList);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, nutrientsList);
        nutrientsListView.setAdapter(adapter);
        URI = (String) intentExtras.get("URI");
        measurements = intentExtras.getStringArrayList("measurements");
        measurementsURI = intentExtras.getStringArrayList("measurementsURI");
        mNutrientsController = new NutrientsController(this, this, measurements, measurementsURI);
        try {
            mNutrientsController.getNutrients(URI,0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        spinnerMeasurements = findViewById(R.id.spinnerMeasurements);
        spinnerMeasurements.setOnItemSelectedListener(this);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, measurements);
        for(int i = 0; i<measurements.size(); i++)
        {
            Log.e(TAG, "onCreate: " + measurements.get(i) );
        }
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMeasurements.setAdapter(dataAdapter);
    }


    public void showNutrients(final List<String> s, final NutrientsActivity sna, Food food){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Stuff that updates the UI
                adapter = new ArrayAdapter<String>(sna, android.R.layout.simple_expandable_list_item_1, s);
                nutrientsListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
    }
    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_food_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mybutton) {
            mNutrientsController.addNutrients(f);
            Toast.makeText(this.getBaseContext(),"You have successfully added this item.",Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        try {

            mNutrientsController.getNutrients(URI,position);
            adapter.clear();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

