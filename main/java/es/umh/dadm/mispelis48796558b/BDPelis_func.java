package es.umh.dadm.mispelis48796558b;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class BDPelis_func extends SQLiteOpenHelper {

    /**
     * Constructor de la tabla actual
     * @param context actual
     */
    public BDPelis_func(Context context) {
        super(context, "BDPelis.db", null, 1);
    }

    /**
     * Creacion de la tabla
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE BDPelis(idMovie INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT, idPlatform INTEGER, image_data BLOB, movieName TEXT, MovieLength TEXT, MovieGenre TEXT, MovieRating INTEGER, FOREIGN KEY(email) REFERENCES Userdetails(email), FOREIGN KEY(idPlatform) REFERENCES Platformdetails(idPlatform))");
        }

    /**
     * Actualiza la tabla si existe
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop TABLE if exists BDPelis");
    }

    /**
     * Funcion que asigna valores en el content value para posteriormente meterlos en la db
     * @param pelicula objeto
     * @return ContentValues
     */
    private ContentValues asignarValoresPeli(Pelicula pelicula) {
        ContentValues nuevapelicula = new ContentValues();
        nuevapelicula.put("email", pelicula.email);
        nuevapelicula.put("idPlatform", pelicula.idPlatform);
        nuevapelicula.put("image_data", pelicula.image_data);
        nuevapelicula.put("movieName", pelicula.movieName);
        nuevapelicula.put("MovieLength", pelicula.MovieLength);
        nuevapelicula.put("MovieGenre", pelicula.MovieGenre);
        nuevapelicula.put("MovieRating", pelicula.MovieRating);
        return nuevapelicula;
    }

    /**
     * Funcion para insertar la película en la base de datos devovlera verdadero o falso dependiendo de si se ha podido realizar la accion
     * @param pelicula objeto
     * @return boolean
     */
     public Boolean insertarPeliculas(Pelicula pelicula){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues nuevaPelicula = asignarValoresPeli(pelicula);
         long result = db.insert("BDPelis", null, nuevaPelicula);
         if(result == -1) return false;
        return true;
     }

    /**
     * Funcion que devuelve un arrayist con todas las peliculas dentro de la base de datos
     * @param email del usuario
     * @return ArrayList
     */
    public ArrayList<Pelicula> obtenerPeliculasTodas(String email) {
        ArrayList<Pelicula> listaPeliculas = new ArrayList<>();
        SQLiteDatabase bdpelis = this.getReadableDatabase();
        Cursor cursor = bdpelis.rawQuery("SELECT * FROM BDPelis WHERE email=?  ORDER BY LOWER(movieName) ASC", new String[]{email});

        if (cursor == null) {
            Log.e("BDPelis", "Cursor es null. No se pueden obtener los datos.");
            return listaPeliculas;  // Retorna lista vacía si el cursor es null
        }

        if (cursor.moveToFirst()) {
            int idMovieIndex = cursor.getColumnIndex("idMovie");
            int idPlatformIndex = cursor.getColumnIndex("idPlatform");
            int imageDataIndex = cursor.getColumnIndex("image_data");
            int movieNameIndex = cursor.getColumnIndex("movieName");
            int movieLengthIndex = cursor.getColumnIndex("MovieLength");
            int movieGenreIndex = cursor.getColumnIndex("MovieGenre");
            int movieRatingIndex = cursor.getColumnIndex("MovieRating");

            if (idMovieIndex == -1 || idPlatformIndex == -1 || imageDataIndex == -1 ||
                    movieNameIndex == -1 || movieLengthIndex == -1 || movieGenreIndex == -1 ||
                    movieRatingIndex == -1) {
                Log.e("BDPelis", "Uno o más índices de columnas son inválidos.");
            } else {
                do {
                    int idMovie = cursor.getInt(idMovieIndex);
                    int idPlatform = cursor.getInt(idPlatformIndex);
                    byte[] imageData = cursor.getBlob(imageDataIndex);
                    String movieName = cursor.getString(movieNameIndex);
                    String movieLength = cursor.getString(movieLengthIndex);
                    String movieGenre = cursor.getString(movieGenreIndex);
                    int movieRating = cursor.getInt(movieRatingIndex);

                    Pelicula pelicula = new Pelicula(idMovie, email, idPlatform, imageData, movieName, movieLength, movieGenre, movieRating);
                    listaPeliculas.add(pelicula);
                } while (cursor.moveToNext());
                Log.d("BDPelis", "Películas cargadas: " + listaPeliculas.size());
            }
        } else {
            Log.d("BDPelis", "No se encontraron registros para el email: " + email);
        }

        cursor.close();
        return listaPeliculas;
    }

    /**
     * Funcion para actualizar las peliculas devuelve boolean para confirmar si se ha podido realizar o no
     * @param pelicula objetos
     * @param idMovie id de la pelicula
     * @return boolean
     */
    public Boolean updatePelicula(Pelicula pelicula, int idMovie){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues peliUpdate = asignarValoresPeli(pelicula);
        Cursor cursor = db.rawQuery("Select * from BDPelis WHERE idMovie=?", new String[]{String.valueOf(idMovie)});
        if(cursor.getCount() > 0 ){
            long result = db.update("BDPelis", peliUpdate, "idMovie = ?", new String[]{String.valueOf(idMovie)});
            if(result==-1) return false;
        }
        return true;
    }

    /**
     * Funcion para borrar las peliculas dentro de las plataformas devuelve bool para confirmar
     * @param idMovie id de pelicula
     * @return boolean
     */
    public Boolean borrarPeliDentroDePlataforma(int idMovie) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete("BDPelis", "idMovie=?", new String[]{String.valueOf(idMovie)});
        if(result == -1) return false;
        return true;
    }
}
