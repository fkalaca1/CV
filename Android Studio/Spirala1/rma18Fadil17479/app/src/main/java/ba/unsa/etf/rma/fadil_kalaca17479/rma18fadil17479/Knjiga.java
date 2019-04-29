package ba.unsa.etf.rma.fadil_kalaca17479.rma18fadil17479;

import java.io.Serializable;

/**
 * Created by Fadil on 3/31/2018.
 */

public class Knjiga implements Serializable {

    String imeAutora;
    String nazivKnjige;
    String kategorija;
    String urlKnjige;

    Boolean dalijeplav;

    public Knjiga(String imeAutora, String nazivKnjige, String kategorija) {
        this.imeAutora = imeAutora;
        this.nazivKnjige = nazivKnjige;
        this.kategorija = kategorija;
        this.urlKnjige = "";
        this.dalijeplav = false;
    }

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

    public Boolean getDalijeplav() {
        return dalijeplav;
    }

    public void setDalijeplav(Boolean dalijeplav) {
        this.dalijeplav = dalijeplav;
    }

    public String getUrlKnjige() {
        return urlKnjige;
    }

    public void setUrlKnjige(String urlKnjige) {
        this.urlKnjige = urlKnjige;
    }



}
