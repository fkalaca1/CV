module.exports = (sequelize, type) => {
    return sequelize.define('godina_vjezba', {
        id: {
          type: type.INTEGER,
          primaryKey: true,
          autoIncrement: true
        },
        idgodina: {
            type: type.INTEGER,
        },
        idvjezba: {
            type: type.INTEGER,
        },
    })
}