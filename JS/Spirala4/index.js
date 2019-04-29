const express = require('express');
const bodyParser = require('body-parser');
const fs = require('fs');
const app = express();
require('./src/sequelize/db');
const defaultRoutes = require('./src/routes/index');
const studentRoutes = require('./src/routes/student')

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

app.use('/', defaultRoutes);
app.use('/users', studentRoutes);

app.use(express.static('./public'));
app.use(express.static('./uploads'));

app.listen(8080);
