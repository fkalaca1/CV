package ba.unsa.etf.rma.fadil_kalaca17479.rma18fadil17479;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.service.chooser.ChooserTargetService;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class DodavanjeKnjigeAkt extends AppCompatActivity {

    ImageView naslovnaStr;
    EditText imeAutora;
    EditText nazivKnjige;
    Button dNadjiSliku;
    Button dUpisiKnjigu;
    Spinner sKategorijaKnjige;
    Button dPonisti;
    Context context;

    ArrayList<String> podaciKnjige;
    String uriSlike = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodavanje_knjige_akt);

        naslovnaStr = findViewById(R.id.naslovnaStr);
        imeAutora = findViewById(R.id.imeAutora);
        nazivKnjige = findViewById(R.id.nazivKnjige);
        dNadjiSliku = findViewById(R.id.dNadjiSliku);
        dUpisiKnjigu = findViewById(R.id.dUpisiKnjigu);
        sKategorijaKnjige = findViewById(R.id.sKategorijaKnjige);
        dPonisti = findViewById(R.id.dPonisti);
        context = this;

        dPonisti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(404);
                finish();
            }
        });

        ArrayList<String> kategorije = getIntent().getStringArrayListExtra("kategorije");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, kategorije);
        sKategorijaKnjige.setAdapter(adapter);

        podaciKnjige = new ArrayList<>();

        dUpisiKnjigu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sKategorijaKnjige.getSelectedItem() != null){
                    podaciKnjige.add(imeAutora.getText().toString());
                    podaciKnjige.add(nazivKnjige.getText().toString());
                    podaciKnjige.add(sKategorijaKnjige.getSelectedItem().toString());
                    podaciKnjige.add(uriSlike);
                    Intent intent = new Intent();
                    intent.putExtra("Podaciknjige", podaciKnjige);
                    setResult(201,intent);
                    Toast.makeText(context, "Knjiga dodana", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    Toast.makeText(context, "Kategorija nije odabrana", Toast.LENGTH_SHORT).show();
                }
            }
        });


        dNadjiSliku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");

                if (photoPickerIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(photoPickerIntent, 999);
                }



            }
        });



    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri;
                imageUri= data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                naslovnaStr.setImageBitmap(selectedImage);
                uriSlike = imageUri.toString();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(context,"Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(context, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }
}