package es.umh.dadm.mispelis48796558b;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Res2peliculaActivity extends AppCompatActivity {
    DBPelis midb;
    Plataformdetails miplatdb;
    BDPelis_func midbpelis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_res2pelicula);
        midb = new DBPelis(this);
        miplatdb = new Plataformdetails(this);
        midbpelis = new BDPelis_func(this);

    }
}