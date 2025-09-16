package com.foster.website;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.mozilla.geckoview.GeckoRuntime;
import org.mozilla.geckoview.GeckoSession;
import org.mozilla.geckoview.GeckoView;

public class MainActivity extends AppCompatActivity {

    // variables
    GeckoView geckoView;
    GeckoSession geckoSession;
    GeckoRuntime geckoRuntime;
    String url;

    TextView statusText; // para mostrar mensajes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        // abre una nueva sesión (esto DEBE ser antes de setSession o crashea)
        geckoSession.open(geckoRuntime);

        // asigna la sesión al geckoview
        geckoView.setSession(geckoSession);

        // manejar progreso de carga
        geckoSession.setProgressDelegate(new GeckoSession.ProgressDelegate() {
            @Override
            public void onPageStart(GeckoSession session, String url) {
                runOnUiThread(() -> statusText.setText("Cargando..."));
            }

            @Override
            public void onPageStop(GeckoSession session, boolean success) {
                runOnUiThread(() -> {
                    if (success) {
                        statusText.setText(""); // limpiar mensaje
                    } else {
                        statusText.setText("Error al cargar la página");
                    }
                });
            }
        });

        // carga la URL
        geckoSession.loadUri(url);
    }

    // inicializa variables
    private void init() {
        WebView webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);
        setContentView(webView);
        webView.loadUrl("https://google.com");

        // geckoView = findViewById(R.id.geckoview);
        // statusText = findViewById(R.id.status_text); // el TextView del layout
        // geckoSession = new GeckoSession();
        // geckoRuntime = GeckoRuntime.create(this);
        // url = "https://google.com"; // usa http o https según tu server
    }

    // permite retroceder en historial
    @Override
    public void onBackPressed() {
        geckoSession.goBack();
    }

    @Override
    protected void onDestroy() {
        geckoSession.close();
        super.onDestroy();
    }
}
