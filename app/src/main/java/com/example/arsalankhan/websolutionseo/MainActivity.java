package com.example.arsalankhan.websolutionseo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.arsalankhan.websolutionseo.Adapter.VideoPlaylistAdapter;
import com.example.arsalankhan.websolutionseo.helper.PlaylistHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String channelId="UCBRBgsoUC893QzkPRsdx8GQ";
    private static final String MaxResult="50";
    public static final String DeveloperKey="AIzaSyDJqOUx2KV3HvgOYBsBhZ8rDcJ0xxMIsx4";

    private static String channelUrl ="https://www.googleapis.com/youtube/v3/search?order=date&part=snippet&channelId="
                                       +channelId+"&maxResults="+MaxResult+"&key="+DeveloperKey;

    private TextView tv_connectionStatus;
    private RecyclerView playlistRecyclerView;
    private LinearLayout ConnectionLayout;
    private ProgressBar mProgress;
    private Toolbar mToolbar;
    private ArrayList<PlaylistHelper> playlistVideoArraylist=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_connectionStatus= (TextView) findViewById(R.id.tv_display_Connection_status);
        playlistRecyclerView= (RecyclerView) findViewById(R.id.playlist_RecyclerView);
        ConnectionLayout= (LinearLayout) findViewById(R.id.layout_Connection);
        mProgress= (ProgressBar) findViewById(R.id.progresbar);
        mToolbar = (Toolbar) findViewById(R.id.main_app_bar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("PSX Training");


        playlistRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        playlistRecyclerView.setHasFixedSize(true);

        //Get Response from Server
        GetResponse();

    }
    //getting the response
    private void GetResponse() {

        StringRequest request=new StringRequest(Request.Method.GET, channelUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response!=null){

                    ConnectionLayout.setVisibility(View.GONE);
                    playlistRecyclerView.setVisibility(View.VISIBLE);
                    mProgress.setVisibility(View.GONE);

                    try {
                        JSONObject parentJsonObject=new JSONObject(response.toString());
                        JSONArray parentJsonArray=parentJsonObject.getJSONArray("items");

                        for(int i=0; i<parentJsonArray.length(); i++) {

                            //TODO AT THESE TWO INDEX THERE IS PLAYLIST AND CHANNEL OBJECT
                            if (i == 8 || i == 12) {
                                //DO nothing
                                //We don't channel and playlist object
                            }
                            else
                            {
                                JSONObject childJsonObject = parentJsonArray.getJSONObject(i);
                                Log.d("TAG","TOTAL: "+i);
                                // getting video id
                                JSONObject jsonObjectVideoId = childJsonObject.getJSONObject("id");
                                String videoId = jsonObjectVideoId.getString("videoId");

                                //Snippet Object
                                JSONObject snippetObject = childJsonObject.getJSONObject("snippet");

                                String title = snippetObject.getString("title");
                                String description = snippetObject.getString("description");
                                String channelTitle = snippetObject.getString("channelTitle");
                                String publishDate = snippetObject.getString("publishedAt");

                                //Thumbnails Ojbect
                                JSONObject thumbnailObject = snippetObject.getJSONObject("thumbnails");
                                JSONObject highRsImageObject = thumbnailObject.getJSONObject("medium");

                                String thumnail = highRsImageObject.getString("url");


                                //Now creating helper class object and storing each video data
                                PlaylistHelper helper = new PlaylistHelper(title, description, thumnail, videoId, channelTitle, publishDate);
                                playlistVideoArraylist.add(helper);

                            }

                            //now setting the Adapter
                            VideoPlaylistAdapter adapter = new VideoPlaylistAdapter(MainActivity.this, playlistVideoArraylist);
                            playlistRecyclerView.setAdapter(adapter);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                            Log.d("TAG","Error: "+e.toString());
                        Log.d("TAG",e.getMessage());
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ConnectionLayout.setVisibility(View.VISIBLE);
                playlistRecyclerView.setVisibility(View.GONE);
                mProgress.setVisibility(View.GONE);
                if(error instanceof NetworkError){
                    tv_connectionStatus.setText("Cannot connect to Internet...Please check your connection!");
                }
                else if(error instanceof NoConnectionError){
                    tv_connectionStatus.setText("Cannot connect to Internet...Please check your connection!");
                }
                else if(error instanceof TimeoutError){
                    tv_connectionStatus.setText("Connection TimeOut! Please check your internet connection!");
                }
                else if (error instanceof ServerError) {
                    tv_connectionStatus.setText("The server could not be found. Please try again after some time!!");
                } else if (error instanceof AuthFailureError) {
                    tv_connectionStatus.setText("Cannot connect to Internet...Please check your connection!");
                } else if (error instanceof ParseError) {
                    tv_connectionStatus.setText("Parsing error! Please try again after some time!!");
                }
            }
        });

        MySingleton.getInstance(MainActivity.this).addToRequestQueue(request);
    }



    //Refresh button

    public void RefreshLayout(View view){
        Intent refreshIntent=new Intent(this,MainActivity.class);
        startActivity(refreshIntent);
        finish();
    }
}
