package com.gadgetroid.collegemate;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


public class AboutAcitvity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_acitvity);

        ImageView bannerImageView = (ImageView)findViewById(R.id.AboutBannerImageView);
        Picasso.with(this).load("http://www.topofandroid.com/wp-content/uploads/2015/05/" +
                "Android-L-Material-Design-Wallpapers-7.png").fit().into(bannerImageView);
        ImageView profileImageView = (ImageView)findViewById(R.id.AboutProfileImageView);
        Picasso.with(this).load("https://lh3.googleusercontent.com/-cUurEDTtPPU/AAAAAAAAAAI/AAAAAAAARbc/b2Hu4G61cC4/s120-c/photo.jpg").into(profileImageView);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about_acitvity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
