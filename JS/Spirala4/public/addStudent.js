const validacijaPojedinacnog = new Validacija(document.getElementById('ispisGreski1'))
const validacijaMasovnog = new Validacija(document.getElementById('ispisGreski2'))
  function prvaFormaSubmit(e){
    e.preventDefault();
    var ime = document.getElementsByName("ime")[0];
    var indeks = document.getElementsByName("index")[0];
    var godina = document.getElementsByName("godina")[0];
    var stringIspis = "Sljedeća polja nisu validna:";
    var brojac = 0;

    if(validacijaPojedinacnog.ime(ime) == false){
        stringIspis += " ime";
        brojac += 1;
    }
    if(validacijaPojedinacnog.index(indeks) == false) {
        if(brojac != 0) stringIspis += ",";
        stringIspis += " indeks";
        brojac+=1;
    }
    if(validacijaPojedinacnog.godina(godina) == false) {
        if(brojac != 0) stringIspis += ",";
        stringIspis += " godina";
        brojac+=1;
    }
    stringIspis += "!";
    if(brojac == 0) stringIspis = '';
    validacijaPojedinacnog.ispisiPoruku(" " + stringIspis);

  }

  function drugaFormaSubmit(e){
    e.preventDefault();
    var godina = document.getElementsByName("godina")[1];
    var stringIspis = "Sljedeća polja nisu validna:";
    var brojac = 0;
    if(validacijaPojedinacnog.godina(godina) == false) { 
        stringIspis += " godina";
        brojac+=1;
    }
    stringIspis += "!";
    if(brojac == 0) stringIspis = '';
    validacijaPojedinacnog.ispisiPoruku(" " + stringIspis);

  }
