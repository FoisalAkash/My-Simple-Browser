package com.example.sky.webview;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.System.load;

public class MainActivity extends AppCompatActivity {
    String currentUrl="https://www.google.com/";
    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;
WebView webView;
EditText editText;
Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        editText = findViewById(R.id.link);
        button = findViewById(R.id.go);
        webView = findViewById(R.id.webview);
        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);

        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe);
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_dark,
                android.R.color.holo_blue_light,
                android.R.color.holo_red_dark,
                android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                swipeRefreshLayout.setRefreshing(true);
                Load(currentUrl);
            }
        });
        Load(currentUrl);
    }
    public void Load(String url)
    {

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    String url=editText.getText().toString();
                    if(url.startsWith("www"))
                    {
                        webView.loadUrl("https://"+url);
                        currentUrl="https://"+url;
                    }
                    else
                    {
                        webView.loadUrl("https://www.google.com/search?q="+url);
                        currentUrl="https://www.google.com/search?q="+url;

                    }

                    return true;
                }
                return false;
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url=editText.getText().toString();
                if(url.startsWith("www"))
                {
                    webView.loadUrl("https://"+url);
                    currentUrl="https://"+url;
                }
                else
                {
                    webView.loadUrl("https://www.google.com/search?q="+url);
                    currentUrl="https://www.google.com/search?q="+url;
                }
            }
        });



        try {


            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDomStorageEnabled(true);
            webView.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress)
                {

                    progressBar.setProgress(progress);

                }


            });
            webView.setWebViewClient(new WebViewClient() {


                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    currentUrl = url;
                    return super.shouldOverrideUrlLoading(view, url);

                }


                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                    Toast.makeText(MainActivity.this, "Error! " + description, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {

                    progressBar.setVisibility(View.VISIBLE);
                    super.onPageStarted(view, url, favicon);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    progressBar.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                    findViewById(R.id.swipe).setVisibility(View.VISIBLE);




                }

            });


            webView.loadUrl(url);


        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "" + e, Toast.LENGTH_SHORT).show();
        }



    }


    @Override
    public void onBackPressed()
    {
        if(webView.canGoBack())
        {
            //Toast.makeText(MainActivity.this,"Loading....please wait",Toast.LENGTH_SHORT).show();
            webView.goBack();
        }
        else
        {
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle("Exit");
            alertDialog.setMessage("Are you sure you want to exit?");
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                    Toast.makeText(MainActivity.this,"Exit Successfully!",Toast.LENGTH_SHORT).show();
                }
            });
            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            alertDialog.setCancelable(false);
            alertDialog.show();
        }
    }
}
