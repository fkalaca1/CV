package ba.unsa.etf.rma.fadil_kalaca17479.rma18kalaca17479;

import java.io.Serializable;

/**
 * Created by Fadil on 4/6/2018.
 */

public class Autor implements Serializable {
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
