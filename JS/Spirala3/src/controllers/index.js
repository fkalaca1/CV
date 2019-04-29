const multer = require('multer');
const fs = require('fs');
const path = require('path');

const uploadsFolder = __dirname + '/../../uploads';
const hiddenFolder = __dirname + '/../../hidden';
const CSV_FIELDS = [ 'nazivGod', 'nazivRepVje', 'nazivRepSpi' ];


var storage = multer.diskStorage({
    destination: function (req, file, cb) {
      cb(null, 'uploads/')
    },
    filename: function (req, file, cb) {
        if(file.mimetype !== 'application/pdf')
           return cb("Greska u tipu podatka", null);

        cb(null, file.originalname + Date.now() + '.pdf');
    }
  })

var upload = multer({ storage: storage }).single('postavka');

const addZadatak = (req, res) => {
    upload(req, res, function (err) {

        if (err || !req.file) {
            return res.redirect('/greska.html');
        }

        const filename = req.body.naziv + '.pdf';

        const files = [];
        fs.readdirSync(uploadsFolder).forEach(file => {
            files.push(file);
        })

        if(files.indexOf(filename) > -1){
          return fs.unlink(uploadsFolder + '/' + req.file.filename, function (err) {
            if (err) throw err;
            return res.redirect('/greska.html');
          });
        }


        fs.rename(uploadsFolder + '/' + req.file.filename, uploadsFolder + '/' + filename, (err) => {
          if(err) {
            return res.redirect('/greska.html');
          }

          const preparedJson = {
            naziv: req.body.naziv,
            postavka: req.protocol + '://' + req.get('host') + '/' + filename
          }
          fs.writeFile(uploadsFolder + '/' + filename.substring(0, filename.length - 4) + 'Zad.json',  JSON.stringify(preparedJson), (err) => {
            if(err) return res.status(400).send(err);
            return res.send(preparedJson)
          })
        })

    });
}

const getZadatakByQueryName = (req, res) => {
  if(!req.query.naziv) return res.status(400).send("no naziv as query parameter");

  return res.sendFile(path.resolve(uploadsFolder + '/' + req.query.naziv + '.pdf'));
}

const saveCSV = (values, cb) => {
  fs.writeFile(path.resolve(hiddenFolder + '/godine.csv'), values, (err) => {
    cb(err);
  })
}

const getCSV = (cb) => {
  fs.readFile(path.resolve(hiddenFolder + '/godine.csv'), 'utf8', (err, data) => {
    let content = data;
    if(!data)
      content = '';
    const param = {
      isEmpty: data === '',
      content
    }
    cb(param)
  })
}

const addGodina = (req, res) => {
  getCSV((param) => {
    let currentCsv = param.content;
    if(!param.isEmpty)
      currentCsv += '\n';
    const indexOfNazivGod = CSV_FIELDS.indexOf('nazivGod');
    let shouldRedirect = false

    const body = req.body;
    currentCsv.split('\n').forEach(lines => {
      const values = lines.split(',');
      if(values[indexOfNazivGod] == body.nazivGod){
        shouldRedirect = true;
      }
    })
    if(shouldRedirect)
      return res.redirect('/greska.html');
    CSV_FIELDS.forEach( field => {
      currentCsv += body[field] + ','
    } )

    currentCsv = currentCsv.substring(0, currentCsv.length - 1);

    saveCSV(currentCsv, (err) => {
      if(err)
        return res.status(400).send(err);

      return res.sendFile(path.resolve(__dirname + '/../../public/addGodina.html'))
    })
  })
}

const getGodineFromCsv = (req, res) => {
  getCSV((param) => {
    const currentCsv = param.content;
    const response = [];
    currentCsv.split('\n').forEach(lines => {
      var shouldPush = true;
      const item = {};
      lines.split(',').forEach((value, index) => {
        if(value === '') shouldPush = false;
        item[CSV_FIELDS[index]] = value;
      });
      if(shouldPush)response.push(item);
    });
    return res.send(response);
  })
}

const getGodineByHeaderAccept = (req, res) => {
  let zadFiles = [];
  fs.readdirSync(uploadsFolder).forEach(file => {
    const mainIndex = file.indexOf( 'Zad.json' );
    if(mainIndex >= 0 && file.substring(mainIndex) == 'Zad.json'){
      zadFiles.push(file);
    }
  });
  let numberOfUnreadedFiles = zadFiles.length;
  if(numberOfUnreadedFiles == 0){
    return res.send({});
  }
  let mainData = [];
  zadFiles.forEach((file) => {
    fs.readFile( uploadsFolder + '/' + file, 'utf8', (err, data) => {
      mainData.push(JSON.parse(data));
      numberOfUnreadedFiles--;
      if(numberOfUnreadedFiles == 0){
        const hasJSON = req.headers.accept.find && !!req.headers.accept.find( item => item.indexOf('json') > -1 ) || req.headers.accept.indexOf('json') > -1
        const hasXML = req.headers.accept.find && !!req.headers.accept.find( item => item.indexOf('xml') > -1 ) || req.headers.accept.indexOf('xml') > -1
        const hasCSV = req.headers.accept.find && !!req.headers.accept.find( item => item.indexOf('csv') > -1 ) || req.headers.accept.indexOf('csv') > -1
        if(hasJSON){
          return res.send(mainData);
        }

        if(hasXML){
          let xml = '<?xml version="1.0" encoding="UTF-8"?>';
          xml += '\n' + '<zadaci>'
          mainData.forEach((item) => {
            xml += '\n' + '<zadatak>' + '\n' + '<naziv> ' + item.naziv + ' </naziv>' + '\n' + '<postavka> ' + item.postavka + ' </postavka>' + '\n' + '</zadatak>'
          });
          xml += '\n' + '</zadaci>';
          res.set('Content-Type', 'application/xml');
          return res.send(xml);
        }
        if(hasCSV){
          let csv = '';
          mainData.forEach(item => {
            csv += item.naziv + ',' + item.postavka + '\n';
          });
          if(csv.length > 0);
            csv = csv.substring(0, csv.length - 1);
  
          return res.send(csv);
        }

        return res.send(mainData);
      }
    } );

  });
}

module.exports = {
    addZadatak,
    getZadatakByQueryName,
    addGodina,
    getGodineFromCsv,
    getGodineByHeaderAccept
}
