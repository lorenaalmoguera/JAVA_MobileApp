package es.umh.dadm.mispelis48796558b;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class Plataformdetails extends SQLiteOpenHelper {

    /**
     * Constructor general que crea todas las tablas
     * @param context actual
     */
    public Plataformdetails(Context context){
        super(context, "BDPelis.db", null, 1);
    }

    /**
     * Creacion de la tabla
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
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
        db.execSQL("drop TABLE if exists Platformdetails");
    }

    /**
     * Establece los valores a meter en la base de datos
     * @param plataforma objeto
     * @return contentvalues
     */
    private ContentValues asignarValoresPlat(Plataforma plataforma){
        ContentValues nuevaPlataforma = new ContentValues();
        nuevaPlataforma.put("email", plataforma.email);
        nuevaPlataforma.put("image_data", plataforma.image_data);
        nuevaPlataforma.put("nombre", plataforma.nombre);
        nuevaPlataforma.put("url", plataforma.url);
        nuevaPlataforma.put("usuario", plataforma.usuario);
        nuevaPlataforma.put("password", plataforma.password);

        return nuevaPlataforma;
    }

    /**
     * Funcion apra insertar los valores dentro de la tala de plataformas
     * @param plataforma objeto
     * @return boolean
     */
    public Boolean insertarPlataforma(Plataforma plataforma) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues nuevaPlataforma = asignarValoresPlat(plataforma);
        long result = db.insert("Platformdetails", null, nuevaPlataforma);
        if(result == -1) return false;
        return true;
    }

    /**
     * Funcion para borrar la plataforma
     * @param idPlatform de plataforma
     * @return boolean
     */
    public Boolean borrarPlataforma(int idPlatform) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete("Platformdetails", "idPlatform=?", new String[]{String.valueOf(idPlatform)});
        if (result == -1) return false;
        return true;
    }


    /**
     * Funcion para actualizar los datos
     * @param plataforma objeto
     * @param Platformid id
     * @return boolean
     */
    public boolean actualizarplataforma(Plataforma plataforma, int Platformid){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues platUpdate = asignarValoresPlat(plataforma);
        Cursor cursor = db.rawQuery("Select * from Platformdetails WHERE idPlatform=?", new String[]{String.valueOf(Platformid)});
        if(cursor.getCount() > 0 ){
            long result = db.update("Platformdetails", platUpdate, "idPlatform = ?", new String[]{String.valueOf(Platformid)});
            if(result==-1) return false;
        }
        return true;
    }

    /**
     * Funcion para indicar si existen plataformas
     * @param email del creador
     * @return boolean
     */
    public Boolean existenPlataformas(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM Platformdetails WHERE email=?", new String[]{email});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count > 0;
    }

    /**
     * Funcion que devuelve un array de plataformas
     * @param email creador
     * @return array
     */
    public ArrayList<Plataforma> obtenerPlataformas(String email){
        ArrayList<Plataforma> listaPlataformas = new ArrayList<>();
        SQLiteDatabase miplatdb = this.getReadableDatabase();
        Cursor cursor = miplatdb.rawQuery("SELECT * FROM Platformdetails WHERE email=? ORDER BY LOWER(nombre) ASC", new String[]{email});
        if(cursor.moveToFirst()){
                int idPlatform_index = cursor.getColumnIndex("idPlatform");
                int email_index = cursor.getColumnIndex("email");
                int imageData_index =cursor.getColumnIndex("image_data");
                int name_index = cursor.getColumnIndex("nombre");
                int url_index = cursor.getColumnIndex("url");
                int user_index = cursor.getColumnIndex("usuario");
                int pass_index = cursor.getColumnIndex("password");
                if(idPlatform_index == -1 || email_index == -1 || imageData_index == -1 || name_index == -1 || url_index == -1 || user_index == -1 || pass_index == -1){
                    Log.e("DB", "No se encuentra(n) una(varias) columna(s)");
                    cursor.close();
                    return null;
                }
            do{
                int idPlatform = cursor.getInt(idPlatform_index);
                byte[] imageData = cursor.getBlob(imageData_index);
                String name = cursor.getString(name_index);
                String url = cursor.getString(url_index);
                String user = cursor.getString(user_index);
                String pass = cursor.getString(pass_index);

                Plataforma miplataforma = new Plataforma(idPlatform, imageData, email, name, url, user, pass);
                listaPlataformas.add(miplataforma);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return listaPlataformas;
    }


}