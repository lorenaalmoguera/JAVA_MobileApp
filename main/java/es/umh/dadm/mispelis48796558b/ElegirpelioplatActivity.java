package es.umh.dadm.mispelis48796558b;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class ElegirpelioplatActivity extends AppCompatActivity {

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_elegirpelioplat);
        LinearLayout linearLayout = findViewById(R.id.elegir_pelioplatid);
        AnimationDrawable animationDrawable = (AnimationDrawable) linearLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();
        Intent newintent = getIntent();
        email = newintent.getStringExtra("email"); // Almacenar el email como variable de instancia
    }

    /**
     * Abrir apartado plataformas
     * @param view actual
     */
    public void openPlat(View view) {
        Intent newIntent = new Intent(ElegirpelioplatActivity.this, GeneralplataformaActivity.class);
        newIntent.putExtra("email", email);
        startActivity(newIntent);
        finish();
    }

    /**
     * Abrir aopartado peliculas
     * @param view actual
     */
    public void openPelis(View view) {
        Intent miIntent = new Intent(ElegirpelioplatActivity.this, GeneralpeliculaActivity.class);
        miIntent.putExtra("email", email);
        startActivity(miIntent);
        finish();
    }

    /**
     * Volver atras
     * @param view actual
     */
    public void volverAtras(View view) {
        Intent intent = getIntent();
        Intent newIntent = new Intent(ElegirpelioplatActivity.this, LoginActivity.class);
        startActivity(newIntent);
        finish();
    }
}