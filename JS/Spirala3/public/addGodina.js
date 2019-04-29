window.onload = () => {
  (new GodineAjax(document.getElementById('glavniSadrzaj'))).osvjezi();
}

function formaAGodina(e){
    let validacijafAddGodina = new Validacija(document.getElementById('ispisGreske'))
    e.preventDefault();
    var godina = document.getElementsByName("nazivGod")[0];
    var repozitorijiV = document.getElementsByName("nazivRepVje")[0];
    var repozitorijiS = document.getElementsByName("nazivRepSpi")[0];
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
    if(brojac !== 0) return;


    const body = {
      nazivGod: document.getElementsByName("nazivGod")[0].value,
      nazivRepVje: document.getElementsByName('nazivRepVje')[0].value,
      nazivRepSpi: document.getElementsByName('nazivRepSpi')[0].value
    }

    const callback = (err, response) => {
        console.log(response)
        if(err){
          document.open();
          document.write(response);
          document.close();
        }
    }

    GetAjax().addGodina(body, callback);

}
