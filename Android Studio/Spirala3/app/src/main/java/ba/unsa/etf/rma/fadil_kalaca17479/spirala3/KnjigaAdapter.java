package ba.unsa.etf.rma.fadil_kalaca17479.spirala3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Fadil on 5/21/2018.
 */

public class KnjigaAdapter extends ArrayAdapter {
    int resource;
    Context context;


    public KnjigaAdapter(@NonNull Context context, int resource, @NonNull List objects){
        super(context, resource, objects);
        this.resource = resource;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LinearLayout view;
        if(convertView == null){
            view = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater layoutInflater;
            layoutInflater = (LayoutInflater) getContext().getSystemService(inflater);
            layoutInflater.inflate(resource,view,true);
        }
        else{
            view = (LinearLayout) convertView;
        }
        ImageView eNaslovna = view.findViewById(R.id.eNaslovna);
        TextView imeAutora = view.findViewById(R.id.eAutor);
        TextView nazivKnjige = view.findViewById(R.id.eNaziv);
        Knjiga trenutna = (Knjiga)getItem(position);
        if(trenutna.dalijeplav) view.setBackgroundResource(R.color.ZadanaBoja);//setBackgroundColor(0xffaabbed);
        if(!trenutna.getUrlKnjige().isEmpty())
        {
            try {
                final Uri imageUri;
                imageUri = Uri.parse(trenutna.getUrlKnjige());
                final InputStream imageStream = context.getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                eNaslovna.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(context,"Something went wrong", Toast.LENGTH_LONG).show();
            }
        } else {
            eNaslovna.setImageResource(R.drawable.uvodna);
        }

        imeAutora.setText(trenutna.getImeAutora());
        nazivKnjige.setText(trenutna.getNazivKnjige());
        return view;
    }
}
