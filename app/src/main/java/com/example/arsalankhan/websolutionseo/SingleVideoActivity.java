package com.example.arsalankhan.websolutionseo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.arsalankhan.websolutionseo.Adapter.ChatAdapter;
import com.example.arsalankhan.websolutionseo.helper.Messages;
import com.example.arsalankhan.websolutionseo.helper.TotalViewsHelper;
import com.example.arsalankhan.websolutionseo.helper.TyperIndicator;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SingleVideoActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private YouTubePlayerView mYoutubePlayerView;
    private String videoId;
    private TextView tv_videoTitle,tv_likes,tv_dislikes,tv_views;
    private LinearLayout allViewsLayout;
    private ProgressBar mProgress;
    private RecyclerView mChatRecyclerView;
    private ChatAdapter chatAdapter;
    private ArrayList<Messages> messagesArrayList = new ArrayList<>();

    private ArrayList<TotalViewsHelper> arraylist_TotalViews=new ArrayList<>();
    private FirebaseAuth mAuth;
    private EditText editTextMessage;
    private String mCurrentUserId;
    private String msenderId;
    private DatabaseReference mTyperDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        //for moving the activity up due keyboard overlay with editText
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setContentView(R.layout.activity_single_video);


        mTyperDatabase = FirebaseDatabase.getInstance().getReference().child("TypingIndicator");

        mYoutubePlayerView = findViewById(R.id.youtubeplayerView);
        mYoutubePlayerView.initialize(MainActivity.DeveloperKey,this);

        //Initializing all views
        initActivityViews();



        // initializing the Adapter
        chatAdapter = new ChatAdapter(messagesArrayList,SingleVideoActivity.this);
        mChatRecyclerView.setAdapter(chatAdapter);


        //getting the intent data
        Intent intent=getIntent();
        if(intent!=null){
            videoId = intent.getStringExtra("videoId");
            String videoTitle=intent.getStringExtra("videoTitle");
            tv_videoTitle.setText(videoTitle);
        }

        // Getting total views, likes and dislikes
        getViewsResponse();

        //load messages into recyclerView
        loadMessages();


        //checking someone is Typing or not
        isUserTyping();
    }


    private void initActivityViews() {

        tv_videoTitle=findViewById(R.id.singleVideoTitle);
        tv_views= findViewById(R.id.videoViews);
        tv_likes=findViewById(R.id.tv_likes);
        tv_dislikes=findViewById(R.id.tv_unlikes);
        allViewsLayout= findViewById(R.id.layout_singleVideoAllViews);

        mProgress=findViewById(R.id.commentsSection_progress);

        mChatRecyclerView = findViewById(R.id.chatRecyclerView);
        mChatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mChatRecyclerView.setHasFixedSize(true);

        editTextMessage = findViewById(R.id.ChateditText);


        //For listening the change in editText
        editTextMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(!TextUtils.isEmpty(charSequence) && charSequence.length() > 0){

                    mTyperDatabase.child(mCurrentUserId).child("isTyping").setValue("true");

                }else{
                    mTyperDatabase.child(mCurrentUserId).child("isTyping").setValue("false");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //when Focus on EditText change also check editText isEmpty or not so that update isTyping value in database
        editTextMessage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                EditText editTextMsg = (EditText) view;

                if(b && editTextMsg.length() > 0){
                    mTyperDatabase.child(mCurrentUserId).child("isTyping").setValue("true");
                }else{
                    mTyperDatabase.child(mCurrentUserId).child("isTyping").setValue("false");
                }
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mcurrentUser = mAuth.getCurrentUser();
        mCurrentUserId = mcurrentUser.getUid();


        //creating node for current in order to detect its typing
        Map typerMap = new HashMap();
        typerMap.put("isTyping","false");
        typerMap.put("senderId",mCurrentUserId);
        mTyperDatabase.child(mCurrentUserId).updateChildren(typerMap);

    }
//checking isUser is Typing or not
    private void isUserTyping(){

        mTyperDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    TyperIndicator typer = snapshot.getValue(TyperIndicator.class);
                    msenderId = typer.getSenderId();

                    if(!msenderId.equals(mCurrentUserId)){

                        String isTyping = typer.getIsTyping();

                        if(isTyping.equals("true")){

                            showAndHideTypeIndicatorLayout(true);

                        }
                        else{

                            showAndHideTypeIndicatorLayout(false);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void showAndHideTypeIndicatorLayout(boolean isTyping){
        LinearLayout layout_typeIndicator = findViewById(R.id.layout_type_indicator);

        if(isTyping){
            layout_typeIndicator.setVisibility(View.VISIBLE);
        }
        else{
         layout_typeIndicator.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        mTyperDatabase.child(mCurrentUserId).child("isTyping").setValue("false");
    }

    //Chat send Button
    public void chatSendButton(View view){


        String message = editTextMessage.getText().toString().trim();

        if(!TextUtils.isEmpty(message)){

            Chat.getInstance().ChatWithUser(mAuth,message);
            editTextMessage.setText("");


        }
        else{
            Toast.makeText(this, "Please write some Message", Toast.LENGTH_SHORT).show();
        }

    }


    private void loadMessages(){

        DatabaseReference mChatDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Chat");

        mChatDatabaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Messages message =dataSnapshot.getValue(Messages.class);
                messagesArrayList.add(message);
                mChatRecyclerView.scrollToPosition(messagesArrayList.size()-1);
                chatAdapter.notifyDataSetChanged();
                startService(new Intent(SingleVideoActivity.this,MyNotificationAlertIntentService.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.d("TAG","Error: "+databaseError.getMessage());
            }
        });
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
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(SingleVideoActivity.this,MyNotificationAlertIntentService.class));
    }
}
