package ba.unsa.etf.rma.fadil_kalaca17479.spirala3;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Fadil on 5/21/2018.
 */

public class Knjiga implements Serializable,Parcelable{
    //SPIRALA 3
    String id;
    String naziv;
    ArrayList<Autor> autori;
    String opis;
    String datumObjavljivanja;
    URL slika;
    int brojStrinica;

    public Knjiga(String id, String naziv, ArrayList<Autor> autori,
                  String opis, String datumObjavljivanja, URL slika, int brojStrinica) {
        this.id = id;
        this.naziv = naziv;
        this.autori = autori;
        this.opis = opis;
        this.datumObjavljivanja = datumObjavljivanja;
        this.slika = slika;
        this.brojStrinica = brojStrinica;
    }

    protected Knjiga(Parcel in) {
        id = in.readString();
        naziv = in.readString();
        opis = in.readString();
        datumObjavljivanja = in.readString();
        brojStrinica = in.readInt();
        imeAutora = in.readString();
        nazivKnjige = in.readString();
        kategorija = in.readString();
        urlKnjige = in.readString();
        byte tmpDalijeplav = in.readByte();
        dalijeplav = tmpDalijeplav == 0 ? null : tmpDalijeplav == 1;
    }

    public static final Creator<Knjiga> CREATOR = new Creator<Knjiga>() {
        @Override
        public Knjiga createFromParcel(Parcel in) {
            return new Knjiga(in);
        }

        @Override
        public Knjiga[] newArray(int size) {
            return new Knjiga[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public ArrayList<Autor> getAutori() {
        return autori;
    }

    public void setAutori(ArrayList<Autor> autori) {
        this.autori = autori;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getDatumObjavljivanja() {
        return datumObjavljivanja;
    }

    public void setDatumObjavljivanja(String datumObjavljivanja) {
        this.datumObjavljivanja = datumObjavljivanja;
    }

    public URL getSlika() {
        return slika;
    }

    public void setSlika(URL slika) {
        this.slika = slika;
    }

    public int getBrojStrinica() {
        return brojStrinica;
    }

    public void setBrojStrinica(int brojStrinica) {
        this.brojStrinica = brojStrinica;
    }

    //SPIRALA 2
    String imeAutora;
    String nazivKnjige;
    String kategorija;
    String urlKnjige;

    Boolean dalijeplav;

    public String getImeAutora() {
        return imeAutora;
    }

    public void setImeAutora(String imeAutora) {
        this.imeAutora = imeAutora;
    }

    public String getNazivKnjige() {
        return nazivKnjige;
    }

    public void setNazivKnjige(String nazivKnjige) {
        this.nazivKnjige = nazivKnjige;
    }

    public String getKategorija() {
        return kategorija;
    }

    public void setKategorija(String kategorija) {
        this.kategorija = kategorija;
    }

    public String getUrlKnjige() {
        return urlKnjige;
    }

    public void setUrlKnjige(String urlKnjige) {
        this.urlKnjige = urlKnjige;
    }

    public Boolean getDalijeplav() {
        return dalijeplav;
    }

    public void setDalijeplav(Boolean dalijeplav) {
        this.dalijeplav = dalijeplav;
    }

    public Knjiga(String imeAutora, String nazivKnjige, String kategorija) {
        this.imeAutora = imeAutora;
        this.nazivKnjige = nazivKnjige;
        this.kategorija = kategorija;
        this.urlKnjige = "";
        this.datumObjavljivanja = "";
        this.opis = "";
        this.brojStrinica = 0;
        this.id = "";
        this.slika = null;
        this.autori = new ArrayList<>();
        dalijeplav = false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(naziv);
        dest.writeString(opis);
        dest.writeString(datumObjavljivanja);
        dest.writeInt(brojStrinica);
        dest.writeString(imeAutora);
        dest.writeString(nazivKnjige);
        dest.writeString(kategorija);
        dest.writeString(urlKnjige);
        dest.writeByte((byte) (dalijeplav == null ? 0 : dalijeplav ? 1 : 2));
    }
}
