const Sequelize = require('sequelize');
const student = require('./student.js');
const godina = require('./godina.js');
const zadatak = require('./zadatak.js');
const vjezba = require('./vjezba.js');
const godinavjezbe = require('./godina_vjezbe.js');
const vjezbazadatak = require('./vjezba_zadatak.js');
const sequelize = new Sequelize('mysql://root:root@localhost:3306/wt2018');

const Student = student(sequelize, Sequelize);
const Godina = godina(sequelize, Sequelize);
const Zadatak = zadatak(sequelize, Sequelize);
const Vjezba = vjezba(sequelize, Sequelize);
const GodinaVjezba = godinavjezbe(sequelize, Sequelize);
const VjezbaZadatak = vjezbazadatak(sequelize, Sequelize);

Godina.hasMany(Student, {foreignKey: 'studentGod', as: 'studenti'})

Godina.belongsToMany(Vjezba, { foreignKey: 'idgodina', as: 'vjezbe', through: GodinaVjezba })
Vjezba.belongsToMany(Godina, { foreignKey: 'idvjezba', as: 'godine', through: GodinaVjezba })

Vjezba.belongsToMany(Zadatak, { foreignKey: 'idvjezba', as: 'zadaci', through: VjezbaZadatak })
Zadatak.belongsToMany(Vjezba, { foreignKey: 'idzadatak', as: 'vjezbe', through: VjezbaZadatak })

sequelize.sync({ force: false })
    .then(() => {
    console.log(`Database & tables created!`)
    }).catch(err => {
        console.log("Database error:", err);
    })


module.exports = {
    student: Student,
    godina: Godina,
    zadatak: Zadatak,
    vjezba: Vjezba,
    godina_vjezba: GodinaVjezba,
    vjezba_zadatak: VjezbaZadatak
};
