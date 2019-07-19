package com.mycroft.sample.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.gyf.immersionbar.ImmersionBar;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultWebClient;
import com.just.agentweb.WebChromeClient;
import com.just.agentweb.WebViewClient;
import com.mycroft.sample.R;
import com.mycroft.sample.common.CommonActivity;
import com.mycroft.sample.model.Article;

/**
 * @author mycroft
 */
public class WebViewActivity extends CommonActivity {

    private static final String EXTRA_ARTICLE = "article.extra";

    public static Intent getIntent(@NonNull Context context, @NonNull Article article) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(EXTRA_ARTICLE, article);
        return intent;
    }

    @Override
    protected int getResId() {
        return R.layout.activity_web_view;
    }

    private Article article;

    @Override
    protected void initFields(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            article = getIntent().getParcelableExtra(EXTRA_ARTICLE);
        } else {
            article = savedInstanceState.getParcelable(EXTRA_ARTICLE);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_ARTICLE, article);
    }

    private AgentWeb agentWeb;

    @Override
    protected void initViews(@Nullable Bundle savedInstanceState) {
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.colorPrimaryDark)
                .statusBarDarkFont(true)
                .init();

        TitleBar titleBar = findViewById(R.id.titleBar);
        titleBar.setTitle(article.getTitle());
        titleBar.setOnTitleBarListener(new OnTitleBarListener() {
            @Override
            public void onLeftClick(View v) {
                finish();
            }

            @Override
            public void onTitleClick(View v) {

            }

            @Override
            public void onRightClick(View v) {

            }
        });

        agentWeb = AgentWeb.with(this)
                .setAgentWebParent(findViewById(R.id.container), new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator(ContextCompat.getColor(this, R.color.colorAccent), 2)
//                .setAgentWebWebSettings(getSettings())
                .setWebViewClient(webViewClient)
                .setWebChromeClient(webChromeClient)
//                .setPermissionInterceptor(mPermissionInterceptor)
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
//                .setAgentWebUIController(new UIController(getActivity()))
                .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
//                .useMiddlewareWebChrome(getMiddlewareWebChrome())
//                .useMiddlewareWebClient(getMiddlewareWebClient())
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.DISALLOW)
                .interceptUnkownUrl()
                .createAgentWeb()
                .ready()
                .go(article.getLink());
    }

    @Override
    protected void loadData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (agentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        agentWeb.getWebLifeCycle().onPause();
        super.onPause();

    }

    @Override
    protected void onResume() {
        agentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        agentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();
    }

    private final WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //do you  work
        }
    };
    private final WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //do you work
        }
    };
}
