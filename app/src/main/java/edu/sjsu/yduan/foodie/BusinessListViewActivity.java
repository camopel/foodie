package edu.sjsu.yduan.foodie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class BusinessListViewActivity extends AppCompatActivity {
    private RecyclerView recyclerView=null;
    private BusinessListviewAdapter adapter=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_map_recycle_list);

        recyclerView = (RecyclerView)findViewById(R.id.biz_list);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        Intent intent = getIntent();
        int mode = intent.getIntExtra("mode",-1);
        if(mode==0) setTitle("Nearby Restaurants");
        else setTitle("Search Result");
        try{
            ArrayList<BusinessAIO> bizes = (ArrayList<BusinessAIO>)intent.getSerializableExtra(getString(R.string.businesses_intent_key));
            adapter = new BusinessListviewAdapter(bizes);
            recyclerView.setAdapter(adapter);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
