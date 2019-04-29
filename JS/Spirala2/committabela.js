var CommitTabela=(function(){
    var tbodyElement = '';
    var _tableElement = '';
    const InitTable = function(brojRedova) {
            let tbodyContent = "<tbody>" ;
            let tHeaderContent = "" ;
            tHeaderContent = `
            <thead>
                <th>Naziv Zadatka</th>
                <th>Commiti</th> 
            </thead>
            `;
        for(let i = 0; i<brojRedova; i++){
            tbodyContent += '<tr>';
            tbodyContent += '<td class="prvaKolona">';
            tbodyContent += '<a href="https://docs.google.com/document/d/1ZjkBwWG_gsRokjp7oGwDpFm40qXM1tB9AwcV5_Qv8W8/edit" target="_blank">Zadatak ';
            tbodyContent += i+1 + '</a></td>';
            tbodyContent += '<td colspan="1"></td></tr>';
        }

        tbodyContent += "</tbody>" ;
        _tableElement.innerHTML = tHeaderContent + tbodyContent;
    }

    const dajVrijednostCelije = (broj, url) => {
        return `<a href=${url} target="_blank">${broj}</a>`
    }
    const popraviTabelu = function() {
        const redovi = tbodyElement.rows;
        let daLiSuZadnjiPrazni = true;
        for(let i = 0; i< redovi.length; i++){
            if(redovi[i].cells[redovi[i].cells.length - 1].innerHTML != '') daLiSuZadnjiPrazni = false;
        }
        if(daLiSuZadnjiPrazni){
            for(let i = 0; i< redovi.length; i++){
                if(redovi[i].cells[redovi[i].cells.length - 1].colSpan == 1) redovi[i].deleteCell(redovi[i].cells.length - 1);
                else redovi[i].cells[redovi[i].cells.length - 1].colSpan -= 1;
            }
        }
        let sviPoJednuCeliju = true;
        for(let i = 0; i<redovi.length;i++){
            if(redovi[i].cells.length != 1) sviPoJednuCeliju = false;
        }
        if(sviPoJednuCeliju){
            for(let i = 0; i<redovi.length;i++){
                redovi[i].insertCell(redovi[i].cells.length);
            }
        }
        for(let i = 0; i< redovi.length; i++){
            let brojPraznih = 0;
            for(let j = 0; j< redovi[i].cells.length; j++){
                if(redovi[i].cells[j].innerHTML == '') brojPraznih +=1;
            }

            if(brojPraznih > 1){
                for(let j = 0; j< redovi[i].cells.length; j++){
                    if(redovi[i].cells[j].innerHTML == '') {
                        redovi[i].cells[j].colSpan += brojPraznih - 1;
                        break;
                    }
                }
                for(let j = 0; j< redovi[i].cells.length; j++){
                    if(redovi[i].cells[j].innerHTML == '' && redovi[i].cells[j].colSpan == 1) {
                        redovi[i].deleteCell(j);
                    }
                }

            }
        }

        let velicina = redovi[0].cells.length - 1 + redovi[0].cells[redovi[0].cells.length - 1].colSpan;
        var x=document.getElementById("commiti");
        y = x.tHead;
        if(velicina != y.rows[0].cells[y.rows[0].cells.length-1].colSpan + 1)
        y.rows[0].cells[y.rows[0].cells.length-1].colSpan -=1;
    }
    var konstruktor=function(tableElement,brojZadataka){

        _tableElement = tableElement;
        InitTable(brojZadataka);
        tbodyElement = tableElement.tBodies[0];
        return{
            dodajCommit:function(rbZadatka,url){
                const redovi = tbodyElement.rows;
                let jelDodan = false;
                let jelDodanRed = false;
                for(let i = 0; i<redovi.length; i++){
                    if(jelDodan) break;
                    for(let j = 1; j<redovi[i].cells.length; j++){
                        if(jelDodan) break;
                        if(i == rbZadatka ){ 
                            if(redovi[i].cells[j].innerHTML !== '' && j=== redovi[i].cells.length-1){
                                redovi[i].insertCell(redovi[i].cells.length);
                                redovi[i].cells[j+1].innerHTML = dajVrijednostCelije(Number(redovi[i].cells[j].children[0].innerHTML) + 1,url);
                                jelDodan = true;
                                jelDodanRed = true;
                            }
                            if(redovi[i].cells[j].innerHTML === ''){
                                if(redovi[i].cells[j].colSpan !== 1){
                                    redovi[i].insertCell(redovi[i].cells.length);
                                    redovi[i].cells[j+1].colSpan = redovi[i].cells[j].colSpan - 1;
                                    redovi[i].cells[j].colSpan = 1;
                                    if(Number(redovi[i].cells[j-1].children[0].innerHTML) == redovi[i].cells[j-1].children[0].innerHTML)
                                    redovi[i].cells[j].innerHTML = dajVrijednostCelije(Number(redovi[i].cells[j-1].children[0].innerHTML)+1,url);
                                    else redovi[i].cells[j].innerHTML = dajVrijednostCelije(1,url);
                                    jelDodan = true;
                                }
                                else{
                                    if(Number(redovi[i].cells[j-1].children[0].innerHTML) == redovi[i].cells[j-1].children[0].innerHTML)
                                    redovi[i].cells[j].innerHTML = dajVrijednostCelije(Number(redovi[i].cells[j-1].children[0].innerHTML)+1,url);
                                    else redovi[i].cells[j].innerHTML = dajVrijednostCelije(1,url);
                                    jelDodan = true;
                                }
                            }
                        }
                                            
                    }
                }
                let brojCelija = 0;
                for(let i=0;i<redovi.length; i++){
                    if(brojCelija < redovi[i].cells.length)
                    brojCelija = redovi[i].cells.length;
                }
                for(let i=0; i<redovi.length; i++){
                    for(let j=1;j<redovi[i].cells.length; j++){
                        if(redovi[i].cells[j].innerHTML === '' && j=== redovi[i].cells.length-1){
                            redovi[i].cells[j].colSpan = brojCelija - j;
                        }
                        if(i != rbZadatka && j == redovi[i].cells.length-1 && redovi[i].cells[j].innerHTML !== '' && jelDodanRed) {
                            redovi[i].insertCell(redovi[i].cells.length);
                        }
                    }
                }
                // ZA COMMIT COLSPAN   
                if(jelDodanRed){
                    jelDodanRed =  false;
                    var x=document.getElementById("commiti");
                    y = x.tHead;
                    y.rows[0].cells[y.rows[0].cells.length-1].colSpan +=1;
                }
                


            },
            editujCommit:function(rbZadatka,rbCommita,url){
                const red = tbodyElement.rows[rbZadatka];
                if(!red) return -1;
                for(let j = 1; j<red.cells.length; j++){
                    if(j == Number(rbCommita) + 1 &&  red.cells[j].innerHTML !=''){
                        red.cells[j].innerHTML = dajVrijednostCelije(Number(red.cells[j].children[0].innerHTML),url);
                        return;
                    }
                }


                return -1;

            },
            obrisiCommit:function(rbZadatka,rbCommita){
                const red = tbodyElement.rows[rbZadatka];
                const redovi = tbodyElement.rows;
                if(!red) return -1;
                for(let j = 1; j<red.cells.length; j++){
                    if(j == Number(rbCommita) + 1 &&  red.cells[j].innerHTML !=''){
                        if(red.cells.length != 2){
                            red.deleteCell(j);
                            red.insertCell(red.cells.length);
                        }
                        else red.cells[j].innerHTML = '';
                        
                        popraviTabelu();
                        return;
                    }
                }



                return -1;        
            }
        }
    }

    return konstruktor;
}());