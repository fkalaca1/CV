const validacijafAddGodina = new Validacija(document.getElementById('ispisGreske'))

function formaAGodina(e){
    e.preventDefault();
    var godina = document.getElementsByName("naziv")[0];
    var repozitorijiV = document.getElementsByName("rvjezbe")[0];
    var repozitorijiS = document.getElementsByName("rspirale")[0];
    var stringIspis = "Sljedeća polja nisu validna:";
    var brojac = 0;
    if(validacijafAddGodina.godina(godina) == false) {
        if(brojac != 0) stringIspis += ","; 
        stringIspis += " godina";
        brojac+=1;
    }
    if(validacijafAddGodina.repozitorij(repozitorijiV) == false) {
        if(brojac != 0) stringIspis += ","; 
        stringIspis += " repozitoriji vjezbe";
        brojac+=1;
    }
    if(validacijafAddGodina.repozitorij(repozitorijiS) == false) {
        if(brojac != 0) stringIspis += ","; 
        stringIspis += " repozitoriji spirale";
        brojac+=1;
    }
    stringIspis += "!";
    if(brojac == 0) stringIspis = '';
    validacijafAddGodina.ispisiPoruku(" " + stringIspis);
}