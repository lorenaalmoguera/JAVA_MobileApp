
package es.umh.dadm.mispelis48796558b;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    DBPelis midb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_register);
        Spinner spinnerPreguntas = findViewById(R.id.mispinner);
        Spinner authSpinner = findViewById(R.id.authspinner);

        String[] preguntas_s = getResources().getStringArray(R.array.Spinner_Preguntas);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.my_selected_item, preguntas_s);
        adapter.setDropDownViewResource(R.layout.my_dropdown_item);
        spinnerPreguntas.setAdapter(adapter);

        String[] authSiNo = getResources().getStringArray(R.array.authSpinner);
        ArrayAdapter adapter2 = new ArrayAdapter(this, R.layout.my_selected_item, authSiNo);
        adapter2.setDropDownViewResource(R.layout.my_dropdown_item);
        authSpinner.setAdapter(adapter2);


        midb = new DBPelis(this);
    }

    /**
     * Si se ha checkeado la checkbox que indica otros se muestra el editText para introducir los datos
     * @param view actual
     */
    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        EditText customInterest = findViewById(R.id.check_interes6);

        if (checked) {
            customInterest.setVisibility(View.VISIBLE);
        } else {
            customInterest.setVisibility(View.GONE);
        }
    }

    /**
     * Comrpueba si se ha cliqueado la checkbox
     * @param checkBox a comprobar
     * @return boolean
     */
    public boolean isCheckedtrue(CheckBox checkBox){
        return checkBox.isChecked();
    }

    /**
     * Valida el formato de fecha
     * @param dateString input del usuario
     * @param format formato
     * @return boolean
     */
    public static boolean dateValidator(String dateString, String format) {
        SimpleDateFormat formatoFecha = new SimpleDateFormat(format, Locale.getDefault());

        if(dateString==null) return false;
        try {
            Date date = formatoFecha.parse(dateString);
            String formattedDate = formatoFecha.format(date);
            return formattedDate.equals(dateString);

        } catch (ParseException e) {
            return false;
        }

    }

    /**
     * Valida el correo electrónico
     * @param emailString input del usuario
     * @return boolean
     */
    public static boolean emailValidator(String emailString){
        Pattern pattern = Pattern.compile("^([0-9a-zA-Z]+[-._+&])*[0-9a-zA-Z]+@([-0-9a-zA-Z]+[.])+[a-zA-Z]{2,6}$");
        Matcher matcher = pattern.matcher(emailString);
        return matcher.matches();
    }

    /**
     * Valida el nombre
     * @param nameString  input
     * @return boolean
     */
    public static boolean nameValidator (String nameString){
        Pattern pattern = Pattern.compile("^[a-zA-Z]+(?:[\\s][a-zA-Z]+)*$");
        Matcher matcher = pattern.matcher(nameString);
        return matcher.matches();
    }

    /**
     * Valida la contraseña
     * @param passwordString introducida
     * @return boolean
     */
    public static boolean passwordValidator (String passwordString){
        Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W_]).{8,}$");
        Matcher matcher = pattern.matcher(passwordString);
        return matcher.matches();
    }

    /**
     * Funcion que valida lo introducido
     * @param view actual
     */
    public void validarCampos(View view) {

        int error = 0, i = 0, check1 = 0, check2 = 0, check3 = 0, check4 = 0, check5 = 0;
        boolean datecheck = true, namecheck = true, namecheckinput = true, apellidocheck = true, passwordformatcheck = true, passwordcheck = true, emailcheck = true, emailinputcheck = true, intersetcheck = true, preguntacheck = true;

        Spinner spinnerPreguntas = findViewById(R.id.mispinner);
        Spinner spinnerAuth = findViewById(R.id.authspinner);

        EditText dateInput = findViewById(R.id.editTextDate);
        EditText nameInput = findViewById(R.id.id_inputNombre);
        EditText interestInput = findViewById(R.id.check_interes6);
        EditText emailInput = findViewById(R.id.id_inputCorreo_registrar);
        EditText passwordInput = findViewById(R.id.id_inputPassword_registrar);
        EditText apellidoInput = findViewById(R.id.id_inputApellido);
        EditText inputSeguridad = findViewById(R.id.id_inputSeguridad);

        CheckBox checkBox1 = findViewById(R.id.check_interes1);
        CheckBox checkBox2 = findViewById(R.id.check_interes2);
        CheckBox checkBox3 = findViewById(R.id.check_interes3);
        CheckBox checkBox4 = findViewById(R.id.check_interes4);
        CheckBox checkBox5 = findViewById(R.id.check_interes5);

        String fechaStr = dateInput.getText().toString();
        String nameStr = nameInput.getText().toString();
        String interesStr = interestInput.getText().toString();
        String emailStr = emailInput.getText().toString();
        String passwordStr =passwordInput.getText().toString();
        String Mensaje = getString(R.string.Str_AlertDialogue_Null);
        String dateFormat = getString(R.string.DateFormat);
        String preguntaseguridadStr = spinnerPreguntas.getSelectedItem().toString();
        String resAuth = spinnerAuth.getSelectedItem().toString();


        String answerseguridadStr = inputSeguridad.getText().toString();
        String space = getString(R.string.Str_Jump);
        String apellidoStr = apellidoInput.getText().toString();

        // comprobacion de errores


        if(!dateValidator(fechaStr, dateFormat)){ // obligamos a cumplir el formato
                error++;
                datecheck = false;

        }
        if(emailStr.length() < 1){ //es obligatorio isnertar un correo
            error++;
            emailinputcheck = false;
        }else{
            if(!emailValidator(emailStr)){ //obligamos a cumplir el formato
                error++;
                emailcheck = false;
            }
        }
        if(nameStr.length() < 1){ // es obligatorio insertar un nombre
            error++;
            namecheck = false;
        }else{ // si se inserta obligamos a cumplir el formato
            if(!nameValidator(nameStr)){
                error++;
                namecheckinput = false;
            }
        }

        if(apellidoStr.length() > 0){ // no es obligatorio insertar un apellido
            if(!nameValidator(nameStr)){ // si se inserta debe cumplir el formato
                error++;
                apellidocheck = false;
            }
        }

        if(passwordStr.length() < 1){ // obligamos a insertar password
            error++;
            passwordcheck = false;
        }else{
            if(!passwordValidator(passwordStr)){ // obligamios a cumplir el formato
                error++;
                passwordformatcheck = false;
            }
        }
        if(isCheckedtrue(checkBox1)){ // se inicializa en la db a 1
            i++;
            check1 = 1;
        }
        if(isCheckedtrue(checkBox2)){ // se inicializa en la db a 1
            i++;
            check2 = 1;
        }
        if(isCheckedtrue(checkBox3)){ // se inicializa en la db a 1
            i++;
            check3 = 1;
        }
        if(isCheckedtrue(checkBox4)){ // se inicializa en la db a 1
            i++;
            check4 = 1;
        }
        if(isCheckedtrue(checkBox5) && !nameValidator(interesStr)){ // si se ha selecionado el interes adicional pero el formato no es correcto
            intersetcheck = false;
            i++;
            error++;
        }else if(isCheckedtrue(checkBox5) && nameValidator(interesStr)){ // si se ha selecionado el interes adicional pero el formato si es correcto
            i++;
        }else if(isCheckedtrue(checkBox5)){ // se inicializa en la db a 1
            check5 = 1;
        }else if(!isCheckedtrue(checkBox5)){ // se inicializa el campo a " " en caso de no haber seleccionado el interes extra
            interesStr = " ";
        }
        if(!(answerseguridadStr.length()>0)){
            preguntacheck = false;
            preguntaseguridadStr = " ";
            answerseguridadStr = " ";
        }

        if(i < 1){
            error++;
        }

        // si existen errores
        if(error > 0) {
            if (!namecheck) Mensaje += space + getString(R.string.Str_AlertDialog_Nombre);
            if (!namecheckinput) Mensaje += space + getString(R.string.Str_AlertDialog_Nombre_formato);
            if (!apellidocheck) Mensaje += space + getString(R.string.Str_AlertDialog_Apellido_formato);
            if (!passwordcheck) Mensaje += space + getString(R.string.Str_AlertDialog_Pass);
            if (!passwordformatcheck) Mensaje += space + getString(R.string.Str_AlertDialog_Pass_formato);
            if (!datecheck) Mensaje += space + getString(R.string.Str_AlertDialog_Fecha);
            if (!emailcheck) Mensaje += space + getString(R.string.Str_AlertDialog_Email_formato);
            if (!emailinputcheck) Mensaje += space + getString(R.string.Str_AlertDialog_Email);
            if(!intersetcheck) Mensaje += space + getString(R.string.Str_AlertDialog_Interes_Vacio);
            if(!preguntacheck) Mensaje += space + getString(R.string.Str_AlertDialog_PreguntaSeguridad);
            if(i < 1) Mensaje += space + getString(R.string.Str_AlertDialog_N_intereses);

            // mensaje con los errores
            mostrarMensaje(Mensaje);
        }else{

            // sino se procede a crear el usuario
            Usuario usuario = new Usuario(nameStr, apellidoStr, emailStr, passwordStr, fechaStr, preguntaseguridadStr, answerseguridadStr, resAuth, check1, check2, check3, check4, check5, interesStr);
            Boolean checkinsertdata = midb.insertuserdata(usuario);

            // se comprueba si se peude crear
            if(checkinsertdata){
                Toast.makeText(RegisterActivity.this, "Se ha registrado al usuario", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, ElegirpelioplatActivity.class);
                intent.putExtra("email", emailStr);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(RegisterActivity.this, "No se ha podido crear el usuario", Toast.LENGTH_SHORT).show();

            }
        }
    }

    /**
     * Funcion para mostrar un alert dialog
     * @param Mensaje a introducir
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
     * Funcion que permite volver atras
     * @param view actual
     */
    public void volverAtras(View view) {
        int title = R.string.str_Login_AlertDialog_Title;
        int si = R.string.str_AlertDialog_Si;
        int no = R.string.str_AlertDialog_No;
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setPositiveButton(si, (dialog, which) -> {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton(no, null)
                .show();
    }
}
