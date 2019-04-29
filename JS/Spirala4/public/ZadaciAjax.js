var ZadaciAjax = (function(){
  var konstruktor = function(callbackFn){
  let zauzet = false;
  const getData = (type) => {
    if(zauzet){
      callbackFn({ greska: 'Vec ste uputili zahtjev' });
      return;
    }
    zauzet = true;
    const ajax = new XMLHttpRequest();
    ajax.onreadystatechange = function(){
      if(ajax.readyState == 4 && ajax.status == 200){
        zauzet = false;
        return callbackFn(ajax.responseText);
      }
    }
    ajax.open('GET', 'http://localhost:8080/zadaci', true);
    ajax.setRequestHeader('Accept', type);
    ajax.ontimeout = function (e) {
      return ajax.abort();
    };
    ajax.timeout = 2000;
    
    ajax.send();
  }
  return {
    dajXML:function(){
      getData('application/xml')
    },
    dajCSV:function(){
      getData('text/csv')
    },
    dajJSON:function(){
      getData('application/json')
    }
    }
  }
  return konstruktor;
}());

module.exports=ZadaciAjax;
