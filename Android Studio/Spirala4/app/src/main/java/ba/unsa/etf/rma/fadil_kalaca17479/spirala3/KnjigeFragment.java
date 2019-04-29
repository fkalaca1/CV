package ba.unsa.etf.rma.fadil_kalaca17479.spirala3;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class KnjigeFragment extends Fragment {
    ArrayList<String> unosi;
    ArrayList<Knjiga> knjige;
    ArrayList<Autor> autori;
    boolean tipfiltriranja;
    String kljuc;

    //
    Button dPovratak;
    ListView listaKnjiga;

    //
    ArrayList<Knjiga> filtrirane = new ArrayList<>();
    KnjigaAdapter adapter;

    //
    povratakIzKnjigeFragment klik;

    BazaOpenHelper bdb;
    public KnjigeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_knjige, container, false);
    }

    @Override
    public void  onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bdb = new BazaOpenHelper(getActivity());
        dPovratak = getView().findViewById(R.id.dPovratak);
        listaKnjiga = getView().findViewById(R.id.listaKnjiga);

        unosi = getArguments().getStringArrayList("unosi");
        autori = (ArrayList<Autor>) getArguments().getSerializable("autori");
        knjige = (ArrayList<Knjiga>) getArguments().getSerializable("knjige");
        tipfiltriranja = getArguments().getBoolean("pretraga");
        kljuc = getArguments().getString("kljuc");

        for(int i = 0; i<knjige.size(); i++){
            filtrirane.add(knjige.get(i));
        }

        for(int i = 0; i<filtrirane.size(); i++){
            if(tipfiltriranja){
                if(!filtrirane.get(i).getImeAutora().equals(kljuc)){
                    filtrirane.remove(i);
                    i--;
                }
            }
            else{
                if(!filtrirane.get(i).getKategorija().equals(kljuc)){
                    filtrirane.remove(i);
                    i--;
                }
            }
        }


        if(!tipfiltriranja) {
            long dajBoze = bdb.dajIdKategorije(kljuc);
            filtrirane = bdb.knjigeKategorije(dajBoze);
        }
        else{
            long dajBoze = bdb.dajIdAutoraOdImena(kljuc);
            filtrirane = bdb.knjigeAutora(dajBoze);
        }

        adapter = new KnjigaAdapter(getContext(),R.layout.lista_knjiga_element ,filtrirane);
        listaKnjiga.setAdapter(adapter);

        listaKnjiga.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                filtrirane.get(position).setDalijeplav(true);
                bdb.promjeniPozadinu(filtrirane.get(position).getNazivKnjige());
                adapter.notifyDataSetChanged();
                /*for(int i = 0; i<knjige.size(); i++){
                    if(knjige.get(i).getNazivKnjige().equals(filtrirane.get(position).getNazivKnjige())){
                        knjige.get(i).setDalijeplav(true);
                    }
                }*/
            }
        });

        dPovratak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                klik = (povratakIzKnjigeFragment)getActivity();
                klik.povratakIzlisteknjigaFragment();
            }
        });


    }

    public interface povratakIzKnjigeFragment{
        public void povratakIzlisteknjigaFragment();
    }

}
