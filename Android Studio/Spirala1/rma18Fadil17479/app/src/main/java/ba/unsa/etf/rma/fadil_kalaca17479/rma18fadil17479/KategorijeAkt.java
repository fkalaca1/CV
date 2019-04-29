package ba.unsa.etf.rma.fadil_kalaca17479.rma18fadil17479;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;

import java.util.ArrayList;

public class KategorijeAkt extends AppCompatActivity {
    Button dPretraga;
    Button dDodajKategoriju;
    Button dDodajKnjigu;
    EditText tekstPretraga;
    ListView listaKategorija;
    ArrayList<String> unosi;
    Context context;
    ArrayAdapter<String> adapter;

    ArrayList<Knjiga> knjige;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategorije_akt);

        dPretraga = findViewById(R.id.dPretraga);
        dDodajKategoriju = findViewById(R.id.dDodajKategoriju);
        dDodajKnjigu = findViewById(R.id.dDodajKnjigu);
        tekstPretraga = findViewById(R.id.tekstPretraga);
        listaKategorija = findViewById(R.id.listaKategorija);
        context = this;


        unosi = new ArrayList<>();
        adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, unosi);
        knjige = new ArrayList<>();

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

        dDodajKnjigu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,DodavanjeKnjigeAkt.class);
                intent.putExtra("kategorije",unosi);
                startActivityForResult(intent,102);
            }
        });

        listaKategorija.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("podaciKnjige", knjige);
                Intent intent = new Intent(KategorijeAkt.this, ListaKnjigaAkt.class);
                intent.putExtra("podaciKnjige",bundle);
                intent.putExtra("kategorija", unosi.get(position));
                startActivityForResult(intent,103);
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (resultCode == 201) {
            ArrayList<String> podaci = data.getStringArrayListExtra("Podaciknjige");
            knjige.add(new Knjiga(podaci.get(0), podaci.get(1), podaci.get(2)));
            if(podaci.size() == 4 )
                knjige.get(knjige.size() - 1).setUrlKnjige(podaci.get(3));
        }
        else if (resultCode == 301){
            ArrayList<Knjiga> poplavi = (ArrayList<Knjiga>) data.getBundleExtra("poplavi").getSerializable("poplavi");
            for(int i = 0; i < knjige.size(); i++){
                for(int j = 0; j < poplavi.size(); j++){
                    if(knjige.get(i).getNazivKnjige().equals(poplavi.get(j).getNazivKnjige())) {
                        knjige.get(i).setDalijeplav(true);
                    }
                }
            }
        }
    }
}
