var Ajax_Impl=(function(){
    const API_BASE_URL = 'http://localhost:8080';
    let checkIsValidJSON = (text) => {
      return /^[\],:{}\s]*$/.test(text.replace(/\\["\\\/bfnrtu]/g, '@').
        replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g, ']').
        replace(/(?:^|:|,)(?:\s*\[)+/g, ''));
    }
    const _sendMultipartRequest = (method, uri, body, cb) => {
        const ajax = new XMLHttpRequest();
        ajax.onreadystatechange = () => {
            if (ajax.readyState == 4 && ajax.status == 200){
                cb(null, ajax.responseText);
            }else if(ajax.readyState === 4) {
                console.log("ERROR IS:", ajax);
            }
        }
        ajax.open(method, API_BASE_URL + uri, true);

        ajax.send(body);

    }

    const _sendJsonRequest = (method, uri, body, cb) => {
      const ajax = new XMLHttpRequest();
      ajax.onreadystatechange = () => {
          if (ajax.readyState == 4 && ajax.status == 200){
            cb(!checkIsValidJSON(ajax.responseText), ajax.responseText);
          }else if(ajax.readyState === 4) {
              console.log("ERROR IS:", ajax);
          }
      }
      ajax.open(method, API_BASE_URL + uri, true);
      ajax.setRequestHeader('Content-Type', 'application/json')
      ajax.send(JSON.stringify(body));
    }

    const dodajZadatak = function(body, cb) {
        _sendMultipartRequest('POST', '/addZadatak', body, cb);
    }

    const addGodina = function(body, cb){
      _sendJsonRequest('POST', '/addGodina', body, cb)
    }



    const konstruktor=function(){
        return {
            dodajZadatak,
            addGodina
        }
    }
    return konstruktor;
  }()
  );

  var GetAjax = () => new Ajax_Impl
