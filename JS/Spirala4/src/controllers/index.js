const multer = require('multer');
const fs = require('fs');
const path = require('path');

const uploadsFolder = __dirname + '/../../uploads';
const hiddenFolder = __dirname + '/../../hidden';
const CSV_FIELDS = [ 'nazivGod', 'nazivRepVje', 'nazivRepSpi' ];

const db = require('../sequelize/db');
const Zadatak = db.zadatak;
const Godina = db.godina;
const Vjezba = db.vjezba;
const GodinaVjezba = db.godina_vjezba;
const VjezbaZadatak = db.vjezba_zadatak;

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

        return Zadatak.findOne({
          where: {naziv: req.body.naziv},
          raw: true
        }).then((response) => {

          if(!!response){
            return fs.unlink(uploadsFolder + '/' + req.file.filename, function (err) {
              if (err) throw err;
              return res.redirect('/greska.html');
            });
          }

          return fs.rename(uploadsFolder + '/' + req.file.filename, uploadsFolder + '/' + filename, (err) => {
            if(err) {
              return res.redirect('/greska.html');
            }

            const preparedJson = {
              naziv: req.body.naziv,
              postavka: req.protocol + '://' + req.get('host') + '/' + filename
            }
            return Zadatak.create(preparedJson).then(response => {
              return res.send(preparedJson);
            })
          })

        }).catch(err => {
          return res.status(500).send({err});
        })
    });
}

const getZadatakByQueryName = (req, res) => {
  if(!req.query.naziv) return res.status(400).send("no naziv as query parameter");
  Zadatak.findOne({ where: { naziv: req.query.naziv }, raw: true }).then( zadatak => {
    if(!zadatak) return res.status(400).send("no naziv as query parameter");
    const host = req.protocol + '://' + req.get('host') + '/';
    return res.sendFile( path.resolve(uploadsFolder + '/' + zadatak.postavka.substring(host.length)) )
  } )
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

const addGodina = async (req, res) => {
  const body = {
    nazivGod: req.body.nazivGod,
    nazivRepSpi: req.body.nazivRepVje,
    nazivRepVje: req.body.nazivRepSpi,
  }
  Godina.findOne({ where: { nazivGod: body.nazivGod } }).then(response => {
    if(!!response)
      return res.redirect('/greska.html');

    Godina.create(body).then(godina => {
      return res.sendFile(path.resolve(__dirname + '/../../public/addGodina.html'))
    })

  }).catch(err => {
    return res.status(500).send(err);
  })
}

const getGodineFromCsv = (req, res) => {
  Godina.findAll({}).then(godine => {
    return res.send(godine || []);
  }).catch(e => {
    return res.status(500).send(e);
  })
}

const getGodineByHeaderAccept = (req, res) => {
  // let zadFiles = [];
  // fs.readdirSync(uploadsFolder).forEach(file => {
  //   const mainIndex = file.indexOf( 'Zad.json' );
  //   if(mainIndex >= 0 && file.substring(mainIndex) == 'Zad.json'){
  //     zadFiles.push(file);
  //   }
  // });
  // let numberOfUnreadedFiles = zadFiles.length;
  // if(numberOfUnreadedFiles == 0){
  //   return res.send({});
  // }
  // let mainData = [];
  // zadFiles.forEach((file) => {
  //   fs.readFile( uploadsFolder + '/' + file, 'utf8', (err, data) => {
  //     mainData.push(JSON.parse(data));
  //     numberOfUnreadedFiles--;

    Zadatak.findAll({}).then(mainData => {
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
    })
      // if(numberOfUnreadedFiles == 0){
    // }
    // } );

  // });
}

const addSpirala = (req, res) => {
  if(req.body.naziv){
    return Vjezba.create({
      naziv: req.body.naziv,
      spirala: !!req.body.spirala
    }).then( vjezba => {
      GodinaVjezba.create({
        idgodina: Number(req.body.sGodine),
        idvjezba: vjezba.get({plain: true }).id,
      }).then( godinaVjezba => {
        return res.sendFile(path.resolve(__dirname + '/../../public/addVjezba.html'));
      } );
    } ).catch(err => {
      return res.status(400).send(err);
    })
  }

  return GodinaVjezba.findOrCreate({
    where: {
      idgodina: req.body.sGodine,
      idvjezba: req.body.sVjezbe
    }
  }).then( godinaVjezba => {
    return res.sendFile(path.resolve(__dirname + '/../../public/addVjezba.html'));
  } ).catch(err => {
    return res.redirect('/greska.html');
  })
}

const getSpirale = (req, res) => {
  Vjezba.findAll({}).then( vjezbe => {
    return res.send(vjezbe)
  } ).catch(err => {
    return res.status(500).send(err);
  })
}

const getZadatakBezVjezbi = (req, res) => {
  VjezbaZadatak.findAll({
    where: {
      idvjezba: req.params.vjezbaId
    }
  }).then( vjezbaZadatak => {
    const zadaci = [];
    vjezbaZadatak.forEach( vZadatak => {
      zadaci.push(vZadatak.idzadatak);
    } );

    Zadatak.findAll({
      where: {
        id: {
          $notIn: zadaci
        }
      }
    }).then( zadatak => {
      return res.send(zadatak);
    } )
  } ).catch(err => {
    return res.redirect('/greska.html');
  })
}

const dodajVjezbaZadatak = (req, res) => {
  VjezbaZadatak.create({
    idvjezba: req.params.idVjezbe,
    idzadatak: req.body.sZadatak,
  }).then( vjezbaZadatak => {
    return res.sendFile(path.resolve(__dirname + '/../../public/addVjezba.html'));
  } ).catch(err => {
    return res.redirect('/greska.html');
  })
}

module.exports = {
    addZadatak,
    getZadatakByQueryName,
    addGodina,
    getGodineFromCsv,
    getGodineByHeaderAccept,
    addSpirala,
    getSpirale,
    getZadatakBezVjezbi,
    dodajVjezbaZadatak
}
