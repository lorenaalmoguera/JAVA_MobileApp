package es.umh.dadm.mispelis48796558b;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class GeneralplataformaActivity extends AppCompatActivity {
    DBPelis midb;
    Plataformdetails miplatdb;
    BDPelis_func mipelidb;
    ArrayList<Plataforma> arrayPlat;
    ArrayList<Pelicula> arrayPeli;
    ListView lvPlat;
    PlataformaAdaptador adaptadorPlat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_generalplataforma);
        LinearLayout linearLayout = findViewById(R.id.generalPlat);
        AnimationDrawable animationDrawable = (AnimationDrawable) linearLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();
        midb = new DBPelis(this);
        miplatdb = new Plataformdetails(this);
        mipelidb = new BDPelis_func(this);
        Intent newIntent = getIntent();
        String email = newIntent.getStringExtra("email");
        // Log y Toast para informar si el email está presente o no
        if (email != null) {
            Log.i("GeneralplataformaActivity", "Email received: " + email);
        } else {
            Toast.makeText(this, "Email no disponible", Toast.LENGTH_LONG).show();
            Log.e("GeneralplataformaActivity", "Email is null");
        }
        arrayPeli = mipelidb.obtenerPeliculasTodas(email);
        cargarPlat(email);

    }


    /**
     * Añadir plataforma nueva
     * @param view actual
     */
    public void platformAddRes(View view) {
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        Intent newIntent = new Intent(GeneralplataformaActivity.this, ResplataformActivity.class);
        newIntent.putExtra("email", email);
        startActivity(newIntent);
        finish();
    }

    /**
     * Cargar plataformas existentes
     * @param email usuario
     */
    public void cargarPlat(String email){
        arrayPlat = miplatdb.obtenerPlataformas(email);
        lvPlat = (ListView)findViewById(R.id.listViewPlataforma);

        if (arrayPlat != null && !arrayPlat.isEmpty()) {

            adaptadorPlat = new PlataformaAdaptador(this,arrayPlat, arrayPeli, email);
            lvPlat.setAdapter(adaptadorPlat);

        } else {
            Toast.makeText(this, "No hay plataformas disponibles o error al cargar", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Funcion para volver atras
     * @param view actual
     */
    public void volverAtras(View view) {
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        Intent newIntent = new Intent(GeneralplataformaActivity.this, ElegirpelioplatActivity.class);
        newIntent.putExtra("email", email);
        startActivity(newIntent);
        finish();
    }

    /**
     * Funcion para guardar en el dispositivo
     * @param view actual
     */
    public void guardarDisp(View view) {
        // Nombre del archivo basado en recursos de string
        String filename = getString(R.string.str_fichero_plataforma);
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(dir, filename);

        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            // Encabezado del archivo, con salto de línea al final
            String header = getString(R.string.str_fichero_plat_header) + "\n";
            fileOutputStream.write(header.getBytes());

            // Iterar sobre cada película para escribir sus detalles
            for (Plataforma plataforma : arrayPlat) {
                String fila = plataforma.getNombre() + "," +
                        plataforma.getUrl() + "," +
                        plataforma.getEmail() + "," +
                        plataforma.getUsuario() + "," +
                        plataforma.getPassword() + ", " + "\n"; // Añade salto de línea
                fileOutputStream.write(fila.getBytes());
            }

            Toast.makeText(this, "Datos exportados de manera correcta", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error al exportar los datos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }
}