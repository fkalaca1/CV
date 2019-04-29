const validacijaFormAZ = new Validacija(document.getElementById('ispisGreskiAZ'))

function FormaSubmitAZ(e){
    e.preventDefault();
    var name = document.getElementsByName("naziv")[0];
    var stringIspis = "Sljedeća polja nisu validna:";
    var brojac = 0;
    if(validacijaFormAZ.naziv(name) == false){
        stringIspis += " naziv";
        brojac += 1;
    }
    stringIspis += "!";
    if(brojac == 0) stringIspis = '';
    validacijaFormAZ.ispisiPoruku("" + stringIspis);
    if(brojac !== 0) return;

    const formData = new FormData();
    formData.append("postavka", document.getElementsByName("postavka")[0].files[0]);
    formData.append('naziv', document.getElementsByName('naziv')[0].value);

    const callback = (err, response) => {
        if(response === "" || !response){
            return;
        }
        document.write(response);
    }

    GetAjax().dodajZadatak(formData, callback)
}
