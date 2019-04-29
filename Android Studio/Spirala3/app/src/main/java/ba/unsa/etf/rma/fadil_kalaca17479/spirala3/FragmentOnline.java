package ba.unsa.etf.rma.fadil_kalaca17479.spirala3;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOnline extends Fragment implements DohvatiKnjige.IDohvatiKnjigeDone,
        DohvatiNajnovije.IDohvatiNajnovijeDone,KnjigaReceiver.Receiver{
    ArrayList<String> unosi;
    ArrayList<Knjiga> knjige;
    ArrayList<Autor> autori;
    ArrayList<Knjiga> knjigesaonline;
    ArrayList<String> podaciKnjige;

    Button dAdd;
    Button dRun;
    Button dPovratak;

    Spinner sKategorije;
    Spinner sRezultat;

    EditText tekstUpit;

    private klikNaPonistifo klikponisti;

    public FragmentOnline() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_online, container, false);
    }

    @Override
    public void onDohvatiDone(ArrayList<Knjiga> x){
        knjigesaonline = x;
        ArrayList<String> k = new ArrayList<>();
        for (Knjiga a: x ) {
            k.add(a.getNaziv());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, k);
        sRezultat.setAdapter(adapter);
    }

    @Override
    public void onNajnovijeDone(ArrayList<Knjiga> x){
        knjigesaonline = x;
        ArrayList<String> k = new ArrayList<>();
        for (Knjiga a: x ) {
            k.add(a.getNaziv());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, k);
        sRezultat.setAdapter(adapter);
    }
    @Override
    public void onReceiveResult(int resultCode,Bundle resultData){
        if(resultCode == 1){
            knjigesaonline = resultData.getParcelableArrayList("result");
            ArrayList<String> k = new ArrayList<>();
            for (Knjiga a: knjigesaonline ) {
                k.add(a.getNaziv());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, k);
            sRezultat.setAdapter(adapter);
        }
    }

    public interface klikNaPonistifo{
        public void kliknutoPonistifo();
    }








    @Override
    public void  onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        unosi = getArguments().getStringArrayList("unosi");
        autori = (ArrayList<Autor>) getArguments().getSerializable("autori");
        knjige = (ArrayList<Knjiga>) getArguments().getSerializable("knjige");

        dAdd = getView().findViewById(R.id.dAdd);
        dRun = getView().findViewById(R.id.dRun);
        dPovratak = getView().findViewById(R.id.dPovratak);

        sKategorije = getView().findViewById(R.id.sKategorije);
        sRezultat = getView().findViewById(R.id.sRezultat);

        tekstUpit = getView().findViewById(R.id.tekstUpit);


        ArrayList<String> kategorije =getArguments().getStringArrayList("unosi");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, kategorije);
        sKategorije.setAdapter(adapter);

        dRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pomocni = tekstUpit.getText().toString();
                if(!(pomocni.contains(";")) && !(pomocni.contains(":")) && !(pomocni.contains(" "))){
                    new DohvatiKnjige((DohvatiKnjige.IDohvatiKnjigeDone)FragmentOnline.this).execute(pomocni);
                }
                else if(pomocni.contains(";")){
                    new DohvatiKnjige((DohvatiKnjige.IDohvatiKnjigeDone)FragmentOnline.this).execute(pomocni.split(";"));
                }
                else if (pomocni.contains("autor:")){
                    String pomocni2 = pomocni.substring(6);
                    new DohvatiNajnovije((DohvatiNajnovije.IDohvatiNajnovijeDone)FragmentOnline.this).execute(pomocni2);
                }
                else if (pomocni.contains("korisnik:")){
                    Intent intent = new Intent(Intent.ACTION_SYNC,null, getActivity(), KnjigePoznanika.class);
                    String pomocni2 = pomocni.substring(9);
                    intent.putExtra("idkorisnika",pomocni2);

                    KnjigaReceiver kReciever = new KnjigaReceiver(new Handler());
                    kReciever.setReceiver(FragmentOnline.this);
                    intent.putExtra("resultReceiver", kReciever);
                    getActivity().startService(intent);
                }
            }
        });
        dAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                podaciKnjige = new ArrayList<>();

                //---------------------------------------------
                if(sKategorije.getSelectedItem() != null && sRezultat != null){
                    for (Knjiga ffs: knjigesaonline) {
                        if(ffs.getNaziv().equals(sRezultat.getSelectedItem().toString())){
                            if(ffs.getAutori().size() != 0)
                            podaciKnjige.add(ffs.getAutori().get(0).getImeiPrezime());
                            else
                                podaciKnjige.add("Nema autora online");
                            podaciKnjige.add(ffs.getNaziv());
                            podaciKnjige.add(sKategorije.getSelectedItem().toString());
                            knjige.add(new Knjiga(podaciKnjige.get(0),podaciKnjige.get(1),podaciKnjige.get(2)));

                            for(Autor fautor: ffs.getAutori()){
                                Autor xautor = new Autor(fautor.getImeiPrezime().toString());

                                //ovdje ide ubacivanje autora
                                {
                                    int brojac = 0;
                                    for (int i = 0; i<autori.size(); i++) {
                                        String pomocni1 = autori.get(i).getIme();
                                        String pomocni2 = xautor.getIme();
                                        if (pomocni1.equals(pomocni2)) {
                                            autori.get(i).setBrojknjiga(autori.get(i).getBrojknjiga() + 1);
                                            brojac++;
                                        }
                                    }
                                    if (brojac == 0) {
                                        autori.add(xautor);
                                    }
                                }
                            }

                        }
                    }

                }
                try{
                    klikponisti = (klikNaPonistifo) getActivity();
                }
                catch (ClassCastException e){
                    throw new ClassCastException(getActivity().toString() + "Treba implementirati");
                }
                klikponisti.kliknutoPonistifo();

            }
        });
        dPovratak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    klikponisti = (klikNaPonistifo) getActivity();
                }
                catch (ClassCastException e){
                    throw new ClassCastException(getActivity().toString() + "Treba implementirati");
                }
                klikponisti.kliknutoPonistifo();
            }
        });

    }

}
