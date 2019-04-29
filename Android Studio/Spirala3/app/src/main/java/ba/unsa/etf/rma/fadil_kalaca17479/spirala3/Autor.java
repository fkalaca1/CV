package ba.unsa.etf.rma.fadil_kalaca17479.spirala3;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Fadil on 5/21/2018.
 */

public class Autor implements Serializable {
    //SPIRALA 3
    String imeiPrezime;
    ArrayList<String> knjige;

    public Autor(String imeiPrezime, String id) {
        this.imeiPrezime = imeiPrezime;
        this.knjige = new ArrayList<>();
        knjige.add(id);
    }
    public void dodajKnjigu(String id){
        int brojac = 0;
        for (String idx: knjige) {
            if(idx.equals(id)){
                brojac++;
            }
        }
        if(brojac != 0) knjige.add(id);
    }

    public String getImeiPrezime() {
        return imeiPrezime;
    }

    public void setImeiPrezime(String imeiPrezime) {
        this.imeiPrezime = imeiPrezime;
    }

    public ArrayList<String> getKnjige() {
        return knjige;
    }

    public void setKnjige(ArrayList<String> knjige) {
        this.knjige = knjige;
    }

    //SPIRALA 2
    String ime;
    int brojknjiga;
    String info;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public int getBrojknjiga() {
        return brojknjiga;
    }

    public void setBrojknjiga(int brojknjiga) {
        this.brojknjiga = brojknjiga;
        this.info = this.ime + "  - Broj knjiga autora je: " + this.brojknjiga;
    }

    public Autor(String ime) {
        this.ime = ime;
        this.brojknjiga = 1;
        this.info = this.ime + "  - Broj knjiga autora je: " + this.brojknjiga;
    }
}
