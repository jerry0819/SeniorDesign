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
    public void showCalories(int total){
        tv.setText("Total: " + total);
    }
}
