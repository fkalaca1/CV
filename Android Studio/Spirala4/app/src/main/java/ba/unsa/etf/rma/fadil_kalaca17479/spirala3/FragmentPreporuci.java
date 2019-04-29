package ba.unsa.etf.rma.fadil_kalaca17479.spirala3;


import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPreporuci extends Fragment {
    String knjiga_poslana;
    String autor_poslan;
    private klikNaDugmeVrati klik;


    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    public FragmentPreporuci() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_preporuci, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        knjiga_poslana = getArguments().getString("Naziv");
        autor_poslan = getArguments().getString("Autor");


        Button dPovratakizPreporuci = getView().findViewById(R.id.dPovratakizPreporuci);
        TextView txtNazivKnjige = getView().findViewById(R.id.txtMjestoKnjige);
        TextView txtNazivAutora = getView().findViewById(R.id.txtMjestoAutora);

        final Spinner sKontakti = getView().findViewById(R.id.sKontakti);
        final TextView stxtIme= getView().findViewById(R.id.stxtIme);

        Button dPosalji = getView().findViewById(R.id.dPosalji);

        //
        ArrayList<String> imenik = new ArrayList<>();//= getNameEmailDetails();
        //KASNIJE POPRAVI
        //imenik.add("emailadresa@hotmail.com");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getContext().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            imenik = getNameEmailDetails();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, imenik);
            sKontakti.setAdapter(adapter);
        }
        //
        //

        txtNazivAutora.setText(autor_poslan);
        txtNazivKnjige.setText(knjiga_poslana);

        dPovratakizPreporuci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    klik = (klikNaDugmeVrati) getActivity();
                }
                catch (ClassCastException e){
                    throw new ClassCastException(getActivity().toString() + "Treba implementirati");
                }
                klik.kliknutoDugmeVrati();
            }
        });

        dPosalji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] TO = {sKontakti.getSelectedItem().toString()};
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Procitaj knjigu");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Zdravo " + sKontakti.getSelectedItem().toString() + ", \nPročitaj knjigu "
                + knjiga_poslana + " od " + autor_poslan);
                try{
                    startActivity(Intent.createChooser(emailIntent,"Send mail..."));
                }catch(android.content.ActivityNotFoundException ex){

                }

            }
        });
    }

    public interface klikNaDugmeVrati{
        void kliknutoDugmeVrati();
    }

    public ArrayList<String> getNameEmailDetails(){
        ArrayList<String> names = new ArrayList<>();
        ContentResolver cr = getContext().getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor cur1 = cr.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        new String[]{id}, null);
                while (cur1.moveToNext()) {
                    //to get the contact names
                    String name=cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    Log.e("Name :", name);
                    String email = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    Log.e("Email", email);
                    if(email!=null){
                        names.add(email);
                    }
                }
                cur1.close();
            }
        }
        cur.close();
        return names;
    }

}
