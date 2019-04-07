package Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import Models.SimpleFood;
import applicationname.companydomain.finalproject1.R;

public class FoodAdapter extends BaseAdapter {
    private final Context mContext;
    private final List<SimpleFood> mFoodList;
    public FoodAdapter(Context context, List<SimpleFood> foodList) {
        mContext = context;
        mFoodList = foodList;
    }

    @Override
    public int getCount() {
        return mFoodList.size();
    }

    @Override
    public SimpleFood getItem(int position) {
        return mFoodList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderItem viewHolder;

        if(convertView==null){
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.food_list_item_layout, parent, false);

            viewHolder = new ViewHolderItem();
            viewHolder.textViewItem = (TextView) convertView.findViewById(R.id.food_name);
            viewHolder.textViewItemCalories = (TextView) convertView.findViewById(R.id.food_calories);
            viewHolder.arrow_right= (ImageView) convertView.findViewById(R.id.arrow_right);
            convertView.setTag(viewHolder);

        }else{

            viewHolder = (ViewHolderItem) convertView.getTag();
        }

        SimpleFood objectItem = mFoodList.get(position);

        if(objectItem != null) {
            viewHolder.textViewItem.setText(objectItem.name);
            viewHolder.textViewItemCalories.setText(String.valueOf("Calories: " + objectItem.calories));
        }

        return convertView;
    }

    static class ViewHolderItem {
        TextView textViewItem;
        TextView textViewItemCalories;
        ImageView arrow_right;
    }
    public List<SimpleFood> getData() {
        return mFoodList;
    }
}

