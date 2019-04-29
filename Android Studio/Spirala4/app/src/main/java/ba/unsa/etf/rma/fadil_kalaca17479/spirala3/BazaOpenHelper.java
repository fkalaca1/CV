package ba.unsa.etf.rma.fadil_kalaca17479.spirala3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceGroup;
import android.util.Log;

import org.w3c.dom.Text;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class BazaOpenHelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "LibraryDB.db";
    public static final int DATABASE_VERSION = 58;

    public static final String DATABASE_TABLE_KATEGORIJA = "Kategorija";
    public static final String KATEGORIJA_ID = "_id";
    public static final String KATEGORIJA_NAZIV = "naziv";

    public static final String DATABASE_TABLE_KNJIGA = "Knjiga";
    public static final String KNJIGA_ID = "_id";
    public static final String KNJIGA_NAZIV = "naziv";
    public static final String KNJIGA_OPIS = "opis";
    public static final String KNJIGA_DATUMOBJAVLJIVANJA = "datumObjavljivanja";
    public static final String KNJIGA_BROJSTRANICA = "brojStranica";
    public static final String KNJIGA_IDWEBSERVISI = "idWebServis";
    public static final String KNJIGA_IDKATEGORIJE = "idkategorije";
    public static final String KNJIGA_SLIKAURL = "slika";
    public static final String KNJIGA_PREGLEDANA = "pregledana";


    public static final String DATABASE_TABLE_AUTOR = "Autor";
    public static final String AUTOR_ID = "_id";
    public static final String AUTOR_IME = "ime";

    public static final String DATABASE_TABLE_AUTORSTVO = "Autorstvo";
    public static final String AUTORSTVO_ID = "_id";
    public static final String AUTORSTVO_AUTOR_ID = "idautora";
    public static final String AUTORSTVO_KNJIGA_ID = "idknjige";

    public int Kategorija_id_autoincrementer = 1;

    SQLiteDatabase db = getWritableDatabase();

    private static final String CREATE_TABLE_KATEGORIJA = "CREATE TABLE " + DATABASE_TABLE_KATEGORIJA + " (" +
            KATEGORIJA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KATEGORIJA_NAZIV + " TEXT );";

    private static final String CREATE_TABLE_AUTOR = "CREATE TABLE " + DATABASE_TABLE_AUTOR + " (" +
            AUTOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            AUTOR_IME + " TEXT );";

    private static final String CREATE_TABLE_AUTORSTVO = "CREATE TABLE " + DATABASE_TABLE_AUTORSTVO + " (" +
            AUTORSTVO_ID +  " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            AUTORSTVO_AUTOR_ID + " INTEGER, " +
            AUTORSTVO_KNJIGA_ID + " INTEGER );";

    private static final String CREATE_TABLE_KNJIGA = "CREATE TABLE " + DATABASE_TABLE_KNJIGA + " (" +
            KNJIGA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KNJIGA_NAZIV + " TEXT, " +
            KNJIGA_OPIS + " TEXT, " +
            KNJIGA_DATUMOBJAVLJIVANJA + " TEXT, " +
            KNJIGA_BROJSTRANICA + " INTEGER, " +
            KNJIGA_IDWEBSERVISI + " TEXT, " +
            KNJIGA_IDKATEGORIJE + " INTEGER, " +
            KNJIGA_SLIKAURL +  " TEXT, " +
            KNJIGA_PREGLEDANA + " INTEGER );";





    //________________________________________________

    public BazaOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_KATEGORIJA);
        db.execSQL(CREATE_TABLE_KNJIGA);
        db.execSQL(CREATE_TABLE_AUTOR);
        db.execSQL(CREATE_TABLE_AUTORSTVO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_KATEGORIJA);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_KNJIGA);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_AUTOR);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_AUTORSTVO);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db){
        super.onOpen(db);
    }
    //KATEGORIJA SVE OKO PRVOG ZADATKA
    public long dodajKategoriju(String naziv) throws Exception{
        long vrati;
        Log.d("Pri ubacanju",  "dosao u dodajKategoriju");
        ContentValues values = new ContentValues();
        values.put(KATEGORIJA_NAZIV,naziv);
        try{
            vrati = db.insertOrThrow(DATABASE_TABLE_KATEGORIJA, null, values);
        }catch (SQLiteConstraintException e){
            return -1;
        }
        Log.d("Poslije ubacanja", vrati + "");
        return vrati;
    }

    public ArrayList<String> dajKategorije() throws Exception{
        ArrayList<String> unosi = new ArrayList<>();
        try{
            Cursor cur = dajSveKategorije();
            if(cur.getCount() == 0) return unosi;
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    unosi.add(cur.getString(1));
                }
            }
            cur.close();
            return unosi;
        } catch(SQLiteException e){
            return unosi;
        }
    }

    public Cursor dajSveKategorije(){
        Cursor cur = db.rawQuery("SELECT * FROM "+ DATABASE_TABLE_KATEGORIJA,null);
        return cur;
    }
    //___________GOTOVO SA PRVIM ZADATKOM
    //ZA KNJIGEEEE____________DRUGI ZADATAK
    public long dodajKnjigu(Knjiga knjiga) throws Exception{
        Log.d("DodavanjeK",  " uslo u dodajKnjigu");
        long vrati = -1;
        ContentValues values = new ContentValues();
        values.put(KNJIGA_NAZIV,knjiga.getNazivKnjige());
        values.put(KNJIGA_OPIS,knjiga.getOpis());
        values.put(KNJIGA_DATUMOBJAVLJIVANJA,knjiga.getDatumObjavljivanja());
        values.put(KNJIGA_BROJSTRANICA,knjiga.getBrojStrinica());
        values.put(KNJIGA_IDWEBSERVISI,knjiga.getId());
        long uhvati = pronadjiKategorijuodKnjige(knjiga.getKategorija());
        values.put(KNJIGA_IDKATEGORIJE,uhvati);
        if(knjiga.getSlika() != null) values.put(KNJIGA_SLIKAURL,knjiga.getSlika().toString());
        else values.put(KNJIGA_SLIKAURL,"");
        int a = 0;
        if(knjiga.getDalijeplav()) a = 1;
        values.put(KNJIGA_PREGLEDANA,a);
        try{
            Cursor cur = db.rawQuery("SELECT * FROM "+ DATABASE_TABLE_KNJIGA + " WHERE " +
                    KNJIGA_NAZIV + " = ?" , new String[]{knjiga.getNazivKnjige()});
            if(cur.getCount() == 0) vrati = db.insertOrThrow(DATABASE_TABLE_KNJIGA, null, values);
            cur.close();
            long autorid1 = dodajAutoraIzKnjige(knjiga.getImeAutora());
            long autorstvoid1 = 0;
            if(vrati != -1 && autorid1 != -1) {
                autorstvoid1 = dodajAutorstvoizKandA(vrati,autorid1);
                Log.d("AutorstvoID1", "ovo je id -> " + autorstvoid1);
            }
            for (Autor x: knjiga.getAutori()) {
                autorid1 = dodajAutoraIzKnjige(x.getImeiPrezime());
                if(vrati != -1 && autorid1 != -1) {
                    autorstvoid1 = dodajAutorstvoizKandA(vrati,autorid1);
                    Log.d("AutorstvoID2", "ovo je id -> " + autorstvoid1);
                }
            }
            Log.d("DodavanjeK",  " ovo je index" + vrati);
        }catch (SQLiteConstraintException e){
            return -1;
        }
        return vrati;
    }
    public long pronadjiKategorijuodKnjige(String s){
        long x = -1;
        Cursor cur = db.rawQuery("SELECT * FROM "+ DATABASE_TABLE_KATEGORIJA + " WHERE " +
                KATEGORIJA_NAZIV + " = ?" , new String[]{s});
        if(cur.getCount() > 0){
            while(cur.moveToNext()){
                x = cur.getLong(0);
            }
        }
        cur.close();
        return x;
    }

    public long dodajAutoraIzKnjige(String autor){
        long x = -1;
        Log.d("DodavanjeK",  " ovo je uslo u autora = " + autor);
        Cursor cur = db.rawQuery("SELECT * FROM "+ DATABASE_TABLE_AUTOR + " WHERE " +
                AUTOR_IME + " = ?" , new String[]{autor});
        if(cur.getCount() == 0){
            ContentValues values = new ContentValues();
            values.put(AUTOR_IME,autor);
            x = db.insertOrThrow(DATABASE_TABLE_AUTOR,null,values);
        }
        else {
            cur.moveToNext();
            x = cur.getLong(0);
        }
        cur.close();
        Log.d("DodavanjeK",  " ovo je zavrsilo autora id = " + x);
        return x;
    }
    public long dodajAutorstvoizKandA(long kid, long aid){
        long x = -1;
        ContentValues values = new ContentValues();
        values.put(AUTORSTVO_KNJIGA_ID, kid);
        values.put(AUTORSTVO_AUTOR_ID,aid);
        // Promjeni treba 2 uslova u where , ako postoji dodaj ako ne vrati stari index
        Cursor cur = db.rawQuery("SELECT * FROM "+ DATABASE_TABLE_AUTORSTVO + " WHERE " + AUTORSTVO_KNJIGA_ID  + " = ? AND "
                + AUTORSTVO_AUTOR_ID  + " = ?",new String[]{String.valueOf(kid),String.valueOf(aid)});
        if(cur.getCount() == 0) x = db.insertOrThrow(DATABASE_TABLE_AUTORSTVO,null,values);
        else{
            cur.moveToFirst();
            x = cur.getLong(0);
        }
        return x;
    }
    //_______________KRAJ KNJIGAAA
    //__________TRECI ZADATAK
    public ArrayList<Knjiga> knjigeKategorije(long idKategorije){
        ArrayList<Knjiga> vratcajlistu = new ArrayList<>();
        //Cursor cur = db.rawQuery("SELECT * FROM "+ DATABASE_TABLE_KNJIGA + " WHERE " +
        //        KNJIGA_IDKATEGORIJE + " = " + idKategorije,null);
        Cursor cur = db.rawQuery("SELECT * FROM "+ DATABASE_TABLE_KNJIGA + " WHERE " + KNJIGA_IDKATEGORIJE + " = ?",new String[]{String.valueOf(idKategorije)});
        if(cur.getCount() > 0){
            while(cur.moveToNext()){
                Knjiga temp = new Knjiga(dajAutoraOdIdKnjige(cur.getLong(0)),cur.getString(1),dajKategorijuOdKnjige(idKategorije));//ime autora, naziv knjige, kategorija);
                temp.setOpis(cur.getString(2));
                temp.setDatumObjavljivanja(cur.getString(3));
                temp.setBrojStrinica(cur.getInt(4));
                temp.setId(cur.getString(5));
                try {
                    if(cur.getString(7).equals("")) temp.setSlika(null);
                        else temp.setSlika(new URL(cur.getString(7)));
                } catch (MalformedURLException e) {
                }
                temp.setAutori(dajAutoreOdIDKnjige(cur.getLong(0)));
                Boolean wtf = false;
                if(cur.getInt(8) == 1) wtf = true;
                temp.setDalijeplav(wtf);
                vratcajlistu.add(temp);
            }
        }
        cur.close();
        return vratcajlistu;
    }

    public String dajKategorijuOdKnjige(long idKategorije){
        String x = "";
        Cursor cur = db.rawQuery("SELECT * FROM "+ DATABASE_TABLE_KATEGORIJA + " WHERE "+ KATEGORIJA_ID + " = ?", new String[]{String.valueOf(idKategorije)});
        if(cur.getCount() > 0){
           cur.moveToFirst();
           x = cur.getString(1);
        }
        cur.close();
        return x;
    }

    public String dajAutoraOdIdKnjige(long idKnjige){
        String x = "";
        Cursor cur = db.rawQuery("SELECT * FROM "+ DATABASE_TABLE_AUTORSTVO + " WHERE "+ AUTORSTVO_KNJIGA_ID + " = ?", new String[]{String.valueOf(idKnjige)});
        if(cur.getCount() > 0){
            cur.moveToFirst();
            long idAutoraTemp = cur.getLong(1);
            Cursor cur1 = db.rawQuery("SELECT * FROM "+ DATABASE_TABLE_AUTOR + " WHERE "+ AUTOR_ID + " = ?",new String[]{String.valueOf(idAutoraTemp)});
            if(cur1.getCount()>0){
                cur1.moveToFirst();
                x = cur1.getString(1);
            }
            cur1.close();
        }
        cur.close();
        return x;
    }

    public ArrayList<Autor> dajAutoreOdIDKnjige(long idKnjige){
        ArrayList<Autor> x = new ArrayList<>();
        Cursor cur = db.rawQuery("SELECT * FROM "+ DATABASE_TABLE_AUTORSTVO + " WHERE "+ AUTORSTVO_KNJIGA_ID + " = ?", new String[]{String.valueOf(idKnjige)});
        if(cur.getCount() > 0){
            while(cur.moveToNext()){
                Cursor cur1 = db.rawQuery("SELECT * FROM "+ DATABASE_TABLE_AUTOR + " WHERE "+ AUTOR_ID + " = ?" , new String[]{String.valueOf(cur.getInt(1))});
                if(cur1.getCount()>0){
                    cur1.moveToFirst();
                    Autor autortemp = new Autor(cur1.getString(1));
                    x.add(autortemp);
                }
            }
        }
        cur.close();
        return x;
    }

    public long dajIdKategorije(String kljuc){
        Cursor cur = db.rawQuery("SELECT * FROM "+ DATABASE_TABLE_KATEGORIJA + " WHERE " + KATEGORIJA_NAZIV + " = ?",new String[]{kljuc});
        long x = -1;
        if(cur.getCount() > 0) {
            cur.moveToFirst();
            x = cur.getLong(0);
        }
        cur.close();
        return x;
    }

    //___________
    //___________
    public ArrayList<Autor> dajSveAutoreUPocetni(){
        ArrayList<Autor> vrati = new ArrayList<>();
        Cursor cur = db.rawQuery("SELECT * FROM " + DATABASE_TABLE_AUTOR , null);
        if(cur.getCount()>0){
            while(cur.moveToNext()){
                Autor novi = new Autor(cur.getString(1));
                int brojac = 0;
                Cursor curPom = db.rawQuery("SELECT * FROM " + DATABASE_TABLE_AUTORSTVO + " WHERE " + AUTORSTVO_AUTOR_ID
                        + " = ?",new String[]{String.valueOf(cur.getLong(0))});
                if(curPom.getCount() > 0){
                    while(curPom.moveToNext()){
                        brojac++;
                    }
                }
                novi.setBrojknjiga(brojac);
                vrati.add(novi);
                curPom.close();
            }
        }
        cur.close();
        return vrati;
    }
    //___________ZADATAK 4
    public ArrayList<Knjiga> knjigeAutora(long idAutora){
        ArrayList<Knjiga> vrati = new ArrayList<>();
        Cursor cur = db.rawQuery("SELECT * FROM "+ DATABASE_TABLE_AUTORSTVO + " WHERE " + AUTORSTVO_AUTOR_ID + " = ?",new String[]{String.valueOf(idAutora)});
        if(cur.getCount()>0){
            Log.d("PoslijeIF", "Kolika je velicina -->" + cur.getCount() );
            while(cur.moveToNext()){
                Cursor curPom = db.rawQuery("SELECT * FROM "+ DATABASE_TABLE_KNJIGA + " WHERE "
                        + KNJIGA_ID + " = ?",new String[]{String.valueOf(cur.getLong(2))});
                if(curPom.getCount()>0){
                    curPom.moveToFirst();
                    Knjiga temp = new Knjiga(dajAutoraOdIdKnjige(curPom.getLong(0)),curPom.getString(1),
                            dajKategorijuOdKnjige(curPom.getLong(6)));//ime autora, naziv knjige, kategorija);
                    temp.setOpis(curPom.getString(2));
                    temp.setDatumObjavljivanja(curPom.getString(3));
                    temp.setBrojStrinica(curPom.getInt(4));
                    temp.setId(curPom.getString(5));
                    try {
                        if(curPom.getString(7).equals("")) temp.setSlika(null);
                        else temp.setSlika(new URL(curPom.getString(7)));
                    } catch (MalformedURLException e) {
                    }
                    temp.setAutori(dajAutoreOdIDKnjige(curPom.getLong(0)));
                    Boolean wtf = false;
                    if(curPom.getInt(8) == 1) wtf = true;
                    temp.setDalijeplav(wtf);
                    vrati.add(temp);
                    Log.d("Tagujem", "Dosao sam ovdje ");
                }
                curPom.close();
            }
        }
        cur.close();
        return vrati;
    }

    public long dajIdAutoraOdImena(String autor){
        long idAutora = -1;
        Cursor cur = db.rawQuery("SELECT * FROM "+ DATABASE_TABLE_AUTOR + " WHERE " + AUTOR_IME + " = ?",new String[]{autor});
        if(cur.getCount()>0){
            cur.moveToFirst();
            idAutora = cur.getLong(0);
        }
        cur.close();
        return idAutora;
    }
    //___________
    public void promjeniPozadinu(String nazivKnjige){
        ContentValues contentV = new ContentValues();
        contentV.put(KNJIGA_PREGLEDANA,1);
        db.update(DATABASE_TABLE_KNJIGA,contentV,KNJIGA_NAZIV + " = \"" + nazivKnjige + "\"",null);
    }
    //___________
    public void Ugasi(){
        db.close();
    }

}
