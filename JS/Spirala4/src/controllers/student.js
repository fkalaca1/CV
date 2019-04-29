const { Student, Godina, Vjezba } = require('../sequelize/db');

const getAllStudents = (req, res) => {
  Godina.findAll({
    include: [
      'vjezbe',
    ]
  }).then(g => {

    res.send(g)
  }).catch(e => {
    res.status(500).send(e)
  })
};



module.exports = {
    getAllStudents,
};
