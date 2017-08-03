package com.example.arsalankhan.websolutionseo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.arsalankhan.websolutionseo.Adapter.CommentsAdapter;
import com.example.arsalankhan.websolutionseo.helper.CommentsHelper;
import com.example.arsalankhan.websolutionseo.helper.TotalViewsHelper;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SingleVideoActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private YouTubePlayerView mYoutubePlayerView;
    private String videoId;
    private TextView tv_videoTitle,tv_Comments,tv_likes,tv_dislikes,tv_views,tv_channelTitle;
    private RecyclerView mCommentsRecyclerView;
    private LinearLayout allViewsLayout;
    private ProgressBar mProgress;

    private ArrayList<TotalViewsHelper> arraylist_TotalViews=new ArrayList<>();
    private ArrayList<CommentsHelper> arraylist_AllComments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_video);


        mYoutubePlayerView = findViewById(R.id.youtubeplayerView);
        mYoutubePlayerView.initialize(MainActivity.DeveloperKey,this);

        //Initializing all views
        initActivityViews();


        //getting the intent data
        Intent intent=getIntent();
        if(intent!=null){
            videoId = intent.getStringExtra("videoId");
            String videoTitle=intent.getStringExtra("videoTitle");
            String ChannelTitle=intent.getStringExtra("channelTitle");

            tv_videoTitle.setText(videoTitle);
            tv_channelTitle.setText(ChannelTitle);
        }

        // Getting total views, likes and dislikes
        getViewsResponse();

        //getting all comments on a video
        getAllComments();

    }


    private void initActivityViews() {

        tv_videoTitle=findViewById(R.id.singleVideoTitle);
        tv_views= findViewById(R.id.videoViews);
        tv_likes=findViewById(R.id.tv_likes);
        tv_dislikes=findViewById(R.id.tv_unlikes);
        tv_Comments= findViewById(R.id.tv_commentsCount);
        allViewsLayout= findViewById(R.id.layout_singleVideoAllViews);
        tv_channelTitle=findViewById(R.id.channelTitle);
        mProgress=findViewById(R.id.commentsSection_progress);

        mCommentsRecyclerView= findViewById(R.id.commentsRecyclerView);
        mCommentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCommentsRecyclerView.setHasFixedSize(true);

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestore) {

        if(!wasRestore){
            youTubePlayer.loadVideo(videoId);
            youTubePlayer.play();
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

        if(youTubeInitializationResult.isUserRecoverableError()){
            youTubeInitializationResult.getErrorDialog(SingleVideoActivity.this,1).show();
        }
        else{
            Toast.makeText(this, "There is some Error occur while Player Initialization", Toast.LENGTH_SHORT).show();
        }
    }


    //VIEWS, LIKES, DISLIKES AND TOTAL COMMENTS RESPONSE
    private void getViewsResponse(){
        String url= "https://www.googleapis.com/youtube/v3/videos?part=statistics&id="+videoId+"&key="+MainActivity.DeveloperKey;

        StringRequest request= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                allViewsLayout.setVisibility(View.VISIBLE);
                mProgress.setVisibility(View.GONE);
                if(response!=null){

                    try {

                        JSONObject parentJsonObject= new JSONObject(response.toString());
                        JSONArray parentJsonArray= parentJsonObject.getJSONArray("items");

                        JSONObject jsonObject =parentJsonArray.getJSONObject(0);
                        JSONObject childJsonObject=jsonObject.getJSONObject("statistics");

                        String totalviews = childJsonObject.getString("viewCount");
                        String totalLikes = childJsonObject.getString("likeCount");
                        String totalDislikes = childJsonObject.getString("dislikeCount");
                        String commentCount =  childJsonObject.getString("commentCount");


                        TotalViewsHelper helper = new TotalViewsHelper(totalviews,totalLikes,totalDislikes,commentCount);
                        arraylist_TotalViews.add(helper);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("TAG","Error: "+e.toString());
                    }

                }
                SetAllViewsData();
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                allViewsLayout.setVisibility(View.GONE);
                mProgress.setVisibility(View.GONE);
            }
        });

        MySingleton.getInstance(SingleVideoActivity.this).addToRequestQueue(request);
    }

    //setting views data i.e. likes, dislikes etc
    private void SetAllViewsData() {

        if(arraylist_TotalViews.size()!=0){

            TotalViewsHelper helper= arraylist_TotalViews.get(0);

            tv_views.setText(helper.getTotalViews()+" views");
            tv_likes.setText(helper.getTotalLiskes());
            tv_dislikes.setText(helper.getTotlaDislikes());
            tv_Comments.setText(helper.getCommentCount());
        }
    }



    //getting all Comments of a video
    private void getAllComments() {
        String url_comments="https://www.googleapis.com/youtube/v3/commentThreads?key="+MainActivity.DeveloperKey+"&textFormat=plainText&part=snippet&videoId="+videoId+"&maxResults=50";

        StringRequest request = new StringRequest(Request.Method.GET, url_comments, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response!=null){

                    try {
                        JSONObject parentJsonObject= new JSONObject(response.toString());
                        JSONArray parentJsonArray= parentJsonObject.getJSONArray("items");

                        for (int i = 0; i < parentJsonArray.length() ; i++) {

                            JSONObject jsonObjectItems= parentJsonArray.getJSONObject(i);

                            JSONObject jsonObjectSnippet = jsonObjectItems.getJSONObject("snippet");

                            JSONObject jsonObjectTopLevelComment = jsonObjectSnippet.getJSONObject("topLevelComment");

                            JSONObject jsonObjectChildSnippet= jsonObjectTopLevelComment.getJSONObject("snippet");

                            String displayName= jsonObjectChildSnippet.getString("authorDisplayName");
                            String profileImageUri = jsonObjectChildSnippet.getString("authorProfileImageUrl");
                            String commentText = jsonObjectChildSnippet.getString("textDisplay");
                            String publishedAt = jsonObjectChildSnippet.getString("publishedAt");

                            CommentsHelper helper = new CommentsHelper(displayName,profileImageUri,commentText,publishedAt);

                            arraylist_AllComments.add(helper);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    CommentsAdapter adapter = new CommentsAdapter(SingleVideoActivity.this,arraylist_AllComments);
                    mCommentsRecyclerView.setAdapter(adapter);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("TAG","Error:  "+error.toString());
            }
        });

        MySingleton.getInstance(SingleVideoActivity.this).addToRequestQueue(request);
        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }
}
