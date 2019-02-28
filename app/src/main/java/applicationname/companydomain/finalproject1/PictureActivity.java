package applicationname.companydomain.finalproject1;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.api.services.vision.v1.model.Feature;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import Controller.PictureController;

public class PictureActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ImageView imageView;
    private TextView tv;
    List<String> data;
    private ArrayAdapter<String> adapter;
    private Feature feature;
    private String visionAPI = "WEB_DETECTION";
    private PictureController mPictureController;
    private ListView nutrientsListView;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private static final int PICK_IMAGE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        Bundle intentExtras = getIntent().getExtras();
        imageView = findViewById(R.id.img);

        nutrientsListView = findViewById(R.id.list_food);

        data = new ArrayList<>();
        feature = new Feature();
        feature.setType(visionAPI);
        feature.setMaxResults(5);

        if(intentExtras!=null){
            openGallery();
        }
        else {
            int nh = (int) ( PractiveCamera2Activity.bitmap.getHeight() * (1024.0 / PractiveCamera2Activity.bitmap.getWidth()) );
            PractiveCamera2Activity.bitmap= Bitmap.createScaledBitmap(PractiveCamera2Activity.bitmap, 1024, nh, true);
            imageView.setImageBitmap(PractiveCamera2Activity.bitmap);
            mPictureController = new PictureController(this,  PractiveCamera2Activity.bitmap, feature, this);
        }
        //TODO change code so if a brand is picked up, combine with top result to find brands food
        nutrientsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String URI = mPictureController.getURI(position);
                nextScreen(URI, mPictureController.measurementsMatrix.get(position), mPictureController.measurementsURIMatrix.get(position), mPictureController.list.get(position));
            }
        });
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nvView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawer.closeDrawers();
                int itemId = menuItem.getItemId();
                //Toast.makeText(getApplicationContext(), menuItem.getTitle().toString(),
                //       Toast.LENGTH_LONG).show();
                int id = menuItem.getItemId();
                Log.e("TAG", "onOptionsItemSelected: " + "WE IN THIS HOE????");
                if (id == R.id.nav_upload) {
                    openGallery();
                } else if (id == R.id.nav_search) {
                    Intent searchScreen = new Intent(PictureActivity.this, SearchFoodActivity.class);
                    startActivity(searchScreen);
                } else if (id == R.id.nav_settings) {
                    Intent searchScreen = new Intent(PictureActivity.this, UserFoodInfoActivity.class);
                    startActivity(searchScreen);
                } else if(id == R.id.mybutton){
                    Intent searchScreen = new Intent(PictureActivity.this, SearchFoodActivity.class);
                    startActivity(searchScreen);
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }


    public void display(final List<String> s, final PictureActivity pa)
    {
        Log.e("IS IT WOKRING?", "display: " + s.size() );
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Stuff that updates the UI
                adapter = new ArrayAdapter<String>(pa, android.R.layout.simple_expandable_list_item_1, s);
                nutrientsListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void nextScreen(String URI, ArrayList<String> measurements, ArrayList<String> measurementsURI, String name) {
        Intent loadNutrientScreen = new Intent(this, NutrientsActivity.class);
        loadNutrientScreen.putExtra("URI", URI);
        loadNutrientScreen.putExtra("measurements", measurements);
        loadNutrientScreen.putExtra("measurementsURI", measurementsURI);
        loadNutrientScreen.putExtra("name", name);
        startActivity(loadNutrientScreen);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();
        Log.e("TAG", "onOptionsItemSelected: " + "WE IN THIS HOE????");
        if (id == R.id.nav_upload) {

        } else if (id == R.id.nav_search) {
            Intent searchScreen = new Intent(this, SearchFoodActivity.class);
            startActivity(searchScreen);
        } else if (id == R.id.nav_settings) {

        } else if(id == R.id.mybutton){
            Intent searchScreen = new Intent(this, SearchFoodActivity.class);
            startActivity(searchScreen);
        }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_food_item, menu);
        return super.onCreateOptionsMenu(menu);
    }



    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        int id = item.getItemId();

        if (id == R.id.nav_upload) {
            // Handle the camera action
        } else if (id == R.id.nav_search) {
            Intent searchScreen = new Intent(this, SearchFoodActivity.class);
            startActivity(searchScreen);
        } else if (id == R.id.nav_settings) {
            Intent searchScreen = new Intent(this, UserFoodInfoActivity.class);
            startActivity(searchScreen);
        } else if(id == R.id.mybutton){
            Intent searchScreen = new Intent(this, SearchFoodActivity.class);
            startActivity(searchScreen);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }
    private void openGallery()
    {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE)
        {
            Uri imageUri = data.getData();
            imageView.setImageURI(imageUri);
            Bitmap b = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
            int nh = (int) ( b.getHeight() * (1024.0 / b.getWidth()) );
            b = Bitmap.createScaledBitmap(b, 1024, nh, true);
            mPictureController = new PictureController(this, b , feature, this);
        }
    }

}
