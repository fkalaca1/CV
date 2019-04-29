const express = require('express');
const bodyParser = require('body-parser');
const fs = require('fs');
const app = express();

const defaultRoutes = require('./src/routes/index');

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

app.use('/', defaultRoutes);
app.use(express.static('./public'));
app.use(express.static('./uploads'));

app.listen(8080);
