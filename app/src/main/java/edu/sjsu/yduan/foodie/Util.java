package edu.sjsu.yduan.foodie;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.yelp.fusion.client.models.Category;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by yduan on 5/7/2017.
 */

public class Util {
//    public static float newrating;
//    public static String newpref;
//    public static int status;
    public static ArrayList<FacebookUser> FriendsAndMe = new ArrayList<>();
    public static String Join(ArrayList<String> al){
        StringBuffer sb = new StringBuffer();
        for(String s : al){
            sb.append(s).append(' ');
        }
        return sb.substring(0,sb.length()-1);
    }
    public static String JoinCategories(ArrayList<Category> al){
        StringBuffer sb = new StringBuffer();
        for(Category c : al){
            sb.append(c.getTitle()).append("  ");
        }
        return sb.substring(0,sb.length()-2);
    }
    public void generateFbFingerPrint(Context context) {
        try {
//            context.getPackageName()
            PackageInfo info = context.getPackageManager().getPackageInfo("edu.sjsu.yduan.foodie",PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String sign = Base64
                        .encodeToString(md.digest(), Base64.DEFAULT);
                Log.i("KEYHASH:", sign);
                Toast.makeText(getApplicationContext(), sign, Toast.LENGTH_LONG)
                        .show();
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    public static int getGradeColor(int grade){
        switch (grade){
            case 0: return R.color.grade0;
            case 1: return R.color.grade1;
            case 2: return R.color.grade2;
            case 3: return R.color.grade3;
            case 4: return R.color.grade4;
            case 5: return R.color.grade5;
            case 6: return R.color.grade6;
            case 7: return R.color.grade7;
            case 8: return R.color.grade8;
            case 9: return R.color.grade9;
            case 10: return R.color.grade10;
            default: return R.color.grey_300;
        }
    }
    public static String formatInteger(long number) {
        String[] suffix = new String[]{"k","m","b","t"};
        int size = (number != 0) ? (int) Math.log10(number) : 0;
        if (size >= 3){
            while (size % 3 != 0) {
                size = size - 1;
            }
        }
        double notation = Math.pow(10, size);
        String result = (size >= 3) ? + (Math.round((number / notation) * 10) / 10)+suffix[(size/3) - 1] : + number + "";
        return result;
    }
}
