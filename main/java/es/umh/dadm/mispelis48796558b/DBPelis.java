package es.umh.dadm.mispelis48796558b;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBPelis extends SQLiteOpenHelper {

    /**
     * Constructor general que crea todas las tablas
     * @param context actual
     */
    public DBPelis(Context context){
        super(context, "BDPelis.db", null, 1);
    }

    /**
     * Creacion de las 3 tablas
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE BDPelis(idMovie INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT, idPlatform INTEGER, image_data BLOB, movieName TEXT, MovieLength TEXT, MovieGenre TEXT, MovieRating INTEGER, FOREIGN KEY(email) REFERENCES Userdetails(email), FOREIGN KEY(idPlatform) REFERENCES Platformdetails(idPlatform))");
        db.execSQL("CREATE TABLE Userdetails(name TEXT, apellido TEXT, email TEXT PRIMARY KEY, password TEXT, preguntaS TEXT, resS TEXT, resAuth TEXT, fechanac TEXT, Interes1 INTEGER, Interes2 INTEGER, Interes3 INTEGER, Interes4 INTEGER, Interes5 INTEGER, Interes5Text TEXT)");
        db.execSQL("CREATE TABLE Platformdetails(idPlatform INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT, image_data BLOB, nombre TEXT, url TEXT, usuario TEXT, password TEXT, FOREIGN KEY(email) REFERENCES Userdetails(email))");
    }

    /**
     * Actualiza la tabla si existe
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop TABLE if exists Userdetails");
    }

    /**
     * Funcion que asigna valores en el content value para posteriormente meterlos en la db
     * @param Usuario objeto
     * @return concentValues
     */
    private ContentValues asignarValoreS(Usuario Usuario){
        ContentValues nuevoUsuario = new ContentValues();
        nuevoUsuario.put("name",Usuario.getNombre());
        nuevoUsuario.put("apellido", Usuario.getApellido());
        nuevoUsuario.put("email", Usuario.getEmail());
        nuevoUsuario.put("password", Usuario.getPassword());
        nuevoUsuario.put("preguntaS", Usuario.getPreguntaS());
        nuevoUsuario.put("resS", Usuario.getResS());
        nuevoUsuario.put("resAuth", Usuario.getResAuth());
        nuevoUsuario.put("fechaNac", Usuario.getFechaNac());
        nuevoUsuario.put("Interes1", Usuario.getCheck1());
        nuevoUsuario.put("Interes2", Usuario.getCheck2());
        nuevoUsuario.put("Interes3", Usuario.getCheck3());
        nuevoUsuario.put("Interes4", Usuario.getCheck4());
        nuevoUsuario.put("Interes5", Usuario.getCheck5());
        nuevoUsuario.put("Interes5Text", Usuario.getInteres5());
        return nuevoUsuario;
    }

    /**
     * Funcion para insertar un nuevo usuario en la db devuelve bool para confirmar
     * @param usuario objeto
     * @return booleano
     */
    public Boolean insertuserdata(Usuario usuario) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues nuevoUsuario = asignarValoreS(usuario);
        long result = db.insert("Userdetails", null, nuevoUsuario);
        if(result == -1) return false;
        return true;
    }

    /**
     * Funcion para comprobar la existencia de un usuario
     * @param email introducido
     * @return boolean
     */
    public Boolean doesUserExist (String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from Userdetails where email=?", new String[]{email});
        boolean existe = cursor.getCount() > 0;
        cursor.close();
        return existe;
    }

    /**
     * Funcion para comprobar la contraseña introducida
     * @param email introducido
     * @param password introducido
     * @return boolean
     */
    @SuppressLint("Range")
    public Boolean isPasswordCorrect(String email, String password){
        String auxpassword;
        if(doesUserExist(email)){
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT password FROM Userdetails WHERE email=?", new String[]{email});
            if(cursor.moveToFirst()){
                auxpassword = cursor.getString(cursor.getColumnIndex("password"));
                if(password.equals(auxpassword)) return true;
                return false;
            }
            return false;
        }
        return false;
    }


    /**
     * Funcion para comprobar si la autenticacion es correcta
     * @param email introducido
     * @param Si mensaje
     * @return boolean
     */
    public boolean is4AuthCorrect(String email, String Si) {
        //SEGUIR AQUÍ
        Log.i("Entra aquí en is authcorrect", Si);
        String res;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT resAuth FROM Userdetails WHERE email=?", new String[]{email});
        if(cursor.moveToFirst()){
            int i = cursor.getColumnIndex("resAuth");
            if(i >= 0){
                res = cursor.getString(i);
                if(Si.equals(res)) return true;
            }

        }
        return false;
    }

    /**
     * Funcion que devuelve la respuesta de seguridad
     * @param email introducido
     * @return respuesta de seguridad
     */
    public String is4AuthRespuesta(String email) {
        String res = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT resS FROM Userdetails WHERE email=?", new String[]{email});
        if(cursor.moveToFirst()){
            int i = cursor.getColumnIndex("resS");
            if(i >= 0){
                res = cursor.getString(i);
                Log.i("Valor respuesta", res);
            }

        }
        return res;
    }


    /**
     * Funcion que devuelve la pregunta de seguridad
     * @param email introducido
     * @return pregunta de seguridad
     */
    public String is4AuthPregunta(String email) {
        String pregunta = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT preguntaS FROM Userdetails WHERE email=?", new String[]{email});
        if(cursor.moveToFirst()){
            int i = cursor.getColumnIndex("preguntaS");
            if(i >= 0){
                pregunta = cursor.getString(i);
                Log.i("Valor respuesta", pregunta);
            }

        }
        return pregunta;
    }

}