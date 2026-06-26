package az.ustan.app;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
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
        
        // Avtomatik səs problemini həll edən əsas kod:
        webSettings.setMediaPlaybackRequiresUserGesture(false);

        // 🛠️ XƏRİTƏ, TELEFON VƏ INTENT LİNKLƏRİNİN TAM HƏLLİ
        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                
                // 1. Şəkildəki INTENT:// problemini həll edən hissə (Mütləqdir!)
                if (url.startsWith("https://")) {
                    try {
                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        if (intent != null) {
                            // Sistemi yoxlayır, əgər telefonda Google Maps tətbiqi varsa, onu açır
                            startActivity(intent);
                            return true;
                        }
                    } catch (Exception e) {
                        // Əgər telefonda xəritə tətbiqi yoxdursa, brauzer versiyasına (fallback) yönləndirir
                        try {
                            Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                            String fallbackUrl = intent.getStringExtra("browser_fallback_url");
                            if (fallbackUrl != null) {
                                view.loadUrl(fallbackUrl);
                                return true;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    return true;
                }

                // 2. Standart Xəritə və Zəng linkləri üçün filter
                if (url.startsWith("geo:") || 
                    url.contains("maps.google.com") || 
                    url.contains("google.com/maps") || 
                    url.contains("yandex.ru/maps") ||
                    url.startsWith("tel:")) {
                    
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        return true; 
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }
                
                return false; 
            }
        });

        // "QƏBUL ET" (CONFIRM) DÜYMƏSİNİN DONMA PROBLEMİNİN HƏLLİ
        myWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                new AlertDialog.Builder(MainActivity.this)
                    .setMessage(message)
                    .setPositiveButton("Bəli", (dialog, which) -> result.confirm())
                    .setNegativeButton("Xeyr", (dialog, which) -> result.cancel())
                    .setCancelable(false)
                    .create()
                    .show();
                return true;
            }
        });

        // Saytı yükləyirik
        myWebView.loadUrl("https://ustan.az/master/dashboard");
    }

    @Override
    public void onBackPressed() {
        if (myWebView.canGoBack()) { myWebView.goBack(); } else { super.onBackPressed(); }
    }
}
