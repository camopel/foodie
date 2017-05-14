package edu.sjsu.yduan.foodie;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.share.widget.LikeView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by xiaoyuliang on 5/12/17.
 */

public class FriendDetailActivity extends AppCompatActivity {
//    private ImageView profile;
//    private TextView name;//, description,label1,label2,label3,label4;
//    private Context context;
//    private String murl,mname,des;
//    public static final HashMap<String, Integer> LABEL_COLORS = new HashMap<String, Integer>()
//    {{
//        put("Suchi", R.color.Suchi);
//        put("Mexico", R.color.Mexico);
//        put("Chinese", R.color.Chinese);
//        put("India", R.color.India);
//        put("Greece", R.color.Greece);
//        put("Englian", R.color.Englian);
//        put("Italy",R.color.Italy);
//        put("El_Salvador", R.color.El_Salvador);
//    }};

    private FlowLayout preference_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_profile);
//        context = this;
        final ArrayList<Recommendation> resList = Recommendation.getRecipesFromFile("friends.json", this);
        final ArrayList<Friend> friendList = Friend.getRecipesFromFile("friends.json", this);
        RecommandAdapter adapter = new RecommandAdapter(this, resList);
        ListView listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(adapter);
        int pos = getIntent().getExtras().getInt("pos");
        Friend f = friendList.get(pos);
//        murl = this.getIntent().getExtras().getString("murl");
//        des = this.getIntent().getExtras().getString("des");
//        mname = this.getIntent().getExtras().getString("mname");

        TextView name = (TextView) findViewById(R.id.name);
        name.setText(f.name);
        ImageView profile = (ImageView) findViewById(R.id.profile);
        Picasso.with(this)
                .load(f.image)
                .transform(new CircleTransform())
                .resize(120, 120)
                .into(profile);
        preference_layout = (FlowLayout)findViewById(R.id.preference_layout);
        String[] sp = f.Preference.split(",");
        Random r = new Random();
        for(String s : sp){
            addPreferenceItem(s.trim(),r.nextInt(10)+1);
        }
        final ImageView biz_fav = (ImageView)findViewById(R.id.biz_fav);
        biz_fav.setTag(false);
        biz_fav.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                boolean fav = (boolean)v.getTag();
                if(!fav){
                    biz_fav.setImageResource(R.drawable.ic_fav);
                    biz_fav.setTag(true);
                }else{
                    biz_fav.setImageResource(R.drawable.ic_fav_gray);
                    biz_fav.setTag(false);
                }
            }
        });
        LikeView likeView = (LikeView) findViewById(R.id.like_view);
        String page=getString(R.string.facebook)+"103817216861013";//da wang
        likeView.setObjectIdAndType(page, LikeView.ObjectType.PAGE);
        likeView.setLikeViewStyle(LikeView.Style.BUTTON);

    }
    private void addPreferenceItem(String content,int value){
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TextView v = (TextView)inflater.inflate(R.layout.preference_item, null);
        v.setText(content);
        GradientDrawable bgShape = (GradientDrawable)v.getBackground();
        int color = ContextCompat.getColor(this, Util.getGradeColor(value));
        bgShape.setColor(color);
        preference_layout.addView(v);
    }
//    private void loadHomePage() {
//        Picasso.with(this)
//                .load(murl)
//                .resize(128, 128)
//                .transform(new CircleTransform())
//                .memoryPolicy(MemoryPolicy.NO_CACHE)
//                .networkPolicy(NetworkPolicy.NO_CACHE)
//                .into(profile);
//        name.setText(mname);
//        description.setText(des);
//        label1.setText("Suchi");
//        label2.setText("Mexico");
//        label3.setText("Chinese");
//        label4.setText("India");
//        label1.setBackgroundColor(android.support.v4.content.ContextCompat.getColor(context, LABEL_COLORS.get("Suchi")));
//        label2.setBackgroundColor(android.support.v4.content.ContextCompat.getColor(context, LABEL_COLORS.get("Mexico")));
//        label3.setBackgroundColor(android.support.v4.content.ContextCompat.getColor(context, LABEL_COLORS.get("Chinese")));
//        label4.setBackgroundColor(android.support.v4.content.ContextCompat.getColor(context, LABEL_COLORS.get("India")));

//    }
}
