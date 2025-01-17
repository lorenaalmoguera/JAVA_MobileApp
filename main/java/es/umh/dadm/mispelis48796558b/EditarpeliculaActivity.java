package es.umh.dadm.mispelis48796558b;

import static android.widget.Toast.LENGTH_SHORT;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EditarpeliculaActivity extends AppCompatActivity {

    DBPelis midb;
    Plataformdetails miplatdb;
    ArrayList<Plataforma> plataformas;
    BDPelis_func bdpelis;
    byte[] imgMovie;
    public byte[] imageData;

    private ImageView imgCaptura;
    String movieName, email, movieLength;
    private static final int PERMISO_CAMRA = 123;
    private static final int CAPTURAR_IMAGEN = 1;
    private static final int SELECCIONAR_IMAGEN_GALERIA = 2;
    private static final int CAPTURAR_IMAGENYGUARDAR = 3;
    private String currentPhotoPath;
    private Button nuevaFoto, elegirFoto, nuevaFotog;
    int idMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editarpelicula);
        midb = new DBPelis(this);
        miplatdb = new Plataformdetails(this);
        bdpelis = new BDPelis_func(this);

        Intent newIntent = getIntent();
        email = newIntent.getStringExtra("email");
        movieName = newIntent.getStringExtra("movieName");
        idMovie = newIntent.getIntExtra("idMovie", -1);  // Usar -1 como valor por defecto para indicar "no encontrado"
        movieLength = newIntent.getStringExtra("MovieLength");
        imgMovie = newIntent.getByteArrayExtra("image_data");
        Log.d("EditarpeliculaActivity", "Email: " + email + ", Movie Name: " + movieName + ", ID Movie: " + idMovie);
        if (idMovie == -1) {
            // Manejar el caso en que idMovie no fue correctamente pasado
            Log.e("EditarpeliculaActivity", "Error: idMovie no recibido");
        }

        nuevaFoto = (Button)findViewById(R.id.fotobtn);
        elegirFoto = (Button)findViewById(R.id.gallerybtn);
        nuevaFotog = (Button)findViewById(R.id.fotogallerybtn);

        imgCaptura = (ImageView)findViewById(R.id.imageViewEditarPelicula);

        if (email != null) {
            Log.i("GeneralplataformaActivity", "Email received: " + email);
        } else {
            Toast.makeText(this, "Email no disponible", Toast.LENGTH_LONG).show();
            Log.e("GeneralplataformaActivity", "Email is null");
        }

        EditText editTextMovieName = this.findViewById(R.id.input_peliname_edit);
        EditText editTextMovieLength = this.findViewById(R.id.input_hora_rm);
        ImageView editImageView = this.findViewById(R.id.imageViewEditarPelicula);
        editTextMovieName.setHint(movieName);
        Bitmap bitmapImage = BitmapFactory.decodeByteArray(imgMovie, 0, imgMovie.length);
        editImageView.setImageBitmap(bitmapImage);


        // declaramos Spinners

        Spinner spinnerGenero = findViewById(R.id.genSpinner);
        Spinner ratingSpinner = findViewById(R.id.ratingSpinner);
        Spinner platformSpinner = findViewById(R.id.platformSpinner);

        // declaramos contenido del spinner

        String[] gen = getResources().getStringArray(R.array.genSpinner);
        ArrayAdapter adapter1 = new ArrayAdapter(this, R.layout.my_selected_item, gen);
        adapter1.setDropDownViewResource(R.layout.my_dropdown_item);
        spinnerGenero.setAdapter(adapter1);

        String[] rating = getResources().getStringArray(R.array.ratingSpinner);
        ArrayAdapter adapter2 = new ArrayAdapter(this, R.layout.my_selected_item, rating);
        adapter2.setDropDownViewResource(R.layout.my_dropdown_item);
        ratingSpinner.setAdapter(adapter2);

        // declaramos spinner con las plataformas
        plataformas = miplatdb.obtenerPlataformas(email);
        String [] plataformasUsuario = plataformasUsuario(plataformas, email);
        ArrayAdapter adapter3 = new ArrayAdapter(this,R.layout.my_selected_item, plataformasUsuario);
        adapter3.setDropDownViewResource(R.layout.my_dropdown_item);
        platformSpinner.setAdapter(adapter3);

        //foto nueva
        nuevaFoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(!checkperm()) giveperm();
                Intent mi_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(mi_intent, CAPTURAR_IMAGEN);
            }
        });

        // elegir una foto del dispositivo
        elegirFoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(!checkperm()) giveperm();
                Intent mi_intent = new Intent();
                mi_intent.setType("image/*");
                mi_intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(mi_intent, "Seleccionar imagen"), SELECCIONAR_IMAGEN_GALERIA);
            }
        });

        // nueva foto y guardar en el dispositivo
        nuevaFotog.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                File photoFile = null;
                try {
                    photoFile = crearImagenCompleta();
                } catch (Exception ex) {
                    // Error occurred while creating the File
                    Log.e("btnCapturarCompleta", "Error al crear la imagen " + ex);
                    ex.printStackTrace();
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {

                    Uri photoURI = FileProvider.getUriForFile(EditarpeliculaActivity.this,
                            "es.umh.dadm.mispelis48796558b.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                    startActivityForResult(takePictureIntent, CAPTURAR_IMAGENYGUARDAR);
                }
            }
        });
    }

    /**
     * Funcion para comprobar permisos
     * @return devuelve bool
     */
    private boolean checkperm(){
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Funcion para dar permisos
     */
    private void giveperm(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISO_CAMRA);
    }

    /**
     * Convierte un bitmap a un array de bits
     * @param bitmap foto
     * @return byte[]
     */
    private byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    /**
     * Devuelve un String[] con el nombre y la id de las plataformas registradas por el usuario en cuestion
     * @param plataformas array de todas las paltaformas
     * @param email de usuario
     * @return array de plataformas del usuario
     */
    String[] plataformasUsuario(ArrayList<Plataforma> plataformas, String email){
        ArrayList<String> listaIdPlataforma = new ArrayList<>();
        String aux;
        String aux2 = getString(R.string.str_id_de_peli);
        for (Plataforma plataforma : plataformas) {
            if (plataforma.getEmail().equalsIgnoreCase(email)) {
                aux = plataforma.getNombre() + aux2 + String.valueOf(plataforma.getIdPlatform());
                listaIdPlataforma.add(aux);
            }
        }
        // Convertir ArrayList a array
        String[] arrayIdPlataforma = new String[listaIdPlataforma.size()];
        arrayIdPlataforma = listaIdPlataforma.toArray(arrayIdPlataforma);

        return arrayIdPlataforma;
    }

    /**
     * AlertDialog
     * @param Mensaje a lanzar
     */
    public void mostrarMensaje(String Mensaje){

        String title = getString(R.string.str_AlertDialog_Errores);
        int ok = R.string.str_AlertDIalog_aceptar;
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(Mensaje)
                .setNegativeButton(ok, null)
                .show();
    }

    /**
     * Volver a la actividad anterior
     * @param view actual
     */
    public void volverAtras(View view) {
        int title = R.string.str_AlertDialog_Volver_Editar;
        int si = R.string.str_AlertDialog_Si;
        int no = R.string.str_AlertDialog_No;
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setPositiveButton(si, (dialog, which) -> {
                    Intent currentIntent = getIntent();
                    String email = currentIntent.getStringExtra("email");
                    if (email != null) {
                        Intent newIntent = new Intent(EditarpeliculaActivity.this, GeneralpeliculaActivity.class);
                        newIntent.putExtra("email", email);
                        Log.i("ResplataformActivity", "Email passed to GeneralplataformaActivity: " + email);
                        startActivity(newIntent);
                        finish();
                    } else {
                        Log.e("ResplataformActivity", "Email is null. Cannot proceed.");
                        Toast.makeText(EditarpeliculaActivity.this, "Error: Email not available", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton(no, null)
                .show();
    }

    /**
     * Funcion que quita una parte del string (nos ayudará a guardar la id)
     * @param aPartir mensaje
     * @param aQuitar parte sobrante
     * @return la id
     */
    public int obtainId(String aPartir, String aQuitar){
        String[] partir = aPartir.split(aQuitar,2);
        String id_str = partir[1].trim();
        return Integer.parseInt(id_str);
    }

    /**
     * Función que comprueba si el formato de tiempo está bien
     * @param time insertado por el usuario
     * @return verdadero o falso
     */
    public boolean isTimeFromatCorrect(String time){
        String pattern = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$";
        return time.matches(pattern);
    }

    /**
     * Función parecida a la de registrar película. Sin embargo, actualizará esta al final.
     * @param view actual
     */
    public void ActualizarPeli(View view) {
        int error = 0;
        boolean checkname = true, checkmovieLength = true, checkimgnull = true;
        ArrayList<Pelicula> arrayPelicula;
        Intent intent = new Intent();
        EditText nombrePeli_editText = findViewById(R.id.input_peliname_edit);
        EditText timePeli_editText = findViewById(R.id.input_hora_rm);

        Spinner spinnerGenero = findViewById(R.id.genSpinner);
        Spinner spinnerRating = findViewById(R.id.ratingSpinner);
        Spinner spinnerPlatform = findViewById(R.id.platformSpinner);

        String Mensaje ="";
        String space = " ";
        String nombrePeli = nombrePeli_editText.getText().toString();
        String movieLength = timePeli_editText.getText().toString();
        String answerSpinnerGenero = spinnerGenero.getSelectedItem().toString();
        String answerSpinnerRating = spinnerRating.getSelectedItem().toString();
        String answerSpinnerPlatform = spinnerPlatform.getSelectedItem().toString();

        int idPlataforma = obtainId(answerSpinnerPlatform, getString(R.string.str_id_de_peli));
        int rating = Integer.parseInt(answerSpinnerRating);

        if(!isTimeFromatCorrect(movieLength)){
            error++;
            checkmovieLength = false;
        }
        if(nombrePeli.length()<1){
            error++;
            checkname = false;
        }

        if(imageData == null){
            error++;
            checkimgnull = false;
        }

        if(error > 0){
            if(!checkname) Mensaje += space + getString(R.string.Str_AlertDialog_MovieName);
            if(!checkmovieLength) Mensaje += space + getString(R.string.Str_AlertDialog_MovieLength);
            if(!checkimgnull) Mensaje += space + getString(R.string.Str_AlertDialog_imgobligatoria);
            mostrarMensaje(Mensaje);
        }else{
            Pelicula pelicula = new Pelicula(email, idPlataforma, imageData, nombrePeli, movieLength, answerSpinnerGenero, rating);
            Boolean chekinsertdata = bdpelis.updatePelicula(pelicula, idMovie);
            if(chekinsertdata){
                Log.i("Prueba", "No entra aquí");
                Toast.makeText(EditarpeliculaActivity.this, "Se ha actualizado la película", LENGTH_SHORT).show();
                intent = new Intent(EditarpeliculaActivity.this, GeneralpeliculaActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(EditarpeliculaActivity.this, "Actualización fallida", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Función para actualizar la caratula de la imagen. Guardará y creará, creará o escogerá una imagen de la galería
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     *
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAPTURAR_IMAGEN){
            if(resultCode == RESULT_OK){
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                imgCaptura.setImageBitmap(bp);
                assert bp != null;
                imageData = convertBitmapToByteArray(bp);
            }
        } else if (requestCode == CAPTURAR_IMAGENYGUARDAR) {
            if(resultCode == RESULT_OK){
                Bitmap myBitmap = BitmapFactory.decodeFile(currentPhotoPath);
                imgCaptura.setImageBitmap(myBitmap);
                assert myBitmap != null;
                imageData = convertBitmapToByteArray(myBitmap);
                Toast.makeText(this, "La foto se ha guardado bajo el path: /storage/emulated/0/Pictures", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == SELECCIONAR_IMAGEN_GALERIA) {
            if(resultCode == RESULT_OK){
                Uri uri = data.getData();
                try {
                    imgCaptura.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri));
                    imageData = convertBitmapToByteArray(MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }else if(resultCode == RESULT_CANCELED){
            Toast.makeText(this, "CANCELADO", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Función para crear una imagen dentro del dispositivo
     * @return fichero de la imagen
     * @throws IOException en caso de que no sé pueda crear
     */
    private File crearImagenCompleta() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format( new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        //Opciones almacenamiento Android: https://developer.android.com/training/data-storage

        //Almacenamiento interno (no visible)
        //Context.getExternalFilesDir(null)  /storage/emulated/0/Android/data/com.example.desim.ejemplocamaragaleria/files
        //Otras rutas posibles que son públicas
        //Environment.getExternalStorageDirectory()  /storage/emulated/0
        //Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)  /storage/emulated/0/Pictures

        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        //Se puede utilizar cualquier ruta
        //File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        /*
        Para ciertas versiones se debe pedir permisos expresamente para poder crear ficheros sino nos fallará
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(intent);
            }
        }*/
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

}