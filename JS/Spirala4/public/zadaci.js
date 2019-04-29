const validacijaLogin = new Validacija(document.getElementById('ispisGreski'))
function validacijaZadaci(e){
    e.preventDefault();
    var ime = document.getElementsByName("query")[0];
    var stringIspis = "Sljedeća polja nisu validna:";
    var brojac = 0;

    if(validacijaLogin.ime(ime) == false){
        stringIspis += " ime";
        brojac += 1;
    }
    stringIspis += "!";
    if(brojac == 0) stringIspis = '';
    validacijaLogin.ispisiPoruku(" " + stringIspis);

}