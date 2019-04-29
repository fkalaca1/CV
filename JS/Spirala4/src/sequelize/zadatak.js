module.exports = (sequelize, type) => {
    return sequelize.define('Zadatak', {
        id: {
          type: type.INTEGER,
          primaryKey: true,
          autoIncrement: true
        },
        naziv: type.STRING,
        postavka: type.STRING,
    })
}