package ba.unsa.etf.rma.fadil_kalaca17479.rma18kalaca17479;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class KategorijeAkt extends AppCompatActivity
        implements ListeFragment.klikNaDugme, DodavanjeKnjigeFragment.klikNaPonisti, ListeFragment.prikaziKnjigeUListi,
        ListeFragment.prikaziAutoreUListi, KnjigeFragment.povratakIzKnjigeFragment {

    ArrayList<String> unosi = new ArrayList<>();
    ArrayList<Knjiga> knjige = new ArrayList<>();
    ArrayList<Autor> autori= new ArrayList<>();
    FragmentManager fragmentManager;
    FrameLayout frameLayout;
    FrameLayout x;
    Boolean siriL= false;
    boolean gasi = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategorije_akt);


        fragmentManager = getFragmentManager();
        frameLayout = (FrameLayout)findViewById(R.id.mjestoFragmenta2);
        x = (FrameLayout) findViewById(R.id.mjestoFragmenta1);
        Bundle argumenti = new Bundle();
        argumenti.putStringArrayList("unosi", unosi);
        argumenti.putSerializable("knjige", knjige);
        argumenti.putSerializable("autori", autori);
        //

        if(frameLayout != null){
            siriL = true;
            KnjigeFragment kf;
            kf = new KnjigeFragment();
            kf.setArguments(argumenti);
            fragmentManager.beginTransaction().replace(R.id.mjestoFragmenta2,kf).commit();

        }

        ListeFragment listeFragment ;
        listeFragment = new ListeFragment();
        listeFragment.setArguments(argumenti);
        fragmentManager.beginTransaction().replace(R.id.mjestoFragmenta1, listeFragment).commit();


    }



    @Override
    public void kliknutoPonisti(){
        ListeFragment listeFragment;
        listeFragment = new ListeFragment();
        Bundle argumenti = new Bundle();
        argumenti.putStringArrayList("unosi", unosi);
        argumenti.putSerializable("knjige", knjige);
        argumenti.putSerializable("autori", autori);
        listeFragment.setArguments(argumenti);
        if(!siriL){
            fragmentManager.beginTransaction().replace(R.id.mjestoFragmenta1, listeFragment).commit();
            gasi = true;
        }
        else{
            fragmentManager.beginTransaction().replace(R.id.mjestoFragmenta1, listeFragment).commit();
            frameLayout.setVisibility(View.VISIBLE);
            x.getLayoutParams().width = 0;
            KnjigeFragment kf;
            kf = new KnjigeFragment();
            kf.setArguments(argumenti);
            fragmentManager.beginTransaction().replace(R.id.mjestoFragmenta2,kf).commit();
            gasi = true;
        }
    }



    @Override
    public void kliknutoDugme(){
        DodavanjeKnjigeFragment dodavanjeKnjigeFragment;
        dodavanjeKnjigeFragment = new DodavanjeKnjigeFragment();
        Bundle argumenti = new Bundle();
        argumenti.putStringArrayList("unosi", unosi);
        argumenti.putSerializable("knjige", knjige);
        argumenti.putSerializable("autori", autori);
        dodavanjeKnjigeFragment.setArguments(argumenti);
        if(siriL){
            frameLayout.setVisibility(View.GONE);
            x.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            fragmentManager.beginTransaction().replace(R.id.mjestoFragmenta1, dodavanjeKnjigeFragment).commit();
            gasi = false;
        }
        else
        fragmentManager.beginTransaction().replace(R.id.mjestoFragmenta1, dodavanjeKnjigeFragment).commit();
        gasi = false;
    }

    @Override
    public void prikazAutoraUlisti(int x){
        KnjigeFragment knjigeFragment;
        knjigeFragment = new KnjigeFragment();
        Bundle argumenti = new Bundle();
        argumenti.putStringArrayList("unosi", unosi);
        argumenti.putSerializable("knjige", knjige);
        argumenti.putSerializable("autori", autori);
        argumenti.putBoolean("pretraga", true);
        argumenti.putString("kljuc",autori.get(x).getIme());
        knjigeFragment.setArguments(argumenti);
        if(!siriL){
            fragmentManager.beginTransaction().replace(R.id.mjestoFragmenta1,knjigeFragment).commit();
            gasi = false;
        }
        else{
            frameLayout.setVisibility(View.VISIBLE);
            fragmentManager.beginTransaction().replace(R.id.mjestoFragmenta2,knjigeFragment).commit();
            gasi = true;
        }

    }

    @Override
    public void prikazKnjigaUlisti(int x){
        KnjigeFragment knjigeFragment;
        knjigeFragment = new KnjigeFragment();
        Bundle argumenti = new Bundle();
        argumenti.putStringArrayList("unosi", unosi);
        argumenti.putSerializable("knjige", knjige);
        argumenti.putSerializable("autori", autori);
        argumenti.putBoolean("pretraga", false);
        argumenti.putString("kljuc",unosi.get(x));
        knjigeFragment.setArguments(argumenti);
        if(!siriL){
            fragmentManager.beginTransaction().replace(R.id.mjestoFragmenta1,knjigeFragment).commit();
            gasi = false;
        }
        else{
            frameLayout.setVisibility(View.VISIBLE);
            fragmentManager.beginTransaction().replace(R.id.mjestoFragmenta2,knjigeFragment).commit();
            gasi = true;
        }
    }

    @Override
    public void povratakIzlisteknjigaFragment(){
        kliknutoPonisti();
    }

    @Override
    public void onBackPressed(){
        if(gasi) finish();
        else povratakIzlisteknjigaFragment();
    }




}
