package com.foster.website;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import org.mozilla.geckoview.GeckoRuntime;
import org.mozilla.geckoview.GeckoSession;
import org.mozilla.geckoview.GeckoView;

public class MainActivity extends AppCompatActivity {
    private GeckoSession geckoSession;
    private GeckoRuntime geckoRuntime;
    private GeckoView geckoView;
    private TextView statusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        geckoView = findViewById(R.id.geckoview);
        statusText = findViewById(R.id.status_text);

        geckoSession = new GeckoSession();
        geckoRuntime = GeckoRuntime.create(this);

        geckoSession.open(geckoRuntime);
        geckoView.setSession(geckoSession);

        // Mostrar "Cargando..." al iniciar
        statusText.setText("Cargando web...");

        // Manejar estado de carga
        geckoSession.getProgressDelegate().setDelegate(new GeckoSession.ProgressDelegate() {
            @Override
            public void onPageStart(GeckoSession session, String url) {
                runOnUiThread(() -> statusText.setText("Cargando: " + url));
            }

            @Override
            public void onPageStop(GeckoSession session, boolean success) {
                runOnUiThread(() -> {
                    if (success) {
                        statusText.setText("");
                    } else {
                        statusText.setText("Error al cargar la página");
                    }
                });
            }
        });

        // URL de tu sitio
        geckoSession.loadUri("http://atencioncolas.chatup.pe/screen");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (geckoSession != null) {
            geckoSession.close();
        }
    }
}
