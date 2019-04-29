var Validacija=(function(){
    var konstruktor=function(divElementPoruke){
      return{
        ime:function(inputElement){
          const value = inputElement.value;
          const parts = value.split(/[\s\-]/);
          if(parts.length > 4) return false;
  
          const regexSingle = /^[A-Z]([a-zA-Z]|'[a-zA-Z]|'$)*$/s;
  
          for(let i = 0; i<parts.length; i++){
            if(parts[i].length < 2)
              return false;
            if(!parts[i].match(regexSingle)){
              return false;
            }
          }
  
          return true;
        },
        godina:function(inputElement){
          const value = inputElement.value;
          const regexSingle = /20[0-9]{2}\/20[0-9]{2}/s;
          if(!value.match(regexSingle)) return false;
  
          const parts = value.split('/');
          return Number(parts[0]) + 1 === Number(parts[1]);
        },
        repozitorij:function(inputElement,regex){
          const value = inputElement.value;
  
          return value.match(regex);
        },
        index:function(inputElement){
          const value = inputElement.value;
  
          const regex = /^[0-9]{5}$/s;
          if(!value.match(regex)) return false;
  
          const num = Number(value[0] + value[1]);
  
          if(num < 14) return false;
          return num <= 20;
        },
        naziv:function(inputElement){
          const value = inputElement.value;
          const regex = /^[a-zA-Z][a-zA-Z0-9\/\\\-\"\'\!\?\:\;\,]+[a-z0-9]$/s;
          return !!value.match(regex);
        },
        password:function(inputElement){
          const value = inputElement.value;
          if(value.length < 8) return false;
          const regexSmallNumber = /[a-zA-Z0-9]*([a-z]+[a-zA-Z0-9]*[0-9]+|[0-9]+[a-zA-Z0-9]*[a-z]+)[a-zA-Z0-9]*/s
          const regexBigNumber = /[a-zA-Z0-9]*([A-Z]+[a-zA-Z0-9]*[0-9]+|[0-9]+[a-zA-Z0-9]*[A-Z]+)[a-zA-Z0-9]*/s
          const regexSmallBig = /[a-zA-Z0-9]*([A-Z]+[a-zA-Z0-9]*[a-z]+|[a-z]+[a-zA-Z0-9]*[A-Z]+)[a-zA-Z0-9]*/s
  
          if(value.match(regexSmallNumber) || value.match(regexBigNumber) || value.match(regexSmallBig))
            return true;
  
          return false
        },
        url:function(inputElement){
          let value = inputElement.value;
  
          let regexByStep = /(http|https|ftp|ssh)\:\/{2}/g;
          if(!value.match(regexByStep))
            return false;
          value = value.substring(value.match(regexByStep)[0].length);
  
          let queryParams = value.split('?')[1];
  
          if(queryParams){
            const regexQueryParams = /[a-z0-9]+\=[a-z0-9]+/g;
  
            queryParams = queryParams.split('&')
            for(let i = 0; i<queryParams.length; i++)
              if(!queryParams[i].match(regexQueryParams))
                return false;
          }
  
          let parts = value.split('?')[0];
          if(parts[parts.length - 1] !== '/')
            parts += '/';
          parts = parts.split('/')
          const host = parts[0];
  
          regexByStep = /^[a-z0-9]([a-z0-9]+|\.[a-z0-9]+|\-[a-z0-9])*$/g;
          if(!host.match(regexByStep)){
            return false;
          }
  
          regexByStep = /^[a-z0-9]+$/g
          for(let i = 1; i < parts.length - 1; i++){
            if(!parts[i].match(regexByStep)){
              return false;
            }
          }
  
          return true;
        },
        ispisiPoruku: function(poruka){
          divElementPoruke.innerHTML = poruka;
        }
      }
    }
    return konstruktor;
  }()
  );
  