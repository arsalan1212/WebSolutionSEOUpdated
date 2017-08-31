package com.example.arsalankhan.websolutionseo.NavigtionViewItems;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.arsalankhan.websolutionseo.InterstitialAdService;
import com.example.arsalankhan.websolutionseo.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class Market_summary_activity extends AppCompatActivity {

    private WebView mWebView;
    private Toolbar mToolbar;
    private ProgressBar mprogrssBar;
    private static final String market_summary_URL ="http://psxon.com/stock/MarketSummary";
    private AdView adView;
    private Intent intentService;
    public static final String ACTIVITY_NAME="market_summary";
    private static InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_summary_activity);

        setUpToolbar();

        mprogrssBar = (ProgressBar) findViewById(R.id.page_progress);

        mWebView = (WebView) findViewById(R.id.market_summary_webView);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        mWebView.setHorizontalScrollBarEnabled(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        mWebView.setWebViewClient(new MyAppWebClient());
        mWebView.loadUrl(market_summary_URL);


        settingUpAdView();
        settingUpInterstitialAd();

        intentService = new Intent(Market_summary_activity.this, InterstitialAdService.class);
        intentService.putExtra("market_summary",ACTIVITY_NAME);
        startService(intentService);
    }

    private void settingUpInterstitialAd() {
        //For Interstitial Ad
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.Interstitial_unit_id));
        interstitialAd.loadAd(new AdRequest.Builder().build());
    }

    public static void showInterstitialAd(){
        if(interstitialAd.isLoaded()){
            interstitialAd.show();
        }
    }

    private void settingUpAdView() {

        adView = (AdView) findViewById(R.id.market_summary_adView);
        MobileAds.initialize(Market_summary_activity.this,getString(R.string.Admob_App_id));
        adView.loadAd(new AdRequest.Builder().build());
    }

    //setting the Toolbar
    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.market_summary_app_bar);
        mToolbar.setTitle("Market Summary");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {

        if(mWebView.canGoBack()){
            mWebView.goBack();
        }
        else{
            super.onBackPressed();
        }

    }

    public class MyAppWebClient extends WebViewClient{
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            mprogrssBar.setVisibility(View.GONE);
            mWebView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopService(intentService);
    }
}
