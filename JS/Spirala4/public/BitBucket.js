var BitBucket = (function(){
  var token = null;

  function procedureAfterUcitaj(response, cb){
    const users = [];
    response.values.forEach( repo => {
      const index = repo.name.substring(repo.name.length - 5);
      const imePrezime = repo.owner.display_name;
      const userExists = users.find( item => item.index == index );
      if(!userExists)
        users.push({
          index,
          imePrezime
        })
    } )
    cb(null, users);
  }

  function ucitaj(nazivRepSpi,nazivRepVje, cb){
    const ajax = new XMLHttpRequest();

    ajax.onreadystatechange = function() {
      if (ajax.readyState == 4 && ajax.status == 200){
        procedureAfterUcitaj(JSON.parse(ajax.responseText), cb);
      }else if(ajax.readyState == 4){
        cb(ajax.responseText, null);
      }
    }
    ajax.open("GET", 'https://api.bitbucket.org/2.0/repositories?pagelen=800&role=member&q=name~'+encodeURIComponent('"')+nazivRepSpi+encodeURIComponent('"')+'+OR+name~'+encodeURIComponent('"')+nazivRepVje+encodeURIComponent('"'), true);
    token.then(newToken => {
      ajax.setRequestHeader("Authorization", 'Bearer ' + newToken);
      ajax.send();
    }).catch(err => {
      cb(err, null);
    })
  }

  function init(key, secret){
    token = new Promise((resolve, reject) => {
      const ajax = new XMLHttpRequest();
      ajax.onreadystatechange = function() {
        if (ajax.readyState == 4 && ajax.status == 200){
          resolve(JSON.parse(ajax.responseText).access_token);
        }else if(ajax.readyState == 4){
          reject("Ne valja key ili secret")
        }
      }
      ajax.open("POST", "https://bitbucket.org/site/oauth2/access_token", true);
      ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
      ajax.setRequestHeader("Authorization", 'Basic ' + btoa(key + ":" + secret));
      ajax.send("grant_type="+encodeURIComponent("client_credentials"));
    })
  }

  var konstruktor = function(key, secret){
    init(key, secret);
    return {
      ucitaj
    }
  }
  return konstruktor;
}());
