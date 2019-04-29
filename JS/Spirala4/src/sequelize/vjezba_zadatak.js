module.exports = (sequelize, type) => {
    return sequelize.define('vjezba_zadatak', {
        id: {
          type: type.INTEGER,
          primaryKey: true,
          autoIncrement: true
        },
        idvjezba: {
            type: type.INTEGER,
        },
        idzadatak: {
            type: type.INTEGER,
        },
    })
}
