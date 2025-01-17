package es.umh.dadm.mispelis48796558b;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity {
    DBPelis midb;
    private String auxStr = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_login);
        LinearLayout linearLayout = findViewById(R.id.loginactivity_id);
        AnimationDrawable animationDrawable = (AnimationDrawable) linearLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();
        midb = new DBPelis(this);
    }

    /**
     * Funcion que inicializa la actividad de registrarse
     * @param view actual
     */
    public void accederARegistro(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Definir un callback como respuesta a un diálogo mostrado
     */
    public interface DialogCallback {
        void onResponse(boolean isCorrect);
    }

    /**
     * AlertDialog
     * @param pregunta de seguridad
     * @param respuesta de seguridad
     * @param callback respuesta al dialogo
     */
    public void mostrarMensaje(String pregunta, String respuesta, DialogCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(pregunta);
        final EditText input = new EditText(this);

        //permitimos al usuario escribir
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton(R.string.str_AlertDIalog_aceptar, null);

        final AlertDialog dialog = builder.create();

        dialog.setOnShowListener(dialogInterface -> {
            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(view -> {

                //obtenemos el input
                String userInput = input.getText().toString();
                if (respuesta.equalsIgnoreCase(userInput)) {
                    Toast.makeText(LoginActivity.this, "Bienvenido a mispelis48796558b", Toast.LENGTH_LONG).show();
                    callback.onResponse(true);
                    dialogInterface.dismiss();
                } else {
                    Toast.makeText(LoginActivity.this, "La respuesta de seguridad NO es correcta", Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                    callback.onResponse(false);
                }
            });
        });
        dialog.show();
    }

    /**
     * Funcion para iniciar sesion se encarga de autenticar tood
     * @param view actual
     */
    public void iniciarSesion(View view) {
        EditText correo = findViewById(R.id.id_inputCorreo);
        EditText password = findViewById(R.id.id_inputPassword);

        String correoStr = correo.getText().toString();
        String passwordStr = password.getText().toString();
        String Si = getResources().getString(R.string.authSpinner_S);
        Log.i("funciona", "funciona");

        if(midb.isPasswordCorrect(correoStr, passwordStr)){ // entra si la contraseña es correcta
            Log.i("entra aqui", "entra aqui");

            if(midb.is4AuthCorrect(correoStr, Si)) {
                String pregunta= midb.is4AuthPregunta(correoStr);
                String respuesta= midb.is4AuthRespuesta(correoStr);

                //llamamos al alertdialog
                mostrarMensaje(pregunta, respuesta, isCorrect -> {
                    //si es correcto entra
                    if (isCorrect) {
                        Intent nextIntent = new Intent(LoginActivity.this, ElegirpelioplatActivity.class);
                        nextIntent.putExtra("email", correoStr);
                        startActivity(nextIntent);
                        finish();
                    }
                });
            }else{
                Intent nextintent = new Intent(LoginActivity.this, ElegirpelioplatActivity.class);
                nextintent.putExtra("email", correoStr);
                startActivity(nextintent);
                finish();
            }

        }else{
            Toast.makeText(LoginActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
        }
    }
}