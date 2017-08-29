package com.example.arsalankhan.websolutionseo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.arsalankhan.websolutionseo.Adapter.ChatAdapter;
import com.example.arsalankhan.websolutionseo.helper.Messages;
import com.example.arsalankhan.websolutionseo.helper.TotalViewsHelper;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    private SignInButton mGoogleButton;
    private RecyclerView mChatRecyclerView;
    private ChatAdapter chatAdapter;
    private ArrayList<Messages> messagesArrayList = new ArrayList<>();

    private ArrayList<TotalViewsHelper> arraylist_TotalViews=new ArrayList<>();
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;
    private String TAG="Facebook";
    private static final int RC_SIGN_IN=1;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    private EditText editTextMessage;
    private String isTyping= "false";
    private String mCurrentUserId;
    private String msenderId;
    private InterstitialAd interstitialAd;
    private String messageKey;
    private DatabaseReference mTyperDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        //for moving the activity up due keyboard overlay with editText
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setContentView(R.layout.activity_single_video);


        mTyperDatabase = FirebaseDatabase.getInstance().getReference().child("TyperIndicator");

        mYoutubePlayerView = findViewById(R.id.youtubeplayerView);
        mYoutubePlayerView.initialize(MainActivity.DeveloperKey,this);

        //Initializing all views
        initActivityViews();



        // initializing the Adapter
        chatAdapter = new ChatAdapter(messagesArrayList);
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

        //Facebook login
        FacebookLogin();

        //For google SignIn
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                          .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                          .build();

        mGoogleButton = findViewById(R.id.google_sign_in_btn);
        mGoogleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();


            }
        });

        // Google SignIn End


        //load messages into recyclerView
        loadMessages();


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

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setTitle("Sign In");
        mProgressDialog.setMessage("Please wait while we setup your Account");

        editTextMessage = findViewById(R.id.ChateditText);


        //For listening the change in editText
        editTextMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

               /* mChatDatabaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                            Messages messages = dataSnapshot1.getValue(Messages.class);
                            msenderId = messages.getSenderId();

                            if(msenderId.equals(mCurrentUserId)){
                                messageKey = dataSnapshot1.getKey();
                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
*/

                if(!TextUtils.isEmpty(charSequence) && charSequence.length() > 0){

/*                    if(mCurrentUserId.equals(msenderId)){
                        isTyping = "true";
                        mChatDatabaseRef.child(messageKey).child("isTyping").setValue(isTyping);
                    }*/



                }else{

/*                    if(mCurrentUserId.equals(msenderId)){
                        isTyping = "false";
                        mChatDatabaseRef.child(messageKey).child("isTyping").setValue(isTyping);
                    }*/
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();

        CheckCurrentUserStatus();

    }

    //Checking the user is login or not
    private void CheckCurrentUserStatus() {
        LinearLayout layout_signIn = findViewById(R.id.layout_SignUp);

        RelativeLayout layout_chat = findViewById(R.id.layout_chat);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth != null){

            FirebaseUser mcurrentUser = mAuth.getCurrentUser();

            if(mcurrentUser==null){
                //  User is Not login
                layout_signIn.setVisibility(View.VISIBLE);
                layout_chat.setVisibility(View.GONE);
            }
            else
            {
                //User is login
                layout_signIn.setVisibility(View.GONE);
                layout_chat.setVisibility(View.VISIBLE);

                mCurrentUserId = mcurrentUser.getUid();

                Map typerMap = new HashMap();
                typerMap.put("isTyping","false");
                typerMap.put("senderId",mCurrentUserId);
                mTyperDatabase.child(mCurrentUserId).updateChildren(typerMap);
            }

        }


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


    //For Google
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            //Update UI if user is login or not
                            CheckCurrentUserStatus();
                            //load messages into recyclerView
                            loadMessages();

                            mProgressDialog.dismiss();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SingleVideoActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    //--------------- ********************************** ------------------------------
    // For FaceBook login
    private void FacebookLogin(){
        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);

                mProgressDialog.show();
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //For Facebook Login
        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        //________________ **************************** ______________________________
        //For Google SignIn

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                mProgressDialog.show();

                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
                Toast.makeText(this, "SignIn Failed onActivityResult", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            //Update UI if user is login or not
                            CheckCurrentUserStatus();
                            //load messages into recyclerView
                            loadMessages();

                            mProgressDialog.dismiss();

                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(SingleVideoActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    // facebook code end here.......

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(SingleVideoActivity.this,MyNotificationAlertIntentService.class));
    }
}
