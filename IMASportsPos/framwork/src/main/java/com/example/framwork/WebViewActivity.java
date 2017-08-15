package com.example.framwork;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.widget.Toast;

import com.example.framwork.baseapp.AppManager;
import com.example.framwork.widget.CustomWebView;
import com.example.framwork.widget.customtoolbar.CommonTitle;


/**
 * Created by gaoy on 2017/1/21.
 * webview
 */

public class WebViewActivity extends AppCompatActivity {
    private CommonTitle actionBar;
    private CustomWebView webview;
    private String url;
    private String title;
    private int titleBg;
    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> uploadMessage;
    public static final int REQUEST_SELECT_FILE = 100;
    private final static int FILECHOOSER_RESULTCODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view_ui);
        AppManager.getAppManager().addActivity(this);
        initView();
        initData();
    }


    protected void initView() {
        webview = (CustomWebView) findViewById(R.id.webview);
        actionBar = (CommonTitle) findViewById(R.id.action_bar);
        actionBar.getLeftRes().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            url = intent.getStringExtra("url");
            title = intent.getStringExtra("title");
            titleBg = intent.getIntExtra("title_bg", 0);
        }
        if (titleBg != 0)
            actionBar.setBackgroundResource(titleBg);
        actionBar.setCenterText(title);
        webview.loadUrl(url);
        registerForContextMenu(webview);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode == REQUEST_SELECT_FILE) {
                if (uploadMessage == null)
                    return;
                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                uploadMessage = null;
            }
        } else if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            // Use MainActivity.RESULT_OK if you're implementing WebView inside Fragment
            // Use RESULT_OK only if you're implementing WebView inside an Activity
            Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        } else
            Toast.makeText(getBaseContext(), "Failed to Upload Image", Toast.LENGTH_LONG).show();
    }
}
