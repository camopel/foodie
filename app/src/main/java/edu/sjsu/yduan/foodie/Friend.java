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

public class Friend {
    public static final String TAG = Friend.class.getSimpleName();

    public String name;
    public String image;
    public String Preference;
    public String Distance;
    public String Label;

    public static ArrayList<Friend> getRecipesFromFile(String filename, Context context){
        final ArrayList<Friend> friendList = new ArrayList<>();

        try {
            // Load data
            String jsonString = loadJsonFromAsset("friends.json", context);
            JSONObject json = new JSONObject(jsonString);
            JSONArray friends = json.getJSONArray("friends");

            // Get Recipe objects from data
            for(int i = 0; i < friends.length(); i++){
                Friend friend = new Friend();

                friend.name = friends.getJSONObject(i).getString("name");
                friend.image = friends.getJSONObject(i).getString("image");
                friend.Preference = friends.getJSONObject(i).getString("Preference");
                friend.Distance = friends.getJSONObject(i).getString("Distance");
                friend.Label = friends.getJSONObject(i).getString("dietLabel");
                friendList.add(friend);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return friendList;
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
