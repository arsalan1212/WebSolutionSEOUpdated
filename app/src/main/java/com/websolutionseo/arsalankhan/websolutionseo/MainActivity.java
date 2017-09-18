package com.websolutionseo.arsalankhan.websolutionseo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
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
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.websolutionseo.arsalankhan.websolutionseo.Adapter.VideoPlaylistAdapter;
import com.websolutionseo.arsalankhan.websolutionseo.NavigtionViewItems.Market_summary_activity;
import com.websolutionseo.arsalankhan.websolutionseo.helper.PlaylistHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements VideoPlaylistAdapter.Communicator,
        NavigationView.OnNavigationItemSelectedListener{

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
    private AdView adView;
    private InterstitialAd interstitialAd;
    private String videoId;
    private FirebaseAuth mAuth;
    private DrawerLayout mDrawerLayout;
    private static InterstitialAd myInterstitialAd;
    private Intent intentService;
    public static final String MAIN_ACTIVITY_NAME="activity_main";

    String[] emails ={"mujahidkhan253@gmail.com", "psxon1@gmail.com",
                       "developer.heerasol@gmail.com"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //For Banner Ads
        MobileAds.initialize(this,getString(R.string.Admob_App_id));
        adView = (AdView) findViewById(R.id.main_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        //setting interstitial Ad
        setInterstitialAd();
        setUpInterstitialAd();

        mAuth = FirebaseAuth.getInstance();

        //---------------- ************************** ---------------------
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


        //setting DrawerLayout
        setUpNavigationDrawer();


        //starting interstitial ad service
        intentService = new Intent(MainActivity.this,InterstitialAdService.class);
        intentService.putExtra("activity_main",MAIN_ACTIVITY_NAME);
        startService(intentService);




    }


    private void setUpNavigationDrawer() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawerLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.bringToFront();

        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
          this,
                mDrawerLayout,
                mToolbar,
                R.string.open_drawer,
                R.string.close_drawer
        );

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    private void CloseDrawer(){
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {

        if(mDrawerLayout.isDrawerOpen(GravityCompat.START))
            CloseDrawer();
        else

        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser mCurrentUser = mAuth.getCurrentUser();
        //User is not Login
        if(mCurrentUser== null){

            Intent signInIntent = new Intent(MainActivity.this,SignInActivity.class);
            signInIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(signInIntent);
            finish();
        }
        else{

            String email = mCurrentUser.getEmail();

            if(email.equals(emails[0]) || email.equals(emails[1]) || email.equals(emails[2])){

                FirebaseMessaging.getInstance().subscribeToTopic("Owner");

            }

            /* if(email.equals("arsalan.ak777@gmail.com")){

                FirebaseMessaging.getInstance().subscribeToTopic("Owner");
            }*/

        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        stopService(intentService);
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

    private void setInterstitialAd(){

        //For Interstitial Ad
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.Interstitial_unit_id));
        interstitialAd.loadAd(new AdRequest.Builder().build());

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {

                interstitialAd.loadAd(new AdRequest.Builder().build());
                Intent singleVideoIntent = new Intent(MainActivity.this,SingleVideoActivity.class);
                singleVideoIntent.putExtra("videoId",videoId);
                startActivity(singleVideoIntent);

            }
        });

    }
    @Override
    public void setMessage(String videoId) {

        this.videoId = videoId;

        if(interstitialAd.isLoaded()){
            interstitialAd.show();

        }
        else
            {
                Intent singleVideoIntent=new Intent(this, SingleVideoActivity.class);
                singleVideoIntent.putExtra("videoId",videoId);
                startActivity(singleVideoIntent);
            }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        CloseDrawer();
        switch (item.getItemId()){
            case R.id.market_summary:
                Intent intent = new Intent(MainActivity.this, Market_summary_activity.class);
                startActivity(intent);
                break;
        }
        return true;
    }


    private void setUpInterstitialAd(){
        //For Interstitial Ad
        myInterstitialAd = new InterstitialAd(this);
        myInterstitialAd.setAdUnitId(getString(R.string.Interstitial_unit_id));
        myInterstitialAd.loadAd(new AdRequest.Builder().build());

    }

    public static void showInterstitialAd(){
        if(myInterstitialAd.isLoaded()){
            myInterstitialAd.show();
        }
    }
}
