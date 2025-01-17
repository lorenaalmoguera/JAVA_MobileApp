package es.umh.dadm.mispelis48796558b;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Editarmiplataforma extends AppCompatActivity {

    DBPelis midb;
    Plataformdetails miplatdb;
    private static final int PERMISO_CAMRA = 123;
    private static final int CAPTURAR_IMAGEN = 1;
    private static final int SELECCIONAR_IMAGEN_GALERIA = 2;
    private static final int CAPTURAR_IMAGENYGUARDAR = 3;
    private ImageView imgCaptura;
    int platformid;
    private String currentPhotoPath;

    private Button nuevaFoto, elegirFoto, nuevaFotog;
    public byte[] imageData, imgPlaceHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_editarmiplataforma);

        // Inicializar la base de datos y detalles de la plataforma
        midb = new DBPelis(this);
        miplatdb = new Plataformdetails(this);

        // Recuperar datos del intent
        Intent newIntent = getIntent();
        String email = newIntent.getStringExtra("email");
        platformid = newIntent.getIntExtra("idPlatform", -1);
        imgPlaceHolder = newIntent.getByteArrayExtra("image_data");

        nuevaFoto = (Button)findViewById(R.id.fotobtn);
        elegirFoto = (Button)findViewById(R.id.gallerybtn);
        nuevaFotog = (Button)findViewById(R.id.fotogallerybtn);
        Bitmap bitmapImage = BitmapFactory.decodeByteArray(imgPlaceHolder, 0, imgPlaceHolder.length);
        imgCaptura = findViewById(R.id.editarImageViewPelicula);
        imgCaptura.setImageBitmap(bitmapImage);

        // Log para verificar los datos recibidos
        Log.d("EditarplataformaActivity", "Email: " + email);
        Log.d("EditarplataformaActivity", "Platform ID: " + platformid);
        if (imgPlaceHolder != null) {
            Log.d("EditarplataformaActivity", "Image data received, length: " + imgPlaceHolder.length);
        } else {
            Log.d("EditarplataformaActivity", "No image data received");
        }

        imgCaptura = findViewById(R.id.editarImageViewPelicula);

        // Revisar si platformId fue correctamente recibido
        if(platformid == -1) {
            Log.e("Error en el intent", "Error al guardar platformid, se guardó como -1");
        }

        nuevaFoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(!checkperm()) giveperm();
                Intent mi_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(mi_intent, CAPTURAR_IMAGEN);
            }
        });

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

        nuevaFotog.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                // Creación del archivo
                File photoFile = null;
                try {
                    photoFile = crearImagenCompleta();
                } catch (Exception ex) {
                    // Error al crear el archivo
                    Log.e("btnCapturarCompleta", "Error al crear la imagen " + ex);
                    ex.printStackTrace();
                }
                // Solo se continua si hay éxito
                if (photoFile != null) {

                    Uri photoURI = FileProvider.getUriForFile(Editarmiplataforma.this,
                            "es.umh.dadm.mispelis48796558b.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                    startActivityForResult(takePictureIntent, CAPTURAR_IMAGENYGUARDAR);
                }
            }
        });
    }

    /**
     * Comprobación de permisos
     *
     * @return boolean
     */
    private boolean checkperm(){
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Dar permisos
     */
    private void giveperm(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISO_CAMRA);
    }

    /**
     * Comprobar el formato de la url. acaba en .com , .net , .es, .de, .uk, .it, .fr, .org permitidos
     * @param string url a comprobar
     * @return boolean
     */
    public boolean isurlvalid(String string){
        String[] str_end = { ".com" , ".net", ".es", ".de", ".uk", ".it", ".fr", ".org"};

        for(String fin: str_end){
            if(string.toLowerCase().endsWith(fin)) return true;
        }
        return false;
    }

    /**
     * Función para mostrar un alert dialog
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
     * Función para actualizar las películas. Igual a la de registrar plataforma, solo que actualiza los datos al final.
     * @param view actual
     */
    public void actualizarPlat(View view) {
        int error = 0;
        boolean checkurl = true, checkname = true, checkuser = true, checkpass = true, checkimgnull = true;

        EditText editNombre = findViewById(R.id.input_platname_rp);
        EditText editUrl = findViewById(R.id.input_urlname_rp);
        EditText editUser = findViewById(R.id.input_user_rp);
        EditText editPass = findViewById(R.id.input_password_rp);

        String nombre_str = editNombre.getText().toString();
        String url_str = editUrl.getText().toString();
        String user_str = editUser.getText().toString();
        String pass_str = editPass.getText().toString();
        String mensaje = "";
        String space = getString(R.string.Str_Jump);
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");

        if(nombre_str.length() < 2){
            error++;
            checkname = false;
        }
        if(user_str.length() < 1){
            error++;
            checkuser = false;
        }
        if(pass_str.length() < 5){
            error++;
            checkpass = false;
        }
        if(!isurlvalid(url_str)){
            error++;
            checkurl = false;
        }

        if(imageData == null){
            error++;
            checkimgnull = false;
        }

        if(error > 0){
            if(!checkimgnull) mensaje += space + getString(R.string.Str_AlertDialog_imgobligatoria);
            if(!checkname) mensaje += space + getString(R.string.Str_AlertDialog_namePlat);
            if(!checkurl) mensaje += space + getString(R.string.Str_AlertDialog_url);
            if(!checkuser) mensaje += space + getString(R.string.Str_AlertDialo_user);
            if(!checkpass) mensaje += space + getString(R.string.Str_AlertDialog_PassPlat);
            mostrarMensaje(mensaje);
            Log.i("Prueba", "Entra aquí");
        }else{
            Plataforma miplataforma = new Plataforma(imageData, email, nombre_str, url_str, user_str, pass_str);
            Log.i("valor platid", String.valueOf(platformid));
            boolean checkUpdate = miplatdb.actualizarplataforma(miplataforma, platformid);
            if(checkUpdate){
                Toast.makeText(Editarmiplataforma.this, "La plataforma ha sido actualizada", Toast.LENGTH_SHORT).show();
                intent = new Intent(Editarmiplataforma.this, GeneralplataformaActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(Editarmiplataforma.this, "Error al actualizar la plataforma", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Función para volver a la actividad anterior
     * @param view actual
     */
    public void volverAtras(View view) {
        int title = R.string.str_Login_AlertDialog_Title;
        int si = R.string.str_AlertDialog_Si;
        int no = R.string.str_AlertDialog_No;
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setPositiveButton(si, (dialog, which) -> {
                    Intent currentIntent = getIntent();
                    String email = currentIntent.getStringExtra("email");
                    if (email != null) {
                        Intent newIntent = new Intent(Editarmiplataforma.this, GeneralplataformaActivity.class);
                        newIntent.putExtra("email", email);
                        Log.i("ResplataformActivity", "Email passed to GeneralplataformaActivity: " + email);
                        startActivity(newIntent);
                        finish();
                    } else {
                        Log.e("ResplataformActivity", "Email is null. Cannot proceed.");
                        Toast.makeText(Editarmiplataforma.this, "Error: Email not available", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton(no, null)
                .show();
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
     * @throws IOException en caso de que no se haya podido realizar
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


    /**
     * Función para convertir un bitmap a un array de bytes
     * @return array de bytes
     */
    private byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

}