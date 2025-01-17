package es.umh.dadm.mispelis48796558b;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;

public class PeliculaAdaptador extends BaseAdapter {

    private Context c;
    ArrayList<Pelicula> arrayPelicula;

    /**
     * Constructor
     * @param c contexto
     * @param arrayPelicula peliculas
     */
    public PeliculaAdaptador(Context c, ArrayList<Pelicula> arrayPelicula) {
        super();
        this.c = c;
        this.arrayPelicula = arrayPelicula;

    }

    /**
     * Esta funcion nos ayudará a mostrar un dialog custom que tendrá los botones aerriba
     * @param Mensaje a mostrar
     * @param titulo a mostrar
     * @param pelicula a utilizar
     * @param ImageData a mostrar
     */
    public void mostrarMensaje(String Mensaje, String titulo, Pelicula pelicula, byte[] ImageData){
        if (c instanceof FragmentActivity) {
            FragmentActivity activity = (FragmentActivity) c;

            // layout y dialogo custom
            CustomDialog dialog = CustomDialog.newInstance(
                    titulo,
                    Mensaje,
                    c.getString(R.string.str_AlertDialog_EditarDatos),
                    c.getString(R.string.str_alertDialog_cerrar),
                    (dialogInterface, which) -> {
                        Intent intent = new Intent(c, EditarpeliculaActivity.class);
                        intent.putExtra("idMovie", pelicula.getIdMovie());
                        intent.putExtra("movieName", pelicula.getMovieName());
                        intent.putExtra("email", pelicula.getEmail());
                        intent.putExtra("image_data", pelicula.getImage_data());
                        intent.putExtra("movieLength", pelicula.getMovieLength());
                        intent.putExtra("MovieGenre", pelicula.getMovieGenre());
                        intent.putExtra("MovieRating", pelicula.getMovieRating());
                        c.startActivity(intent);
                    },
                    (dialogInterface, which) -> dialogInterface.dismiss(),
                    ImageData
            );
            dialog.show(activity.getSupportFragmentManager(), "editMovieDialog");
        }
    }


    /**
     * Nos devolverá el total de peliculas
     * @return total de peliculas
     */
    @Override
    public int getCount() {
        return arrayPelicula.size();
    }

    /**
     * Nos devolvera la pelicula
     * @param i Position of the item whose data we want within the adapter's
     * data set.
     * @return pelicula
     */
    @Override
    public Pelicula getItem(int i) {
        return arrayPelicula.get(i);
    }

    /**
     * Nos devolverá el id de la posicion i
     * @param i The position of the item within the adapter's data set whose row id we want.
     * @return i
     */
    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * Funcion que nos ayudará a inflar el layout para mostrar todas las peliculas de la db registradas por el usuaro
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return convertView
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(c).inflate(R.layout.activity_res2pelicula, parent, false);
        }

        BDPelis_func bdPelisFunc;
        bdPelisFunc = new BDPelis_func(c);

        Pelicula pelicula = getItem(position);

        //obtenemos ids
        TextView textViewTitulo = convertView.findViewById(R.id.textViewTituloPelicula);
        TextView textViewId = convertView.findViewById(R.id.textViewIDPlatOculto);
        TextView textViewMovieId = convertView.findViewById(R.id.textViewIDPeliOculto);
        ImageView imageViewId = convertView.findViewById(R.id.imageViewPelicula);

        String mensaje1 = c.getString(R.string.str_id_plat) + pelicula.getIdPlatform();
        String mensaje2 = c.getString(R.string.str_id_peli) + pelicula.getIdMovie();

        //actualizamos datos
        textViewTitulo.setText(pelicula.getMovieName());
        textViewId.setText(mensaje1);
        textViewMovieId.setText(mensaje2);


        if (pelicula.getImage_data() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(pelicula.getImage_data(), 0, pelicula.getImage_data().length);

            // actualizamos la imagen
            imageViewId.setImageBitmap(bitmap);
        } else {
            imageViewId.setImageResource(R.drawable.ic_launcher_background);
        }

        String space = c.getString(R.string.Str_Jump);
        String idMovie = c.getString(R.string.str_AlertDialog_idMovieInPeli) + String.valueOf(pelicula.getIdMovie());
        String email = c.getString(R.string.str_AlertDialog_idMovie_emailresInPeli) + String.valueOf(pelicula.getEmail());
        String idPlatform = c.getString(R.string.str_AlertDialog_idPlatformInPeli) + String.valueOf(pelicula.getIdPlatform());
        String image_data = c.getString(R.string.str_AlertDialog_ImgDataMoviesInPeli);
        String movieName = c.getString(R.string.str_AlertDialog_MovieNamePeli) + String.valueOf(pelicula.getMovieName());
        String MovieLength = c.getString(R.string.str_AlertDialog_MovieLengthPeli) + String.valueOf(pelicula.getMovieLength());
        String MovieGenre = c.getString(R.string.str_AlertDIalog_MovieGenrePeli) + String.valueOf(pelicula.getMovieGenre());
        String MovieRating = c.getString(R.string.str_AlertDialog_MovieRating) + String.valueOf(pelicula.getMovieRating());
        String titulo = c.getString(R.string.Str_AlertDialog_datosPeli);
        String mensaje = space + idMovie + space + email + space + idPlatform + space + movieName + space + MovieLength + space + MovieGenre + space + MovieRating + space + image_data;



        // para editar
        convertView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mostrarMensaje(mensaje, titulo, pelicula, pelicula.getImage_data());
            }
        });



        // para borrar
        convertView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view){
                String tituloEliminar = c.getString(R.string.str_AlertDialogEliminarPeli);
                String msjeliminar = c.getString(R.string.str_AlertDialogEliminarPelicheck);
                String aceptar = c.getString(R.string.str_AlertDialog_Si);
                String cancelar = c.getString(R.string.Str_AlertDialog_cancelar);

                if (c instanceof FragmentActivity) {
                    FragmentActivity activity = (FragmentActivity) c;
                    CustomDialog2 dialog = CustomDialog2.newInstance(
                            tituloEliminar,
                            msjeliminar,
                            aceptar,
                            cancelar,
                            (dialogInterface, which) -> {
                                // manejo ocpion de aceptar
                                if (!bdPelisFunc.borrarPeliDentroDePlataforma(pelicula.getIdMovie())) { // en caso de que no se pueda borrar las pelis dentro de la plataforma se lanza un toast
                                    Toast.makeText(c, "No se ha podido borrar la película", Toast.LENGTH_SHORT).show();
                                }
                                if (c instanceof Activity) {
                                    Activity activity2 = (Activity) c;
                                    activity2.finish();
                                    activity2.startActivity(activity2.getIntent());
                                }
                            },
                            (dialogInterface, which) -> {
                                // opcion cancelar
                                dialogInterface.dismiss();
                            }
                    );
                    dialog.show(activity.getSupportFragmentManager(), "deleteDialog");
                }
                return true;
            }
        });
        // animacion de todos los adaptadores
        Animation animation = AnimationUtils.loadAnimation(c, android.R.anim.slide_in_left);
        convertView.startAnimation(animation);
        return convertView;
    }

}

