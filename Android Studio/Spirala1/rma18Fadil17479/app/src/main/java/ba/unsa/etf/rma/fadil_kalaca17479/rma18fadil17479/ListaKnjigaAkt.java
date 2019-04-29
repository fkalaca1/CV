package ba.unsa.etf.rma.fadil_kalaca17479.rma18fadil17479;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListaKnjigaAkt extends AppCompatActivity {

    ListView listaKnjiga;
    Button dPovratak;
    ArrayList<Knjiga> filtrirane;
    KnjigaAdapter adapter;

    ArrayList<Knjiga> povratne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_knjiga_akt);

        listaKnjiga = findViewById(R.id.listaKnjiga);
        dPovratak = findViewById(R.id.dPovratak);

        filtrirane = (ArrayList<Knjiga>) getIntent().getBundleExtra("podaciKnjige").getSerializable("podaciKnjige");
        String kate = getIntent().getExtras().getString("kategorija");
        for(int i = 0; i<filtrirane.size(); i++){
            if(!filtrirane.get(i).getKategorija().equals(kate)){
                filtrirane.remove(i);
                i--;
            }
        }
        adapter = new KnjigaAdapter(this,R.layout.lista_knjiga_element ,filtrirane);
        listaKnjiga.setAdapter(adapter);

        povratne = new ArrayList<>();

        listaKnjiga.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setBackgroundColor(0xffaabbed);
                povratne.add(filtrirane.get(position));
            }
        });

        dPovratak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("poplavi", povratne);
                Intent intent = new Intent();
                intent.putExtra("poplavi",bundle);
                setResult(301, intent);
                finish();
            }
        });


    }
    @Override
    public void onBackPressed()
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable("poplavi", povratne);
        Intent intent = new Intent();
        intent.putExtra("poplavi",bundle);
        setResult(301, intent);
        finish();
    }
}
