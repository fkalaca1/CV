package ba.unsa.etf.rma.fadil_kalaca17479.spirala3;


import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import java.util.ArrayList;

public class KategorijeAkt extends AppCompatActivity
    implements ListeFragment.klikNaDugme, ListeFragment.prikaziAutoreUListi, ListeFragment.prikaziKnjigeUListi, ListeFragment.addbookonlineklik,
    DodavanjeKnjigeFragment.klikNaPonisti,
    KnjigeFragment.povratakIzKnjigeFragment,
    FragmentOnline.klikNaPonistifo,
    FragmentPreporuci.klikNaDugmeVrati{

    ArrayList<String> unosi = new ArrayList<>();
    ArrayList<Knjiga> knjige = new ArrayList<>();
    ArrayList<Autor> autori= new ArrayList<>();

    FragmentManager fragmentManager;
    FrameLayout frameLayout;
    BazaOpenHelper bdb;

    boolean gasi = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategorije_akt);
        //_______________________________________
        bdb = new BazaOpenHelper(this);
        try {
            unosi = bdb.dajKategorije();
        } catch (Exception e) {
            unosi = new ArrayList<>();
        }
        try{
            autori = bdb.dajSveAutoreUPocetni();
        }catch (Exception e){
            autori = new ArrayList<>();
        }


        //_______________________________________
        fragmentManager = getFragmentManager();
        frameLayout = (FrameLayout) findViewById(R.id.mjestoFragmenta1);
        Bundle argumenti = new Bundle();
        argumenti.putStringArrayList("unosi", unosi);
        argumenti.putSerializable("knjige", knjige);
        argumenti.putSerializable("autori", autori);

        ListeFragment listeFragment;
        listeFragment = new ListeFragment();
        listeFragment.setArguments(argumenti);
        fragmentManager.beginTransaction().replace(R.id.mjestoFragmenta1, listeFragment).commit();
    }

    //Kraj app
    @Override
    public void onBackPressed(){
        if(gasi){
            finish();
            bdb.Ugasi();
            bdb.close();
        }
        else kliknutoPonisti();
    }

    //_____________________ Interface-i
    //LISTEFRAGMENT   klikNaDugme -> idi u dodaj knjigu
    @Override
    public void kliknutoDugme(){
        DodavanjeKnjigeFragment dodavanjeKnjigeFragment;
        dodavanjeKnjigeFragment = new DodavanjeKnjigeFragment();
        Bundle argumenti = new Bundle();
        argumenti.putStringArrayList("unosi", unosi);
        argumenti.putSerializable("knjige", knjige);
        argumenti.putSerializable("autori", autori);
        dodavanjeKnjigeFragment.setArguments(argumenti);
        fragmentManager.beginTransaction().replace(R.id.mjestoFragmenta1, dodavanjeKnjigeFragment).commit();
        gasi = false;
    }
    //Autori ako su odabrani item click
    @Override
    public void prikazAutoraUlisti(int x){
        UpdateAutoriUListi();
        KnjigeFragment knjigeFragment;
        knjigeFragment = new KnjigeFragment();
        Bundle argumenti = new Bundle();
        argumenti.putStringArrayList("unosi", unosi);
        argumenti.putSerializable("knjige", knjige);
        argumenti.putSerializable("autori", autori);
        argumenti.putBoolean("pretraga", true);
        argumenti.putString("kljuc",autori.get(x).getIme());
        knjigeFragment.setArguments(argumenti);
        fragmentManager.beginTransaction().replace(R.id.mjestoFragmenta1,knjigeFragment).commit();
        gasi = false;
    }
    //Knjige odabrane item click
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
        fragmentManager.beginTransaction().replace(R.id.mjestoFragmenta1,knjigeFragment).commit();
        gasi = false;
    }

    //SPIRALA 3 ADD BOOK ONLINE
    @Override
    public void addbookonlinekliknuto(){
        FragmentOnline fragmentOnline;
        fragmentOnline = new FragmentOnline();
        Bundle argumenti = new Bundle();
        argumenti.putStringArrayList("unosi", unosi);
        argumenti.putSerializable("knjige", knjige);
        argumenti.putSerializable("autori", autori);
        fragmentOnline.setArguments(argumenti);
        fragmentManager.beginTransaction().replace(R.id.mjestoFragmenta1, fragmentOnline).commit();
        gasi = false;
    }
    //DODAVANJE KNJIGE FRAGMENT
    public void kliknutoPonisti(){
        ListeFragment listeFragment;
        listeFragment = new ListeFragment();
        Bundle argumenti = new Bundle();
        argumenti.putStringArrayList("unosi", unosi);
        argumenti.putSerializable("knjige", knjige);
        argumenti.putSerializable("autori", autori);
        listeFragment.setArguments(argumenti);
        fragmentManager.beginTransaction().replace(R.id.mjestoFragmenta1, listeFragment).commit();
        gasi = true;
    }
    //KNJIGE FRAGMENT
    public void povratakIzlisteknjigaFragment(){
        kliknutoPonisti();
    }
    //KNJIGE FRAGMENT
    public void kliknutoPonistifo(){
        kliknutoPonisti();
    }
    public void kliknutoDugmeVrati(){
        kliknutoPonisti();
    }
    public void UpdateAutoriUListi(){
        try{
            autori = bdb.dajSveAutoreUPocetni();
        }catch (Exception e){
            autori = new ArrayList<>();
        }
    }



}
