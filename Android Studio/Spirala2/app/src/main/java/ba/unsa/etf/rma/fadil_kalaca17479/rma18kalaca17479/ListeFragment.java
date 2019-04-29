package ba.unsa.etf.rma.fadil_kalaca17479.rma18kalaca17479;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListeFragment extends Fragment {
    Button dPretraga;
    Button dDodajKategoriju;
    Button dDodajKnjigu;
    Button dKategorije;
    Button dAutori;
    EditText tekstPretraga;
    ListView listaKategorija;
    ArrayList<String> unosi;
    ArrayList<String> autori_stringovi;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapterAutora;
    ArrayList<Knjiga> knjige;
    ArrayList<Autor> autori;
    Context context;
    boolean jesuLiKliknuliDugmeAutori = false;

    //dodano
    private klikNaDugme klik;
    private prikaziAutoreUListi klikItemAutor;
    private prikaziKnjigeUListi klikItemKnjiga;


    public ListeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_liste, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        dPretraga = getView().findViewById(R.id.dPretraga);
        dDodajKategoriju = getView().findViewById(R.id.dDodajKategoriju);
        dDodajKnjigu = getView().findViewById(R.id.dDodajKnjigu);
        dKategorije = getView().findViewById(R.id.dKategorije);
        dAutori = getView().findViewById(R.id.dAutori);
        tekstPretraga = getView().findViewById(R.id.tekstPretraga);
        listaKategorija = getView().findViewById(R.id.listaKategorija);
        context = getActivity();

        unosi = getArguments().getStringArrayList("unosi");
        adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,unosi);
        knjige =  (ArrayList<Knjiga>) getArguments().getSerializable("knjiga");
        autori = (ArrayList<Autor>) getArguments().getSerializable("autori");
        autori_stringovi = new ArrayList<>();
        for(Autor x : autori){
            autori_stringovi.add(x.getInfo());
        }
        adapterAutora = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,autori_stringovi);

        listaKategorija.setAdapter(adapter);

        dPretraga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.getFilter().filter(tekstPretraga.getText().toString(), new Filter.FilterListener() {
                    @Override
                    public void onFilterComplete(int count) {
                        if (listaKategorija.getCount() == 0) {
                            dDodajKategoriju.setEnabled(true);
                        }
                    }
                });
            }
        });

        tekstPretraga.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                dDodajKategoriju.setEnabled(false);
            }
        });

        dDodajKategoriju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unosi.add(tekstPretraga.getText().toString());
                adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, unosi);
                listaKategorija.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                dDodajKategoriju.setEnabled(false);
            }
        });

        dKategorije.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listaKategorija.setAdapter(adapter);
                dPretraga.setVisibility(View.VISIBLE);
                dDodajKategoriju.setVisibility(View.VISIBLE);
                tekstPretraga.setVisibility(View.VISIBLE);
                jesuLiKliknuliDugmeAutori = false;
            }
        });

        dAutori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listaKategorija.setAdapter(adapterAutora);
                dPretraga.setVisibility(View.GONE);
                dDodajKategoriju.setVisibility(View.GONE);
                tekstPretraga.setVisibility(View.GONE);
                jesuLiKliknuliDugmeAutori = true;
            }
        });

        dDodajKnjigu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    klik = (klikNaDugme)getActivity();
                }
                catch (ClassCastException e){
                    throw new ClassCastException(getActivity().toString() + "Treba implementirati");
                }
                klik.kliknutoDugme();

            }
        });

        listaKategorija.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(jesuLiKliknuliDugmeAutori){
                    klikItemAutor = (prikaziAutoreUListi) getActivity();
                    int temp = -1;
                    for(int i=0; i<autori.size(); i++){
                        if(adapterAutora.getItem(position).equals(autori.get(i).getInfo())){
                            temp = i;
                        }
                    }
                    if(temp != -1) klikItemAutor.prikazAutoraUlisti(temp);
                }
                else{
                    klikItemKnjiga = (prikaziKnjigeUListi) getActivity();
                    int temp = -1;
                    for(int i=0; i<unosi.size(); i++){
                        if(adapter.getItem(position).equals(unosi.get(i))) temp = i;
                    }
                    if(temp != -1) klikItemKnjiga.prikazKnjigaUlisti(temp);
                }
            }
        });






    }

    public interface klikNaDugme{
        public void kliknutoDugme();
    }

    public interface prikaziAutoreUListi{
        public void prikazAutoraUlisti(int x);
    }

    public interface prikaziKnjigeUListi{
        public void prikazKnjigaUlisti(int x);
    }


}
