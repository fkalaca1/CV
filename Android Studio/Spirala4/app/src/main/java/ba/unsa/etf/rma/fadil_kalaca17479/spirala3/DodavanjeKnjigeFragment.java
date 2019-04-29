package ba.unsa.etf.rma.fadil_kalaca17479.spirala3;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class DodavanjeKnjigeFragment extends Fragment {
    Button dPonisti;
    private klikNaPonisti klikponisti;

    ImageView naslovnaStr;
    Button dNadjiSliku;
    String uriSlike = "";


    Button dUpisiKnjigu;
    Spinner sKategorijaKnjige;
    ArrayList<String> podaciKnjige;
    EditText imeAutora;
    EditText nazivKnjige;

    BazaOpenHelper bdb;
    public DodavanjeKnjigeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dodavanje_knjige, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bdb = new BazaOpenHelper(getActivity());
        dPonisti = getView().findViewById(R.id.dPonisti);

        dPonisti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    klikponisti = (klikNaPonisti) getActivity();
                }
                catch (ClassCastException e){
                    throw new ClassCastException(getActivity().toString() + "Treba implementirati");
                }
                bdb.close();
                klikponisti.kliknutoPonisti();
            }
        });

        //_________________________________________________________________________________________

        naslovnaStr = getView().findViewById(R.id.naslovnaStr);
        dNadjiSliku = getView().findViewById(R.id.dNadjiSliku);

        dNadjiSliku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");

                if (photoPickerIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(photoPickerIntent, 999);
                }
            }
        });
        //________________________________________________________________
        imeAutora = getView().findViewById(R.id.imeAutora);
        nazivKnjige = getView().findViewById(R.id.nazivKnjige);
        dUpisiKnjigu = getView().findViewById(R.id.dUpisiKnjigu);
        sKategorijaKnjige = getView().findViewById(R.id.sKategorijaKnjige);

        ArrayList<String> kategorije =getArguments().getStringArrayList("unosi");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, kategorije);
        sKategorijaKnjige.setAdapter(adapter);
        //________________________________________________________________
        podaciKnjige = new ArrayList<>();
        dUpisiKnjigu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sKategorijaKnjige.getSelectedItem() != null){
                    podaciKnjige.add(imeAutora.getText().toString());
                    podaciKnjige.add(nazivKnjige.getText().toString());
                    podaciKnjige.add(sKategorijaKnjige.getSelectedItem().toString());
                    podaciKnjige.add(uriSlike);
                    ArrayList<Knjiga> knjige;
                    ArrayList<Autor> autori;
                    knjige = (ArrayList<Knjiga>)getArguments().getSerializable("knjige");
                    autori = (ArrayList<Autor>) getArguments().getSerializable("autori");
                    Autor x = new Autor(imeAutora.getText().toString());
                    //ovdje ide ubacivanje autora
                    {
                        int brojac = 0;
                        for (int i = 0; i<autori.size(); i++) {
                            String pomocni1 = autori.get(i).getIme();
                            String pomocni2 = x.getIme();
                            if (pomocni1.equals(pomocni2)) {
                                autori.get(i).setBrojknjiga(autori.get(i).getBrojknjiga() + 1);
                                brojac++;
                            }
                        }
                        if (brojac == 0) {
                            autori.add(x);
                        }
                    }
                    //Kraj
                    knjige.add(new Knjiga(podaciKnjige.get(0),podaciKnjige.get(1),podaciKnjige.get(2)));
                    knjige.get(knjige.size()-1).setUrlKnjige(podaciKnjige.get(3));
                    //BAZA
                    long zabazu = 0;
                    try {
                        zabazu = bdb.dodajKnjigu(knjige.get(knjige.size()-1));
                    } catch (Exception e) {
                        Log.d("Knjiga"," -> Nije dodana knjiga - Exception");
                    }
                    if(zabazu == 0 || zabazu == -1) Log.d("Knjiga"," -> Nije dodana knjiga");
                    //BAZA
                    try{
                        klikponisti = (klikNaPonisti) getActivity();
                    }
                    catch (ClassCastException e){
                        throw new ClassCastException(getActivity().toString() + "Treba implementirati");
                    }
                    bdb.close();
                    klikponisti.kliknutoPonisti();
                }
            }
        });

    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri;
                imageUri= data.getData();
                final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                naslovnaStr.setImageBitmap(selectedImage);
                uriSlike = imageUri.toString();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    public interface klikNaPonisti{
        public void kliknutoPonisti();
    }
}
