package applicationname.companydomain.finalproject1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
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
    private TextView measureNum;
    private NutrientsController mNutrientsController;
    private List<String> nutrientsList;
    private Spinner spinnerMeasurements;
    private List<String> measurements;
    private List<String> measurementsURI;
    private String URI;
    private String foodName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_nutrients);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        nutrientsList = new ArrayList<>();
        Bundle intentExtras = getIntent().getExtras();
        nutrientsListView = (ListView) findViewById(R.id.nutrientsList);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, nutrientsList);
        nutrientsListView.setAdapter(adapter);
        URI = (String) intentExtras.get("URI");
        measurements = intentExtras.getStringArrayList("measurements");
        measurementsURI = intentExtras.getStringArrayList("measurementsURI");
        foodName = intentExtras.getString("name");
        getSupportActionBar().setTitle(foodName);

        mNutrientsController = new NutrientsController(this, this, measurements, measurementsURI, foodName);

        spinnerMeasurements = findViewById(R.id.spinnerMeasurements);
        spinnerMeasurements.setOnItemSelectedListener(this);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, measurements);
        for(int i = 0; i<measurements.size(); i++)
        {
            Log.e(TAG, "onCreate: " + measurements.get(i) );
        }
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMeasurements.setAdapter(dataAdapter);
        measureNum = (TextView) findViewById(R.id.measureNum);
        measureNum.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    mNutrientsController.multiplyNutrients(Integer.parseInt(String.valueOf(measureNum.getText())));
                }
                return false;
            }
        });
    }


    public void showNutrients(final List<String> s, final NutrientsActivity sna, Food food){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.clear();
                adapter.addAll(s);
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
            mNutrientsController.addNutrients();
            Toast.makeText(this.getBaseContext(),"You have successfully added this item.",Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        try {
            mNutrientsController.getNutrients(URI,position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

