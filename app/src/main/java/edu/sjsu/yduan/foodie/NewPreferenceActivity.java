package edu.sjsu.yduan.foodie;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

public class NewPreferenceActivity extends ProgressActivity implements View.OnClickListener {
    private RatingBar ratingBar;
    private EditText newpref;
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newpreference);
        setTitle("New Preference");
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        newpref = (EditText)findViewById(R.id.newpref);
        btn = (Button)findViewById(R.id.donebtn);
        btn.setOnClickListener(this);
    }
    @Override
    public void onClick(View v){
        if(newpref.getText().length()==0) this.toastLongMessage("Input can not be empty!");
        else{
            SharedPreferences sharedPref = getSharedPreferences(getString(R.string.share),MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putFloat("rating", ratingBar.getRating());
            editor.putString("pref",newpref.getText().toString());
            editor.commit();
            this.finish();
        }

    }
}
