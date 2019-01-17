package Util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceHelper {
    public static final String PREFERENCES = "FoodPreferences";
    public static final String CODE = "code";
    private Context mContext;
    private SharedPreferences mSharedPreferences;


    public SharedPreferenceHelper(Context context) {
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(PREFERENCES, 0);
    }

    public void saveCode(String code)
    {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(CODE, code);
        editor.apply();
    }

    public String getCode(){
        return mSharedPreferences.getString(CODE,"");
    }

}
