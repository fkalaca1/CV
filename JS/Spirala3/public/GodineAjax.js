var GodineAjax = (function(){
  var konstruktor = function(divSadrzaj){
    const API_BASE_URL = 'http://localhost:8080';

    const _sendJsonRequest = (method, uri, body, cb) => {
      const ajax = new XMLHttpRequest();
      ajax.onreadystatechange = () => {
          if (ajax.readyState == 4 && ajax.status == 200){
            cb(JSON.parse(ajax.responseText));
          }
      }
      ajax.open(method, API_BASE_URL + uri, true);
      ajax.setRequestHeader('Content-Type', 'application/json')
      ajax.send(JSON.stringify(body));
    }

    function osvjezi(){

      _sendJsonRequest('GET', '/godine', {}, (items) => {
        const pravogaonici = [];
        items.forEach( (item, index) => {
          let itemString = '';
          itemString += '<div class="godina">'
          itemString += item.nazivGod;
          itemString += '<div> <b>Naziv repozitorija:</b> '+item.nazivRepVje+' </div>';
          itemString += '<div> <b>Naziv spirale:</b>' + item.nazivRepSpi + '</div>';
          itemString += '</div>'
          pravogaonici.push(itemString)
        } );
        let isEven = pravogaonici.length%2;
        let ispis = '';
        pravogaonici.forEach((item, index) => {
          if(index%2 == 0)
            ispis +='<div class="pravougaonik">'
          ispis += item
          if(index%2 == 1)
            ispis += '</div>'
        })

        if(pravogaonici.length > 0 && !isEven)
          ispis += '</div>'

        divSadrzaj.innerHTML = ispis;
      });



    }
    osvjezi();
    return {
      osvjezi: osvjezi
      }
    }
  return konstruktor;
}());

module.exports=GodineAjax;