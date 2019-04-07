package Models;

import java.util.ArrayList;
import java.util.List;

public class SimpleFood {
    public String name;
    public String foodURI;
    public int calories;
    public ArrayList<String> measurements;
    public ArrayList<String> measurementsURI;

    public SimpleFood(String name, String foodURI, int calories, ArrayList<String> measurements, ArrayList<String> measurementsURI){
        this.name = name;
        this.foodURI = foodURI;
        this.calories = calories;
        this.measurements = measurements;
        this.measurementsURI = measurementsURI;
    }
}
