const validacijaLogin = new Validacija(document.getElementById('ispisGreski'))
function loginValidacija(e){
    e.preventDefault();
    var ime = document.getElementsByName("username")[0];
    var password = document.getElementsByName("password")[0];
    var stringIspis = "Sljedeća polja nisu validna:";
    var brojac = 0;

    if(validacijaLogin.ime(ime) == false){
        stringIspis += " ime";
        brojac += 1;
    }
    if(validacijaLogin.password(password) == false) {
        if(brojac != 0) stringIspis += ","; 
        stringIspis += " password";
        brojac+=1;
    }
    stringIspis += "!";
    if(brojac == 0) stringIspis = '';
    validacijaLogin.ispisiPoruku(" " + stringIspis);

}