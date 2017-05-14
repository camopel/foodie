package edu.sjsu.yduan.foodie;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by xiaoyuliang on 5/12/17.
 */

class Recommendation {
    public String name, image, distance, discription;
    public static ArrayList<Recommendation> getRecipesFromFile(String filename, Context context) {
        final ArrayList<Recommendation> resList = new ArrayList<>();

        try {
            // Load data
            String jsonString = loadJsonFromAsset(filename, context);
            JSONObject json = new JSONObject(jsonString);
            JSONArray rest = json.getJSONArray("restaurants");

            // Get Recipe objects from data
            for(int i = 0; i < rest.length(); i++){
                Recommendation res = new Recommendation();

                res.name = rest.getJSONObject(i).getString("name");
                res.image = rest.getJSONObject(i).getString("image");
                res.distance = rest.getJSONObject(i).getString("Distance");
                res.discription = rest.getJSONObject(i).getString("Discription");


                resList.add(res);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resList;
    }

    private static String loadJsonFromAsset(String filename, Context context) {
        String json = null;

        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        }
        catch (java.io.IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return json;
    }
}
