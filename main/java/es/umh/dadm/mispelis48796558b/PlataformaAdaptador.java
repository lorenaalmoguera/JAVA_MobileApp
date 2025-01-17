package es.umh.dadm.mispelis48796558b;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;


public class PlataformaAdaptador extends BaseAdapter {

    private Context c;
ArrayList<Plataforma> arrayPlataformas;
ArrayList<Pelicula> arrayPelis;

String email;

    public PlataformaAdaptador(Context c, ArrayList<Plataforma> arrayPlataformas, ArrayList<Pelicula> arrayPelicula, String email) {
        super();
        this.c = c;
        this.arrayPlataformas = arrayPlataformas;
        this.arrayPelis = arrayPelicula;
        this.email = email;

    }

    /**
     * Funcion para obtener la lista de peliculas de plataformas concretas
     * @param pelis array de peliculas
     * @param email correo del creador
     * @param platformid id de la plataforma
     * @return el array de las pelis
     */
    String[] obtenerPelisDePlataforma(ArrayList<Pelicula> pelis, String email, int platformid){
        ArrayList<String> listaPelis = new ArrayList<>();
        for (Pelicula pelicula : pelis) {
            if (pelicula.getIdPlatform() == platformid) {
                listaPelis.add(pelicula.getMovieName());
            }
        }
        // Convertir ArrayList a array
        String[] arrayNamePelis = new String[listaPelis.size()];
        arrayNamePelis = listaPelis.toArray(arrayNamePelis);
        return arrayNamePelis;
    }

    public void mostrarMensaje(String mensaje, String titulo, Plataforma plataforma, byte[] ImageData) {
        if (c instanceof FragmentActivity) {
            FragmentActivity activity = (FragmentActivity) c;
            CustomDialog dialog = CustomDialog.newInstance(
                    titulo,
                    mensaje,
                    "Editar",
                    "Cerrar",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            Intent intent = new Intent(c, Editarmiplataforma.class);
                            intent.putExtra("idPlatform", plataforma.getIdPlatform());
                            intent.putExtra("image_data", plataforma.getImage_data());
                            intent.putExtra("email", plataforma.getEmail());
                            intent.putExtra("nombre", plataforma.getNombre());
                            intent.putExtra("url", plataforma.getUrl());
                            intent.putExtra("usuario", plataforma.getUsuario());
                            intent.putExtra("password", plataforma.getPassword());
                            c.startActivity(intent);
                        }
                    },
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            dialogInterface.dismiss();
                        }
                    },
                    ImageData
            );
            dialog.show(activity.getSupportFragmentManager(), "editDialog");
        }
    }

    @Override
    public int getCount() {return arrayPlataformas.size();}

    @Override
    public Plataforma getItem(int i){return arrayPlataformas.get(i);}

    @Override
    public long getItemId(int i){return i;}

    public String hidePssword(String password){
        String i = "";
        for(int j = 0 ; j < password.length(); j++){
            i += "*";
        }
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BDPelis_func bdPelisFunc;
        bdPelisFunc = new BDPelis_func(c);
        Plataformdetails bdplat;
        bdplat = new Plataformdetails(c);

        if (convertView == null) {
            convertView = LayoutInflater.from(c).inflate(R.layout.activity_res2plataform, parent, false);
        }
        Plataforma plataforma = getItem(position);
        String[] str_valoresSpinner = obtenerPelisDePlataforma(arrayPelis, email, plataforma.getIdPlatform());
        Spinner spinner = convertView.findViewById(R.id.generoSpinner);

        ArrayAdapter adapter2 = new ArrayAdapter(c, R.layout.my_selected_item, str_valoresSpinner);
        adapter2.setDropDownViewResource(R.layout.my_dropdown_item);
        spinner.setAdapter(adapter2);

        String mensaje = c.getString(R.string.str_id_plat)+ String.valueOf(plataforma.getIdPlatform());
        TextView textViewTitulo = convertView.findViewById(R.id.textViewTitulo);
        TextView textViewId = convertView.findViewById(R.id.textViewIDOculto);
        ImageView imageViewId = convertView.findViewById(R.id.imageViewPlataforma);
        textViewTitulo.setText(plataforma.getNombre());
        textViewId.setText(mensaje);

        if(plataforma.getImage_data()!=null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(plataforma.getImage_data(), 0, plataforma.getImage_data().length);
            imageViewId.setImageBitmap(bitmap);
        }else{
            imageViewId.setImageResource(R.drawable.ic_launcher_background);
        }


        String passwordhidden = hidePssword(plataforma.getPassword());
        String titulo = c.getString(R.string.str_detalles_plat);
        String space = c.getString(R.string.Str_Jump);
        String nombre_plataforma = c.getString(R.string.Str_nombrePlat) + plataforma.getNombre();
        String id_plataforma = c.getString(R.string.Str_idplat) + String.valueOf(plataforma.getIdPlatform());
        String email = c.getString(R.string.Str_email_plat) + plataforma.getEmail();
        String urlplat = c.getString(R.string.Str_url_plat) + plataforma.getUrl();
        String usuario = c.getString(R.string.Str_user_plat) + plataforma.getUsuario();
        String password = c.getString(R.string.Str_user_pass) + passwordhidden;
        String imagenPlat = c.getString(R.string.Str_img_plat);
        String alertDialogmsj = nombre_plataforma + space + id_plataforma + space + email + space + urlplat + space + usuario + space + password + space + imagenPlat;



        convertView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mostrarMensaje(alertDialogmsj, titulo, plataforma, plataforma.getImage_data());
            }
        });

        convertView.setOnLongClickListener(view -> {
            String tituloEliminar = c.getString(R.string.str_AlertDialog);
            String aceptar = c.getString(R.string.str_AlertDialog_Si);
            String cancelar = c.getString(R.string.Str_AlertDialog_cancelar);
            String msjeliminar = c.getString(R.string.Str_AlertDialog_eliminarPlatform);

            if (c instanceof FragmentActivity) {
                FragmentActivity activity = (FragmentActivity) c;
                CustomDialog2 dialog = CustomDialog2.newInstance(tituloEliminar, msjeliminar, aceptar, cancelar,
                        (dialogInterface, which) -> {
                            boolean flagdeletePeli = true;
                            int miidPlataform = plataforma.getIdPlatform();
                            boolean flagdeletePlat = bdplat.borrarPlataforma(miidPlataform);

                            if (flagdeletePlat) {
                                for (Pelicula pelicula : arrayPelis) {
                                    if (miidPlataform == pelicula.getIdPlatform()) {
                                        flagdeletePeli = bdPelisFunc.borrarPeliDentroDePlataforma(pelicula.getIdMovie()) && flagdeletePeli;
                                    }
                                }

                                if (!flagdeletePeli) {
                                    Toast.makeText(c, "No se ha podido borrar alguna película", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(c, "No se ha podido borrar la plataforma", Toast.LENGTH_SHORT).show();
                            }

                            if (c instanceof Activity) {
                                Activity a = (Activity) c;
                                a.finish();
                                a.startActivity(a.getIntent());
                            }
                        },
                        (dialogInterface, which) -> dialogInterface.dismiss()
                );
                dialog.show(activity.getSupportFragmentManager(), "deleteDialog");
            }
            return true;
        });


        Animation animation = AnimationUtils.loadAnimation(c, android.R.anim.slide_in_left);
        convertView.startAnimation(animation);
        return convertView;
    }


}
