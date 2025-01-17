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

public class GeneralpeliculaActivity extends AppCompatActivity {
    DBPelis midb;
    Plataformdetails miplatdb;
    BDPelis_func miPelisdb;
    ArrayList<Pelicula> arrayPeli;
    ListView lvPeli;
    PeliculaAdaptador adaptadorPeli;

    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_generalpelicula);

        //animacion
        LinearLayout linearLayout = findViewById(R.id.generalPelis);
        AnimationDrawable animationDrawable = (AnimationDrawable) linearLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();

        midb = new DBPelis(this);
        miplatdb = new Plataformdetails(this);
        miPelisdb = new BDPelis_func(this);

        Intent newIntent = getIntent();
        email = newIntent.getStringExtra("email");

        if (email != null) {
            Log.i("GeneralplataformaActivity", "Email recibido: " + email);
        } else {
            Toast.makeText(this, "Email no disponible", Toast.LENGTH_LONG).show();
            Log.e("GeneralplataformaActivity", "Email is null");
        }
        cargarPelis(email);

    }

    /**
     * Boton registrar peliculas
     * @param view actual
     */
    public void peliculaAddRes(View view) {
        if(!miplatdb.existenPlataformas(email)){
            Toast.makeText(this, "No existen plataformas en las que usted pueda registrar una película.", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent(this, RespeliculasActivity.class);
            intent.putExtra("email", getIntent().getStringExtra("email"));
            startActivity(intent);
            finish();
        }

    }

    /**
     * Cargar peliculas actuales
     * @param email usuario
     */
    public void cargarPelis(String email){
        arrayPeli = miPelisdb.obtenerPeliculasTodas(email);

        lvPeli = (ListView)findViewById(R.id.listViewPelis);

        Log.d("Pelis", "Número de películas cargadas: " + (arrayPeli != null ? arrayPeli.size() : "null"));


        if (arrayPeli != null && !arrayPeli.isEmpty()) {
            //si no está vacio el array de peliculas llama al adaptador
            adaptadorPeli = new PeliculaAdaptador(this, arrayPeli);
            lvPeli.setAdapter(adaptadorPeli);

        } else {
            Toast.makeText(this, "No hay películas disponibles o error al cargar", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Funcion para volver atras
     * @param view actual
     */
    public void volverAtras(View view) {

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        Intent newIntent = new Intent(GeneralpeliculaActivity.this, ElegirpelioplatActivity.class);
        newIntent.putExtra("email", email);
        startActivity(newIntent);
        finish();
    }


    /**
     * Funcion para guardar las peliculas en un fichero .csv
     * @param view actual
     */
    public void guardarDisp(View view) {

        String filename = getString(R.string.str_fichero_peliculas);
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(dir, filename);

        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            String header = getString(R.string.str_fichero_peliculas_header) + "\n";
            fileOutputStream.write(header.getBytes());

            for (Pelicula pelicula : arrayPeli) {
                String fila = pelicula.getMovieName() + "," +
                        pelicula.getMovieGenre() + "," +
                        pelicula.getMovieLength() + "," +
                        pelicula.getMovieRating() + "\n";
                fileOutputStream.write(fila.getBytes());
            }

            Toast.makeText(this, "Datos exportados de manera correcta", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error al exportar los datos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}