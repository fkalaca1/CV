const express = require('express');
const DefaultController = require('../controllers/index');
const router = express.Router();

router.route('/addZadatak')
    .post(DefaultController.addZadatak);
router.route('/zadatak')
    .get(DefaultController.getZadatakByQueryName)

router.route('/zadatak/:vjezbaId')
    .get(DefaultController.getZadatakBezVjezbi);

router.route('/addGodina')
    .post(DefaultController.addGodina)
router.route('/godine')
    .get(DefaultController.getGodineFromCsv)

router.route('/vjezbe').get(DefaultController.getSpirale);

router.route('/vjezba/:idVjezbe/zadatak')
    .post(DefaultController.dodajVjezbaZadatak);

router.route('/addVjezba')
    .post( DefaultController.addSpirala );

router.route('/zadaci')
    .get(DefaultController.getGodineByHeaderAccept);

module.exports = router;
