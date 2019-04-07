package applicationname.companydomain.finalproject1;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.EventLogTags;
import android.widget.TextView;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import Controller.UserFoodInfoController;
import Util.SharedPreferenceHelper;

public class UserFoodInfoActivity extends AppCompatActivity {
    private UserFoodInfoController mUserFoodInfoController;
    private SharedPreferenceHelper mSharedPreferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_food_info);
        mSharedPreferenceHelper = new SharedPreferenceHelper(this);
        mUserFoodInfoController = new UserFoodInfoController(this, mSharedPreferenceHelper.getCode());


    }
    public void showCalories(int monTotal, int tuesTotal, int wedTotal, int thursTotal, int friTotal, int satTotal, int sunTotal){

        BarChart barChart = (BarChart) findViewById(R.id.barchart);

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(monTotal, 0));
        entries.add(new BarEntry(tuesTotal, 1));
        entries.add(new BarEntry(wedTotal, 2));
        entries.add(new BarEntry(thursTotal, 3));
        entries.add(new BarEntry(friTotal, 4));
        entries.add(new BarEntry(satTotal, 5));
        entries.add(new BarEntry(sunTotal, 6));

        BarDataSet bardataset = new BarDataSet(entries, "Calories");
        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Mon");
        labels.add("Tues");
        labels.add("Wed");
        labels.add("Thurs");
        labels.add("Fri");
        labels.add("Sat");
        labels.add("Sun");
        BarData data = new BarData(labels, bardataset);
        barChart.setData(data);
        barChart.setScaleEnabled(false);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setLabelsToSkip(0);
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        barChart.setDescription("");
        barChart.animateY(5000);

    }


}



