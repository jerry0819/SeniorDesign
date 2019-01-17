package applicationname.companydomain.finalproject1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import Controller.NutrientsController;
import Models.Food;

public class NutrientsActivity extends AppCompatActivity {
    String TAG = "hey";
    private Food f;
    private ArrayAdapter<String> adapter;
    private ListView nutrientsListView;
    private NutrientsController mNutrientsController;
    private List<String> nutrientsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_nutrients);
        nutrientsList = new ArrayList<>();
        Bundle intentExtras = getIntent().getExtras();
        nutrientsListView = (ListView) findViewById(R.id.nutrientsList);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, nutrientsList);
        nutrientsListView.setAdapter(adapter);
        String URI = (String) intentExtras.get("URI");
        mNutrientsController = new NutrientsController(this, this);
        try {
            mNutrientsController.getNutrients(URI);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void showNutrients(final List<String> s, final NutrientsActivity sna, Food food){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Stuff that updates the UI
                f = food;
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
}

