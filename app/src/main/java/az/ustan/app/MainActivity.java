package az.ustan.app;

import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myWebView = findViewById(R.id.siteWebView);
        WebSettings webSettings = myWebView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        CookieManager.getInstance().setAcceptCookie(true);
        
        // 🚨 Avtomatik səs problemini həll edən əsas kod:
        webSettings.setMediaPlaybackRequiresUserGesture(false);

        myWebView.setWebViewClient(new WebViewClient());
        myWebView.loadUrl("https://ustan.az/master/dashboard");
    }

    @Override
    public void onBackPressed() {
        if (myWebView.canGoBack()) { myWebView.goBack(); } else { super.onBackPressed(); }
    }
}
