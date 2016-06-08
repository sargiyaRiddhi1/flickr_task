package com.example.riddhi.assignmentloktra;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class ImageActivity extends AppCompatActivity {
    ArrayList imageArrayList=new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        final GridView gridview = (GridView) findViewById(R.id.gridview);




        String url=UrlHelper.getBaseUrl()+"/?method="+UrlHelper.getRecentImageMethod()+"&api_key="+Constants.getFlickrAPI()+"&format=json&nojsoncallback=1";

        Log.i("url",url);
        Ion.with(this)
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        Log.i("result",result.toString());
                        JsonObject resultObject=result.getAsJsonObject("photos");
                        JsonArray recentPhotoJsonArray=resultObject.getAsJsonArray("photo");


                        for(int i=0;i<recentPhotoJsonArray.size();i++){
                            ImageModel jsonJavaRootObject = new Gson().fromJson(recentPhotoJsonArray.get(i), ImageModel.class);
                            imageArrayList.add(jsonJavaRootObject);

                        }
                        gridview.setAdapter(new ImageAdapter(ImageActivity.this,imageArrayList));



                    }
                });




        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                flipCard(v);

            }
        });
    }
    private void flipCard(View v)
    {
        View rootLayout = (View) findViewById(R.id.gridview);
        View cardFace = (View) findViewById(R.id.gridview);
        View cardBack = (View) findViewById(R.id.gridview);

        cardFace=(RelativeLayout)v.findViewById(R.id.front);
        cardBack=v.findViewById(R.id.back);

        FlipAnimation flipAnimation = new FlipAnimation(cardFace, cardBack);

        if (cardFace.getVisibility() == View.GONE)
        {
            flipAnimation.reverse();
        }
        v.startAnimation(flipAnimation);
    }
}
