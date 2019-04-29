const validacijafPostojeceg = new Validacija(document.getElementById('ispisGreski1'))
const validacijafNovog = new Validacija(document.getElementById('ispisGreski2'))

function fPostojecaFormaSubmit(e){
    e.preventDefault();
    var godina = document.getElementsByName("sGodine")[0];
    var stringIspis = "Sljedeća polja nisu validna:";
    var brojac = 0;
    if(validacijafPostojeceg.godina(godina) == false) {
        if(brojac != 0) stringIspis += ","; 
        stringIspis += " godina";
        brojac+=1;
    }
    stringIspis += "!";
    if(brojac == 0) stringIspis = '';
    validacijafPostojeceg.ispisiPoruku(" " + stringIspis);
}

function fNovaFormaSubmit(e){
    e.preventDefault();
    var naziv = document.getElementsByName("naziv")[0];
    var godina = document.getElementsByName("sGodine")[1];
    var stringIspis = "Sljedeća polja nisu validna:";
    var brojac = 0;
    if(validacijafNovog.naziv(naziv) == false) {
        if(brojac != 0) stringIspis += ","; 
        stringIspis += " naziv";
        brojac+=1;
    }
    if(validacijafNovog.godina(godina) == false) {
        if(brojac != 0) stringIspis += ","; 
        stringIspis += " godina";
        brojac+=1;
    }
    stringIspis += "!";
    if(brojac == 0) stringIspis = '';
    validacijafNovog.ispisiPoruku(" " + stringIspis);
}