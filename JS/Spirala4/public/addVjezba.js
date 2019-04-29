
window.onload = () => {
    GetAjax().getGodine(
        (err, response) => {
            const sGodine = document.getElementsByName('sGodine');
            let options = '';
            const godine = JSON.parse(response);
            godine.forEach(godina => {
                options += '<option id="'+ godina.id +'">'+godina.nazivGod+'</option>';
            });
            if(options == ''){
                sGodine.forEach(godineElem => {
                    godineElem.disabled = true;
                })
            }
            sGodine.forEach(godineElem => {
                godineElem.innerHTML = options
            })
        }
    );

    GetAjax().getSpirale(
      (err, response) => {
        const sVjezbe = document.getElementsByName('sVjezbe');
        let options = '';
        const vjezbe = JSON.parse(response);
        vjezbe.forEach( item => {
          options += '<option id="'+item.id+'">'+item.naziv+'</option>'
        } );
        if(options == ''){
          sVjezbe.forEach( item => {
            item.disabled = true;
          } )
        }
        sVjezbe.forEach(item => {
          item.innerHTML = options
        });
        if(vjezbe[0]){
          updateZadatak(vjezbe[0].id);
        }
      }
    );
}

function fPostojecaFormaSubmit(e){
    e.preventDefault();
    const validacijafPostojeceg = new Validacija(document.getElementById('ispisGreski1'))

    var godina = document.getElementsByName("sGodine")[0];
    const vjezbe = document.getElementsByName('sVjezbe')[0];

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

    if(vjezbe.disabled || godina.disabled || !godina.options[godina.selectedIndex] || !vjezbe.options[vjezbe.selectedIndex]) return;

    const body = {
      sGodine: godina.options[godina.selectedIndex].id,
      sVjezbe: vjezbe.options[vjezbe.selectedIndex].id
    }


    const callback = (err, response) => {
      document.open();
      document.write(response);
      document.close();
    }

    GetAjax().addVjezba(body, callback);

}

function fNovaFormaSubmit(e){
    e.preventDefault();
    const validacijafNovog = new Validacija(document.getElementById('ispisGreski2'))

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
    if(godina.disabled) return;
    if(!naziv.value || !godina.options[godina.selectedIndex]){
      return;
    }
    const body = {
        naziv: naziv.value,
        sGodine: godina.options[godina.selectedIndex].id,
        spirala: document.getElementsByName('spirala')[0].checked
    }

    const callback = (err, response) => {
      document.open();
      document.write(response);
      document.close();
    }

    GetAjax().addVjezba(body, callback)
}

function updateZadatak(vjezbaId){
  const sZadatak = document.getElementsByName('sZadatak')[0];
  const callback = (err, response) => {
    const zadaci = JSON.parse(response);
    let options = '';
    zadaci.forEach( zadatak => {
      options += '<option id="'+ zadatak.id +'">'+zadatak.naziv+'</option>';
    } );
    sZadatak.innerHTML = options;
  }

  if(!vjezbaId && vjezbaId != 0){
    return;
  }

  GetAjax().getZadatakBezVjezbi(vjezbaId, callback)
}

function fVjezbeOnChange(e){
  e.preventDefault();
  const vjezbe = document.getElementsByName('sVjezbe')[1];
  updateZadatak(vjezbe.options[vjezbe.selectedIndex].id)
}

function fPoveziZadatakSubmit(e){
  e.preventDefault();
  const vjezbe = document.getElementsByName('sVjezbe')[1];
  const zadatak = document.getElementsByName('sZadatak')[0];

  const callback = (err, response) => {
    document.open();
    document.write(response);
    document.close();
  }

  if(!vjezbe.options[vjezbe.selectedIndex] || !zadatak.options[zadatak.selectedIndex]){
    return;
  }

  GetAjax().addVjezbaZadatak(
    vjezbe.options[vjezbe.selectedIndex].id,
    { sZadatak: zadatak.options[zadatak.selectedIndex].id },
    callback
  )
}
