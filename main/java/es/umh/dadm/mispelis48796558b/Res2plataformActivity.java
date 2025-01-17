package es.umh.dadm.mispelis48796558b;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Res2plataformActivity extends AppCompatActivity {

    DBPelis midb;
    Plataformdetails miplatdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_res2plataform);
        midb = new DBPelis(this);
        miplatdb = new Plataformdetails(this);
    }


}