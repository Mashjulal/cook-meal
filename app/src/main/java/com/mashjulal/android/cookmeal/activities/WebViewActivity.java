package com.mashjulal.android.cookmeal.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mashjulal.android.cookmeal.R;

import static com.mashjulal.android.cookmeal.activities.EditRecipeActivity.PARAM_PAGE_URL;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        WebView webView = (WebView) findViewById(R.id.webView_aWebView_page);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        Bundle args = getIntent().getExtras();
        if (args.containsKey(PARAM_PAGE_URL)) {
            webView.loadUrl(args.getString(PARAM_PAGE_URL));
        }
    }
}
