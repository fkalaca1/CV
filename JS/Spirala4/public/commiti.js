
    var tabela ;
    const validacijaAdd = new Validacija(document.getElementById('ispisGreskiAdd'));
    const validacijaEdit = new Validacija(document.getElementById('ispisGreskiEdit'));
    const validacijaDelete = new Validacija(document.getElementById('ispisGreskiDelete'));

function formatTabele(e){
    e.preventDefault();
    var x = document.getElementsByName("brRedovi")[0];
    tabela = new CommitTabela(document.getElementById('commiti'), x.value);

}

function addC(e){
    e.preventDefault();
    var x = document.getElementsByName("brRed")[0];
    var y = document.getElementsByName("xUrl")[0];


    var stringIspis = "Sljedeća polja nisu validna:";
    var brojac = 0;

    if(validacijaAdd.url(y) == false){
        stringIspis += " url";
        brojac += 1;
    }
    stringIspis += "!";
    if(brojac == 0) {
        stringIspis = '';
        tabela.dodajCommit(x.value,y.value);
    }
    validacijaAdd.ispisiPoruku(" " + stringIspis);

}

function editC(e){
    e.preventDefault();
    var x = document.getElementsByName("brRedE")[0];
    var y = document.getElementsByName("yUrl")[0];
    var z = document.getElementsByName("brKolE")[0];


    var stringIspis = "Sljedeća polja nisu validna:";
    var brojac = 0;

    if(validacijaAdd.url(y) == false){
        stringIspis += " url";
        brojac += 1;
    }
    stringIspis += "!";
    if(brojac == 0) {
        stringIspis = '';
        tabela.editujCommit(x.value,z.value,y.value);
    }
    validacijaAdd.ispisiPoruku(" " + stringIspis);

}

function deleteC(e){
    e.preventDefault();
    var x = document.getElementsByName("brRedE")[1];
    var z = document.getElementsByName("brKolE")[1];
    tabela.obrisiCommit(x.value,z.value);

}
