package applicationname.companydomain.finalproject1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import Controller.UserFoodInfoController;
import Util.SharedPreferenceHelper;

public class UserFoodInfoActivity extends AppCompatActivity {
    private UserFoodInfoController mUserFoodInfoController;
    private SharedPreferenceHelper mSharedPreferenceHelper;
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_food_info);
        mSharedPreferenceHelper = new SharedPreferenceHelper(this);
        mUserFoodInfoController = new UserFoodInfoController(this, mSharedPreferenceHelper.getCode());
        tv = (TextView) findViewById(R.id.caloriesTextView);
    }
    public void showCalories(int monTotal, int tuesTotal, int wedTotal, int thursTotal, int friTotal, int satTotal, int sunTotal){
        tv.setText("Total Calories this Monday: " + monTotal + "\n" + "Total Calories this Tuesday: " + tuesTotal + "\n" +"Total Calories this Wednesday: " + wedTotal + "\n" +"Total Calories this Thursday: " + thursTotal + "\n" +"Total Calories this Friday: " + friTotal + "\n" +"Total Calories this Saturday: " + satTotal + "\n" +"Total Calories this Sunday: " + sunTotal + "\n");
    }
}
